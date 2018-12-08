package com.dream.codec.tlv.codec;

import com.dream.codec.tlv.TLVContext;
import com.dream.codec.tlv.entity.Header;
import com.dream.codec.tlv.header.HeaderCodec;

/**
 * TLV Codec for Default Header
 *
 * @author DreamJM
 */
public class TLVCodec extends RawTLVCodec<Header> {

    public TLVCodec(TLVContext context) {
        super(context, new HeaderCodec());
    }
}
