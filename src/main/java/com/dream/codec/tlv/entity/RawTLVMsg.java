package com.dream.codec.tlv.entity;

import com.google.common.io.ByteArrayDataInput;

/**
 * Raw TLV Message
 *
 * @author DreamJM
 */
public class RawTLVMsg<T extends Header> {

    /**
     * TLV Message Header
     */
    T header;

    /**
     * Remaining data after Header pre-decoding
     */
    private ByteArrayDataInput dataInput;

    public T getHeader() {
        return header;
    }

    public void setHeader(T header) {
        this.header = header;
    }

    public ByteArrayDataInput getDataInput() {
        return dataInput;
    }

    public void setDataInput(ByteArrayDataInput dataInput) {
        this.dataInput = dataInput;
    }

    /**
     * @return true if totally decoded
     */
    public boolean isDecoded() {
        return dataInput == null;
    }
}
