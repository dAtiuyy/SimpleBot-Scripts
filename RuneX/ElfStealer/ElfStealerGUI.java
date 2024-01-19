package ElfStealer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ElfStealerGUI {

    private JFrame frame;
    private JButton startStopButton;
    private boolean scriptStarted;

    public ElfStealerGUI() {
        initialize();
    }

    //4375 afk patrol guard

    private void initialize() {
        frame = new JFrame("Elf Stealer GUI");
        frame.setBounds(100, 100, 300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        startStopButton = new JButton("Start Script");
        startStopButton.setBounds(100, 80, 120, 30);
        frame.getContentPane().add(startStopButton);

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

    public void onCloseGUI() {
        frame.dispose();
    }
}
