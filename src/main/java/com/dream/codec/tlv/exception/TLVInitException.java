package com.dream.codec.tlv.exception;

/**
 * @author DreamJM
 */
public class TLVInitException extends TLVException {

    private static final long serialVersionUID = 3907520056977154773L;

    public TLVInitException(String message) {
        super(message);
    }

    public TLVInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
