package v02;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;


import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TankStartMovingMsgCodecTest {

    @Test
    public void decode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MyDecode());
        ByteBuf buf = Unpooled.buffer();
        UUID id = UUID.randomUUID();
        buf.writeInt(MsgType.TankStartMovingMsg.ordinal());
        buf.writeInt(28);
        buf.writeInt(200);
        buf.writeInt(100);
        buf.writeInt(Direction.UP.ordinal());
        buf.writeLong(id.getMostSignificantBits());
        buf.writeLong(id.getLeastSignificantBits());

        ch.writeInbound(buf);
        TankStartMovingMsg msg = ch.readInbound();
        assertEquals(200,msg.x);
        assertEquals(100,msg.y);
        assertEquals(Direction.UP,msg.direction);
        assertEquals(id,msg.id);
    }

    @Test
    public void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MyEncode());
        TankStartMovingMsg msg = new TankStartMovingMsg(50,80, Direction.UP,UUID.randomUUID());
        ch.writeOutbound(msg);
        ByteBuf buf = ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        int length = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        Direction direction  = Direction.values()[buf.readInt()];
        UUID id = new UUID(buf.readLong(),buf.readLong());
        assertEquals(MsgType.TankJoinMsg,msgType);
        assertEquals(28,length);
        assertEquals(50,x);
        assertEquals(80,y);
        assertEquals(Direction.UP,direction);
        assertEquals(msg.id,id);
    }
}