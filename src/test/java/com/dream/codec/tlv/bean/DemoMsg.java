package com.dream.codec.tlv.bean;

import com.dream.codec.tlv.annotation.TLVField;
import com.dream.codec.tlv.annotation.TLVMsgBean;
import com.dream.codec.tlv.field.FieldLenType;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author DreamJM
 */
@TLVMsgBean(type = 1)
public class DemoMsg extends BaseMsg {

    @TLVField(tag = 0x01)
    private byte rawByte;

    @TLVField(tag = 0x02)
    private Byte wrapByte;

    @TLVField(tag = 0x03)
    private short rawShort;

    @TLVField(tag = 0x04)
    private Short wrapShort;

    @TLVField(tag = 0x05)
    private int rawInt;

    @TLVField(tag = 0x06)
    private Integer wrapInt;

    @TLVField(tag = 0x07)
    private long rawLong;

    @TLVField(tag = 0x08)
    private Long wrapLong;

    @TLVField(tag = 0x09, fieldLenType = FieldLenType.LONG)
    private String str;

    @TLVField(tag = 0x0a)
    private byte[] rawBytes;

    /**
     * For List or Array Type, there should be two TLVField Annotations. First one is for wrap Array, second one is for wrapped TLV Field
     */
    @TLVField(tag = 0x0b)
    @TLVField(tag = 0x0c)
    private Byte[] wrapBytes;

    @TLVField(tag = 0x0d)
    @TLVField(tag = 0x0e)
    private short[] rawShorts;

    @TLVField(tag = 0x0f)
    @TLVField(tag = 0x10)
    private Short[] wrapShorts;

    @TLVField(tag = 0x11)
    @TLVField(tag = 0x12)
    private int[] rawInts;

    @TLVField(tag = 0x13)
    @TLVField(tag = 0x14)
    private Integer[] wrapInts;

    @TLVField(tag = 0x15)
    @TLVField(tag = 0x16)
    private long[] rawLongs;

    @TLVField(tag = 0x17)
    @TLVField(tag = 0x18)
    private Long[] wrapLongs;

    @TLVField(tag = 0x19)
    @TLVField(tag = 0x1a)
    private String[] strs;

    @TLVField(tag = 0x1b)
    @TLVField(tag = 0x1c)
    private List<Byte> listBytes;

    @TLVField(tag = 0x1d)
    @TLVField(tag = 0x1e)
    private List<Short> listShorts;

    @TLVField(tag = 0x1f)
    @TLVField(tag = 0x20)
    private List<Integer> listInts;

    @TLVField(tag = 0x21)
    @TLVField(tag = 0x22)
    private List<Long> listLongs;

    @TLVField(tag = 0x23)
    @TLVField(tag = 0x24)
    private List<String> listStrs;

    @TLVField(tag = 0x25)
    private DemoObj obj;

    @TLVField(tag = 0x26)
    @TLVField(tag = 0x27)
    private DemoObj[] objs;

    @TLVField(tag = 0x28)
    @TLVField(tag = 0x29)
    private List<DemoObj> listObjs;

    public DemoMsg() {

    }

    public DemoMsg(String baseString, int baseInt, int identity) {
        super(baseString, baseInt);
        setIdentification(identity);
        rawByte = 1;
        wrapByte = 2;
        rawShort = 3;
        wrapShort = 4;
        rawInt = 5;
        wrapInt = 6;
        rawLong = 7L;
        wrapLong = 8L;
        str = "hello";
        rawBytes = new byte[]{1, 2, 3, 4};
        wrapBytes = new Byte[]{1, 2, 3, 4};
        rawShorts = new short[]{1, 2, 3, 4, 5};
        wrapShorts = new Short[]{1, 2, 3, 4, 5};
        rawInts = new int[]{1, 2, 3, 4, 5, 6};
        wrapInts = new Integer[]{1, 2, 3, 4, 5, 6};
        rawLongs = new long[]{1, 2, 3, 4, 5, 6, 7};
        wrapLongs = new Long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L};
        strs = new String[]{"hello", "world"};
        listBytes = Lists.newArrayList(new Byte[]{1, 2, 3, 4});
        listShorts = Lists.newArrayList(new Short[]{1, 2, 3, 4});
        listInts = Lists.newArrayList(1, 2, 3, 4);
        listLongs = Lists.newArrayList(1L, 2L, 3L, 4L);
        listStrs = Lists.newArrayList("hello", "world", "too");
        obj = new DemoObj((byte) 88);
        objs = new DemoObj[]{new DemoObj((byte) 1), new DemoObj((byte) 2), new DemoObj((byte) 3)};
        listObjs = Lists.newArrayList(new DemoObj((byte) 1), new DemoObj((byte) 2), new DemoObj((byte) 3));
    }

    public byte getRawByte() {
        return rawByte;
    }

    public Byte getWrapByte() {
        return wrapByte;
    }

    public short getRawShort() {
        return rawShort;
    }

    public Short getWrapShort() {
        return wrapShort;
    }

    public int getRawInt() {
        return rawInt;
    }

    public Integer getWrapInt() {
        return wrapInt;
    }

    public long getRawLong() {
        return rawLong;
    }

    public Long getWrapLong() {
        return wrapLong;
    }

    public String getStr() {
        return str;
    }

    public byte[] getRawBytes() {
        return rawBytes;
    }

    public Byte[] getWrapBytes() {
        return wrapBytes;
    }

    public short[] getRawShorts() {
        return rawShorts;
    }

    public Short[] getWrapShorts() {
        return wrapShorts;
    }

    public int[] getRawInts() {
        return rawInts;
    }

    public Integer[] getWrapInts() {
        return wrapInts;
    }

    public long[] getRawLongs() {
        return rawLongs;
    }

    public Long[] getWrapLongs() {
        return wrapLongs;
    }

    public String[] getStrs() {
        return strs;
    }

    public List<Byte> getListBytes() {
        return listBytes;
    }

    public List<Short> getListShorts() {
        return listShorts;
    }

    public List<Integer> getListInts() {
        return listInts;
    }

    public List<Long> getListLongs() {
        return listLongs;
    }

    public List<String> getListStrs() {
        return listStrs;
    }

    public DemoObj getObj() {
        return obj;
    }

    public DemoObj[] getObjs() {
        return objs;
    }

    public List<DemoObj> getListObjs() {
        return listObjs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DemoMsg demoMsg = (DemoMsg) o;
        return rawByte == demoMsg.rawByte &&
                rawShort == demoMsg.rawShort &&
                rawInt == demoMsg.rawInt &&
                rawLong == demoMsg.rawLong &&
                Objects.equals(wrapByte, demoMsg.wrapByte) &&
                Objects.equals(wrapShort, demoMsg.wrapShort) &&
                Objects.equals(wrapInt, demoMsg.wrapInt) &&
                Objects.equals(wrapLong, demoMsg.wrapLong) &&
                Objects.equals(str, demoMsg.str) &&
                Arrays.equals(rawBytes, demoMsg.rawBytes) &&
                Arrays.equals(wrapBytes, demoMsg.wrapBytes) &&
                Arrays.equals(rawShorts, demoMsg.rawShorts) &&
                Arrays.equals(wrapShorts, demoMsg.wrapShorts) &&
                Arrays.equals(rawInts, demoMsg.rawInts) &&
                Arrays.equals(wrapInts, demoMsg.wrapInts) &&
                Arrays.equals(rawLongs, demoMsg.rawLongs) &&
                Arrays.equals(wrapLongs, demoMsg.wrapLongs) &&
                Arrays.equals(strs, demoMsg.strs) &&
                Objects.equals(listBytes, demoMsg.listBytes) &&
                Objects.equals(listShorts, demoMsg.listShorts) &&
                Objects.equals(listInts, demoMsg.listInts) &&
                Objects.equals(listLongs, demoMsg.listLongs) &&
                Objects.equals(listStrs, demoMsg.listStrs) &&
                Objects.equals(obj, demoMsg.obj) &&
                Arrays.equals(objs, demoMsg.objs) &&
                Objects.equals(listObjs, demoMsg.listObjs);
    }

    @Override
    public int hashCode() {

        int result =
                Objects.hash(super.hashCode(), rawByte, wrapByte, rawShort, wrapShort, rawInt, wrapInt, rawLong, wrapLong, str, listBytes,
                        listShorts, listInts, listLongs, listStrs, obj, listObjs);
        result = 31 * result + Arrays.hashCode(rawBytes);
        result = 31 * result + Arrays.hashCode(wrapBytes);
        result = 31 * result + Arrays.hashCode(rawShorts);
        result = 31 * result + Arrays.hashCode(wrapShorts);
        result = 31 * result + Arrays.hashCode(rawInts);
        result = 31 * result + Arrays.hashCode(wrapInts);
        result = 31 * result + Arrays.hashCode(rawLongs);
        result = 31 * result + Arrays.hashCode(wrapLongs);
        result = 31 * result + Arrays.hashCode(strs);
        result = 31 * result + Arrays.hashCode(objs);
        return result;
    }
}
