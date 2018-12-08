package com.dream.codec.tlv.entity;

/**
 * Default TLV Message Header
 *
 * @author DreamJM
 */
public class Header {

    /**
     * TLV Message Type
     */
    private short type;

    /**
     * TLV Message Length
     */
    private int length;

    /**
     * Message ID
     */
    private int id;

    /**
     * true if is response message
     */
    private boolean response;

    Header(int id) {
        this.id = id;
    }

    public Header(short type, int length, int id) {
        this.type = type;
        this.length = length;
        this.id = id;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}
