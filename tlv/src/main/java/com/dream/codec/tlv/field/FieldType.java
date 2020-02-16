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

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Field Type for TLV Field
 *
 * @author DreamJM
 */
public enum FieldType {

    /**
     * Message type enums
     */
    BYTE, SHORT, INT, LONG, RAW, STRING, ARRAY, LIST, OBJECT;

    @SuppressWarnings("serial")
    private static final Map<Class<?>, FieldType> FIELD_TYPE_MAP = ImmutableMap.copyOf(new HashMap<Class<?>, FieldType>() {
        {
            put(Byte.class, BYTE);
            put(byte.class, BYTE);
            put(Short.class, SHORT);
            put(short.class, SHORT);
            put(Integer.class, INT);
            put(int.class, INT);
            put(Long.class, LONG);
            put(long.class, LONG);
            put(String.class, STRING);
            put(List.class, LIST);
        }
    });

    /**
     * Parse Field Type from Field class except for TLV Object
     *
     * @param clazz Field Class
     * @return Field Type
     */
    public static FieldType parseRawFieldType(Class<?> clazz) {
        if (clazz.isArray()) {
            if (byte.class == clazz.getComponentType()) {
                // byte[] is decode as byte stream(RAW type)
                return RAW;
            } else {
                return ARRAY;
            }
        }
        return FIELD_TYPE_MAP.get(clazz);
    }

}
