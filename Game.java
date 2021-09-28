import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Game extends JPanel {
    public static Font font = new Font("Verdana", Font.BOLD, 60);
    public static Font titleFont = new Font("Verdana", Font.BOLD, 200);
    static private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int screenHeight = (int) screenSize.getHeight();
    static int screenWidth = (int) screenSize.getWidth();

    // Variable that holds whether the screen is showing the game or the main menu
    String state = "Homescreen";
    boolean paused = false;
    
    // Initialize both paddles, the ball, and set the initial speed to 1
    Paddle Lpaddle = new Paddle(this, 100, (screenHeight - Paddle.HEIGHT)/2, KeyEvent.VK_W, KeyEvent.VK_S);
    Paddle Rpaddle = new Paddle(this, screenWidth-120, (screenHeight - Paddle.HEIGHT)/2, KeyEvent.VK_UP, KeyEvent.VK_DOWN);
    Ball ball = new Ball(this, Lpaddle, Rpaddle);
    int speed = 1;

    // Though unlikely to reach this high, max speed of the game is 50
    static int MAX_SPEED = 50;

    // Wrapper classes that hold scores of each player respectively.
    Integer playerOneScore = 0, playerTwoScore = 0;
    
    public Game() {

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
                Lpaddle.keyReleased(e);
                Rpaddle.keyReleased(e);
			}

			@Override
			public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE ) {
                    paused = true;
                }
                Lpaddle.keyPressed(e);
                Rpaddle.keyPressed(e);
			}
        });
        
        // add a mouse listener to make the buttons on the main menu work
        addMouseListener(new MouseInput(this));
        setFocusable(true);
	}
    
    public void paused() {
        JOptionPane.showMessageDialog(null, "Game is paused.", "Paused", JOptionPane.YES_NO_OPTION);
        paused = false;
    }

    public void homescreen(Graphics2D g2d) {
        // Font for text in buttons
        Font buttonText = new Font("Verdana", Font.BOLD, 60);
        // Array that holds the text of each button
        String[] Text = {"One Player", "Two Players", "Quit"};
        // all buttons are 600 x 100
        int WIDTH = 600;
        int HEIGHT = 100;
        // holds the y-coordinate of the top button
        int initButtonHeight = (screenHeight + HEIGHT)/2 - HEIGHT;
        Rectangle[] buttons = { 
            new Rectangle((screenWidth+WIDTH)/2 - WIDTH, initButtonHeight, WIDTH, HEIGHT),
            new Rectangle((screenWidth+WIDTH)/2 - WIDTH, initButtonHeight + 100, WIDTH, HEIGHT),
            new Rectangle((screenWidth+WIDTH)/2 - WIDTH, initButtonHeight + 200, WIDTH, HEIGHT)
        };

        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        // draw the title on the center of the screen
        g2d.drawString("PONG", (screenWidth-g2d.getFontMetrics(titleFont).stringWidth("PONG"))/2, 200);

        g2d.setFont(buttonText);
        // for every button, find the dimensions of the text it will hold, use those numbers to center the text, and
        // then draw the buttons
        for (int i = 0; i < 3; i++) {
            int textWidth = g2d.getFontMetrics(buttonText).stringWidth(Text[i]);
            int textHeight = g2d.getFontMetrics(buttonText).getHeight();
            g2d.drawString(Text[i], buttons[i].x + ( WIDTH - textWidth)/2, buttons[i].y + ( 2 * HEIGHT + textHeight)/4);
            g2d.draw(buttons[i]);
        }
    }

    @Override
    public void paint(Graphics g){
        // holds whether or not the dashed lines in the middle should appear on the screen
        boolean linesDrawn = false;
        //this clears the screen before reprinting circle at new position
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        //Antialiasing makes the figure smoother
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.setBackground(Color.BLACK);

        // If the screen is currently showing the homescreen, call that function
        if (state == "Homescreen") {
            homescreen(g2d);
        } else { // otherwise, repaint the new frame of the game
            if (!linesDrawn) {
                g2d.setColor(Color.WHITE);
                // Draws the dashed lines that separate the players and their scores respectively.
                for (int i = 0; i < screenHeight; i += 40) {
                    g2d.fillRect(screenWidth/2 - 5, i, 10, 30);
                }
                linesDrawn = false;
            }
            // Draws the circle at new position with same diameter.
            ball.paint(g2d);
            // Draws the left paddle
            Lpaddle.paint(g2d);
            // Draws the right paddle
            Rpaddle.paint(g2d);
            g2d.setFont(Game.font);
            // Draws the scores of each player equidistant from each other, the dividing line being half the screen width
            g2d.drawString(String.valueOf(playerOneScore), 
            screenWidth/2 - 40 - g2d.getFontMetrics().stringWidth(String.valueOf(playerOneScore)), 60);
            g2d.drawString(String.valueOf(playerTwoScore), screenWidth/2 + 40, 60);
        }
    }

    public void gameOver(String winner) {
        this.repaint();
        String[] options = {"YES", "NO", "Return to homescreen"};
        // prompts the user to either play again, quit, or go to the main menu
        int choice = JOptionPane.showOptionDialog(this, winner + " wins! " + "\nWould you like to play again?", 
        "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            state = "Game";
            restartGame();
        } else if (choice == 1){
            System.exit(ABORT);
        } else {
            state = "Homescreen";
            restartGame();
        }
    }

    /**
     * When a person scores, this method is called to start the ball from the 
     * center of the screen, make it begin moving towards the player who won,
     * and resets the speed of the game
     */
    public void newRound(int xVelocity, int yVelocity) throws InterruptedException {
        Thread.sleep(100);
        ball.x = Ball.initialX;
        ball.y = Ball.initialY;
        ball.xVelocity = xVelocity;
        ball.yVelocity = yVelocity;
        speed = 1;
    }

    // Resets all the variables to restart the game
    private void restartGame() {
        Lpaddle.x = 100;
        Lpaddle.y = (screenHeight - Paddle.HEIGHT)/2;
        Rpaddle.x = screenWidth-120;
        Rpaddle.y = (screenHeight - Paddle.HEIGHT)/2;
        ball.x = Ball.initialX;
        ball.y = Ball.initialY;
        ball.xVelocity = -1;
        ball.yVelocity = -1;
        playerOneScore = 0;
        playerTwoScore = 0;
        this.speed = 1;
        this.repaint();
    }

    private void move() throws InterruptedException {
        ball.moveBall();
        Lpaddle.move();
        Rpaddle.move();
    }

    public void run() throws InterruptedException {
        while (state == "Game") {
            if (this.paused) {
                paused();
                break;
            }
            this.move();
            this.repaint();
            Thread.sleep(5);
        }
    }

    public void bot() {
        JOptionPane.showMessageDialog(null, "Unfortunately this part of the game still has some bugs; stay tuned!",
        "Computer Player Unavailable", JOptionPane.YES_NO_OPTION);
    }

    public static void main(String[] args) throws InterruptedException {
        // creates a new, initially invisible Frame with the specified title.
        JFrame frame = new JFrame("Pong");

        // set window size (width, height)
        frame.setSize(screenWidth, screenHeight);

        // close the frame when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     

        // Create an instance of the game
        Game game = new Game();

        frame.add(game);
        frame.setVisible(true);
        while(true) {
            game.run();
        }
    }
}
