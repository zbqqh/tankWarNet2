package v08;

import java.io.*;
import java.util.UUID;

public class MissileMsg extends Msg {

    public int x;
    public int y;
    public boolean life = true;
    public Direction direction ;
    public Group group;
    UUID id = UUID.randomUUID();
    UUID tankId = null;

    public MissileMsg() {
    }

    public MissileMsg(Missile missile) {
        this.x = missile.x;
        this.y = missile.y;
        this.direction = missile.direction;
        this.id = missile.id;
        this.tankId = missile.tankId;
        this.life = missile.life;
        this.group = missile.group;
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
            dos.writeInt(group.ordinal());
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
            this.group = Group.values()[dis.readInt()];
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
                ", life=" + life +
                ", direction=" + direction +
                ", group=" + group +
                ", id=" + id +
                ", tankId=" + tankId +
                '}';
    }

    public void handler() {
        if (NettyClientFrame.INSTANCE.findMissileById(id) != null) return;
        Missile missile = new Missile(this);

        NettyClientFrame.INSTANCE.missileList.add(missile);

    }

    @Override
    public MsgType getMsgType() {
        return MsgType.MissileMsg;
    }
}
