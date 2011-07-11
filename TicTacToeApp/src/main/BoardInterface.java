package main;
import boardHelpers.*;
import exceptions.*;

/**
 * How the GUI and players can interact with the model of the board
 * turn(), isOver(), and winner() are used by the game engine
 * isTokenAt(), getOpenSpots(), and getTokenLocations() can be used by players
 * @author Randy
 */
public interface BoardInterface {
    public Board turn(Turn t, Side currSide);
    public boolean isOver();
    public Side winner() throws GameNotOverException;
    public boolean isTokenAt(Location l);
    public Location[] getOpenSpots();
    public Location[] getTokenLocations();
}
