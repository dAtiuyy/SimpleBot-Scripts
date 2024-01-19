package daPaintTest;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.Objects;

public class daPaintTestGui extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String[] LIST = {"item1", "item2", "item3", "item4"};
    public static JComboBox<String> potionsComboBox;
    private JTextField itemTextField;
    private JButton startButton;
    private JButton pauseButton;

    public daPaintTestGui() {
        setTitle("Paint Test");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initGUI();

        pack();
    }

    private void initGUI() {
        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(Color.DARK_GRAY);
        setContentPane(contentPane);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Add title
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        addLabel("Please choose options below!", contentPane, constraints, true);

        constraints.gridwidth = 1; // Resetting gridwidth
        constraints.anchor = GridBagConstraints.WEST; // Resetting anchor

        constraints.gridy++; // Moving to next row
        constraints.gridx = 0; // Resetting x-axis position to 0

        // Select location
        addLabel("Select potions: ", contentPane, constraints, false);
        constraints.gridx = 1; // Setting x-axis position to 1
        potionsComboBox = addComboBox(contentPane, constraints);
        potionsComboBox.setPreferredSize(new Dimension(150, potionsComboBox.getPreferredSize().height));

        constraints.gridx = 0; // Resetting x-axis position to 0
        constraints.gridy++; // Moving to next row

        constraints.gridx = 0; // Resetting x-axis position to 0
        constraints.gridy++; // Moving to next row
        constraints.gridwidth = 2; // Resetting gridwidth
        constraints.anchor = GridBagConstraints.CENTER; // Resetting anchor

        // Start and Pause buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        startButton = new JButton("Start");
        startButton.addActionListener(e -> startBot());
        startButton.setBackground(Color.GREEN);
        buttonsPanel.add(startButton);
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> pauseBot());
        pauseButton.setVisible(false); // Setting button invisible
        pauseButton.setBackground(Color.RED);
        buttonsPanel.add(pauseButton);
        contentPane.add(buttonsPanel, constraints);
    }

    private void addLabel(String text, Container container, GridBagConstraints constraints, boolean isTitle) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        if (isTitle) {
            label.setFont(label.getFont().deriveFont(Font.BOLD, 16));
        }
        container.add(label, constraints);
        constraints.gridx++;
    }

    private JComboBox<String> addComboBox(Container container, GridBagConstraints constraints) {
        JComboBox<String> comboBox = new JComboBox<>(LIST);
        container.add(comboBox, constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        return comboBox;
    }

    private void startBot() {
        daPaintTestMain.botStarted = true;
        pauseButton.setVisible(true);
        startButton.setVisible(false);
        potionsComboBox.setEnabled(false);
    }

    private void pauseBot() {
        daPaintTestMain.botStarted = false;
        pauseButton.setVisible(false);
        startButton.setVisible(true);
        potionsComboBox.setEnabled(true);
    }
}
