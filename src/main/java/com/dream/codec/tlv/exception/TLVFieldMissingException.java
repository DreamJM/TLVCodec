package com.dream.codec.tlv.exception;

/**
 * @author DreamJM
 */
public class TLVFieldMissingException extends TLVException {

    private static final long serialVersionUID = -9020163999592586484L;

    public TLVFieldMissingException(String message) {
        super(message);
    }

    public TLVFieldMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
