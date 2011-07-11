package players;

import boardHelpers.*;
import exceptions.*;
import main.*;

/**
 * All Players must implement be able to return a Turn (play()) and can init anything they need
 * @author Randy
 */
public interface PlayerInterface {
    public Turn play(Board board, Side side);
    public void init(MainWindow window);
}
