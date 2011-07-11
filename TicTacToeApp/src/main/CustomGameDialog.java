package main;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Dialog for starting a new game with custom settings
 *
 * @author Randy Miller
 */
public class CustomGameDialog extends JDialog implements ActionListener {

    private JComboBox p1List;
    private JLabel p1Label;
    private JComboBox p2List;
    private JLabel p2Label;
    private JSpinner sizeSpinner;
    private JLabel sizeSpinnerLabel;
    private JSpinner delaySpinner;
    private JLabel delaySpinnerLabel;
    private JRadioButton xButton;
    private JRadioButton oButton;
    boolean newGame;
    private JButton myButtonOkay;
    private JButton myButtonCancel;
    public MainWindow window;
    //initial, min, max, step
    private static final int[] sizeValues = {3, 3, 10, 1};
    private static final int[] delayValues = {1, 0, 10, 1};
    String sizeLabel = "Board Size: ";
    String delayLabel = "Round Delay: ";

    public CustomGameDialog(MainWindow window) {
        super(window, true);
        this.window = window;

        setTitle("Custom Game");
        setResizable(false);

        newGame = false;

        p1List = new JComboBox(window.OPPONENTS);
        p1List.setSelectedItem(window.OPPONENTS[0]);
        p1Label = new JLabel("Player 1 (X): ");
        JPanel p1Panel = createComboBoxPanel(p1List, p1Label);

        p2List = new JComboBox(window.OPPONENTS);
        p2List.setSelectedItem(window.OPPONENTS[1]);
        p2Label = new JLabel("Player 2 (O): ");
        JPanel p2Panel = createComboBoxPanel(p2List, p2Label);

        sizeSpinner = new JSpinner(new SpinnerNumberModel(3, 3, 10, 1));
        sizeSpinnerLabel = new JLabel(sizeLabel);
        JPanel sizeSpinnerPanel = createSpinnerPanel(sizeSpinner, sizeSpinnerLabel);

        delaySpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10, 1));
        delaySpinnerLabel = new JLabel(delayLabel);
        JPanel delaySpinnerPanel = createSpinnerPanel(delaySpinner, delaySpinnerLabel);

        xButton = new JRadioButton("X");
        oButton = new JRadioButton("O");
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));
        radioPanel.add(xButton);
        //radioPanel.add(Box.createGlue());
        radioPanel.add(oButton);
        radioPanel.setAlignmentX(radioPanel.LEFT_ALIGNMENT);

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
        content.add(p1Panel);
        content.add(p2Panel);
        content.add(Box.createGlue());
        content.add(sizeSpinnerPanel);
        content.add(delaySpinnerPanel);
        content.add(Box.createGlue());
        content.add(radioPanel);
        content.add(Box.createGlue());
        content.add(buttonPanel);
        content.add(Box.createGlue());
        pack();

        setSize(getSize().width + 30, getSize().height + 30);

        setLocation(window.getLocation().x + window.getSize().width / 2 - getSize().width / 2,
                window.getLocation().y + window.getSize().height / 2 - getSize().height / 2);

        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    //private JPanel createPanel() {}
    private JPanel createSpinnerPanel(JSpinner spinner, JLabel label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(label);
        panel.add(Box.createGlue());
        panel.add(spinner);
        return panel;
    }

    private JPanel createComboBoxPanel(JComboBox box, JLabel label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(label);
        panel.add(Box.createGlue());
        panel.add(box);
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == myButtonOkay) {
            setVisible(false);
            newGame = true;
        } else if (src == myButtonCancel) {
            setVisible(false);
        }
    }

    private void showCurrentValues() {
        //eventually, update players too using hashes once I have other opponents...
        sizeSpinner.setValue(window.args.boardSize);
        delaySpinner.setValue(window.args.delay/1000);
        if (window.args.mainPlayer == window.args.mainPlayer.P1) {
            xButton.setSelected(true);
        } else {
            oButton.setSelected(true);
        }
    }

    private void changeValues() {
        String p1 = (String) p1List.getSelectedItem();
        String p2 = (String) p2List.getSelectedItem();
        window.args.setP1(window.opponentHash.get(p1));
        window.args.setP2(window.opponentHash.get(p2));
        window.args.setSize(((Integer) sizeSpinner.getValue()).intValue());
        window.args.setDelay(((Integer) delaySpinner.getValue()).intValue()*1000);
        if (xButton.isSelected()) {
            window.args.setMainPlayer(window.args.mainPlayer.P1);
        } else {
            window.args.setMainPlayer(window.args.mainPlayer.P2);
        }
    }

    public boolean run() {
        newGame = false;
        showCurrentValues();
        setVisible(true);
        while (isVisible()) {
        }
        if (newGame) {
            changeValues();
        }
        return newGame;
    }
}
