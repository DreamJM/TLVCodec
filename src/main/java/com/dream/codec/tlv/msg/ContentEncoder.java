package com.dream.codec.tlv.msg;

import com.dream.codec.tlv.TLVContext;
import com.dream.codec.tlv.entity.RawTLVMsg;
import com.dream.codec.tlv.exception.TLVException;
import com.dream.codec.tlv.exception.TLVFieldMissingException;
import com.dream.codec.tlv.field.FieldDefinition;
import com.dream.codec.tlv.field.FieldLenType;
import com.dream.codec.tlv.utils.ReflectionUtils;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.List;

/**
 * TLV Encoder for TLV Message Content which consists of TLV Fields
 *
 * @author DreamJM
 */
public class ContentEncoder {

    private static final Logger logger = LoggerFactory.getLogger(ContentEncoder.class);

    /**
     * TLV Codec Context
     */
    private TLVContext context;

    public ContentEncoder(TLVContext context) {
        this.context = context;
    }

    /**
     * Content Encode
     *
     * @param body Content Raw TLV Object
     * @return Encoded Byte Array
     * @throws TLVException Encoding Exception
     */
    public byte[] encode(RawTLVMsg<?> body) throws TLVException {
        ByteArrayDataOutput content = ByteStreams.newDataOutput();
        // Look for fieldDefinitions
        List<FieldDefinition> fieldDefs = context.getMsgDefinition(body.getClass());
        for (FieldDefinition fieldDef : fieldDefs) {
            try {
                encode(content, fieldDef, ReflectionUtils.getField(fieldDef.getRawField(), body));
            } catch (IllegalAccessException e) {
                throw new TLVException("field " + fieldDef.getRawField().getName() + " value access error", e);
            }
        }
        logger.debug("Msg with type {} and Identification {} encoded success", body.getHeader().getType(), body.getHeader().getId());
        return content.toByteArray();
    }

    private void encode(ByteArrayDataOutput output, FieldDefinition def, Object value) throws TLVException {
        if (value == null) {
            if (def.getAnnotation().require()) { // check if required
                throw new TLVFieldMissingException("Field with Tag " + def.getAnnotation().tag() + " required, but missing...");
            }
            return;
        }
        logger.trace("Encoding Field with tag {} and type {}", def.getAnnotation().tag(), def.getFieldType().name());
        try {
            output.writeShort(def.getAnnotation().tag());
            switch (def.getFieldType()) {
                case BYTE:
                    output.writeByte(1);
                    output.writeByte((byte) value);
                    break;
                case SHORT:
                    output.writeByte(2);
                    output.writeShort((short) value);
                    break;
                case INT:
                    output.writeByte(4);
                    output.writeInt((int) value);
                    break;
                case LONG:
                    output.writeByte(8);
                    output.writeLong((long) value);
                    break;
                case RAW:
                    byte[] raw = (byte[]) value;
                    writeLength(output, raw.length, def.getAnnotation().fieldLenType());
                    output.write(raw);
                    break;
                case STRING:
                    byte[] bytes = ((String) value).getBytes(def.getAnnotation().charset().getCharset());
                    writeLength(output, bytes.length, def.getAnnotation().fieldLenType());
                    output.write(bytes);
                    break;
                case ARRAY:
                    int arrayLength = Array.getLength(value);
                    ByteArrayDataOutput arrayOutput = ByteStreams.newDataOutput();
                    for (int i = 0; i < arrayLength; i++) {
                        encode(arrayOutput, def.getSubFieldDef(), Array.get(value, i));
                    }
                    byte[] arrayBytes = arrayOutput.toByteArray();
                    writeLength(output, arrayBytes.length, def.getAnnotation().fieldLenType());
                    output.write(arrayBytes);
                    break;
                case LIST:
                    List listObjs = (List) value;
                    ByteArrayDataOutput listOutput = ByteStreams.newDataOutput();
                    for (Object listObj : listObjs) {
                        encode(listOutput, def.getSubFieldDef(), listObj);
                    }
                    byte[] listBytes = listOutput.toByteArray();
                    writeLength(output, listBytes.length, def.getAnnotation().fieldLenType());
                    output.write(listBytes);
                    break;
                case OBJECT:
                    List<FieldDefinition> fieldDefs = context.getObjDefinition(value.getClass());
                    ByteArrayDataOutput objOutput = ByteStreams.newDataOutput();
                    for (FieldDefinition fieldDef : fieldDefs) {
                        encode(objOutput, fieldDef, ReflectionUtils.getField(fieldDef.getRawField(), value));
                    }
                    byte[] objBytes = objOutput.toByteArray();
                    writeLength(output, objBytes.length, def.getAnnotation().fieldLenType());
                    output.write(objBytes);
                    break;
                default:
                    break;
            }
        } catch (IllegalAccessException e) {
            throw new TLVException("TLV Field with tag " + def.getAnnotation().tag() + " encode failure", e);
        }
    }

    private void writeLength(ByteArrayDataOutput output, int length, FieldLenType fieldLenType) {
        if ((FieldLenType.LONG == fieldLenType)) {
            output.writeShort(length);
        } else {
            output.writeByte(length);
        }
    }

}
