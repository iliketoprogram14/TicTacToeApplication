package main;
import exceptions.*;

import java.util.ArrayList;
import boardHelpers.*;

/**
 * A Board object keeps an underlying model of what's really going
 * @author Randy
 */
public class Board {
    /** Defines which side will move next */
    private Side turnToMove;
    /** Represents the underlying model of the board itself */
    private Token[][] squares;
    /** Keeps track of how many rounds have completed*/
    private int moveNumber;
    /** Defines the size of the board */
    public int BOARD_SIZE;

    public Board(int size) {
        BOARD_SIZE = size;
        squares = new Token[BOARD_SIZE][BOARD_SIZE];
        turnToMove = Side.X;
        moveNumber = 0;
    }

    /**
     * Updates the underlying model given the most recent Turn
     * @param t
     * @param currSide
     * @return
     */
    public Board turn(Turn t, Side currSide) {
        //init new board
        Board ret = new Board(BOARD_SIZE);
        for (int i = 0; i < BOARD_SIZE; i++)
            System.arraycopy(squares[i], 0, ret.squares[i], 0, BOARD_SIZE);

        //update fields
        ret.moveNumber = moveNumber + 1;
        ret.turnToMove = Side.otherSide(turnToMove);

        //update board itself by placing token in location given by turn
        Location loc = t.LOC;
        Token token = new Token(currSide);
        ret.squares[loc.ROW][loc.COL] = token;

        return ret;
    }

    /** Checks winning configurations and stuck configurations to see if the game is over
     * @return */
    public boolean isOver() {
        return isWinner() || isStuck();
    }

    /** Returns the side of the winner (or null if there is no winner)
     * @return */
    public Side winner() {
        if (isWinner()) {
            return Side.otherSide(turnToMove);
        } else if (isStuck()) {
            return null;
        }
        return null;
    }

    //checks to see if the game is stuck/tied
    private boolean isStuck() {
        //checks moveNumber against max number of moves
        if (moveNumber >= BOARD_SIZE*BOARD_SIZE)
            return true;

        //I'M NOT SURE WHY THIS IS HERE ACTUALLY
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (isTokenAt(new Location(i, j)))
                    return false;

        return true;
    }

    private boolean isWinner() {
        Side side = Side.otherSide(turnToMove);
        int tokensInaRow = 0;

        //checks columns for win
        for (int i = 0; i < BOARD_SIZE; i++) {
            tokensInaRow = 0;
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (squares[i][j] != null && squares[i][j].SIDE == side)
                    tokensInaRow++;
                if (tokensInaRow == BOARD_SIZE)
                    return true;
            }
        }

        //checks rows for win
        for (int j = 0; j < BOARD_SIZE; j++) {
            tokensInaRow = 0;
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (squares[i][j] != null && squares[i][j].SIDE == side)
                    tokensInaRow++;
                if (tokensInaRow == BOARD_SIZE)
                    return true;
            }
        }

        //checks one diagonal for win
        tokensInaRow = 0;
        for (int i = 0, j = 0; i < BOARD_SIZE; i++, j++) {
            if (squares[i][j] != null && squares[i][j].SIDE == side)
                tokensInaRow++;
            if (tokensInaRow == BOARD_SIZE)
                return true;
        }

        //checks other diagonal for win
        tokensInaRow = 0;
        for (int i = 0, j = BOARD_SIZE - 1; i < BOARD_SIZE; i++, j--) {
            if (squares[i][j] != null && squares[i][j].SIDE == side)
                tokensInaRow++;
            if (tokensInaRow == BOARD_SIZE)
                return true;
        }

        return false;
    }

    /**
     * Simply returns whether a token is at a particular spot in the board or not
     * @param l
     * @return */
    public boolean isTokenAt(Location l) {
        if (squares[l.ROW][l.COL] == null) {
            return false;
        }
        return true;
    }
    
    /** Adds locations of open spots on the board to an array and returns it
     * @return */
    public Location[] getOpenSpots() {
        ArrayList<Location> locations = new ArrayList<Location>();
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (squares[i][j] == null)
                    locations.add(new Location(i, j));

        return locations.toArray(new Location[locations.size()]);
    }

    /**
     * Returns an array of all the spots on the board that are taken (opposite of getOpenSpots)
     * @return */
    public Location[] getTokenLocations() {
        ArrayList<Location> locations = new ArrayList<Location>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (squares[i][j] != null) {
                    locations.add(new Location(i, j));
                }
            }
        }
        return locations.toArray(new Location[locations.size()]);
    }

    @Override
    public String toString() {
        String dashes = "----------\n";
        String toRet = "";
        int i = 0; int j = 0; int k = 0;
        for (i = 0; i < BOARD_SIZE; i++, k++) {
            for (j = 0; j < BOARD_SIZE - 1; j++) {
                toRet += tokenToString(squares[i][j]);
                toRet += "|";
            }
            toRet += tokenToString(squares[i][j]) + "\n";
            if (k < BOARD_SIZE - 1)
                toRet += dashes;
        }
        //toRet += dashes + "\n";
        toRet += "\nThe next player to move is:" + turnToMove + "\n";
        toRet += dashes + "\n\n";

        return toRet;
    }

    public String tokenToString(Token token) {
        String ret = null;
        if (token == null) {
            ret = " . ";
        } else if (token.SIDE == Side.X) {
            ret = " X ";
        } else if (token.SIDE == Side.O) {
            ret = " O ";
        } else {
            ret = "..";
        }
        return ret;
    }
}
