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
