/* NEED TO ADD ACCESSOR METHODS!!!!! */

package boardHelpers;

import java.util.HashMap;

/**
 * Contains the parameters of a new game
 * @author Randy
 */
public class Arguments {
    public enum MainPlayer { P1, P2 }

    /** Represents player1 in terms of a Player class */
    public String p1;
    /** Represents player2 in terms of a Player class */
    public String p2;
    /** Represents the delay between rounds */
    public int delay;
    /** Represents the size of the board */
    public int boardSize;
    /** Holds the default players */
    public static HashMap<String, String> defaultPlayers = new HashMap<String, String>();
    /** Holds the default settings */
    public static HashMap<String, Integer> defaultSettings = new HashMap<String, Integer>();

    public MainPlayer mainPlayer;

    static {
        defaultPlayers.put("p1", "GuiPlayer");
        defaultPlayers.put("p2", "RandomPlayer");
        defaultSettings.put("delay", Integer.valueOf(1000));
        defaultSettings.put("boardSize", Integer.valueOf(3));
    }

    public Arguments(boolean defaultArgs, String p1, String p2, int delay, int boardSize) {
        if (defaultArgs) {
            this.p1 = defaultPlayers.get("p1");
            this.p2 = defaultPlayers.get("p2");
            this.delay = defaultSettings.get("delay").intValue();
            this.boardSize = defaultSettings.get("boardSize").intValue();
        } else {
            this.p1 = p1;
            this.p2 = p2;
            this.delay = delay;
            this.boardSize = boardSize;
        }
        mainPlayer = MainPlayer.P1;
    }

    /** Sets player1
     * @param p1 */
    public void setP1(String p1) {
        if (p1.equals(""))
            this.p1 = defaultPlayers.get("p1");
        else
            this.p1 = p1;
    }

    /** Sets player1
     * @param p2 */
    public void setP2(String p2) {
        if (p2.equals(""))
            this.p2 = defaultPlayers.get("p2");
        else
            this.p2 = p2;
    }

    public void setOpponent(String opponent) {
        if (mainPlayer == MainPlayer.P1)
	setP2(opponent);
        else
	setP1(opponent);
    }

    /** Sets the delay between each round
     * @param delay */
    public void setDelay(int delay) {
        if (delay < 0 || delay >= 10000)
            this.delay = defaultSettings.get("delay").intValue();
        else
            this.delay = delay;
    }

    /** Sets the size of the board
     * @param size */
    public void setSize(int size) {
        if (size <= 0 || size > 5)
            this.boardSize = defaultSettings.get("boardSize").intValue();
        else
            this.boardSize = size;
    }

    public void setMainPlayer(MainPlayer mp) {
        mainPlayer = mp;
    }

    /** Switches players and sides
     */
    public void switchSides() {
    	if (mainPlayer == MainPlayer.P1)
    	    mainPlayer = MainPlayer.P2;
    	else
    	    mainPlayer = MainPlayer.P1;

    	String tmp = p1;
        setP1(p2);
        setP2(tmp);
    }
}
