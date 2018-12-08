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

    BYTE, SHORT, INT, LONG, RAW, STRING, ARRAY, LIST, OBJECT;

    @SuppressWarnings("serial")
    private static final Map<Class<?>, FieldType> fieldTypeMap = ImmutableMap.copyOf(new HashMap<Class<?>, FieldType>() {
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
            if (byte.class == clazz.getComponentType()) { // byte[] is decode as byte stream(RAW type)
                return RAW;
            } else {
                return ARRAY;
            }
        }
        return fieldTypeMap.get(clazz);
    }

}
