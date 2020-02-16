/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dream.codec.tlv.msg;

import com.dream.codec.tlv.TLVContext;
import com.dream.codec.tlv.entity.RawTLVMsg;
import com.dream.codec.tlv.exception.TLVException;
import com.dream.codec.tlv.exception.TLVFieldUnrecognizedException;
import com.dream.codec.tlv.field.FieldDefinition;
import com.dream.codec.tlv.field.FieldLenType;
import com.dream.codec.tlv.utils.ReflectionUtils;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TLV Message Content Decoder
 *
 * @author DreamJM
 */
public class ContentDecoder {

    private static final Logger logger = LoggerFactory.getLogger(ContentDecoder.class);

    /**
     * TLV Codec Context
     */
    private TLVContext context;

    public ContentDecoder(TLVContext context) {
        this.context = context;
    }

    /**
     * Decode TLV Content
     *
     * @param preResult TLV Message Pre-Decode(Header Parsed)
     * @return Decoded Object
     * @throws TLVException Decode Exception
     */
    public RawTLVMsg<?> bodyDecode(RawTLVMsg<?> preResult) throws TLVException {
        try {
            Class<?> msgClass = context.getMsgClass(preResult.getHeader().getType());
            if (msgClass == null) {
                logger.error("Unknown TLV Message Type {}", preResult.getHeader().getType());
                throw new TLVException("Unknown msg type: " + preResult.getHeader().getType());
            }
            Map<Short, FieldDefinition> defMap = new HashMap<>();
            context.getMsgDefinition(msgClass)
                    .forEach(fieldDefinition -> defMap.put(fieldDefinition.getAnnotation().tag(), fieldDefinition));
            Object msg = msgClass.newInstance();
            // start parsing
            decode(msg, preResult.getDataInput(), defMap, preResult.getHeader().getLength());
            logger.debug("Message with type {} and id {} decoded success", preResult.getHeader().getType(), preResult.getHeader().getId());
            return (RawTLVMsg<?>) msg;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new TLVException("Message Body decode failure", e);
        } catch (IllegalStateException e) {
            throw new TLVException("Message Content Length Error", e);
        }
    }

    private void decode(Object msg, ByteArrayDataInput input, Map<Short, FieldDefinition> defMap, int length)
            throws IllegalAccessException, InstantiationException, TLVFieldUnrecognizedException {
        while (length > 0) {
            short tag = input.readShort();
            length -= 2;
            FieldDefinition fieldDef = defMap.get(tag);
            if (fieldDef == null) {
                throw new TLVFieldUnrecognizedException("Unknown Field Tag " + tag + " for class: " + msg.getClass());
            }
            logger.trace("Start Decode Field with Tag {} and Type {}", tag, fieldDef.getFieldType());
            int[] lengthInfo = getFieldLengthInfo(input, fieldDef.getAnnotation().fieldLenType());
            int fieldLength = lengthInfo[0];
            length -= lengthInfo[1];
            switch (fieldDef.getFieldType()) {
                case BYTE:
                    ReflectionUtils.setField(fieldDef.getRawField(), msg, input.readByte());
                    length -= 1;
                    break;
                case SHORT:
                    ReflectionUtils.setField(fieldDef.getRawField(), msg, input.readShort());
                    length -= 2;
                    break;
                case INT:
                    ReflectionUtils.setField(fieldDef.getRawField(), msg, input.readInt());
                    length -= 4;
                    break;
                case LONG:
                    ReflectionUtils.setField(fieldDef.getRawField(), msg, input.readLong());
                    length -= 8;
                    break;
                case RAW:
                    byte[] raw = new byte[fieldLength];
                    input.readFully(raw);
                    ReflectionUtils.setField(fieldDef.getRawField(), msg, raw);
                    length -= fieldLength;
                    break;
                case STRING:
                    byte[] content = new byte[fieldLength];
                    input.readFully(content);
                    ReflectionUtils
                            .setField(fieldDef.getRawField(), msg, new String(content, fieldDef.getAnnotation().charset().getCharset()));
                    length -= fieldLength;
                    break;
                case ARRAY:
                    byte[] arrayContent = new byte[fieldLength];
                    input.readFully(arrayContent);
                    Object arrayObj = decodeArrayContent(fieldDef.getSubFieldDef(), arrayContent, fieldDef.getSubFieldDef().getRawType());
                    ReflectionUtils.setField(fieldDef.getRawField(), msg, arrayObj);
                    length -= fieldLength;
                    break;
                case LIST:
                    byte[] listContent = new byte[fieldLength];
                    input.readFully(listContent);
                    List<?> listObj = decodeListContent(fieldDef.getSubFieldDef(), listContent, fieldDef.getSubFieldDef().getRawType());
                    ReflectionUtils.setField(fieldDef.getRawField(), msg, listObj);
                    length -= fieldLength;
                    break;
                case OBJECT:
                    Object tlvObj = fieldDef.getRawType().newInstance();
                    Map<Short, FieldDefinition> subFieldDefMap = new HashMap<>();
                    context.getObjDefinition(fieldDef.getRawType())
                            .forEach(subFieldDef -> subFieldDefMap.put(subFieldDef.getAnnotation().tag(), subFieldDef));
                    decode(tlvObj, input, subFieldDefMap, fieldLength);
                    ReflectionUtils.setField(fieldDef.getRawField(), msg, tlvObj);
                    length -= fieldLength;
                    break;
                default:
                    break;
            }
        }
    }

    private <T> Object decodeArrayContent(FieldDefinition fieldDef, byte[] content, Class<?> type)
            throws InstantiationException, IllegalAccessException, TLVFieldUnrecognizedException {
        List<?> decodeListContent = decodeListContent(fieldDef, content, type);
        Object result = Array.newInstance(type, decodeListContent.size());
        for (int i = 0; i < decodeListContent.size(); i++) {
            Array.set(result, i, decodeListContent.get(i));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> decodeListContent(FieldDefinition fieldDef, byte[] content, Class<T> type)
            throws IllegalAccessException, InstantiationException, TLVFieldUnrecognizedException {
        ByteArrayDataInput input = ByteStreams.newDataInput(content);
        List<T> listContent = new ArrayList<>();
        int length = content.length;
        while (length > 0) {
            short tag = input.readShort();
            if (fieldDef.getAnnotation().tag() != tag) {
                throw new TLVFieldUnrecognizedException(
                        "Field registered with tag " + fieldDef.getAnnotation().tag() + " got unknown tag " + tag + " in List or Array!");
            }
            logger.trace("Decoding List or Array SubField with tag {}", tag);
            length -= 2;
            int[] lengthInfo = getFieldLengthInfo(input, fieldDef.getAnnotation().fieldLenType());
            int fieldLength = lengthInfo[0];
            length -= lengthInfo[1];
            switch (fieldDef.getFieldType()) {
                case BYTE:
                    listContent.add((T) new Byte(input.readByte()));
                    length -= 1;
                    break;
                case SHORT:
                    listContent.add((T) new Short(input.readShort()));
                    length -= 2;
                    break;
                case INT:
                    listContent.add((T) new Integer(input.readInt()));
                    length -= 4;
                    break;
                case LONG:
                    listContent.add((T) new Long(input.readLong()));
                    length -= 8;
                    break;
                case RAW:
                    byte[] raw = new byte[fieldLength];
                    input.readFully(raw);
                    Byte[] rawByte = new Byte[fieldLength];
                    for (int i = 0; i < fieldLength; i++) {
                        rawByte[i] = raw[i];
                    }
                    listContent.add((T) rawByte);
                    length -= fieldLength;
                    break;
                case STRING:
                    byte[] strBytes = new byte[fieldLength];
                    input.readFully(strBytes);
                    listContent.add((T) new String(strBytes, fieldDef.getAnnotation().charset().getCharset()));
                    length -= fieldLength;
                    break;
                case OBJECT:
                    T tlvObj = type.newInstance();
                    Map<Short, FieldDefinition> subFieldDefMap = new HashMap<>();
                    context.getObjDefinition(type)
                            .forEach(subFieldDef -> subFieldDefMap.put(subFieldDef.getAnnotation().tag(), subFieldDef));
                    decode(tlvObj, input, subFieldDefMap, fieldLength);
                    listContent.add(tlvObj);
                    length -= fieldLength;
                    break;
                default:
                    break;
            }
        }
        return listContent;
    }

    private int[] getFieldLengthInfo(ByteArrayDataInput input, FieldLenType fieldLenType) {
        int totalLength;
        int fieldLength;
        if (FieldLenType.LONG == fieldLenType) {
            totalLength = input.readShort();
            fieldLength = 2;
        } else {
            totalLength = input.readByte();
            fieldLength = 1;
        }
        return new int[]{totalLength, fieldLength};
    }

}
