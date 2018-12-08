package com.dream.codec.tlv.field;

import com.google.common.base.Charsets;

import java.nio.charset.Charset;

/**
 * TLV Charsets for String type TLV Field
 *
 * @author DreamJM
 */
public enum TLVCharset {

    ASCII(Charsets.US_ASCII), UTF_8(Charsets.UTF_8), ISO8859(Charsets.ISO_8859_1), UTF_16(Charsets.UTF_16),
    GB2312(Charset.forName("gb2312")), UTF_16BE(Charsets.UTF_16BE), UTF_16LE(Charsets.UTF_16LE);

    private Charset charset;

    TLVCharset(Charset charset) {
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }
}
