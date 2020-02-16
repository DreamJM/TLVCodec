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
 * Default Header Codec
 *
 * @author DreamJM
 */
public class HeaderCodec implements IHeaderCodec<Header> {

    @Override
    public void encode(ByteArrayDataOutput dataOutput, short msgType, int contentLength, Header header) throws TLVException {
        dataOutput.writeShort(msgType);
        // message identification should be included in length, so plus 4
        dataOutput.writeShort(contentLength + 4);
        dataOutput.writeInt(header.getId());
    }

    @Override
    public Header decode(ByteArrayDataInput dataInput) throws TLVException {
        short msgType = dataInput.readShort();
        // message identification should be subtract from message length
        int length = dataInput.readShort() - 4;
        int id = dataInput.readInt();
        return new Header(msgType, length, id);
    }
}
