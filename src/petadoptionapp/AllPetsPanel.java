// AllPetsPanel.java
package petadoptionapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.geom.RoundRectangle2D;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class AllPetsPanel extends JPanel {
    // Encapsulation - Private fields with public getters/setters
    private JFrame ownerFrame;
    private JPanel petsGridPanel;
    private ArrayList<Pet> fullPetList;
    private String currentPetTypeFilter = "All";
    private String currentGenderFilter = "All";

    private static final Color BACKGROUND_COLOR = Color.decode("#F2F4F8");
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = Color.decode("#E0E0E0");
    private static final Color TEXT_COLOR = Color.decode("#333333");
    private static final Color ACCENT_BLUE = Color.decode("#2B4576");
    private static final Color ACCENT_BLUE_HOVER = Color.decode("#4A699A");
    private static final Color TEXT_LIGHT_GREY = Color.decode("#888888");

    public AllPetsPanel(JFrame ownerFrame) {
        this.ownerFrame = ownerFrame;
        try {
            // Abstraction - Hiding complex data loading implementation
            this.fullPetList = PetDataManager.loadPets();
        } catch (Exception e) {
            this.fullPetList = new ArrayList<>();
            JOptionPane.showMessageDialog(ownerFrame,
                "Failed to load pet data.",
                "Data Loading Error",
                JOptionPane.ERROR_MESSAGE);
        }

        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        JPanel topContainerPanel = new JPanel();
        topContainerPanel.setLayout(new BoxLayout(topContainerPanel, BoxLayout.Y_AXIS));
        topContainerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Our Adoptable Fur Babies");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(ACCENT_BLUE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(20, 50, 20, 50));
        topContainerPanel.add(titleLabel);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        filterPanel.setBackground(BACKGROUND_COLOR);
        filterPanel.setBorder(new EmptyBorder(0, 10, 15, 10));
        filterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        typeLabel.setForeground(TEXT_COLOR);
        filterPanel.add(typeLabel);

        String[] petTypes = {"All", "Cat", "Dog"};
        JComboBox<String> typeDropdown = new JComboBox<>(petTypes);
        styleDropdown(typeDropdown);
        typeDropdown.setSelectedItem(currentPetTypeFilter);
        typeDropdown.addActionListener(e -> {
            currentPetTypeFilter = (String) typeDropdown.getSelectedItem();
            updatePetsDisplay();
        });
        filterPanel.add(typeDropdown);

        filterPanel.add(Box.createRigidArea(new Dimension(40, 0)));

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        genderLabel.setForeground(TEXT_COLOR);
        filterPanel.add(genderLabel);

        String[] genders = {"All", "Male", "Female"};
        JComboBox<String> genderDropdown = new JComboBox<>(genders);
        styleDropdown(genderDropdown);
        genderDropdown.setSelectedItem(currentGenderFilter);
        genderDropdown.addActionListener(e -> {
            currentGenderFilter = (String) genderDropdown.getSelectedItem();
            updatePetsDisplay();
        });
        filterPanel.add(genderDropdown);

        topContainerPanel.add(filterPanel);
        add(topContainerPanel, BorderLayout.NORTH);

        petsGridPanel = new JPanel(new GridLayout(0, 3, 25, 25));
        petsGridPanel.setBackground(BACKGROUND_COLOR);
        petsGridPanel.setBorder(new EmptyBorder(30, 150, 30, 150));

        JScrollPane scrollPane = new JScrollPane(petsGridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        add(scrollPane, BorderLayout.CENTER);

        updatePetsDisplay();
    }

    // Encapsulation - Public methods to access/modify private data
    public void addPet(Pet pet) {
        fullPetList.add(pet);
        PetDataManager.savePets(fullPetList);
        updatePetsDisplay();
    }

    public void removePet(Pet pet) {
        fullPetList.remove(pet);
        PetDataManager.savePets(fullPetList);
        updatePetsDisplay();
    }

    public ArrayList<Pet> getFullPetList() {
        return fullPetList;
    }

    private void styleDropdown(JComboBox<String> dropdown) {
        dropdown.setFont(new Font("SansSerif", Font.PLAIN, 15));
        dropdown.setBackground(Color.WHITE);
        dropdown.setForeground(Color.decode("#333333"));
        dropdown.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dropdown.setBorder(BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1));
        dropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(7, 7, 7, 7));
                if (isSelected) {
                    label.setBackground(ACCENT_BLUE_HOVER);
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
        petsGridPanel.removeAll();
        boolean petsFound = false;

        for (Pet pet : fullPetList) {
        	// Polymorphism - Using parent Pet class to handle Cat/Dog objects
            boolean matchesType = currentPetTypeFilter.equals("All") ||
                                (currentPetTypeFilter.equals("Cat") && pet instanceof Cat) ||
                                (currentPetTypeFilter.equals("Dog") && pet instanceof Dog);

            boolean matchesGender = currentGenderFilter.equals("All") ||
                                  currentGenderFilter.equals(pet.getGender());

            if (matchesType && matchesGender) {
                petsFound = true;
                JPanel petCard = new JPanel() {
                    private float scale = 1.0f;
                    private Timer scaleTimer;
                    private final int ANIMATION_STEPS = 5;
                    private final int ANIMATION_DELAY = 20;

                    {
                        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                        setOpaque(false);
                        setBorder(new EmptyBorder(15, 15, 15, 15));
                        setPreferredSize(new Dimension(250, 350));
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

                JLabel petImageLabel = new JLabel();
                petImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                try {
                    URL imageUrl = getClass().getResource(pet.getImagePath());
                    if (imageUrl != null) {
                        ImageIcon originalIcon = new ImageIcon(imageUrl);
                        Image scaledImage = originalIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                        petImageLabel.setIcon(new ImageIcon(scaledImage));
                    } else {
                        petImageLabel.setText("Image N/A");
                        petImageLabel.setPreferredSize(new Dimension(250, 250));
                        petImageLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
                        petImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        petImageLabel.setVerticalAlignment(SwingConstants.CENTER);
                    }
                } catch (Exception e) {
                    petImageLabel.setText("Image Error");
                    petImageLabel.setPreferredSize(new Dimension(250, 250));
                    petImageLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
                    petImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    petImageLabel.setVerticalAlignment(SwingConstants.CENTER);
                }

                petImageLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

                JLabel petNameLabel = new JLabel(pet.getName());
                petNameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
                petNameLabel.setForeground(TEXT_COLOR);
                petNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                petNameLabel.setBorder(new EmptyBorder(5, 0, 10, 0));

                JButton aboutMeButton = new JButton("ABOUT ME");
                aboutMeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
                aboutMeButton.setForeground(ACCENT_BLUE);
                aboutMeButton.setBackground(CARD_BACKGROUND);
                aboutMeButton.setFocusPainted(false);
                aboutMeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                aboutMeButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
                aboutMeButton.setPreferredSize(new Dimension(120, 40));
                aboutMeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                aboutMeButton.setUI(new BasicButtonUI() {
                    @Override
                    public void paint(Graphics g, JComponent c) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        JButton btn = (JButton) c;
                        int width = btn.getWidth();
                        int height = btn.getHeight();
                        int arc = 10;

                        g2.setColor(btn.getBackground());
                        g2.fillRoundRect(0, 0, width, height, arc, arc);

                        g2.setColor(BORDER_COLOR);
                        g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

                        super.paint(g2, c);
                        g2.dispose();
                    }
                });

                aboutMeButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        aboutMeButton.setBackground(Color.decode("#F0F0F0"));
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        aboutMeButton.setBackground(CARD_BACKGROUND);
                    }
                });

                aboutMeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PetDetailsDialog petDetailsDialog = new PetDetailsDialog(ownerFrame, pet);
                        petDetailsDialog.setVisible(true);
                    }
                });

                petCard.add(petImageLabel);
                petCard.add(petNameLabel);
                petCard.add(aboutMeButton);
                
                petsGridPanel.add(petCard);
            }
        }
        
        if (!petsFound) {
            petsGridPanel.removeAll(); 
            JLabel noPetsLabel = new JLabel("No pets found matching your criteria.");
            noPetsLabel.setFont(new Font("SansSerif", Font.ITALIC, 20));
            noPetsLabel.setForeground(TEXT_LIGHT_GREY);
            noPetsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            JPanel messagePanel = new JPanel(new GridBagLayout());
            messagePanel.setBackground(BACKGROUND_COLOR);
            GridBagConstraints msgGbc = new GridBagConstraints();
            msgGbc.gridx = 0;
            msgGbc.gridy = 0;
            msgGbc.gridwidth = 3;
            msgGbc.weightx = 1.0;
            msgGbc.weighty = 1.0;
            msgGbc.anchor = GridBagConstraints.CENTER;
            messagePanel.add(noPetsLabel, msgGbc);
            
            petsGridPanel.setLayout(new BorderLayout());
            petsGridPanel.add(messagePanel, BorderLayout.CENTER);
        } else {
            if (!(petsGridPanel.getLayout() instanceof GridLayout)) {
                 petsGridPanel.setLayout(new GridLayout(0, 3, 25, 25));
            }
        }

        petsGridPanel.revalidate();
        petsGridPanel.repaint();
    }

    public void savePetsToFile() {
    }

    public void update(Pet currentEditingPet) {
    }
}