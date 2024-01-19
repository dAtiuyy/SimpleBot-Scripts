package KrakenKiller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KrakenKillerGUI {
    private JFrame frame;
    private JComboBox<String> List1;
    private JComboBox<String> List2;
    private JButton startStopButton;
    private JLabel helloLabel;
    private boolean scriptStarted;

    public KrakenKillerGUI() {
        initialize();
    }
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("Kraken Killer GUI");
        frame.setBounds(100, 100, 350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        String[] options = {"Normal", "Curses", "Curses - SoulSplit"};
        List1 = new JComboBox<>(options);
        List1.setBounds(190, 20, 120, 25);
        frame.getContentPane().add(List1);

        startStopButton = new JButton("Start Script");
        startStopButton.setBounds(120, 120, 120, 25);
        frame.getContentPane().add(startStopButton);

        helloLabel = new JLabel("Choose your PrayerBook");
        helloLabel.setBounds(10, 20, 150, 25);
        helloLabel.setFont(new Font("Arial", Font.BOLD, 12));
        frame.getContentPane().add(helloLabel);

        helloLabel = new JLabel("Choose what to boost");
        helloLabel.setBounds(10, 50, 150, 25);
        helloLabel.setFont(new Font("Arial", Font.BOLD, 12));
        frame.getContentPane().add(helloLabel);

        String[] options2 = {"None", "Eagle Eye", "Rigour", "Mystic Might", "Augury", "Leech Magic", "Leech Range", "Turmoil"};
        List2 = new JComboBox<>(options2);
        List2.setBounds(190, 50, 120, 25);
        frame.getContentPane().add(List2);

        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scriptStarted) {
                    scriptStarted = false;
                    startStopButton.setText("Start Script");
                } else {
                    scriptStarted = true;
                    startStopButton.setText("Stop Script");
                }
            }
        });
        frame.setVisible(true);
    }
    public String getSelectedItem1() {
        return (String) List1.getSelectedItem();
    }

    public String getSelectedItem2() {
        return (String) List2.getSelectedItem();
    }

    public boolean isScriptStarted() {
        return scriptStarted;
    }

    public void onCloseGUI() {
        frame.dispose();
    }
}
