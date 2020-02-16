/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dream.codec.tlv.field;

import com.google.common.base.Charsets;

import java.nio.charset.Charset;

/**
 * TLV Charsets for String type TLV Field
 *
 * @author DreamJM
 */
public enum TLVCharset {

    /**
     * Supported TLV Charsets for String type TLV Field
     */
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
