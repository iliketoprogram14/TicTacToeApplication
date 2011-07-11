package players;

import main.*;
import boardHelpers.*;
import java.util.Random;

/**
 * All Players that are controlled by the computer must extend this class.
 * The algorithm determining the move must be written in calculateMove()
 * @author Randy
 */
abstract public class AI_AbstractPlayer implements PlayerInterface {
    protected Random myRandom;

    /** Method to init additional things if necessary
     * @param window */
    abstract protected void initExtraStuff(MainWindow window);
    /** Initializes the fields (window and location)
     * @param window */
    final public void init(MainWindow window) {
        myRandom = new Random();
        initExtraStuff(window);
    }

    /** Algorithm for determining the next move
     * @param board
     * @param side
     * @return */
    abstract protected Location calculateMove(Board board, Side side);
    /** Location for turn must be determined beforehand in calculateMove()
     * @param board
     * @param side
     * @return */
    final public Turn play(Board board, Side side) {
        Location loc = calculateMove(board, side);
        return new Turn(loc);
    }
}
