package com.dream.codec.tlv;

import com.dream.codec.tlv.bean.DemoMsg;
import com.dream.codec.tlv.codec.TLVCodec;
import com.dream.codec.tlv.entity.Header;
import com.dream.codec.tlv.entity.RawTLVMsg;
import com.dream.codec.tlv.exception.TLVException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author DreamJM
 */
public class DemoTest {

    @Test
    void useDemo() throws TLVException {
        TLVContext context = new TLVContext("com.dream.codec.tlv");
        TLVCodec codec = new TLVCodec(context);

        DemoMsg msg = new DemoMsg("hello", 1, 123456);
        byte[] encodeResult = codec.encode(msg);

        DemoMsg decodedMsg = codec.convertTo(codec.decode(encodeResult), DemoMsg.class);
        assertEquals(decodedMsg, msg);

        RawTLVMsg<Header> preDecode = codec.preDecode(encodeResult);
        assertFalse(preDecode.isDecoded());
        assertEquals(123456, preDecode.getHeader().getId());
        assertEquals(1, (int) preDecode.getHeader().getType());
    }

}
