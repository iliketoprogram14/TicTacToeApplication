package main;

import boardHelpers.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import players.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.concurrent.locks.*;

/**
 * Acts as the main hub for the game.
 * Primarily controls the GUI here, as well as delegates control to drivers
 * @author Randy
 */
public class MainWindow extends JFrame implements ActionListener, WindowListener, Runnable {

    MainWindow thisWindow;
    public HashMap<String, String> opponentHash = new HashMap<String, String>();
    //for the Menus
    static final String[] OPPONENTS = {
        "Random Opponent",
        "Probabilistic Opponent",
        "Mini-max Opponent",
        "Human Opponent",};
    static final String[] SETTINGS = {
        "Switch Sides",
        "Board Size",
        "Time Limit",};
    boolean isReset; //for new games
    boolean buttonsOff = true;
    String xChar = " X ";
    String oChar = " O ";
    Label infoLabel = new Label("Welcome to Tic-Tac-Toe");
    Label turnLabel = new Label();
    JMenuBar menuBar = new JMenuBar();
    JMenu menu[] = new JMenu[3];
    JMenuItem[] menuOpponents = new JMenuItem[OPPONENTS.length];
    JMenuItem customGameMenuItem;
    JMenuItem[] menuSettings = new JMenuItem[SETTINGS.length];
    Panel nPanel, sPanel; //holds menu and info labels
    GridPanel grid; //physical board
    private PlayerInterface x, o; //players

    NewGameThread ngThread; //initializes new games
    final Lock newGameLock = new ReentrantLock(); //for newGameCond
    final Condition newGameCond = newGameLock.newCondition(); //to wake ngThread

    String infoMsg, turnMsg;
    public GameEngine engine; //drives game
    Arguments args; //argument container; used when initializing new games
    SingleSettingDialog ssDialog;
    CustomGameDialog cgDialog;

    public MainWindow() {
        isReset = false;
        thisWindow = this;
    }

    /** Initialize GUI and start a NewGameThread */
    public void run() {
        init();

        ngThread = new NewGameThread("new game thread");
        ngThread.setDaemon(true);
        newGameLock.lock();
        ngThread.start();
        newGameLock.unlock();
    }
    
    /** Initializes GUI and default arguments */
    public void init() {

        opponentHash.put("Random Opponent", "RandomPlayer");
        opponentHash.put("Probabilistic Opponent", "RandomPlayer");
        opponentHash.put("Mini-max Opponent", "Random Player");
        opponentHash.put("Human Opponent", "GuiPlayer");

        addWindowListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //set default arguments for new game (see Arguments class)
        args = new Arguments(true, "", "", -1, -1);
        initGUI();
        ssDialog = new SingleSettingDialog(this);
        cgDialog = new CustomGameDialog(this);
        setVisible(true);
    }

    /** Starts a new game and sleeps until needed to start another new game */
    private class NewGameThread extends Thread {
        public NewGameThread(String name) { super(name); }

        /** Begins a new game and waits on a condition variable */
        public void run() {
            newGameLock.lock();
            while (true) {
                beginNewGame();
                //sleepThenPlay();
                thisWindow.setVisible(true);
                try { newGameCond.await(); }
                catch (InterruptedException ex) { Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex); }
            }
        }

        /** Resets GUI (if necessary) and then begins a new game */
        private void beginNewGame() {
            if (isReset) {
                grid.resetGrid(args.boardSize);
                menuBar.validate();
                infoLabel.validate();
                turnLabel.validate();
            }
            isReset = true;
            thisWindow.setBottomBar("Next to move: ", xChar);
            thisWindow.play();
        }
    }

    /** Begin a new game by calling this method, which signals the new game thread */
    public void newGame() {
        newGameLock.lock();
        newGameCond.signal();
        newGameLock.unlock();

    }

    /** Loads Players and plays first round */
    public void play() {
        try {
            x = PlayerLoader.load(args.p1, this);
            o = PlayerLoader.load(args.p2, this);
        } catch (java.lang.ClassNotFoundException cnfe) {
            System.out.println(cnfe);
            return;
        }

        engine = new GameEngine(x, o, args.delay, args.boardSize, this);
        engine.playRound();
    }

    private void initGUI() {
        try { SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                initPanels();
                initMenu();
                initInfoBar();
                grid.initGrid(args.boardSize);
                initLayout();
                setVisible(false);
            }
        });
        } catch (InterruptedException e) { e.printStackTrace(); }
        catch (InvocationTargetException e) { e.printStackTrace(); }
    }

    //3 panels make up the board (north, grid, and south); init them
    private void initPanels() {
        nPanel = new Panel();
        nPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        grid = new GridPanel();

        sPanel = new Panel();
        sPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    //initialize menu
    private void initMenu() {
        //opponents menu - starts new game
        menu[0] = new JMenu("Play");
        menuBar.add(menu[0]);
        for (int i = 0; i < OPPONENTS.length; i++) {
            menuOpponents[i] = new JMenuItem(OPPONENTS[i]);
            menuOpponents[i].addActionListener(this);
            menu[0].add(menuOpponents[i]);
        }

        menu[0].addSeparator();

        customGameMenuItem = new JMenuItem("Custom Game");
        customGameMenuItem.addActionListener(this);
        menu[0].add(customGameMenuItem);

        //settings menu - starts new game
        menu[1] = new JMenu("Settings");
        menuBar.add(menu[1]);
        for (int i = 0; i < SETTINGS.length; i++) {
            menuSettings[i] = new JMenuItem(SETTINGS[i]);
            menuSettings[i].addActionListener(this);
            menu[1].add(menuSettings[i]);
        }

        //info menu - CURRENTLY DOES NOTHING
        menu[2] = new JMenu("Info");
        menuBar.add(menu[2]);

        //put menu in the top bar
        nPanel.add(menuBar);
    }

    //initialize south bar
    private void initInfoBar() {
        turnLabel.setText(null);
        sPanel.add(infoLabel);
        sPanel.add(turnLabel);
    }

    //add panels to the window and resize
    private void initLayout() {
        add(nPanel, BorderLayout.NORTH);
        //add(grid, BorderLayout.CENTER);
        this.getContentPane().add(grid, BorderLayout.CENTER);
        add(sPanel, BorderLayout.AFTER_LAST_LINE);
        this.pack();
    }

    /** Updates GUI given a move
     * @param loc
     * @param side */
    public void makeMove(Location loc, Side side) {
        if (side == Side.X) {
            grid.buttons[loc.ROW][loc.COL].setText(xChar);
            //updateBottomBar(infoMsg,oChar);
            turnLabel.setText(oChar);
            System.out.println("set to oChar");
        } else {
            grid.buttons[loc.ROW][loc.COL].setText(oChar);
            //updateBottomBar(infoMsg,xChar);
            System.out.println("set to xChar");
            turnLabel.setText(xChar);
        }
        grid.buttons[loc.ROW][loc.COL].setEnabled(false);
    }

    /** Adds user Player as listener to the grid/physical board.
     * Needs to be done for player to register event to make move.
     * @param p */
    public void addActionListeners(User_AbstractPlayer p) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid.buttons[i][j].addActionListener(p);
            }
        }
    }

    /** Remove user Player as listener from the grid/physical board.
     * Needs to be done so that player does not mess up rounds or the board
     * @param p */
    public void removeActionListeners(User_AbstractPlayer p) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                grid.buttons[i][j].removeActionListener(p);
    }

    /** Listens to menu item clicks and can start a new game
     * @param e */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JMenuItem) {
            JMenuItem clicked = (JMenuItem) obj;
            if (clicked.getText().equals("Info")) {
                return;
            }

            for (int i = 0; i < OPPONENTS.length; i++) {
                if (clicked.getText().equals(OPPONENTS[i])) {
                    args.setOpponent(opponentHash.get(OPPONENTS[i]));
                }
            }

            if (clicked == customGameMenuItem) {
                cgDialog.run();
            }

            if (clicked.getText().equals(SETTINGS[0])) {
                args.switchSides();
            }

            if (clicked.getText().equals(SETTINGS[1])) {
                boolean newGame = ssDialog.run(ssDialog.whichSetting.BOARD_SIZE);
                if (!newGame) return;
            }

            if (clicked.getText().equals(SETTINGS[2])) {
                boolean newGame = ssDialog.run(ssDialog.whichSetting.ROUND_DELAY);
                if (!newGame) return;
            }
            System.out.println("clicked menu item to start new game");
            newGame();
        }

    }

    //helper for won()
    public void setBottomBar(String infoLabelMsg, String turnLabelMsg) {
        infoLabel.setText(infoLabelMsg);
        turnLabel.setText(turnLabelMsg);
    }

    /** Updates infoLabel with the winner and calls Game Over dialog
     * @param winner */
    public void won(Side winner) {
        if (winner == Side.X) {
            setBottomBar("X wins","");
        } else if (winner == Side.O) {
            setBottomBar("O wins","");
        } else {
            setBottomBar("Tie game","");
        }
        startOver();
    }

    private void startOver() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try { Thread.sleep(3000); }
                catch (InterruptedException ex) { Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex); }
                cgDialog.run();
                newGame();
            }
        });
        thread.start();
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
