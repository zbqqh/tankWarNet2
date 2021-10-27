package v04;

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
        UUID id = UUID.randomUUID();
        UUID tid = UUID.randomUUID();
        buf.writeInt(MsgType.MissileMsg.ordinal());
        buf.writeInt(45);
        buf.writeInt(200);
        buf.writeInt(100);
        buf.writeInt(Direction.UP.ordinal());
        buf.writeLong(id.getMostSignificantBits());
        buf.writeLong(id.getLeastSignificantBits());
        buf.writeLong(tid.getMostSignificantBits());
        buf.writeLong(tid.getLeastSignificantBits());
        buf.writeBoolean(true);
        ch.writeInbound(buf);
        MissileMsg msg = ch.readInbound();
        assertEquals(200,msg.x);
        assertEquals(100,msg.y);
        assertEquals(Direction.UP,msg.direction);
        assertEquals(id,msg.id);
        assertEquals(tid,msg.tankId);
        assertTrue(msg.life);
    }

    @Test
    public void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();
        ch.pipeline().addLast(new MyEncode());
        MissileMsg msg = new MissileMsg(50,80, Direction.UP,UUID.randomUUID(),
                UUID.randomUUID(),true);
        ch.writeOutbound(msg);
        ByteBuf buf = ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        int length = buf.readInt();
        int x = buf.readInt();
        int y = buf.readInt();
        Direction direction  = Direction.values()[buf.readInt()];
        UUID id = new UUID(buf.readLong(),buf.readLong());
        UUID tid = new UUID(buf.readLong(),buf.readLong());
        boolean life = buf.readBoolean();
        assertEquals(MsgType.MissileMsg, msgType);
        assertEquals(45,length);
        assertEquals(50,x);
        assertEquals(80,y);
        assertEquals(Direction.UP,direction);
        assertEquals(msg.id,id);
        assertEquals(msg.tankId,tid);
        assertTrue(true);
    }
}