package com.dream.codec.tlv.codec;

import com.dream.codec.tlv.TLVContext;
import com.dream.codec.tlv.annotation.TLVMsgBean;
import com.dream.codec.tlv.entity.Header;
import com.dream.codec.tlv.entity.RawTLVMsg;
import com.dream.codec.tlv.exception.TLVException;
import com.dream.codec.tlv.exception.TLVTypeException;
import com.dream.codec.tlv.header.IHeaderCodec;
import com.dream.codec.tlv.msg.ContentDecoder;
import com.dream.codec.tlv.msg.ContentEncoder;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TLV Codec for Generic Header
 * <p>
 * To customize TLV Message Header, IHeaderCodec interface can be implemented and passed through constructor parameter
 *
 * @author DreamJM
 */
public class RawTLVCodec<T extends Header> {

    private static final Logger logger = LoggerFactory.getLogger(RawTLVMsg.class);

    private TLVContext context;

    private IHeaderCodec<T> headerCodec;

    private ContentEncoder encoder;

    private ContentDecoder decoder;

    public RawTLVCodec(TLVContext context, IHeaderCodec<T> headerCodec) {
        this.context = context;
        this.headerCodec = headerCodec;
        this.encoder = new ContentEncoder(context);
        this.decoder = new ContentDecoder(context);
    }

    /**
     * Start message decoding
     *
     * @param msg TLV Message Object
     * @return encoded bytes
     * @throws TLVException encoding exception
     */
    public byte[] encode(RawTLVMsg<T> msg) throws TLVException {
        Class<?> clazz = msg.getClass();
        if (!clazz.isAnnotationPresent(TLVMsgBean.class)) {
            throw new TLVTypeException("Unsupported TLV Message Type with class: " + clazz.getName() + ", missing TLVMsgBean annotation");
        }
        TLVMsgBean tlvMsg = clazz.getAnnotation(TLVMsgBean.class);
        short msgType = tlvMsg.type();

        Class<?> registeredClass = context.getMsgClass(msgType);
        if (registeredClass != clazz) {
            throw new TLVTypeException(
                    "Message Type " + msgType + " is registered with class " + registeredClass.getName() + ", but found " + clazz
                            .getName());
        }

        T header = msg.getHeader();
        if (header == null) {
            throw new TLVException("TLV Msg(Type:" + msgType + ") has no header, please initialize it first!");
        }
        header.setType(msgType);
        header.setResponse(tlvMsg.isResponse());

        logger.debug("Start encoding class {} with type {}", clazz, msgType);
        byte[] contentBytes = encoder.encode(msg);
        ByteArrayDataOutput result = ByteStreams.newDataOutput(contentBytes.length + 16);
        // 消息头域准备
        headerCodec.encode(result, msgType, contentBytes.length, msg.getHeader());
        result.write(contentBytes);
        return result.toByteArray();
    }

    /**
     * Decode Whole Message
     *
     * @param value Message Raw Bytes
     * @return TLV Message Decode Result
     * @throws TLVException Decode Exception
     */
    public RawTLVMsg<T> decode(byte[] value) throws TLVException {
        return bodyDecode(preDecode(value));
    }

    /**
     * TLV Message Header PreDecode
     *
     * @param value TLV Raw Message Byte
     * @return PreDecoded Message
     * @throws TLVException Decode Exception
     */
    public RawTLVMsg<T> preDecode(byte[] value) throws TLVException {
        ByteArrayDataInput input = ByteStreams.newDataInput(value);
        T header = headerCodec.decode(input);
        Class<?> registeredClass = context.getMsgClass(header.getType());
        if (registeredClass == null) {
            logger.error("Unknown TLV Message Type {}", header.getType());
            throw new TLVException("Unknown message type: " + header.getType());
        }
        TLVMsgBean tlvMsg = registeredClass.getAnnotation(TLVMsgBean.class);
        header.setResponse(tlvMsg.isResponse());
        RawTLVMsg<T> preResult = new RawTLVMsg<>();
        preResult.setHeader(header);
        preResult.setDataInput(input);
        logger.debug("Pre-Decoding complete, got Message Type {} and Identification {}", header.getType(), header.getId());
        return preResult;
    }

    /**
     * Decode TLV Message Body
     *
     * @param preResult Header PreDecoding Result
     * @return Decoded Message
     * @throws TLVException Decode Exception
     */
    @SuppressWarnings("unchecked")
    public RawTLVMsg<T> bodyDecode(RawTLVMsg<T> preResult) throws TLVException {
        T header = preResult.getHeader();
        logger.debug("Start content decoding for Message Type {} and Identification {}", header.getType(), header.getId());
        RawTLVMsg<T> result = (RawTLVMsg<T>) decoder.bodyDecode(preResult);
        result.setHeader(header);
        return result;
    }

    /**
     * Convert RawTLVMessage to Target Class
     *
     * @param msg         Raw TLV Message
     * @param targetClass Target Class
     * @param <G>         Target Generic Type
     * @return Converted Object
     * @throws TLVException Convert Exception
     */
    @SuppressWarnings("unchecked")
    public <G> G convertTo(RawTLVMsg<?> msg, Class<G> targetClass) throws TLVException {
        short msgType = msg.getHeader().getType();
        Class<?> registeredClass = context.getMsgClass(msgType);
        if (!targetClass.equals(registeredClass)) {
            throw new TLVException("Can't convert msg to " + targetClass.getName() + ", registered class is " + registeredClass.getName());
        }
        return (G) msg;
    }

}
