package daPrayers;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.Objects;

public class daGui extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String[] POTIONS = {"Prayer potion"};
    public static JComboBox<String> potionsComboBox;
    private JTextField itemTextField;
    private JButton startButton;
    private JButton pauseButton;

    public daGui() {
        setTitle("eHerbloreBot by Esmaabi, copied by alex");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("esmaabi-icon.png"))).getImage());

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

        // Enter NPC ID only as digits
        addLabel("DO NOT USE: ", contentPane, constraints, false);
        constraints.gridx = 1; // Setting x-axis position to 1
        itemTextField = addTextField(contentPane, constraints);
        itemTextField.setPreferredSize(new Dimension(150, itemTextField.getPreferredSize().height));

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
        JComboBox<String> comboBox = new JComboBox<>(POTIONS);
        container.add(comboBox, constraints);
        constraints.gridx = 0;
        constraints.gridy++;

        comboBox.addActionListener(e -> daMain.returnItem = getSelectedPotionIndex());

        return comboBox;
    }

    private JTextField addTextField(Container container, GridBagConstraints constraints) {
        JTextField textField = new JTextField();
        ((PlainDocument) textField.getDocument()).setDocumentFilter(new DigitDocumentFilter());
        container.add(textField, constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        return textField;
    }

    private void startBot() {
        daMain.botStarted = true;
        pauseButton.setVisible(true);
        startButton.setVisible(false);
        itemTextField.setEnabled(false);
        potionsComboBox.setEnabled(false);
        daMain.returnItem = getSelectedPotionIndex();
    }

    private void pauseBot() {
        daMain.botStarted = false;
        pauseButton.setVisible(false);
        startButton.setVisible(true);
        itemTextField.setEnabled(false);
        potionsComboBox.setEnabled(true);
    }

    public JTextField getItemTextField() {
        return itemTextField;
    }

    public int getSelectedPotionIndex() {
        int selectedPotionIndex = potionsComboBox.getSelectedIndex();
        return selectedPotionIndex;
    }

    public int getItemId() {
        String intAsText = getItemTextField().getText();
        int itemId = 0;
        try {
            itemId = Integer.parseInt(intAsText);
        } catch (NumberFormatException e) {
            // Handle the exception, e.g., show an error message or set a default value
        }
        return itemId;
    }

    // DocumentFilter class that accepts only digits
    private static class DigitDocumentFilter extends javax.swing.text.DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.chars().allMatch(Character::isDigit)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null && text.chars().allMatch(Character::isDigit)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}