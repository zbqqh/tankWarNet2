/*package v05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MissileMsgCodecTest {

    @Test
    public void decode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MyDecode());
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(MsgType.MissileHitTankMsg.ordinal());
        buf.writeInt(8);
        buf.writeInt(200);
        buf.writeInt(100);
        ch.writeInbound(buf);
        MissileMsg msg = ch.readInbound();
        assertEquals(200,msg.x);
        assertEquals(100,msg.y);

    }

    @Test
    public void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MyEncode());
        MissileMsg msg = new MissileMsg(50,80);
        ch.writeOutbound(msg);
        ByteBuf buf = ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        int length = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        assertEquals(MsgType.MissileMsg, msgType);
        assertEquals(45,length);
        assertEquals(50,x);
        assertEquals(80,y);
    }
}*/