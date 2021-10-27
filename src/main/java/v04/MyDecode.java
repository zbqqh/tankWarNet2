package v04;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 8)
            return;
        in.markReaderIndex();
        MsgType msgType = MsgType.values()[in.readInt()];
        int length = in.readInt();
        if (in.readableBytes() < length){
            in.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        System.out.println("MyDecode-" + msgType.toString());
        Msg msg = (Msg) Class.forName("v04." + msgType.toString()).getDeclaredConstructor()
                .newInstance();
        msg.parse(bytes);
        out.add(msg);
        //out.add(new TankMsg(x,y));
    }
}
