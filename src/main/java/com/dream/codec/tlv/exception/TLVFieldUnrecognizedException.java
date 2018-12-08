package com.dream.codec.tlv.exception;

/**
 * @author DreamJM
 */
public class TLVFieldUnrecognizedException extends TLVException {

    private static final long serialVersionUID = 7083442456586829979L;

    public TLVFieldUnrecognizedException(String message) {
        super(message);
    }

    public TLVFieldUnrecognizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
