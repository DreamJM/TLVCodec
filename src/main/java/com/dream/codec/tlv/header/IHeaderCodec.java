package com.dream.codec.tlv.header;

import com.dream.codec.tlv.entity.Header;
import com.dream.codec.tlv.exception.TLVException;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

/**
 * TLV Message Header Codec Interface
 *
 * @author DreamJM
 */
public interface IHeaderCodec<T extends Header> {

    /**
     * Encode TLV Message Header
     *
     * @param dataOutput    Data Output Stream
     * @param msgType       TLV Message Type
     * @param contentLength TLV Message Content Length
     * @param header        TLV Message Header
     * @throws TLVException TLV Encoding Exception
     */
    void encode(ByteArrayDataOutput dataOutput, short msgType, int contentLength, T header) throws TLVException;

    /**
     * Decode TLV Message Header
     *
     * @param dataInput Data Raw Input Stream
     * @return T Decoded Header
     * @throws TLVException TLV Decoding Exception
     */
    T decode(ByteArrayDataInput dataInput) throws TLVException;

}
