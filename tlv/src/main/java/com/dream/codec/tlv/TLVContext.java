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

package com.dream.codec.tlv;

import com.dream.codec.tlv.annotation.TLVField;
import com.dream.codec.tlv.annotation.TLVFields;
import com.dream.codec.tlv.annotation.TLVMsgBean;
import com.dream.codec.tlv.annotation.TLVObject;
import com.dream.codec.tlv.entity.RawTLVMsg;
import com.dream.codec.tlv.exception.TLVInitException;
import com.dream.codec.tlv.field.FieldDefinition;
import com.dream.codec.tlv.field.FieldType;
import com.dream.codec.tlv.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TLV Codec Context
 *
 * @author DreamJM
 */
public class TLVContext {

    private static final Logger logger = LoggerFactory.getLogger(TLVContext.class);

    private Map<Short, Class<?>> msgTypeMap = new HashMap<>();

    private Map<Short, Class<?>> fieldTypeMap = new HashMap<>();

    private Map<Class<?>, List<FieldDefinition>> msgMap = new HashMap<>();

    private Map<Class<?>, List<FieldDefinition>> complexObjMap = new HashMap<>();

    public TLVContext(String... packageNames) throws TLVInitException {
        for (String packageName : packageNames) {
            logger.info("Scanning package {} for TLV Msg Bean...", packageName);
            List<Class<?>> classList = ReflectionUtils.getClasses(packageName);
            for (Class<?> clazz : classList) {
                if (clazz.isAnnotationPresent(TLVMsgBean.class)) {
                    TLVMsgBean tlvMsg = clazz.getAnnotation(TLVMsgBean.class);
                    if (msgTypeMap.containsKey(tlvMsg.type())) {
                        throw new TLVInitException("Duplicate TLV Msg Bean with type " + tlvMsg.type() + " found!");
                    }
                    if (!RawTLVMsg.class.isAssignableFrom(clazz)) {
                        throw new TLVInitException("TLV Msg Bean isn't extended from RawTLVMsg");
                    }
                    logger.debug("TLV Msg Bean {} with Type {} detected, start parsing fields...", clazz.getName(), tlvMsg.type());
                    msgTypeMap.put(tlvMsg.type(), clazz);
                    msgMap.put(clazz,
                            parseFields(clazz).stream().sorted(Comparator.comparingInt(fieldDef -> fieldDef.getAnnotation().index()))
                                    .collect(Collectors.toList()));
                }
            }
        }
        logger.info("TLV Msg Context initialization success");
    }

    private List<FieldDefinition> parseFields(Class<?> clazz) throws TLVInitException {
        Field[] fields = ReflectionUtils.loadAllFields(clazz);
        List<FieldDefinition> fds = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TLVField.class) || field.isAnnotationPresent(TLVFields.class)) {
                Class<?> type = field.getType();
                FieldType fieldType = FieldType.parseRawFieldType(type);
                // Raw Type except TLV Object
                if (fieldType != null) {
                    if (FieldType.ARRAY == fieldType || FieldType.LIST == fieldType) {
                        // Parse Sub Type
                        TLVFields tlvFields = field.getAnnotation(TLVFields.class);
                        if (tlvFields == null || tlvFields.value().length < 2) {
                            throw new TLVInitException(
                                    "Multiple TLVField Annotation should be add to List or Array field: " + field.getName());
                        }
                        TLVField tlvField = tlvFields.value()[0];
                        TLVField subTlvField = tlvFields.value()[1];
                        logger.debug("Find TLV Field {} with Tag {} and Type {}", field.getName(), tlvField.tag(), fieldType.name());
                        Class<?> subType = ReflectionUtils.parseSubType(field);
                        if (subType == null) {
                            throw new TLVInitException("Parse Array or List Sub Field Type failure: " + field.getName());
                        }
                        FieldType subFieldType = FieldType.parseRawFieldType(subType);
                        if (subFieldType != null && FieldType.LIST != subFieldType && FieldType.ARRAY != subFieldType) {
                            // List or Array Inside List or Array is unsupported
                            fds.add(checkFieldConflict(
                                    new FieldDefinition(tlvField, fieldType, field, subTlvField, subFieldType, subType)));
                        } else if (subType.isAnnotationPresent(TLVObject.class)) {
                            complexObjMap.put(subType, parseFields(subType));
                            fds.add(checkFieldConflict(
                                    new FieldDefinition(tlvField, fieldType, field, subTlvField, FieldType.OBJECT, subType)));
                        } else {
                            logger.error("Unsupported Sub Field Type {}", subType);
                            throw new TLVInitException("Unsupported Sub Field Type " + subType);
                        }
                    } else {
                        TLVField tlvField = field.getAnnotation(TLVField.class);
                        logger.debug("Find TLV Field {} with Tag {} and Type {}", field.getName(), tlvField.tag(), fieldType.name());
                        fds.add(checkFieldConflict(new FieldDefinition(tlvField, fieldType, field)));
                    }
                } else {
                    TLVField tlvField = field.getAnnotation(TLVField.class);
                    if (!type.isAnnotationPresent(TLVObject.class)) {
                        logger.error("Unsupported Field Type: {}", type);
                        throw new TLVInitException("Unsupported Field Type: " + type);
                    }
                    logger.debug("Find Object Type TLV Field {} with tag {} and class {}", field.getName(), tlvField.tag(),
                            field.getType());
                    complexObjMap.put(type,
                            parseFields(type).stream().sorted(Comparator.comparingInt(fieldDef -> fieldDef.getAnnotation().index()))
                                    .collect(Collectors.toList()));
                    fds.add(checkFieldConflict(new FieldDefinition(tlvField, FieldType.OBJECT, field)));
                }
            }
        }
        return fds;
    }


    /**
     * Check if field definition conflict
     *
     * @param fieldDefinition TLV Field Definition
     * @return TLV Field
     * @throws TLVInitException TLV fields conflict exception
     */
    private FieldDefinition checkFieldConflict(FieldDefinition fieldDefinition) throws TLVInitException {
        Class<?> fieldClass = fieldTypeMap.get(fieldDefinition.getAnnotation().tag());
        if (fieldClass == null) {
            fieldTypeMap.put(fieldDefinition.getAnnotation().tag(), fieldDefinition.getRawType());
        } else if (fieldClass != fieldDefinition.getRawType()) {
            throw new TLVInitException("Find conflict tag " + fieldDefinition.getAnnotation().tag() + " with different type");
        }
        if (fieldDefinition.getSubFieldDef() != null) {
            checkFieldConflict(fieldDefinition.getSubFieldDef());
        }
        return fieldDefinition;
    }

    /**
     * Get Message Class By Message Type
     *
     * @param type Msg Type
     * @return Message Class
     */
    public Class<?> getMsgClass(short type) {
        return msgTypeMap.get(type);
    }

    /**
     * Get Field Definitions inside specific TLV Message Class
     *
     * @param clazz TLV Message Class
     * @return Field Definition List
     */
    public List<FieldDefinition> getMsgDefinition(Class<?> clazz) {
        return msgMap.get(clazz);
    }

    /**
     * Get Field Definitions inside specific TLV Object Class
     *
     * @param clazz TLV Object Class
     * @return Field Definition List
     */
    public List<FieldDefinition> getObjDefinition(Class<?> clazz) {
        return complexObjMap.get(clazz);
    }

}
