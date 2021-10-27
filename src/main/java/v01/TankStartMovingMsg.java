package v01;

import java.io.*;
import java.util.UUID;

public class TankStartMovingMsg extends Msg {
    int x,y;
    Direction direction;
    UUID id;

    public TankStartMovingMsg() {
    }

    public TankStartMovingMsg(int x, int y, Direction direction, UUID id) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.id = id;
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
        return "PlayerJoinMsg{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                ", id=" + id +
                '}';
    }

    public void handler() {
        if (this.id.equals(NettyClientFrame.INSTANCE.tank.id))return;
        if (NettyClientFrame.INSTANCE.findTankByID(id) != null)return;
        //Tank tank = new Tank(this);
       // NettyClientFrame.INSTANCE.tankList.add(tank);
        //NettyClient.INSTANCE.send(new TankStartMovingMsg(NettyClientFrame.INSTANCE.tank));
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankJoinMsg;
    }


}

