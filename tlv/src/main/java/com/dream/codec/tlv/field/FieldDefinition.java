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

package com.dream.codec.tlv.field;

import com.dream.codec.tlv.annotation.TLVField;

import java.lang.reflect.Field;

/**
 * Field Definition for TLV Message
 *
 * @author DreamJM
 */
public class FieldDefinition {

    /**
     * TLV field annotation
     */
    private TLVField annotation;

    /**
     * TLV field type
     */
    private FieldType fieldType;

    /**
     * JAVA Field of corresponding TLV field
     */
    private Field rawField;

    /**
     * Raw Type of JAVA Field
     */
    private Class<?> rawType;

    /**
     * Sub Field Definition for LIST or Array JAVA Field Type
     */
    private FieldDefinition subFieldDef;

    public FieldDefinition(TLVField annotation, FieldType fieldType, Field rawField) {
        this.annotation = annotation;
        this.fieldType = fieldType;
        this.rawField = rawField;
        // Set Accessible to true in advance to handle private parameter
        rawField.setAccessible(true);
        this.rawType = rawField.getType();
    }

    public FieldDefinition(TLVField annotation, FieldType fieldType, Field rawField, TLVField subAnnotation, FieldType subFieldType,
                           Class<?> subType) {
        this(annotation, fieldType, rawField);
        subFieldDef = new FieldDefinition(subAnnotation, subFieldType, subType);
    }

    /**
     * For List or Array JAVA Field Type's Sub Field Definition
     *
     * @param annotation Field Annotation
     * @param fieldType  Sub Field Type
     * @param rawType    Sub Field Raw Type
     */
    private FieldDefinition(TLVField annotation, FieldType fieldType, Class<?> rawType) {
        this.annotation = annotation;
        this.fieldType = fieldType;
        this.rawType = rawType;
    }

    /**
     * @return TLVField annotation information
     */
    public TLVField getAnnotation() {
        return annotation;
    }

    /**
     * @return TLV field type
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    /**
     * @return JAVA Field of corresponding TLV field
     */
    public Field getRawField() {
        return rawField;
    }

    /**
     * @return raw type of JAVA Field
     */
    public Class<?> getRawType() {
        return rawType;
    }

    /**
     * @return Sub Field Definition for LIST or Array JAVA Field Type
     */
    public FieldDefinition getSubFieldDef() {
        return subFieldDef;
    }
}
