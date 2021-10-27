package v08;

import java.awt.*;
import java.util.UUID;

public class Missile {

    public int x;
    public int y;
    public boolean life = true;
    public Direction direction ;
    public Group group;
    UUID id;
    UUID tankId = null;

    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    public static final int SPEED = 10;


    public static Toolkit toolkit = Toolkit.getDefaultToolkit();
    public static Image bulletL;
    public static Image bulletU;
    public static Image bulletR;
    public static Image bulletD;

    static {
        bulletL = toolkit.createImage(
                Blast.class.getClassLoader().getResource("images/bulletL.png"));
        bulletU = toolkit.createImage(
                Blast.class.getClassLoader().getResource("images/bulletU.png"));
        bulletR = toolkit.createImage(
                Blast.class.getClassLoader().getResource("images/bulletR.png"));
        bulletD = toolkit.createImage(
                Blast.class.getClassLoader().getResource("images/bulletD.png"));
    }

    public boolean isLife() {
        return life;
    }

    public void setLife() {
        if (x < 0 || x > 800 || y < 0 || y > 800) {
            life = false;
        }
    }

    public Missile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Missile(int x, int y, Direction direction, Group group) {
        this(x, y);
        this.direction = direction;
        this.group = group;
    }

    public Missile(int x, int y, Direction direction, UUID id) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.id = id;
    }

    public Missile(int x, int y, boolean life, Direction direction, Group group,
                   UUID id, UUID tankId) {
        this.x = x;
        this.y = y;
        this.life = life;
        this.direction = direction;
        this.group = group;
        this.id = id;
        this.tankId = tankId;
    }

    public Missile(MissileMsg msg){
        this.x = msg.x;
        this.y = msg.y;
        this.life = msg.life;
        this.direction = msg.direction;
        this.group = msg.group;
        this.id = msg.id;
        this.tankId = msg.tankId;

    }

    public void paint(Graphics g) {
        if (!life) {
            NettyClientFrame.INSTANCE.missileList.remove(this);
            return;
        }
        move(g);
        setLife();
    }

    public void move(Graphics g) {
        switch (direction) {
            case LEFT:
                g.drawImage(bulletL,x,y,null);
                x -= SPEED;
                break;
            case UP:
                g.drawImage(bulletU,x,y,null);
                y -= SPEED;
                break;
            case RIGHT:
                g.drawImage(bulletR,x,y,null);
                x += SPEED;
                break;
            case DOWN:
                g.drawImage(bulletD,x,y,null);
                y += SPEED;
                break;
        }
    }

    public void hitTank(Tank tank){
        if (this.group.equals(tank.group)) return;

        if (getRect().intersects(tank.getRect())){
            //爆炸画面
            //System.out.println("missileHitTank:" + "missile-" + this +
                   // "tank-" + tank);

            life = false;
            tank.life = false;

            NettyClientFrame.INSTANCE.blastsList.add(new Blast(tank.x, tank.y));
//            NettyClient.INSTANCE.send(new MissileHitTankMsg());
        }
    }

    @Override
    public String toString() {
        return "Missile{" +
                "x=" + x +
                ", y=" + y +
                ", life=" + life +
                ", direction=" + direction +
                ", group=" + group +
                ", id=" + id +
                ", tankId=" + tankId +
                '}';
    }

    private Rectangle getRect(){
        Rectangle rectangle = new Rectangle(x,y,WIDTH,HEIGHT);
        return rectangle;
    }
}
