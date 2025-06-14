// AdminLoginDialog.java
package petadoptionapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter; // For hover effects
import java.awt.event.MouseEvent; // For hover effects
import java.awt.geom.RoundRectangle2D; // For rounded corners
import javax.swing.plaf.basic.BasicButtonUI; // For custom button UI

public class AdminLoginDialog extends JDialog {

    private JTextField usernameField; // New username field
    private JPasswordField passwordField;
    private JButton loginButton;
    private boolean loggedIn = false;

    private static final String ADMIN_USERNAME = "admin"; // Default admin username
    private static final String ADMIN_PASSWORD = "admin"; // Default admin password
    private static final Color PRIMARY_BLUE = Color.decode("#2B4576"); // Consistent accent color
    private static final Color LIGHT_GREY_BG = Color.decode("#F8F8F8");
    private static final Color BORDER_GREY = Color.decode("#E0E0E0");
    private static final Color TEXT_DARK_GREY = Color.decode("#333333");

    public AdminLoginDialog(Frame owner) {
        super(owner, "Admin Login", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true); // Make undecorated to allow custom shape and shadow
        setBackground(new Color(0, 0, 0, 0)); // Transparent background for shadow effect

        // Increased height for better spacing for fields and overall dialog
        setSize(350, 340); // Increased height from 300 to 340

        // Main panel with custom painting for rounded corners and shadow
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int arc = 20; // Rounded corner radius

                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 50)); // Shadow color
                g2.fill(new RoundRectangle2D.Double(5, 5, width - 10, height - 10, arc, arc));

                // Draw main background
                g2.setColor(LIGHT_GREY_BG);
                g2.fillRoundRect(0, 0, width - 1, height - 1, arc, arc);

                // Draw border
                g2.setColor(BORDER_GREY);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

                super.paintComponent(g2); // Paint child components
                g2.dispose();
            }
        };
        mainPanel.setOpaque(false); // Crucial for custom painting
        mainPanel.setBorder(new EmptyBorder(30, 30, 20, 30)); // Padding inside the dialog

        JLabel titleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26)); // Larger, bolder title
        titleLabel.setForeground(PRIMARY_BLUE); // Primary blue color
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false); // Transparent
        formPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Padding for the form fields

        // Username Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_DARK_GREY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align text to left
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(8)); // Increased spacer from 5 to 8

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        usernameField.setForeground(TEXT_DARK_GREY);
        usernameField.setBackground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GREY, 1),
                new EmptyBorder(8, 10, 8, 10) // Inner padding
        ));
        // Ensure consistent height for text fields
        usernameField.setPreferredSize(new Dimension(200, 45)); // Slightly increased preferred height
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Max height to match preferred
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT); // Align field to left
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(20)); // Increased spacer from 15 to 20

        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_DARK_GREY);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(8)); // Increased spacer from 5 to 8

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passwordField.setForeground(TEXT_DARK_GREY);
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GREY, 1),
                new EmptyBorder(8, 10, 8, 10) // Inner padding
        ));
        // Ensure consistent height for password field
        passwordField.setPreferredSize(new Dimension(200, 45)); // Slightly increased preferred height
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Max height to match preferred
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(PRIMARY_BLUE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(new EmptyBorder(12, 30, 12, 30)); // Generous padding

        // Custom UI for rounded corners and hover/pressed states for the login button
        loginButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                JButton btn = (JButton) c;
                int width = btn.getWidth();
                int height = btn.getHeight();
                int arc = 15; // Rounded corners for button

                // Shadow for button
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(2, 2, width - 4, height - 4, arc, arc);

                // Button background
                if (btn.getModel().isArmed()) {
                    g2.setColor(PRIMARY_BLUE.darker());
                } else if (btn.getModel().isRollover()) {
                    g2.setColor(Color.decode("#4A699A")); // ACCENT_LIGHT_BLUE
                } else {
                    g2.setColor(PRIMARY_BLUE);
                }
                g2.fillRoundRect(0, 0, width, height, arc, arc);

                // Paint the text
                super.paint(g2, c);
                g2.dispose();
            }
        });

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.repaint();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setLocationRelativeTo(owner); // Center the dialog relative to the owner

        // Request focus for the username field when the dialog becomes visible
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                usernameField.requestFocusInWindow();
            }
        });
    }

    private void attemptLogin() {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = new String(passwordField.getPassword());

        if (enteredUsername.equals(ADMIN_USERNAME) && enteredPassword.equals(ADMIN_PASSWORD)) {
            loggedIn = true;
            dispose(); // Close the dialog
        } else {
            // Reverted to using JOptionPane
            JOptionPane.showMessageDialog(this, "Incorrect username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText(""); // Clear password
            usernameField.requestFocusInWindow(); // Keep focus on username
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
