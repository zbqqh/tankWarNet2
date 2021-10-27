package v05;

import java.io.*;
import java.util.UUID;

/*
* 减少消息发送量
* */

public class TankStartMovingMsg extends Msg {
    int x,y;
    Direction direction;
    UUID id;
    boolean stop = true;

    public TankStartMovingMsg() {
    }

    public TankStartMovingMsg(int x, int y, Direction direction, UUID id, boolean stop) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.id = id;
        this.stop = stop;
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
            dos.writeBoolean(stop);
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
            this.stop = dis.readBoolean();
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
        return "TankStartMovingMsg{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                ", id=" + id +
                ", stop=" + stop +
                '}';
    }

    public void handler() {
        if (this.id.equals(NettyClientFrame.INSTANCE.tank.id))return;
        Tank tank = NettyClientFrame.INSTANCE.findTankById(this.id);
        //tank.stop = false;
        tank.x = this.x;
        tank.y = this.y;
        tank.direction = this.direction;
        tank.stop = this.stop;

        //NettyClient.INSTANCE.send(new TankStartMovingMsg();
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankStartMovingMsg;
    }


}

