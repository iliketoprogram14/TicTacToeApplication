package main;

import boardHelpers.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

import players.*;

/**
 * Drives the game via events, which call playRound()
 * @author Randy
 */
public class GameEngine {

    private Board board;
    private PlayerInterface xPlayer, oPlayer, currPlayer; //keeps track of the players
    private int roundDelay, roundsCompleted;
    private MainWindow window;
    private boolean isFirstRound;

    public GameEngine(PlayerInterface xPlayer, PlayerInterface oPlayer, int roundDelay, int size, MainWindow window) {
        this.board = null;
        this.board = new Board(size);
        this.xPlayer = xPlayer;
        this.oPlayer = oPlayer;
        this.currPlayer = xPlayer;
        this.roundDelay = roundDelay;
        this.window = window;
        roundsCompleted = 0;
        isFirstRound = true;
    }

    /** Plays a round of a game.  It's responsible for keeping track of
     * which player is up, asking the player for their move, asking the applet
     * to update, and checking for a winner
     */
    public void playRound() {
        //If a user is first, exit immediately and wait for event
        //otherwise continue normally
        if (isFirstRound) {
            isFirstRound = false;
            if (currPlayer instanceof User_AbstractPlayer) {
                window.addActionListeners((User_AbstractPlayer) currPlayer);
                return;
            }
        }
        Side currSide;
        PlayerInterface nextPlayer;
        currSide = (currPlayer == xPlayer) ? Side.X : Side.O;
        nextPlayer = (currPlayer == xPlayer) ? oPlayer : xPlayer;

        //get Turn of the current player, update the board and GUI
        Turn t = currPlayer.play(board, currSide);
        board = board.turn(t, currSide);
        window.makeMove(t.LOC, currSide);

        //update current Player and counters
        currPlayer = nextPlayer;
        roundsCompleted++;
        window.setVisible(true);

        //check for win/tie; else, go on to the next round/wait for the next event
        if (board.isOver()) {
            window.won(board.winner());
        } else if (currPlayer instanceof AI_AbstractPlayer) {
            sleepThenPlayNextRound();
        } else if (currPlayer instanceof User_AbstractPlayer) {
            //a user is up, so make sure they can register events
            window.addActionListeners((User_AbstractPlayer) currPlayer);
        }
    }

    private void sleepThenPlayNextRound() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try { Thread.sleep(roundDelay); }
                catch (InterruptedException ex) { Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex); }
                playRound();
            }
        });
        thread.start();
    }
}
