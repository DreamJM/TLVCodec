package com.dream.codec.tlv.bean;

import com.dream.codec.tlv.annotation.TLVField;
import com.dream.codec.tlv.annotation.TLVObject;

import java.util.Objects;

/**
 * @author DreamJM
 */
@TLVObject
public class DemoObj {

    @TLVField(tag = 0x1)
    private byte rawByte;

    public DemoObj() {

    }

    public DemoObj(byte rawByte) {
        this.rawByte = rawByte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemoObj demoObj = (DemoObj) o;
        return rawByte == demoObj.rawByte;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawByte);
    }
}
