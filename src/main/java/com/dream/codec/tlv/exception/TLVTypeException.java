package com.dream.codec.tlv.exception;

/**
 * @author DreamJM
 */
public class TLVTypeException extends TLVException {

    private static final long serialVersionUID = -4610946792814759982L;

    public TLVTypeException(String message) {
        super(message);
    }

    public TLVTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
