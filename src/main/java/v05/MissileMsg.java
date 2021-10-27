package v05;

import java.io.*;
import java.util.UUID;

public class MissileMsg extends Msg {

    int x,y;
    Direction direction;
    UUID id;
    UUID tankId;
    boolean life;

    public MissileMsg() {
    }

    public MissileMsg(int x, int y, Direction direction, UUID id, UUID tankId, boolean life) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.id = id;
        this.tankId = tankId;
        this.life = life;
    }

    public byte[] toBytes(){
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            bos = new ByteArrayOutputStream();
            dos = new DataOutputStream(bos);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(direction.ordinal());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeLong(tankId.getMostSignificantBits());
            dos.writeLong(tankId.getLeastSignificantBits());
            dos.writeBoolean(life);
            dos.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.direction = Direction.values()[dis.readInt()];
            this.id = new UUID(dis.readLong(),dis.readLong());
            this.tankId = new UUID(dis.readLong(),dis.readLong());
            this.life = dis.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "MissileMsg{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                ", id=" + id +
                ", tankId=" + tankId +
                '}';
    }

    public void handler() {
        if (NettyClientFrame.INSTANCE.findMissileById(id) != null) return;
        Missile missile = new Missile(x,y,direction,id);
        missile.x = this.x;
        missile.y = this.y;
        missile.direction = this.direction;
        missile.id = id;
        NettyClientFrame.INSTANCE.missileList.add(missile);

    }

    @Override
    public MsgType getMsgType() {
        return MsgType.MissileMsg;
    }
}
