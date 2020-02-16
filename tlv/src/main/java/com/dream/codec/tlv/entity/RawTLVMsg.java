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

package com.dream.codec.tlv.entity;

import com.google.common.io.ByteArrayDataInput;

/**
 * Raw TLV Message
 *
 * @param <T> Header Type
 * @author DreamJM
 */
public class RawTLVMsg<T extends Header> {

    /**
     * TLV Message Header
     */
    T header;

    /**
     * Remained data after Header pre-decoding
     */
    private ByteArrayDataInput dataInput;

    /**
     * @return TLV Message Header
     */
    public T getHeader() {
        return header;
    }

    /**
     * TLV Message Header
     *
     * @param header TLV Message Header to set
     */
    public void setHeader(T header) {
        this.header = header;
    }

    /**
     * @return Remained data after Header pre-decoding
     */
    public ByteArrayDataInput getDataInput() {
        return dataInput;
    }

    /**
     * Set remained data after Header pre-decoding
     *
     * @param dataInput remained data after Header pre-decoding
     */
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
