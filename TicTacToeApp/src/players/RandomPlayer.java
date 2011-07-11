package players;
import boardHelpers.*;
import main.*;

import java.util.Random;

/**
 * Player that randomly picks a legal move
 * @author Randy
 */
public class RandomPlayer extends AI_AbstractPlayer {
   
    public RandomPlayer() {}

    protected void initExtraStuff(MainWindow window){}

    /** Randomly picks an open location for the next move
     * @param board
     * @param side
     * @return  */
    protected Location calculateMove(Board board, Side side){
        Location[] openSpots = board.getOpenSpots();
        Location loc = openSpots[myRandom.nextInt(openSpots.length)];
        return loc;
    }
}
