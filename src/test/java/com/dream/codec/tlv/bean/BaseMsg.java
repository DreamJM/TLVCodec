package com.dream.codec.tlv.bean;

import com.dream.codec.tlv.annotation.TLVField;
import com.dream.codec.tlv.entity.TLVMsg;
import com.dream.codec.tlv.field.TLVCharset;

import java.util.Objects;

/**
 * @author DreamJM
 */
public class BaseMsg extends TLVMsg {

    @TLVField(tag = 0xD0, index = 999, require = true, charset = TLVCharset.ASCII)
    private String baseString;

    @TLVField(tag = 0xD1, index = -1, require = true)
    private int baseInt;

    public BaseMsg() {

    }

    public BaseMsg(String baseString, int baseInt) {
        this.baseString = baseString;
        this.baseInt = baseInt;
    }

    public String getBaseString() {
        return baseString;
    }

    public int getBaseInt() {
        return baseInt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMsg baseMsg = (BaseMsg) o;
        return baseInt == baseMsg.baseInt &&
                Objects.equals(baseString, baseMsg.baseString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseString, baseInt);
    }
}
