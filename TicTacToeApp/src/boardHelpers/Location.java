package boardHelpers;

/**
 * Stores coordinates in an object, and can be changed via setLoc
 * @author Randy
 */
public class Location {
    public int ROW;
    public int COL;

    public Location (int row, int col) {
        this.ROW = row;
        this.COL = col;
    }

    public void setLoc (int row, int col) {
    	this.ROW = row;
    	this.COL = col;
    }
}
