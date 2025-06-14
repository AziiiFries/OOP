package petadoptionapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;

public class ContactDialog extends JDialog {

    private Image backgroundImage;
    private Image secondImage;
    private boolean showingSecondImage = false;
    private JPanel contentPanel;

    public ContactDialog(Frame owner, String contactNumber1, String contactNumber2, String contactEmail) {
        super(owner, "Contact Us", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setSize(700, 500);

        try {
            URL firstImageUrl = getClass().getResource("/resources/2.png");
            if (firstImageUrl != null) {
                backgroundImage = new ImageIcon(firstImageUrl).getImage();
            }
            
            URL secondImageUrl = getClass().getResource("/resources/4.png");
            if (secondImageUrl != null) {
                secondImage = new ImageIcon(secondImageUrl).getImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Image currentImage = showingSecondImage ? secondImage : backgroundImage;
                if (currentImage != null) {
                    g2.drawImage(currentImage, 0, 0, getWidth(), getHeight(), this);
                }

                int arc = 20;
                g2.setColor(new Color(0, 0, 0, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            }
        };
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 24));
        closeButton.setForeground(Color.BLACK);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        
        JPanel closeButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeButtonPanel.setOpaque(false);
        closeButtonPanel.add(closeButton);
        mainPanel.add(closeButtonPanel, BorderLayout.NORTH);

        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!SwingUtilities.isDescendingFrom(e.getComponent(), closeButtonPanel)) {
                    showingSecondImage = !showingSecondImage;
                    toggleContactVisibility();
                    mainPanel.repaint();
                    
                    Timer timer = new Timer(20, null);
                    final float[] opacity = {0f};
                    timer.addActionListener(evt -> {
                        opacity[0] += 0.05f;
                        if (opacity[0] >= 1.0f) {
                            opacity[0] = 1.0f;
                            timer.stop();
                        }
                        mainPanel.repaint();
                    });
                    timer.start();
                }
            }
        });

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Get in Touch!");
        titleLabel.setFont(new Font("Arial Narrow", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 0, 128));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Contact details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        detailsPanel.setMaximumSize(new Dimension(350, Integer.MAX_VALUE));

        // Phone numbers
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Arial Narrow", Font.BOLD, 20));
        phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel number1Label = new JLabel(contactNumber1);
        number1Label.setFont(new Font("Arial Narrow", Font.PLAIN, 18));
        number1Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel number2Label = new JLabel(contactNumber2);
        number2Label.setFont(new Font("Arial Narrow", Font.PLAIN, 18));
        number2Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Email
        JLabel emailStaticLabel = new JLabel("Email:");
        emailStaticLabel.setFont(new Font("Arial Narrow", Font.BOLD, 20));
        emailStaticLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailStaticLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel emailLabel = new JLabel(contactEmail);
        emailLabel.setFont(new Font("Arial Narrow", Font.PLAIN, 18));
        emailLabel.setForeground(new Color(0, 0, 220));
        emailLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    String url = "https://mail.google.com/mail/?view=cm&fs=1&to=furgiveph@gmail.com";
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    try {
                        Desktop.getDesktop().mail(new URI("mailto:furgiveph@gmail.com"));
                    } catch (Exception ex2) {
                        JOptionPane.showMessageDialog(ContactDialog.this, 
                            "Unable to open email client.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        detailsPanel.add(phoneLabel);
        detailsPanel.add(number1Label);
        detailsPanel.add(number2Label);
        detailsPanel.add(emailStaticLabel);
        detailsPanel.add(emailLabel);

        contentPanel.add(titleLabel);
        contentPanel.add(detailsPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setLocationRelativeTo(owner);
        
        toggleContactVisibility();
    }

    private void toggleContactVisibility() {
        contentPanel.setVisible(showingSecondImage);
    }
}