import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MouseInput implements MouseListener {
    private Game game;

    MouseInput(Game game) {
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        /*
         * int WIDTH = 600; int HEIGHT = 100; int initButtonHeight = (screenHeight +
         * HEIGHT)/2 - HEIGHT; Rectangle[] buttons = { new
         * Rectangle((screenWidth+WIDTH)/2 - WIDTH, initButtonHeight, WIDTH, HEIGHT),
         * new Rectangle((screenWidth+WIDTH)/2 - WIDTH, initButtonHeight + 100, WIDTH,
         * HEIGHT), new Rectangle((screenWidth+WIDTH)/2 - WIDTH, initButtonHeight + 200,
         * WIDTH, HEIGHT)
         */
        // get coordinates of where the mouse was pressed
        int mx = e.getX();
        int my = e.getY();
        int ButtonY = (Game.screenHeight + 100) / 2 - 100;

        // the mouse listener is only needed when the homescreen is being displayed
        if (game.state == "Homescreen") {
            if (mx >= ((Game.screenWidth + 600) / 2 - 600) && mx <= (Game.screenWidth + 600) / 2) {
                // if the first button was pressed
                if (my >= ButtonY && my <= ButtonY + 100) {
                    // play with one player
                    game.bot();
                // if the second button was pressed
                } else if (my >= ButtonY + 100 && my <= ButtonY + 200) {
                    game.state = "Game";
                // if the third button was pressed
                } else if (my >= ButtonY + 200 && my <= ButtonY + 300) {
                    // Quit
                    System.exit(0);
                }
            }    
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}