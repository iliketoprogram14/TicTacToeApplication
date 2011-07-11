package main;

import javax.swing.JApplet;

/**
 * This is the entry point for the applet.  It starts a new thread in the MainWindow class, where most of the action happens
 * @author Randy
 */
public class Applet extends JApplet {

    private MainWindow main;

    @Override
    public void init() {
        main = new MainWindow();
        Thread t = new Thread(main, "main thread");
        t.start();
    }
}
