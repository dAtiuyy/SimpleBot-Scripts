package Killer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class KillerGUI {
    private JFrame frame;
    private JButton startStopButton;
    private JCheckBox enableOpenCCheckbox; // Added checkbox
    private boolean scriptStarted;
    private boolean openCEnabled; // Added variable for the feature state
    public KillerGUI() {
        initialize();
    }
    private void initialize() {
        frame = new JFrame("Killer GUI");
        frame.setBounds(100, 100, 300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        startStopButton = new JButton("Start Script");
        startStopButton.setBounds(100, 80, 120, 30);
        frame.getContentPane().add(startStopButton);
        enableOpenCCheckbox = new JCheckBox("Open the caskets?");
        enableOpenCCheckbox.setBounds(100, 120, 150, 20);
        frame.getContentPane().add(enableOpenCCheckbox);
        enableOpenCCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    openCEnabled = true;
                } else {
                    openCEnabled = false;
                }
            }
        });
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scriptStarted) {
                    // If the script is already running, stop it
                    scriptStarted = false;
                    startStopButton.setText("Start Script");
                } else {
                    // If the script is not running, start it
                    scriptStarted = true;
                    startStopButton.setText("Stop Script");
                }
            }
        });
        frame.setVisible(true);
    }
    public boolean isScriptStarted() {
        return scriptStarted;
    }
    public boolean OpenCEnabled() {
        return openCEnabled;
    }
    public JButton getStartButton() {
        return startStopButton;
    }
    public void onCloseGUI() {
        frame.dispose();
    }
}
