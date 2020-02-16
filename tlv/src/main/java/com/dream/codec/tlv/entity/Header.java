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
     * Whether the message is a response message
     */
    private boolean response;

    public Header(int id) {
        this.id = id;
    }

    public Header(short type, int length, int id) {
        this.type = type;
        this.length = length;
        this.id = id;
    }

    /**
     * @return TLV Message Type
     */
    public short getType() {
        return type;
    }

    /**
     * TLV Message Type
     *
     * @param type TLV Message Type to set
     */
    public void setType(short type) {
        this.type = type;
    }

    /**
     * @return TLV Message Length
     */
    public int getLength() {
        return length;
    }

    /**
     * @return Message ID
     */
    public int getId() {
        return id;
    }

    /**
     * Set Message ID
     *
     * @param id Message ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Whether the message is a response message
     */
    public boolean isResponse() {
        return response;
    }

    /**
     * Whether the message is a response message
     *
     * @param response true if it's a response message
     */
    public void setResponse(boolean response) {
        this.response = response;
    }
}
