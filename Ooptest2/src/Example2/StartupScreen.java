package Example2;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartupScreen extends JFrame {
    private JTextField playerNameField;
    private JButton startButton;

    public StartupScreen() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Startup Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Create a main panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.MAGENTA);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Create a content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE); // Ensure the content panel has a background color
        TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(10000), "Enter Player Name");
        titledBorder.setTitleJustification(TitledBorder.CENTER); // Center the title
        contentPanel.setBorder(titledBorder); // Set the border with a title
        contentPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Prewritten text label
        JLabel prewrittenTextLabel = new JLabel("Bike Game made by HABIBA BASATHIA :)");
        prewrittenTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        prewrittenTextLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(prewrittenTextLabel);

        // Player name input
        playerNameField = new JTextField();
        contentPanel.add(playerNameField);

        // Start button
        startButton = new JButton("START GAME");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(playerNameField.getText());
            }
        });
        contentPanel.add(startButton);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Ensure components are properly rendered
        setVisible(true);
    }

    private void startGame(String playerName) {
        SwingUtilities.invokeLater(() -> new Game2(playerName).setVisible(true));
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartupScreen startupScreen = new StartupScreen();
            startupScreen.setVisible(true);
        });
    }
}
