import java.awt.*;
import java.math.*;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import javax.swing.JOptionPane;

class Ball {
    public static Random random = new Random();
    String fileName = "blipG5.wav";
    // Create ball size
    public static final int DIAMETER = 30;
    public static final int initialY = Game.screenHeight/2-DIAMETER;
    public static final int initialX = Game.screenWidth/2-DIAMETER;
    // Initialize ball position and initial velocity
    int x = initialX, y = initialY, yVelocity = -1, xVelocity = -1;
    private Game game;
    private Paddle Lpaddle;
    private Paddle Rpaddle;
    public Ball(Game game, Paddle Lpaddle, Paddle Rpaddle) {
        this.game = game;
        this.Lpaddle = Lpaddle;
        this.Rpaddle = Rpaddle;
    }

    public static void playSound(String filepath) {
        // InputStream music;
        // try {
        //     music = new FileInputStream(new File(filepath));
        //     AudioStream audio = new AudioStream(music);
        //     AudioPlayer.player.start(audio);
        // } catch (Exception e) {
        //     JOptionPane.showMessageDialog(null, "Error");
        // }
    }

    void moveBall() throws InterruptedException {
        // goes past the right paddle
        if (x >= game.getWidth()-DIAMETER) {
            xVelocity = -1;
            game.playerOneScore++;
            playSound(fileName);
            if (game.playerOneScore == 10) {
                game.gameOver("Player One");
            }
            else {
                game.newRound(-1, -1);
            }
        }
        // goes past the left paddle
        else if (x <= 0)  {
            xVelocity = 1;
            game.playerTwoScore++;
            playSound(fileName);
            if (game.playerTwoScore == 10) {
                game.gameOver("Player Two");
            }
            else {
                game.newRound(1, -1);
            }
        }
        // collides with left paddle
        else if (collision(Lpaddle)) {
            xVelocity = game.speed;
            x = Lpaddle.getTopX() + Paddle.WIDTH;
            game.speed = game.speed >= Game.MAX_SPEED? game.speed: ++game.speed;
        }
        // collides with right paddle
        else if (collision(Rpaddle)) {
            xVelocity = -game.speed ;
            x = Rpaddle.getTopX() - DIAMETER;
            game.speed = game.speed >= Game.MAX_SPEED? game.speed: ++game.speed;
        }
        // collides with bottom
        else if (y >= game.getHeight()-DIAMETER){
            yVelocity = -game.speed;
            playSound(fileName);
        }
        // collides with top
        else if (y <= 0) {
            yVelocity = game.speed;
            playSound(fileName);
        }

        // Move ball 
        x = x + xVelocity;
        y = y + yVelocity;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETER, DIAMETER);
    }
    
    private boolean collision(Paddle paddle) {
        return paddle.getBounds().intersects(getBounds());
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, DIAMETER, DIAMETER);
    }

}