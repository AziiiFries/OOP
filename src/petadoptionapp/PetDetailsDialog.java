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

/**
 * PetDetailsDialog displays detailed information about a specific pet
 * in a modal dialog window. It includes the pet's image, name, age,
 * a scrollable description, and buttons for adoption and sponsorship.
 * The dialog features a modern, clean UI with rounded corners and shadows.
 *
 * This version uses a JEditorPane for the description to reliably display
 * HTML formatted text, while retaining all custom design elements.
 */
public class PetDetailsDialog extends JDialog {

    // --- Color Palette Definitions ---
    private static final Color BACKGROUND_LIGHT_GREY = Color.decode("#F8F8F8");
    private static final Color BORDER_LIGHT_GREY = Color.decode("#E0E0E0");
    private static final Color TEXT_DARK_GREY = Color.decode("#333333");
    private static final Color ACCENT_BLUE = Color.decode("#2B4576"); // Primary accent for "Adopt" button
    private static final Color ACCENT_BLUE_HOVER = Color.decode("#4A699A"); // Lighter blue for hover effect on primary buttons
    private static final Color SECONDARY_ACCENT_GREY = Color.decode("#78909C"); // Secondary accent for "Back", "Sponsor" buttons
    private static final Color SECONDARY_ACCENT_GREY_HOVER = Color.decode("#546E7A"); // Darker blue-grey for hover effect on secondary buttons
    private static final Color ERROR_TEXT_GREY = Color.decode("#666666"); // Color for error messages (e.g., missing image)

    // --- Font Definitions ---
    private static final Font FONT_NAME_LABEL = new Font("SansSerif", Font.BOLD, 28);
    private static final Font FONT_AGE_LABEL = new Font("SansSerif", Font.PLAIN, 19);
    private static final Font FONT_DESCRIPTION_TEXT = new Font("SansSerif", Font.PLAIN, 17); // Used for JEditorPane content
    private static final Font FONT_BUTTON_PRIMARY = new Font("SansSerif", Font.BOLD, 18);
    private static final Font FONT_BUTTON_SECONDARY = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_IMAGE_ERROR = new Font("SansSerif", Font.ITALIC, 14);

    // --- UI Dimension and Styling Parameters ---
    private static final int DIALOG_ARC_RADIUS = 25;
    private static final int DIALOG_SHADOW_OFFSET = 5;
    private static final int DIALOG_BORDER_PADDING = 30;
    private static final int PET_IMAGE_SIZE = 250;
    private static final int BUTTON_ROUND_ARC = 25;

    // --- External URLs ---
    private static final String DONATE_URL = "https://docs.google.com/forms/d/e/1FAIpQLSda_jQUn0XWPIzr3Eli5bVkoWOW10H_VVsiQDWC-dWoHyiPMQ/viewform?usp=sharing&ouid=108321204493867680753";
    private static final String ADOPT_URL = "https://docs.google.com/forms/d/e/1FAIpQLSfid0_HZ6eX5P7FRYnQ2NTmOALPW6lwjK5EKyP-n515s8tbMQ/viewform?usp=sharing&ouid=108321204493867680753";


    /**
     * Constructs a new PetDetailsDialog.
     * @param owner The parent Frame of this dialog.
     * @param pet The Pet object whose details are to be displayed.
     */
    public PetDetailsDialog(Frame owner, Pet pet) {
        super(owner, "Details for " + pet.getName(), true); // true makes it modal, blocking interaction with parent
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Close operation disposes the dialog
        setResizable(false); // Dialog is not resizable by the user
        setUndecorated(true); // Remove default window decorations to allow custom painting

        // Set the preferred size for the dialog window.
        // Increased size to accommodate more content comfortably.
        setSize(900, 850); // Width: 900, Height: 850
        setLocationRelativeTo(owner); // Center the dialog relative to its owner frame

        // Main panel for the dialog content. This panel is custom-painted
        // to have rounded corners and a subtle shadow effect.
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                // Ensure super.paintComponent is called to handle background if any
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = DIALOG_ARC_RADIUS; // Radius for rounded corners
                int x = 0;
                int y = 0;
                int width = getWidth();
                int height = getHeight();

                // Draw subtle shadow before filling the background.
                // The shadow is slightly offset to give a "lifted" appearance.
                g2.setColor(new Color(0, 0, 0, 20)); // Subtle shadow color (black with 20% opacity)
                g2.fill(new RoundRectangle2D.Double(
                    DIALOG_SHADOW_OFFSET, // Offset
                    DIALOG_SHADOW_OFFSET, // Offset
                    width - DIALOG_SHADOW_OFFSET,
                    height - DIALOG_SHADOW_OFFSET,
                    arc, arc
                ));

                // Fill the main background of the dialog with rounded corners.
                g2.setColor(BACKGROUND_LIGHT_GREY); // Modern: Very light grey background
                g2.fillRoundRect(x, y, width, height, arc, arc);

                // Draw a subtle border around the rounded rectangle.
                g2.setColor(BORDER_LIGHT_GREY); // Modern: Light grey border
                g2.setStroke(new BasicStroke(1)); // 1-pixel stroke for the border
                g2.drawRoundRect(x, y, width - 1, height - 1, arc, arc); // Draw inside bounds
            }
        };
        mainPanel.setOpaque(false); // Make sure it's transparent for custom painting to show through
        // Add increased padding around the content within the main panel.
        mainPanel.setBorder(new EmptyBorder(DIALOG_BORDER_PADDING,
                                            DIALOG_BORDER_PADDING,
                                            DIALOG_BORDER_PADDING,
                                            DIALOG_BORDER_PADDING));

        // --- Pet Image Section ---
        JLabel petImageLabel = new JLabel();
        try {
            // Load and scale the pet's image from the resource path.
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(pet.getImagePath()));
            Image scaledImage = originalIcon.getImage().getScaledInstance(
                PET_IMAGE_SIZE, PET_IMAGE_SIZE, Image.SCALE_SMOOTH
            );
            petImageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (NullPointerException | IllegalArgumentException ex) { // Catch IllegalArgumentException for bad path
            // Handle cases where the image path is invalid or image cannot be loaded.
            petImageLabel.setText("No Image Available"); // User-friendly message
            petImageLabel.setFont(FONT_IMAGE_ERROR); // Apply error font
            petImageLabel.setForeground(ERROR_TEXT_GREY); // Apply error text color
            System.err.println("Could not load image for: " + pet.getName() + " at path: " + pet.getImagePath() + " - " + ex.getMessage());
        }
        petImageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image horizontally
        petImageLabel.setBorder(new EmptyBorder(0, 0, 25, 0)); // Padding below the image
        mainPanel.add(petImageLabel, BorderLayout.NORTH); // Place image at the top

        // --- Pet Details Section (Name, Age, Description) ---
        JPanel detailsPanel = new JPanel();
        detailsPanel.setOpaque(false); // Make transparent for main panel's background
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS)); // Stack components vertically
        detailsPanel.setBorder(new EmptyBorder(0, 0, 20, 0)); // Padding above buttons

        // Pet Name Label: Formatted with HTML for bold label and accent color.
        JLabel nameLabel = new JLabel("<html><b style='color:" + toHtmlColor(TEXT_DARK_GREY) + ";'>Name:</b> <span style='color:" + toHtmlColor(ACCENT_BLUE) + ";'>" + pet.getName() + "</span></html>");
        nameLabel.setFont(FONT_NAME_LABEL); // Apply defined font for name
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align within BoxLayout

        // Pet Age Label: Calculates age text (years, months, or both).
        String ageText;
        if (pet.getAge() == 0 && pet.getMonths() > 0) {
            ageText = pet.getMonths() + " months";
        } else if (pet.getAge() > 0 && pet.getMonths() > 0) {
            ageText = pet.getAge() + " years & " + pet.getMonths() + " months";
        } else {
            ageText = pet.getAge() + " years";
        }
        // Formatted with HTML for bold label.
        JLabel ageLabel = new JLabel("<html><b style='color:" + toHtmlColor(TEXT_DARK_GREY) + ";'>Age:</b> " + ageText + "</html>");
        ageLabel.setFont(FONT_AGE_LABEL); // Apply defined font for age
        ageLabel.setForeground(TEXT_DARK_GREY); // Apply text color
        ageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align

        // Pet Description JEditorPane: Used for displaying HTML formatted text reliably.
        JEditorPane descriptionEditorPane = new JEditorPane();
        descriptionEditorPane.setContentType("text/html"); // Set content type to HTML
        descriptionEditorPane.setEditable(false); // Make it read-only
        descriptionEditorPane.setOpaque(false); // Make transparent to show parent background
        descriptionEditorPane.setBackground(BACKGROUND_LIGHT_GREY); // Match dialog background for seamless look

        // Apply HTML formatting for bolding keywords and handling newlines.
        String rawDescription = pet.getDescription();
        String formattedDescription = "<html><body style='font-family:SansSerif; font-size:" + FONT_DESCRIPTION_TEXT.getSize() + "px; color:" + toHtmlColor(TEXT_DARK_GREY) + "; margin:0; padding:0;'>" +
                                      rawDescription.replace("Color:", "<b>Color:</b>")
                                                    .replace("Breed:", "<b>Breed:</b>")
                                                    .replace("Health Status:", "<b>Health Status:</b>")
                                                    .replace("Spayed/Neutered:", "<b>Spayed/Neutered:</b>")
                                                    .replace("Vaccinations & Deworm:", "<b>Vaccinations & Deworm:</b>")
                                                    .replace("Description:", "<b>Description:</b>")
                                                    .replace("\n", "<br>") // Convert newlines to HTML breaks
                                      + "</body></html>";
        descriptionEditorPane.setText(formattedDescription); // Set HTML content

        // JScrollPane for the description JEditorPane, ensuring scrollability for long text.
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionEditorPane);
        descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Only show vertical scrollbar if content exceeds height
        descriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Never show horizontal scrollbar
        descriptionScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT_GREY, 1)); // Subtle border for the scroll pane
        descriptionScrollPane.setOpaque(false); // Make transparent
        descriptionScrollPane.getViewport().setOpaque(false); // Make viewport transparent
        descriptionScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT); // Helps the scroll pane expand within BoxLayout

        // Add details components to the details panel.
        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Vertical spacing
        detailsPanel.add(ageLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Larger vertical spacing before description
        detailsPanel.add(descriptionScrollPane);
        detailsPanel.add(Box.createVerticalGlue()); // Allows the descriptionScrollPane to take available vertical space

        mainPanel.add(detailsPanel, BorderLayout.CENTER); // Place details panel in the center of main dialog

        // --- Button Section (Back, Sponsor, Adopt) ---
        JPanel bottomButtonsContainer = new JPanel(new BorderLayout());
        bottomButtonsContainer.setOpaque(false); // Make transparent
        bottomButtonsContainer.setBorder(new EmptyBorder(20, 0, 0, 0)); // Top padding for separation from description

        JPanel topRowButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0)); // FlowLayout for horizontal buttons
        topRowButtons.setOpaque(false); // Make transparent

        // Back button: Disposes the dialog when clicked.
        JButton backButton = createStyledButton("Back", SECONDARY_ACCENT_GREY);
        backButton.addActionListener(e -> dispose());
        topRowButtons.add(backButton);

        // Donate/Sponsor button: Opens a Google Form URL in the default browser.
        JButton donateButton = createStyledButton("Sponsor Me", SECONDARY_ACCENT_GREY);
        donateButton.addActionListener(e -> {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(DONATE_URL));
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot open browser. Please visit:\n" + DONATE_URL, "Browser Not Supported", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace(); // Print stack trace for debugging
                JOptionPane.showMessageDialog(this, "Error opening link: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        topRowButtons.add(donateButton);

        bottomButtonsContainer.add(topRowButtons, BorderLayout.NORTH); // Place the top row of buttons

        // Adopt This Dog/Cat button: Larger, prominent button for the main action.
        String adoptButtonText = (pet instanceof Dog) ? "ADOPT THIS DOG!" : "ADOPT THIS CAT!";
        JButton adoptButton = createStyledButton(adoptButtonText, ACCENT_BLUE);
        adoptButton.setPreferredSize(new Dimension(250, 55)); // Made significantly bigger
        adoptButton.setFont(FONT_BUTTON_PRIMARY); // Larger font for prominence
        adoptButton.addActionListener(e -> {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(ADOPT_URL));
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot open browser. Please visit:\n" + ADOPT_URL, "Browser Not Supported", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace(); // Print stack trace for debugging
                JOptionPane.showMessageDialog(this, "Error opening link: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel adoptButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Wrapper to center the button
        adoptButtonWrapper.setOpaque(false); // Make transparent
        adoptButtonWrapper.add(adoptButton);
        bottomButtonsContainer.add(adoptButtonWrapper, BorderLayout.SOUTH); // Place the adopt button at the bottom

        mainPanel.add(bottomButtonsContainer, BorderLayout.SOUTH); // Add the entire button container to the main panel

        setContentPane(mainPanel); // Set the main panel as the dialog's content pane
    }

    /**
     * Helper method to convert a Java AWT Color object to its HTML hexadecimal string representation.
     * This is useful for embedding colors directly into HTML strings used by JLabels.
     * @param color The Color object to convert.
     * @return A String representing the color in #RRGGBB hexadecimal format.
     */
    private String toHtmlColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Helper method to create consistently styled JButtons with rounded corners,
     * custom background colors, and a subtle hover effect.
     * @param text The text to display on the button.
     * @param bgColor The background Color of the button.
     * @return A pre-styled JButton instance.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON_SECONDARY); // Apply defined secondary button font
        button.setBackground(bgColor); // Set initial background color
        button.setForeground(Color.WHITE); // Set text color to white
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25)); // Padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor on hover to indicate clickable

        // Define hover and pressed colors based on the original background color.
        final Color originalBg = bgColor;
        final Color darkerBg; // This will be the hover color

        // Determine specific hover color based on whether it's a primary or secondary button color
        if (originalBg.equals(ACCENT_BLUE)) {
            darkerBg = ACCENT_BLUE_HOVER;
        } else if (originalBg.equals(SECONDARY_ACCENT_GREY)) {
            darkerBg = SECONDARY_ACCENT_GREY_HOVER;
        } else {
            // Fallback: If not a predefined color, just darken the original color.
            darkerBg = new Color(Math.max(0, originalBg.getRed() - 20),
                                 Math.max(0, originalBg.getGreen() - 20),
                                 Math.max(0, originalBg.getBlue() - 20));
        }

        // Add mouse listener for hover effects.
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(darkerBg); // Change to darker color on mouse entry
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalBg); // Revert to original color on mouse exit
            }
        });

        // Custom UI for rounded corners using BasicButtonUI override.
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                JButton btn = (JButton) c;
                int width = btn.getWidth();
                int height = btn.getHeight();
                int arc = BUTTON_ROUND_ARC; // Rounded corner radius

                // Apply darker color when button is pressed/armed.
                if (btn.getModel().isArmed()) {
                    g2.setColor(bgColor.darker()); // Darken current background color when pressed
                } else {
                    g2.setColor(btn.getBackground()); // Use the button's current background color
                }
                g2.fillRoundRect(0, 0, width, height, arc, arc); // Fill the button shape

                // Paint the text and icon (if any) of the button.
                super.paint(g2, c);
                g2.dispose(); // Release Graphics2D resources
            }
        });

        return button;
    }
}
