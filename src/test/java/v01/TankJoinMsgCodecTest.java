package v01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class TankJoinMsgCodecTest {

    @Test
    public void decode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MyDecode());
        ByteBuf buf = Unpooled.buffer();
        UUID id = UUID.randomUUID();
        buf.writeInt(MsgType.TankJoinMsg.ordinal());
        buf.writeInt(30);
        buf.writeInt(200);
        buf.writeInt(100);
        buf.writeInt(Direction.UP.ordinal());
        buf.writeBoolean(true);
        buf.writeBoolean(true);
        buf.writeLong(id.getMostSignificantBits());
        buf.writeLong(id.getLeastSignificantBits());

        ch.writeInbound(buf);
        TankJoinMsg msg = ch.readInbound();
        assertEquals(200,msg.x);
        assertEquals(100,msg.y);
        assertEquals(Direction.UP,msg.direction);
        assertTrue(msg.group);
        assertTrue(msg.stop);
        assertEquals(id,msg.id);
    }

    @Test
    public void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MyEncode());
        Tank tank = new Tank(500,200, Direction.UP,true,true);
        TankJoinMsg msg = new TankJoinMsg(tank);
        ch.writeOutbound(msg);
        ByteBuf buf = ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        int length = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        Direction direction  = Direction.values()[buf.readInt()];
        buf.readBoolean();
        buf.readBoolean();
        UUID id = new UUID(buf.readLong(),buf.readLong());
        assertEquals(MsgType.TankJoinMsg,msgType);
        assertEquals(30,length);
        assertEquals(500,x);
        assertEquals(200,y);
        assertEquals(Direction.UP,direction);
        assertTrue(true);
        assertTrue(true);
        assertEquals(tank.id,id);
    }
}