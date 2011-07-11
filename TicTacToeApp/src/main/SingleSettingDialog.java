package main;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Dialog for changing a single setting and then starting a new game.
 * Currently can change board size or round delay
 *
 * @author Randy Miller
 */
public class SingleSettingDialog extends JDialog implements ActionListener {

    public enum Setting {

        BOARD_SIZE, ROUND_DELAY
    }
    private JSpinner spinner;
    private JLabel spinnerLabel;
    boolean newGame;
    public Setting whichSetting;
    private JButton myButtonOkay;
    private JButton myButtonCancel;
    public MainWindow window;
    private static final int[] sizeValues = {3, 3, 10, 1};
    private static final int[] delayValues = {1, 0, 10, 1};
    String sizeLabel = "Board Size: ";
    String delayLabel = "Round Delay: ";

    public SingleSettingDialog(MainWindow window) {
        super(window, true);
        this.window = window;

        setTitle("Change Board Size");
        setResizable(false);

        newGame = false;
        whichSetting = Setting.BOARD_SIZE;

        spinner = new JSpinner(new SpinnerNumberModel(3, 3, 10, 1));
        spinnerLabel = new JLabel(sizeLabel);
        JPanel spinnerPanel = createSpinnerPanel(spinner, spinnerLabel);

        myButtonOkay = new JButton("Okay");
        myButtonCancel = new JButton("Cancel");

        myButtonOkay.addActionListener(this);
        myButtonCancel.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(myButtonOkay);
        buttonPanel.add(myButtonCancel);
        buttonPanel.setAlignmentX(0.5f);

        Container content = getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Box.createGlue());
        content.add(spinnerPanel);
        content.add(Box.createGlue());
        content.add(buttonPanel);
        content.add(Box.createGlue());

        pack();

        setSize(getSize().width + 30, getSize().height + 30);

        setLocation(window.getLocation().x + window.getSize().width / 2 - getSize().width / 2,
                window.getLocation().y + window.getSize().height / 2 - getSize().height / 2);

        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    private JPanel createSpinnerPanel(JSpinner spinner, JLabel label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(label);
        panel.add(Box.createGlue());
        panel.add(spinner);
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == myButtonOkay) {
            setVisible(false);
            newGame = true;
        } else if (src == myButtonCancel) {
            setVisible(false);
            newGame = false;
        }
    }

    private void setModel(Setting setting) {
        if (setting == Setting.BOARD_SIZE) {
            spinner.setModel(new SpinnerNumberModel(3, 3, 10, 1));
            spinner.setValue(window.args.boardSize);
            spinnerLabel.setText(sizeLabel);
            setTitle("Change Board Size");
        } else if (setting == Setting.ROUND_DELAY) {
            spinner.setModel(new SpinnerNumberModel(1, 0, 10, 1));
            spinner.setValue(window.args.delay / 1000);
            spinnerLabel.setText(delayLabel);
            setTitle("Change Round Delay");
        }
    }

    private void changeValue(Setting setting) {
        int val = ((Integer) spinner.getValue()).intValue();
        if (setting == Setting.BOARD_SIZE) {
            window.args.setSize(val);
        } else if (setting == Setting.ROUND_DELAY) {
            window.args.setDelay(val * 1000);
        }
    }

    /** Shows the dialog, captures the new setting, and changes the arguments
     * for the new game accordingly
     * @param setting */
    public boolean run(Setting setting) {
        newGame = false;
        setModel(setting);
        setVisible(true);
        while (isVisible()) {
        }
        if (newGame) {
            changeValue(setting);
        }
        return newGame;
    }
}
