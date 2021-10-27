package v08;

public abstract class Msg {
    public abstract byte[] toBytes();

    public abstract void parse(byte[] bytes);

    public abstract void handler();

    public abstract MsgType getMsgType();
}
