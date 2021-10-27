package v08;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyEncode extends MessageToByteEncoder<Msg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
        MsgType msgType = msg.getMsgType();
        out.writeInt(msgType.ordinal());
        byte[] bytes = msg.toBytes();
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
