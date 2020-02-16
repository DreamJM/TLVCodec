package com.dream.codec.tlv.entity;

/**
 * TLV Message for Default Header
 *
 * @author DreamJM
 */
public class TLVMsg extends RawTLVMsg<Header> {

    /**
     * Set Message id before sending
     *
     * @param identification message id
     */
    public void setIdentification(int identification) {
        if (header == null) {
            header = new Header(identification);
        } else {
            header.setId(identification);
        }
    }

    /**
     * @return message id
     */
    public Integer getIdentification() {
        return header == null ? null : header.getId();
    }

}
