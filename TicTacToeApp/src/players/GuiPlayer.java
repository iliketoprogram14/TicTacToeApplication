package players;

import boardHelpers.*;
import main.*;

/**
 * Player that makes moves by clicking on the board
 * @author Randy
 */
public class GuiPlayer extends User_AbstractPlayer {
    boolean gotLoc; //CURRENTLY UNUSED
    
    public GuiPlayer() { }

    @Override
    public void initExtraStuff(MainWindow window) {
        gotLoc = false;
    }

    /** Grabs location from BoardButton clicked and updates location if necessary
     * @param obj Source of event from ActionPerformed()
     * @return */
    @Override
    protected boolean processInput(Object obj) {
        if (obj instanceof BoardButton) {
            BoardButton clicked = (BoardButton) obj;
            loc.setLoc(clicked.getX(), clicked.getY());
            return true;
        }
        return false;
    }

}