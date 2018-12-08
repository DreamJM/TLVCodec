package com.dream.codec.tlv.exception;

/**
 * @author DreamJM
 */
public class TLVException extends Exception {

    private static final long serialVersionUID = -9135045494381222379L;

    public TLVException(String message) {
        super(message);
    }

    public TLVException(String message, Throwable cause) {
        super(message, cause);
    }
}
