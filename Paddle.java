import java.awt.*;
import java.awt.event.KeyEvent;

class Paddle {
    private Game game;
    int x;
    int y;
    private int up;
    private int down;

    public Paddle(Game game, int x, int y, int up, int down) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.up = up;
        this.down = down;
    }
    // initial x and y coordinates of the paddle; x coordinate must be constant
    // Height and Width of the paddle
    static final int HEIGHT = 200;
    static final int WIDTH = 20;
    // current speed
    private int speed = 0;

    public void move() {
        if (y + speed > 0 && y + speed < Game.screenHeight - HEIGHT)
            y = y + speed;
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public void keyReleased(KeyEvent e) {
        speed = 0;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == up)
            speed = -game.speed;
        if (e.getKeyCode() == down)
            speed = game.speed;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public int getTopX() {
        return x;
    }
}