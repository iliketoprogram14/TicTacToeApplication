package main;
import boardHelpers.*;

/**
 * Captures the notion of a move, noting its location and status
 * @author Randy
 */
public class Turn {
    public final Location LOC;
    public boolean taken;
    public Turn(Location loc) {
        this.LOC = loc;
        taken = true;
    }
}
