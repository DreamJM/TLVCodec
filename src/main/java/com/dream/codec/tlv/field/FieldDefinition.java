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

    public TLVField getAnnotation() {
        return annotation;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public Field getRawField() {
        return rawField;
    }

    public Class<?> getRawType() {
        return rawType;
    }

    public FieldDefinition getSubFieldDef() {
        return subFieldDef;
    }
}
