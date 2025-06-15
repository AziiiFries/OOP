package petadoptionapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.awt.Desktop;
import java.awt.geom.RoundRectangle2D;
import javax.swing.plaf.basic.BasicButtonUI;

// Inheritance - Extends JDialog base class
public class PetDetailsDialog extends JDialog {

    // Encapsulation - Private constants for UI configuration
    private static final Color BACKGROUND_LIGHT_GREY = Color.decode("#F8F8F8");
    private static final Color BORDER_LIGHT_GREY = Color.decode("#E0E0E0");
    private static final Color TEXT_DARK_GREY = Color.decode("#333333");
    private static final Color ACCENT_BLUE = Color.decode("#2B4576");
    private static final Color ACCENT_BLUE_HOVER = Color.decode("#4A699A");
    private static final Color SECONDARY_ACCENT_GREY = Color.decode("#78909C");
    private static final Color SECONDARY_ACCENT_GREY_HOVER = Color.decode("#546E7A");
    private static final Color ERROR_TEXT_GREY = Color.decode("#666666");

    private static final Font FONT_NAME_LABEL = new Font("SansSerif", Font.BOLD, 28);
    private static final Font FONT_AGE_LABEL = new Font("SansSerif", Font.PLAIN, 19);
    private static final Font FONT_DESCRIPTION_TEXT = new Font("SansSerif", Font.PLAIN, 17);
    private static final Font FONT_BUTTON_PRIMARY = new Font("SansSerif", Font.BOLD, 18);
    private static final Font FONT_BUTTON_SECONDARY = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_IMAGE_ERROR = new Font("SansSerif", Font.ITALIC, 14);

    private static final int DIALOG_ARC_RADIUS = 25;
    private static final int DIALOG_SHADOW_OFFSET = 5;
    private static final int DIALOG_BORDER_PADDING = 30;
    private static final int PET_IMAGE_SIZE = 250;
    private static final int BUTTON_ROUND_ARC = 25;

    private static final String DONATE_URL = "https://docs.google.com/forms/d/e/1FAIpQLSda_jQUn0XWPIzr3Eli5bVkoWOW10H_VVsiQDWC-dWoHyiPMQ/viewform?usp=sharing&ouid=108321204493867680753";
    private static final String ADOPT_URL = "https://docs.google.com/forms/d/e/1FAIpQLSfid0_HZ6eX5P7FRYnQ2NTmOALPW6lwjK5EKyP-n515s8tbMQ/viewform?usp=sharing&ouid=108321204493867680753";

    // Encapsulation - Controls how dialog is created and set up
    public PetDetailsDialog(Frame owner, Pet pet) {
        super(owner, "Details for " + pet.getName(), true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setSize(900, 850);
        setLocationRelativeTo(owner);

        // Polymorphism - Custom JPanel implementation
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = DIALOG_ARC_RADIUS;
                int width = getWidth();
                int height = getHeight();

                g2.setColor(new Color(0, 0, 0, 20));
                g2.fill(new RoundRectangle2D.Double(DIALOG_SHADOW_OFFSET, DIALOG_SHADOW_OFFSET, 
                    width - DIALOG_SHADOW_OFFSET, height - DIALOG_SHADOW_OFFSET, arc, arc));

                g2.setColor(BACKGROUND_LIGHT_GREY);
                g2.fillRoundRect(0, 0, width, height, arc, arc);

                g2.setColor(BORDER_LIGHT_GREY);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(DIALOG_BORDER_PADDING, DIALOG_BORDER_PADDING, 
                                         DIALOG_BORDER_PADDING, DIALOG_BORDER_PADDING));

        JLabel petImageLabel = new JLabel();
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(pet.getImagePath()));
            Image scaledImage = originalIcon.getImage().getScaledInstance(PET_IMAGE_SIZE, PET_IMAGE_SIZE, Image.SCALE_SMOOTH);
            petImageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception ex) {
            petImageLabel.setText("No Image Available");
            petImageLabel.setFont(FONT_IMAGE_ERROR);
            petImageLabel.setForeground(ERROR_TEXT_GREY);
        }
        petImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        petImageLabel.setBorder(new EmptyBorder(0, 0, 25, 0));
        mainPanel.add(petImageLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setOpaque(false);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel nameLabel = new JLabel("<html><b style='color:" + toHtmlColor(TEXT_DARK_GREY) + ";'>Name:</b> <span style='color:" + toHtmlColor(ACCENT_BLUE) + ";'>" + pet.getName() + "</span></html>");
        nameLabel.setFont(FONT_NAME_LABEL);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String ageText;
        if (pet.getAge() == 0 && pet.getMonths() > 0) {
            ageText = pet.getMonths() + " months";
        } else if (pet.getAge() > 0 && pet.getMonths() > 0) {
            ageText = pet.getAge() + " years & " + pet.getMonths() + " months";
        } else {
            ageText = pet.getAge() + " years";
        }
        JLabel ageLabel = new JLabel("<html><b style='color:" + toHtmlColor(TEXT_DARK_GREY) + ";'>Age:</b> " + ageText + "</html>");
        ageLabel.setFont(FONT_AGE_LABEL);
        ageLabel.setForeground(TEXT_DARK_GREY);
        ageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JEditorPane descriptionEditorPane = new JEditorPane();
        descriptionEditorPane.setContentType("text/html");
        descriptionEditorPane.setEditable(false);
        descriptionEditorPane.setOpaque(false);
        descriptionEditorPane.setBackground(BACKGROUND_LIGHT_GREY);

        String formattedDescription = "<html><body style='font-family:SansSerif; font-size:" + FONT_DESCRIPTION_TEXT.getSize() + "px; color:" + toHtmlColor(TEXT_DARK_GREY) + "; margin:0; padding:0;'>" +
                                      pet.getDescription().replace("Color:", "<b>Color:</b>")
                                        .replace("Breed:", "<b>Breed:</b>")
                                        .replace("Health Status:", "<b>Health Status:</b>")
                                        .replace("Spayed/Neutered:", "<b>Spayed/Neutered:</b>")
                                        .replace("Vaccinations & Deworm:", "<b>Vaccinations & Deworm:</b>")
                                        .replace("Description:", "<b>Description:</b>")
                                        .replace("\n", "<br>") + "</body></html>";
        descriptionEditorPane.setText(formattedDescription);

        JScrollPane descriptionScrollPane = new JScrollPane(descriptionEditorPane);
        descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        descriptionScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT_GREY, 1));
        descriptionScrollPane.setOpaque(false);
        descriptionScrollPane.getViewport().setOpaque(false);
        descriptionScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(ageLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(descriptionScrollPane);
        detailsPanel.add(Box.createVerticalGlue());

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel bottomButtonsContainer = new JPanel(new BorderLayout());
        bottomButtonsContainer.setOpaque(false);
        bottomButtonsContainer.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel topRowButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        topRowButtons.setOpaque(false);

        JButton backButton = createStyledButton("Back", SECONDARY_ACCENT_GREY);
        backButton.addActionListener(e -> dispose());
        topRowButtons.add(backButton);

        JButton donateButton = createStyledButton("Sponsor Me", SECONDARY_ACCENT_GREY);
        donateButton.addActionListener(e -> {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(DONATE_URL));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening link", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        topRowButtons.add(donateButton);

        bottomButtonsContainer.add(topRowButtons, BorderLayout.NORTH);

        // Polymorphism - Dynamic behavior based on pet type
        String adoptButtonText = (pet instanceof Dog) ? "ADOPT THIS DOG!" : "ADOPT THIS CAT!";
        JButton adoptButton = createStyledButton(adoptButtonText, ACCENT_BLUE);
        adoptButton.setPreferredSize(new Dimension(250, 55));
        adoptButton.setFont(FONT_BUTTON_PRIMARY);
        adoptButton.addActionListener(e -> {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(ADOPT_URL));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening link", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel adoptButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        adoptButtonWrapper.setOpaque(false);
        adoptButtonWrapper.add(adoptButton);
        bottomButtonsContainer.add(adoptButtonWrapper, BorderLayout.SOUTH);

        mainPanel.add(bottomButtonsContainer, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    // Encapsulation - Private helper method
    private String toHtmlColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    // Abstraction - Complex button creation encapsulated
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON_SECONDARY);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        final Color originalBg = bgColor;
        final Color darkerBg = bgColor.equals(ACCENT_BLUE) ? ACCENT_BLUE_HOVER : 
                             bgColor.equals(SECONDARY_ACCENT_GREY) ? SECONDARY_ACCENT_GREY_HOVER :
                             new Color(Math.max(0, bgColor.getRed() - 20),
                                      Math.max(0, bgColor.getGreen() - 20),
                                      Math.max(0, bgColor.getBlue() - 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(darkerBg);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalBg);
            }
        });

        // Polymorphism - Custom UI implementation
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                JButton btn = (JButton) c;
                int width = btn.getWidth();
                int height = btn.getHeight();
                int arc = BUTTON_ROUND_ARC;

                g2.setColor(btn.getModel().isArmed() ? bgColor.darker() : btn.getBackground());
                g2.fillRoundRect(0, 0, width, height, arc, arc);

                super.paint(g2, c);
                g2.dispose();
            }
        });

        return button;
    }
}