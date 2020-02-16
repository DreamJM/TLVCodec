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
