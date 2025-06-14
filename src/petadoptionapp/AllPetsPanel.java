// AllPetsPanel.java
package petadoptionapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.geom.RoundRectangle2D; // For rounded corners
import javax.swing.plaf.basic.BasicButtonUI; // For custom button UI
import java.awt.event.MouseAdapter; // For hover effects
import java.awt.event.MouseEvent; // For hover effects
import java.net.URL; // Import URL

public class AllPetsPanel extends JPanel {
    private JFrame ownerFrame;
    private JPanel petsGridPanel;
    private ArrayList<Pet> fullPetList; // This will now be loaded/saved

    private String currentPetTypeFilter = "All"; // "All", "Cat", "Dog"
    private String currentGenderFilter = "All"; // "All", "Male", "Female"

    // --- Color Palette Definitions (from design-elements-find-a-pet) ---
    private static final Color BACKGROUND_COLOR = Color.decode("#F2F4F8");
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = Color.decode("#E0E0E0");
    private static final Color TEXT_COLOR = Color.decode("#333333");
    private static final Color ACCENT_BLUE = Color.decode("#2B4576"); // Consistent accent color
    private static final Color ACCENT_BLUE_HOVER = Color.decode("#4A699A"); // Lighter blue for hover
    private static final Color TEXT_LIGHT_GREY = Color.decode("#888888");


    public AllPetsPanel(JFrame ownerFrame) {
        this.ownerFrame = ownerFrame;
        // Load pets when the panel is initialized
        try {
            this.fullPetList = PetDataManager.loadPets();
            System.out.println("AllPetsPanel: Successfully loaded " + fullPetList.size() + " pets from PetDataManager.");
        } catch (Exception e) {
            System.err.println("AllPetsPanel: Failed to load pet data during initialization: " + e.getMessage());
            e.printStackTrace();
            // Fallback to an empty list or show an error to the user
            this.fullPetList = new ArrayList<>();
            JOptionPane.showMessageDialog(ownerFrame,
                                          "Failed to load pet data. Application might not function correctly. Check console for details.",
                                          "Data Loading Error",
                                          JOptionPane.ERROR_MESSAGE);
        }

        setBackground(BACKGROUND_COLOR); // Consistent light grey background
        setLayout(new BorderLayout()); // Main panel uses BorderLayout

        // --- Top Section: Title and Filters (from design-elements-find-a-pet) ---
        JPanel topContainerPanel = new JPanel();
        topContainerPanel.setLayout(new BoxLayout(topContainerPanel, BoxLayout.Y_AXIS)); // Stack vertically
        topContainerPanel.setBackground(BACKGROUND_COLOR); // Consistent light grey background

        // Title for the All Pets page
        JLabel titleLabel = new JLabel("Our Adoptable Fur Babies"); // More appealing title
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36)); // Increased font size for wider look
        titleLabel.setForeground(ACCENT_BLUE); // Changed accent color
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally within BoxLayout
        titleLabel.setBorder(new EmptyBorder(20, 50, 20, 50)); // Added horizontal padding for wider appearance
        topContainerPanel.add(titleLabel);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10)); // Increased horizontal gap and vertical padding
        filterPanel.setBackground(BACKGROUND_COLOR); // Consistent light grey background
        filterPanel.setBorder(new EmptyBorder(0, 10, 15, 10)); // More bottom padding
        filterPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally within BoxLayout
        
        // Pet Type Filter (Dropdown)
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 16)); // Slightly larger font for labels
        typeLabel.setForeground(TEXT_COLOR); // Dark grey text
        filterPanel.add(typeLabel);

        String[] petTypes = {"All", "Cat", "Dog"};
        JComboBox<String> typeDropdown = new JComboBox<>(petTypes);
        styleDropdown(typeDropdown); // Apply consistent styling (using the new styleDropdown method)
        typeDropdown.setSelectedItem(currentPetTypeFilter); // Set initial selection
        typeDropdown.addActionListener(e -> {
            currentPetTypeFilter = (String) typeDropdown.getSelectedItem();
            updatePetsDisplay();
        });
        filterPanel.add(typeDropdown);

        // Spacer between dropdowns
        filterPanel.add(Box.createRigidArea(new Dimension(40, 0))); // Larger gap

        // Gender Filter (Dropdown)
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("SansSerif", Font.BOLD, 16)); // Slightly larger font for labels
        genderLabel.setForeground(TEXT_COLOR); // Dark grey text
        filterPanel.add(genderLabel);

        String[] genders = {"All", "Male", "Female"};
        JComboBox<String> genderDropdown = new JComboBox<>(genders);
        styleDropdown(genderDropdown); // Apply consistent styling (using the new styleDropdown method)
        genderDropdown.setSelectedItem(currentGenderFilter); // Set initial selection
        genderDropdown.addActionListener(e -> {
            currentGenderFilter = (String) genderDropdown.getSelectedItem();
            updatePetsDisplay();
        });
        filterPanel.add(genderDropdown);

        topContainerPanel.add(filterPanel); // This adds the filter panel to the top container
        add(topContainerPanel, BorderLayout.NORTH);

        // --- Pet Grid Panel (from design-elements-find-a-pet) ---
        // Changed to GridLayout for fixed columns
        petsGridPanel = new JPanel(new GridLayout(0, 3, 25, 25)); // Increased gaps, 3 columns
        petsGridPanel.setBackground(BACKGROUND_COLOR); // Consistent light grey background
        // Widen horizontal padding for margins
        petsGridPanel.setBorder(new EmptyBorder(30, 150, 30, 150)); // Top: 30, Left: 150, Bottom: 30, Right: 150

        // Use a JScrollPane for the pet grid to handle many pets
        JScrollPane scrollPane = new JScrollPane(petsGridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default scroll pane border
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        scrollPane.setBackground(BACKGROUND_COLOR); // Consistent light grey background
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR); // Ensure viewport matches background

        add(scrollPane, BorderLayout.CENTER);

        updatePetsDisplay(); // Initial display of pets
    }

    // New methods for PetDataManager integration (from previous AllPetsPanel)
    public void addPet(Pet pet) {
        fullPetList.add(pet);
        PetDataManager.savePets(fullPetList); // Save after adding
        updatePetsDisplay();
    }

    public void removePet(Pet pet) {
        fullPetList.remove(pet);
        PetDataManager.savePets(fullPetList); // Save after removing
        updatePetsDisplay();
    }

    public ArrayList<Pet> getFullPetList() {
        return fullPetList;
    }

    // --- Helper Method to style JComboBoxes (Dropdowns) (from design-elements-find-a-pet) ---
    private void styleDropdown(JComboBox<String> dropdown) {
        dropdown.setFont(new Font("SansSerif", Font.PLAIN, 15)); // Slightly larger font for dropdown items
        dropdown.setBackground(Color.WHITE); // Modern: White background
        dropdown.setForeground(Color.decode("#333333")); // Modern: Dark grey text
        dropdown.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dropdown.setBorder(BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1)); // Modern: Light grey border
        dropdown.setRenderer(new DefaultListCellRenderer() { // For better dropdown item appearance
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(7, 7, 7, 7)); // Increased padding for dropdown items
                if (isSelected) {
                    label.setBackground(ACCENT_BLUE_HOVER); // Changed accent color (lighter blue for selection)
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(TEXT_COLOR);
                }
                return label;
            }
        });
    }

    public void updatePetsDisplay() {
        petsGridPanel.removeAll(); // Clear existing cards
        System.out.println("AllPetsPanel: Updating pets display. Current filter: Type=" + currentPetTypeFilter + ", Gender=" + currentGenderFilter);
        System.out.println("AllPetsPanel: Number of pets in fullPetList: " + fullPetList.size());

        boolean petsFound = false; // Flag to check if any pets are displayed

        for (Pet pet : fullPetList) {
            boolean matchesType = currentPetTypeFilter.equals("All") ||
                                  (currentPetTypeFilter.equals("Cat") && pet instanceof Cat) ||
                                  (currentPetTypeFilter.equals("Dog") && pet instanceof Dog);

            boolean matchesGender = currentGenderFilter.equals("All") ||
                                    currentGenderFilter.equals(pet.getGender());

            if (matchesType && matchesGender) {
                petsFound = true;
                // --- Pet Card Design (from design-elements-find-a-pet) ---
                JPanel petCard = new JPanel() {
                    private float scale = 1.0f;
                    private Timer scaleTimer;
                    private final int ANIMATION_STEPS = 5;
                    private final int ANIMATION_DELAY = 20;

                    { // Instance initializer block for setup
                        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                        setOpaque(false);
                        setBorder(new EmptyBorder(15, 15, 15, 15)); // Inner padding

                        setPreferredSize(new Dimension(250, 350)); // Adjusted to 250x350
                        setMaximumSize(new Dimension(250, 350));
                        setMinimumSize(new Dimension(250, 350));

                        addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseEntered(java.awt.event.MouseEvent evt) {
                                startScaleAnimation(1.05f);
                            }

                            @Override
                            public void mouseExited(java.awt.event.MouseEvent evt) {
                                startScaleAnimation(1.0f);
                            }
                        });
                    }

                    private void startScaleAnimation(float targetScale) {
                        if (scaleTimer != null && scaleTimer.isRunning()) {
                            scaleTimer.stop();
                        }

                        float startScale = scale;
                        float deltaScale = (targetScale - startScale) / ANIMATION_STEPS;

                        scaleTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
                            int step = 0;
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                step++;
                                scale = startScale + deltaScale * step;
                                if (step >= ANIMATION_STEPS) {
                                    scale = targetScale;
                                    ((Timer)e.getSource()).stop();
                                }
                                repaint();
                            }
                        });
                        scaleTimer.start();
                    }

                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        int cx = getWidth() / 2;
                        int cy = getHeight() / 2;
                        g2.translate(cx, cy);
                        g2.scale(scale, scale);
                        g2.translate(-cx, -cy);

                        int arc = 15;
                        int width = getWidth();
                        int height = getHeight();

                        g2.setColor(new Color(0, 0, 0, 15));
                        g2.fill(new RoundRectangle2D.Double(2, 2, width - 2, height - 2, arc, arc));

                        g2.setColor(CARD_BACKGROUND);
                        g2.fillRoundRect(0, 0, width, height, arc, arc);

                        super.paintComponent(g2);
                        g2.dispose();
                    }

                    @Override
                    protected void paintBorder(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        int cx = getWidth() / 2;
                        int cy = getHeight() / 2;
                        g2.translate(cx, cy);
                        g2.scale(scale, scale);
                        g2.translate(-cx, -cy);

                        int arc = 15;
                        int width = getWidth();
                        int height = getHeight();

                        g2.setColor(BORDER_COLOR);
                        g2.setStroke(new BasicStroke(1));
                        g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);
                        g2.dispose();
                    }
                };

                // --- Styling for Pet Image Label (from design-elements-find-a-pet) ---
                JLabel petImageLabel = new JLabel();
                petImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                try {
                    URL imageUrl = getClass().getResource(pet.getImagePath());
                    if (imageUrl != null) {
                        ImageIcon originalIcon = new ImageIcon(imageUrl);
                        // Scale image to 250x250 to fit within the fixed card size
                        Image scaledImage = originalIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                        petImageLabel.setIcon(new ImageIcon(scaledImage));
                        System.out.println("AllPetsPanel: Successfully loaded image for " + pet.getName() + " from " + pet.getImagePath());
                    } else {
                        // Fallback if image resource is null
                        petImageLabel.setText("Image N/A");
                        petImageLabel.setPreferredSize(new Dimension(250, 250)); // Adjusted fallback size
                        petImageLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
                        petImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        petImageLabel.setVerticalAlignment(SwingConstants.CENTER);
                        System.err.println("AllPetsPanel: Image resource (getClass().getResource) returned null for " + pet.getName() + " at path: " + pet.getImagePath());
                    }
                } catch (Exception e) {
                    // Catch all other exceptions during image loading
                    petImageLabel.setText("Image Error");
                    petImageLabel.setPreferredSize(new Dimension(250, 250)); // Adjusted fallback size
                    petImageLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
                    petImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    petImageLabel.setVerticalAlignment(SwingConstants.CENTER);
                    System.err.println("AllPetsPanel: Error loading image for " + pet.getName() + " at path: " + pet.getImagePath() + ". Error: " + e.getMessage());
                    e.printStackTrace(); // Print stack trace for detailed error
                }

                petImageLabel.setBorder(new EmptyBorder(0, 0, 10, 0)); // Padding below image

                // --- Styling for Pet Name Label (from design-elements-find-a-pet) ---
                JLabel petNameLabel = new JLabel(pet.getName());
                petNameLabel.setFont(new Font("SansSerif", Font.BOLD, 18)); // Slightly larger font for name
                petNameLabel.setForeground(TEXT_COLOR); // Modern: Dark grey text
                petNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align
                petNameLabel.setBorder(new EmptyBorder(5, 0, 10, 0)); // Padding above and below name

                // --- Styling for "ABOUT ME" Button (from design-elements-find-a-pet) ---
                JButton aboutMeButton = new JButton("ABOUT ME");
                aboutMeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
                aboutMeButton.setForeground(ACCENT_BLUE); // Changed accent color
                aboutMeButton.setBackground(CARD_BACKGROUND); // Modern: White background
                aboutMeButton.setFocusPainted(false);
                aboutMeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                aboutMeButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1)); // Modern: Light grey border
                aboutMeButton.setPreferredSize(new Dimension(120, 40)); // Fixed size for consistency
                aboutMeButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button

                // Custom UI for rounded corners for the ABOUT ME button
                aboutMeButton.setUI(new BasicButtonUI() {
                    @Override
                    public void paint(Graphics g, JComponent c) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        JButton btn = (JButton) c;
                        int width = btn.getWidth();
                        int height = btn.getHeight();
                        int arc = 10; // Smaller rounded corner radius for button

                        g2.setColor(btn.getBackground());
                        g2.fillRoundRect(0, 0, width, height, arc, arc);

                        // Draw border
                        g2.setColor(BORDER_COLOR); // Modern: Light grey border
                        g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

                        // Paint the text
                        super.paint(g2, c);
                        g2.dispose();
                    }
                });

                // Add hover effect for the ABOUT ME button
                aboutMeButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        aboutMeButton.setBackground(Color.decode("#F0F0F0")); // Modern: Very subtle hover
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) { // Corrected method name: mouseExited
                        aboutMeButton.setBackground(CARD_BACKGROUND); // Modern: Back to white
                    }
                });


                aboutMeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PetDetailsDialog petDetailsDialog = new PetDetailsDialog(ownerFrame, pet);
                        petDetailsDialog.setVisible(true);
                    }
                });

                // Add components to the petCard
                petCard.add(petImageLabel);
                petCard.add(petNameLabel);
                petCard.add(aboutMeButton);
                
                petsGridPanel.add(petCard); // Add to GridLayout
                
                System.out.println("AllPetsPanel: Added pet card for: " + pet.getName()); // Confirm card addition

            } else {
                System.out.println("AllPetsPanel: Pet " + pet.getName() + " did not match filters (Type=" + currentPetTypeFilter + ", Gender=" + currentGenderFilter + ").");
            }
        }
        
        // Handle "No pets found" message for GridLayout
        if (!petsFound) {
            // Remove any previous "No pets found" label before adding a new one
            petsGridPanel.removeAll(); 
            JLabel noPetsLabel = new JLabel("No pets found matching your criteria.");
            noPetsLabel.setFont(new Font("SansSerif", Font.ITALIC, 20));
            noPetsLabel.setForeground(TEXT_LIGHT_GREY);
            noPetsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            // To center it in GridLayout, we might need a wrapper or adjust the layout of petsGridPanel further
            // For now, adding it directly and relying on outer panel's layout.
            // If petsGridPanel has 3 columns, it might still appear left-aligned.
            // A dedicated panel for this message could be better.
            JPanel messagePanel = new JPanel(new GridBagLayout());
            messagePanel.setBackground(BACKGROUND_COLOR);
            GridBagConstraints msgGbc = new GridBagConstraints();
            msgGbc.gridx = 0;
            msgGbc.gridy = 0;
            msgGbc.gridwidth = 3; // Span all 3 columns
            msgGbc.weightx = 1.0;
            msgGbc.weighty = 1.0; // Take all available vertical space
            msgGbc.anchor = GridBagConstraints.CENTER;
            messagePanel.add(noPetsLabel, msgGbc);
            
            petsGridPanel.setLayout(new BorderLayout()); // Temporarily switch layout to center message
            petsGridPanel.add(messagePanel, BorderLayout.CENTER); // Add message in the center
            System.out.println("AllPetsPanel: No pets to display based on filters.");
        } else {
            // Restore GridLayout if pets are found and if it was temporarily changed
            if (!(petsGridPanel.getLayout() instanceof GridLayout)) {
                 petsGridPanel.setLayout(new GridLayout(0, 3, 25, 25)); // Revert to 3 columns
            }
        }


        petsGridPanel.revalidate(); // Re-layout the panel
        petsGridPanel.repaint(); // Repaint the panel
    }

	public void savePetsToFile() {
		// TODO Auto-generated method stub
		
	}

	public void update(Pet currentEditingPet) {
		// TODO Auto-generated method stub
		
	}
}
