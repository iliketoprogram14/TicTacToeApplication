package main;

/**
 *
 * @author Randy
 */
public class Main {

    public static void main(String [] args) {
        MainWindow main = new MainWindow();
        Thread t = new Thread(main, "main thread");
        t.start();
    }
}
