package main;
import boardHelpers.*;

import java.awt.GridLayout;
import java.awt.Panel;

/**
 * Physical representation of the board, full of BoardButtons
 * @author Randy
 */
public class GridPanel extends Panel {
    private static final long serialVersionUID = -7782726056503036377L;
    int gridSize;
    /** Represents the actual, physical board */
    public BoardButton[][] buttons;
    GridLayout gridLayout;
    String dots = "...";
    String xChar = " X ";
    String oChar = " O ";

    public GridPanel() {
        super();
    }

    /** Initializes all BoardButtons, enables them, and add to the board/grid
     * @param size */
    public void initGrid(int size) {
        this.gridSize = size;
        gridLayout = new GridLayout(gridSize, gridSize);
        buttons = new BoardButton[gridSize][gridSize];
        setLayout(gridLayout);
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                buttons[i][j] = new BoardButton(i, j, dots);
                buttons[i][j].setEnabled(true);
                add(buttons[i][j]);
            }
        }
    }

    /** Readjusts the physical representation of the board
     * @param size */
    public void resetGrid(int size) {
        removeAll();
        initGrid(size);
        validate();
    }
}
