package com.dream.codec.tlv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TLV Message Bean Annotation
 *
 * @author DreamJM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TLVMsgBean {

    /**
     * @return TLV Message Type
     */
    short type();

    /**
     * @return true if TLV Message is response
     */
    boolean isResponse() default false;

}
