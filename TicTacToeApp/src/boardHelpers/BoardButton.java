package boardHelpers;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Buttons on the board that have coordinates
 * @author Randy
 */
public class BoardButton extends JButton {
    private static final long serialVersionUID = -8877527981473889880L;
    private int x, y;

    public BoardButton(int x, int y, String text) {
        super(text);
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public BoardButton(Icon icon) { super(icon); }

    public BoardButton(String text) { super(text); }

    public BoardButton(Action a) { super(a); }

    public BoardButton(String text, Icon icon) { super(text, icon); }
}
