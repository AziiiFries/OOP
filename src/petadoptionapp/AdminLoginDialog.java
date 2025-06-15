package petadoptionapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.plaf.basic.BasicButtonUI;

// Encapsulation - Bundling data (fields) and methods that operate on the data
public class AdminLoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private boolean loggedIn = false;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final Color PRIMARY_BLUE = Color.decode("#2B4576");
    private static final Color LIGHT_GREY_BG = Color.decode("#F8F8F8");
    private static final Color BORDER_GREY = Color.decode("#E0E0E0");
    private static final Color TEXT_DARK_GREY = Color.decode("#333333");

    public AdminLoginDialog(Frame owner) {
        super(owner, "Admin Login", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(350, 340);

        // Abstraction - Hiding complex implementation details (custom painting)
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = getWidth();
                int height = getHeight();
                int arc = 20;
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fill(new RoundRectangle2D.Double(5, 5, width - 10, height - 10, arc, arc));
                g2.setColor(LIGHT_GREY_BG);
                g2.fillRoundRect(0, 0, width - 1, height - 1, arc, arc);
                g2.setColor(BORDER_GREY);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(30, 30, 20, 30));

        JLabel titleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(PRIMARY_BLUE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_DARK_GREY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(8));

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        usernameField.setForeground(TEXT_DARK_GREY);
        usernameField.setBackground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GREY, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        usernameField.setPreferredSize(new Dimension(200, 45));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(20));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_DARK_GREY);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(8));

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passwordField.setForeground(TEXT_DARK_GREY);
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GREY, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        passwordField.setPreferredSize(new Dimension(200, 45));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(PRIMARY_BLUE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(new EmptyBorder(12, 30, 12, 30));

        // Polymorphism - Same method name (paint) with different implementations
        loginButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                JButton btn = (JButton) c;
                int width = btn.getWidth();
                int height = btn.getHeight();
                int arc = 15;
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(2, 2, width - 4, height - 4, arc, arc);
                if (btn.getModel().isArmed()) {
                    g2.setColor(PRIMARY_BLUE.darker());
                } else if (btn.getModel().isRollover()) {
                    g2.setColor(Color.decode("#4A699A"));
                } else {
                    g2.setColor(PRIMARY_BLUE);
                }
                g2.fillRoundRect(0, 0, width, height, arc, arc);
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
        setLocationRelativeTo(owner);

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
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            usernameField.requestFocusInWindow();
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}