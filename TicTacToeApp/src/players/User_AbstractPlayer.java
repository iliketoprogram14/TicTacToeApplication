package players;

import main.*;
import boardHelpers.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * All Players that are controlled by the user and require user input must extend this class.
 * Moves are event driven, so a new Player must handle input/events in processInput.
 * @author Randy
 */
abstract public class User_AbstractPlayer implements PlayerInterface, ActionListener {
    private MainWindow window;
    protected Location loc = new Location(0,0); //location of next move (set to 0,0 initially)

    /** Method to init additional things if necessary
     * @param window */
    abstract public void initExtraStuff(MainWindow window);
    /** Initializes the fields (window and location)
     * @param window */
    final public void init(MainWindow window) {
        this.window = window;
        initExtraStuff(window);
    }

    /** Location for turn must be determined beforehand in actionPerformed()/processInput()
     * @param board
     * @param side
     * @return */
    @Override
    final public Turn play(Board board, Side side) {
        return new Turn(loc);
    }

    /** In here, necessary info is extracted from the object and the location is calculated accordingly.
     * Object can be a board button, or some object that holds input (keystrokes, mouse clicks, etc).
     * Must return whether it should move or not (illegal move or wrong object return false)
     * @param obj Source of the event from
     * @return */
    abstract protected boolean processInput(Object obj);
    /** Turns off button interaction and triggers round of play for this player to move
     * @param e  */
    final public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        boolean shouldMove = processInput(obj);
        if (shouldMove) {
            window.removeActionListeners(this);
            window.engine.playRound();
            return;
        }
    }
}
