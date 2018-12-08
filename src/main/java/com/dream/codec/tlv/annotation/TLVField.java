package com.dream.codec.tlv.annotation;

import com.dream.codec.tlv.field.FieldLenType;
import com.dream.codec.tlv.field.TLVCharset;

import java.lang.annotation.*;

/**
 * Annotation to Mark TLV Field
 *
 * @author DreamJM
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(TLVFields.class)
public @interface TLVField {

    /**
     * @return Tag Value
     */
    short tag();

    /**
     * @return sequence number of tlv while sending
     */
    int index() default 5;

    /**
     * @return whether this field is required while encoding
     */
    boolean require() default false;

    /**
     * @return Charset for 'String' field decoding
     */
    TLVCharset charset() default TLVCharset.UTF_8;

    /**
     * @return Field Type(Long or Short)
     */
    FieldLenType fieldLenType() default FieldLenType.SHORT;

}
