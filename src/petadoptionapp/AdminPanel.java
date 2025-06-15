package petadoptionapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.plaf.basic.BasicButtonUI;

// Inheritance - Extends JPanel to create custom panel
public class AdminPanel extends JPanel {
    private AllPetsPanel allPetsPanel;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField monthsField;
    private JTextField imagePathField;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> typeComboBox;
    private JTextField colorField;
    private JTextField breedField;
    private JTextField healthStatusField;
    private JComboBox<String> spayedNeuteredComboBox;
    private JTextField vaccinationsDewormField;
    private JTextArea descriptionArea;
    private JLabel managePetTitle;
    private JButton saveOrUpdateButton;
    private JButton clearFormButton;
    private Pet currentEditingPet;
    private DefaultListModel<String> petListModel;
    private JList<String> petJList;
    private JButton removePetButton;
    private JButton editPetButton;

    // Encapsulation - Private fields with public methods to access them
    private static final Color BACKGROUND_LIGHT_GREY = Color.decode("#F2F4F8");
    private static final Color PANEL_WHITE = Color.WHITE;
    private static final Color BORDER_GREY = Color.decode("#E0E0E0");
    private static final Color TEXT_DARK_GREY = Color.decode("#333333");
    private static final Color PRIMARY_BLUE = Color.decode("#2B4576");
    private static final Color PRIMARY_BLUE_HOVER = Color.decode("#4A699A");
    private static final Color DELETE_RED = Color.decode("#D9534F");
    private static final Color DELETE_RED_HOVER = Color.decode("#C9302C");
    private static final Color CLEAR_BUTTON_COLOR = Color.decode("#6C757D");
    private static final Color CLEAR_BUTTON_HOVER = Color.decode("#5A6268");

    public AdminPanel(AllPetsPanel allPetsPanel) {
        this.allPetsPanel = allPetsPanel;
        setBackground(BACKGROUND_LIGHT_GREY);
        setLayout(new BorderLayout(30, 30));
        setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel adminTitle = new JLabel("Admin Dashboard");
        adminTitle.setFont(new Font("SansSerif", Font.BOLD, 45));
        adminTitle.setForeground(PRIMARY_BLUE);
        adminTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(adminTitle, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout()); 
        contentPanel.setBackground(BACKGROUND_LIGHT_GREY);

        GridBagConstraints contentGbc = new GridBagConstraints();
        contentGbc.fill = GridBagConstraints.BOTH;
        contentGbc.weightx = 0.5;
        contentGbc.weighty = 1.0;

        // Abstraction - Hiding complex panel creation details
        JPanel managePetWrapperPanel = createManagePetPanel();
        contentGbc.gridx = 0;
        contentGbc.gridy = 0;
        contentGbc.insets = new Insets(0, 0, 0, 20);
        contentPanel.add(managePetWrapperPanel, contentGbc);

        JPanel removePetWrapperPanel = createRemovePetPanel();
        contentGbc.gridx = 1;
        contentGbc.gridy = 0;
        contentGbc.insets = new Insets(0, 20, 0, 0);
        contentPanel.add(removePetWrapperPanel, contentGbc);

        add(contentPanel, BorderLayout.CENTER);

        clearManagePetForm(); 
        updatePetListDisplay(); 
    }

    private JPanel createManagePetPanel() {
        JPanel managePetWrapperPanel = new JPanel(new GridBagLayout());
        managePetWrapperPanel.setBackground(BACKGROUND_LIGHT_GREY);

        JPanel managePetPanel = new JPanel();
        managePetPanel.setLayout(new GridBagLayout());
        managePetPanel.setBackground(PANEL_WHITE);
        managePetPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GREY, 1),
                new EmptyBorder(30, 40, 30, 40)
        ));
        managePetPanel.putClientProperty("JComponent.roundRectangle", true);
        managePetPanel.setMinimumSize(new Dimension(500, 750));

        managePetTitle = new JLabel("Add New Pet");
        managePetTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        managePetTitle.setForeground(TEXT_DARK_GREY);
        
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;
        titleGbc.gridwidth = 2;
        titleGbc.anchor = GridBagConstraints.CENTER;
        titleGbc.insets = new Insets(25, 0, 25, 0);
        managePetPanel.add(managePetTitle, titleGbc);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 1;
        nameField = new JTextField(20);
        gbc.gridy = row; gbc.gridx = 0; 
        managePetPanel.add(createLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; 
        managePetPanel.add(styleTextField(nameField), gbc);
        row++; gbc.gridx = 0; gbc.weightx = 0; 

        ageField = new JTextField(20);
        gbc.gridy = row; gbc.gridx = 0;
        managePetPanel.add(createLabel("Age (Years):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        managePetPanel.add(styleTextField(ageField), gbc);
        row++; gbc.gridx = 0; gbc.weightx = 0;

        monthsField = new JTextField(20);
        gbc.gridy = row; gbc.gridx = 0;
        managePetPanel.add(createLabel("Age (Months):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        managePetPanel.add(styleTextField(monthsField), gbc);
        row++; gbc.gridx = 0; gbc.weightx = 0;

        imagePathField = new JTextField(20);
        gbc.gridy = row; gbc.gridx = 0;
        managePetPanel.add(createLabel("Image Path:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        managePetPanel.add(styleTextField(imagePathField), gbc);
        row++; gbc.gridx = 0; gbc.weightx = 0;

        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        gbc.gridy = row; gbc.gridx = 0;
        managePetPanel.add(createLabel("Gender:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        managePetPanel.add(styleComboBox(genderComboBox), gbc);
        row++; gbc.gridx = 0; gbc.weightx = 0;

        typeComboBox = new JComboBox<>(new String[]{"Cat", "Dog"});
        gbc.gridy = row; gbc.gridx = 0;
        managePetPanel.add(createLabel("Type:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        managePetPanel.add(styleComboBox(typeComboBox), gbc);
        row++; gbc.gridx = 0; gbc.weightx = 0;

        colorField = new JTextField(8); 
        breedField = new JTextField(8); 
        
        JPanel colorBreedSubPanel = new JPanel(new GridBagLayout());
        colorBreedSubPanel.setOpaque(false);
        GridBagConstraints subGbc = new GridBagConstraints();
        subGbc.insets = new Insets(0, 0, 0, 0); 
        subGbc.anchor = GridBagConstraints.WEST;
        subGbc.fill = GridBagConstraints.HORIZONTAL;

        subGbc.gridx = 0; subGbc.gridy = 0; subGbc.weightx = 0; subGbc.fill = GridBagConstraints.NONE; subGbc.insets = new Insets(0, 0, 0, 10); 
        colorBreedSubPanel.add(createLabel("Color:"), subGbc);
        subGbc.gridx = 1; subGbc.gridy = 0; subGbc.weightx = 0.5; subGbc.fill = GridBagConstraints.HORIZONTAL; subGbc.insets = new Insets(0, 0, 0, 30); 
        colorBreedSubPanel.add(styleTextField(colorField), subGbc);
        subGbc.gridx = 2; subGbc.gridy = 0; subGbc.weightx = 0; subGbc.fill = GridBagConstraints.NONE; subGbc.insets = new Insets(0, 0, 0, 10); 
        colorBreedSubPanel.add(createLabel("Breed:"), subGbc);
        subGbc.gridx = 3; subGbc.gridy = 0; subGbc.weightx = 0.5; subGbc.fill = GridBagConstraints.HORIZONTAL; subGbc.insets = new Insets(0, 0, 0, 0); 
        colorBreedSubPanel.add(styleTextField(breedField), subGbc);

        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; 
        managePetPanel.add(colorBreedSubPanel, gbc);
        gbc.gridwidth = 1; gbc.weightx = 0; 

        healthStatusField = new JTextField(20);
        gbc.gridy = row; gbc.gridx = 0;
        managePetPanel.add(createLabel("Health Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        managePetPanel.add(styleTextField(healthStatusField), gbc);
        row++; gbc.gridx = 0; gbc.weightx = 0;
        
        spayedNeuteredComboBox = new JComboBox<>(new String[]{"Not yet", "Spayed", "Neutered"});
        gbc.gridy = row; gbc.gridx = 0;
        managePetPanel.add(createLabel("Spayed/Neutered:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        managePetPanel.add(styleComboBox(spayedNeuteredComboBox), gbc);
        row++; gbc.gridx = 0; gbc.weightx = 0;

        vaccinationsDewormField = new JTextField(20);
        gbc.gridy = row; gbc.gridx = 0;
        managePetPanel.add(createLabel("Vaccinations & Deworm:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        managePetPanel.add(styleTextField(vaccinationsDewormField), gbc);
        row++; gbc.gridx = 0; gbc.weightx = 0;
        
        descriptionArea = new JTextArea(3, 20); 
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        styleTextArea(descriptionArea, descriptionScrollPane);

        gbc.gridy = row; gbc.gridx = 0;
        managePetPanel.add(createLabel("Additional Description:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weighty = 1.0;
        managePetPanel.add(descriptionScrollPane, gbc);
        row++; gbc.gridx = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; 

        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); 
        actionButtonsPanel.setOpaque(false);
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 0, 30, 0);
        gbc.weighty = 0;
        managePetPanel.add(actionButtonsPanel, gbc);

        saveOrUpdateButton = new JButton("Add Pet"); 
        styleButton(saveOrUpdateButton, PRIMARY_BLUE, PRIMARY_BLUE_HOVER);
        saveOrUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrUpdatePet();
            }
        });
        actionButtonsPanel.add(saveOrUpdateButton);

        clearFormButton = new JButton("Clear Form");
        styleButton(clearFormButton, CLEAR_BUTTON_COLOR, CLEAR_BUTTON_HOVER);
        clearFormButton.addActionListener(e -> clearManagePetForm());
        actionButtonsPanel.add(clearFormButton);

        GridBagConstraints wrapperGbc = new GridBagConstraints();
        wrapperGbc.anchor = GridBagConstraints.CENTER; 
        wrapperGbc.weightx = 1.0; 
        wrapperGbc.weighty = 1.0; 
        wrapperGbc.fill = GridBagConstraints.BOTH;
        managePetWrapperPanel.add(managePetPanel, wrapperGbc);
        
        return managePetWrapperPanel;
    }

    private JPanel createRemovePetPanel() {
        JPanel removePetWrapperPanel = new JPanel(new GridBagLayout());
        removePetWrapperPanel.setBackground(BACKGROUND_LIGHT_GREY);

        JPanel removePetPanel = new JPanel();
        removePetPanel.setLayout(new BoxLayout(removePetPanel, BoxLayout.Y_AXIS));
        removePetPanel.setBackground(PANEL_WHITE);
        removePetPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GREY, 1),
                new EmptyBorder(30, 40, 30, 40)
        ));
        removePetPanel.putClientProperty("JComponent.roundRectangle", true);
        removePetPanel.setMinimumSize(new Dimension(500, 750));

        JLabel removePetTitle = new JLabel("Manage Existing Pets");
        removePetTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        removePetTitle.setForeground(TEXT_DARK_GREY);
        removePetTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        removePetPanel.add(removePetTitle);
        removePetPanel.add(Box.createVerticalStrut(35));

        petListModel = new DefaultListModel<>();
        petJList = new JList<>(petListModel);
        petJList.setFont(new Font("SansSerif", Font.PLAIN, 15));
        petJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        petJList.setFixedCellHeight(26);
        petJList.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        // Polymorphism - Different rendering for selected vs unselected items
        petJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(5, 10, 5, 10));
                if (isSelected) {
                    label.setBackground(PRIMARY_BLUE_HOVER);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(PANEL_WHITE);
                    label.setForeground(TEXT_DARK_GREY);
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(petJList);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_GREY, 1));
        scrollPane.getViewport().setBackground(PANEL_WHITE);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        removePetPanel.add(scrollPane);

        removePetPanel.add(Box.createVerticalStrut(35));

        JPanel removeEditButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); 
        removeEditButtonsPanel.setOpaque(false);

        removePetButton = new JButton("Remove Selected Pet");
        styleButton(removePetButton, DELETE_RED, DELETE_RED_HOVER);
        removePetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedPet();
            }
        });
        removeEditButtonsPanel.add(removePetButton);

        editPetButton = new JButton("Edit Selected Pet");
        styleButton(editPetButton, PRIMARY_BLUE, PRIMARY_BLUE_HOVER);
        editPetButton.addActionListener(e -> editSelectedPet());
        removeEditButtonsPanel.add(editPetButton);
        
        removePetPanel.add(removeEditButtonsPanel);
        
        GridBagConstraints wrapperGbcRemove = new GridBagConstraints();
        wrapperGbcRemove.anchor = GridBagConstraints.CENTER; 
        wrapperGbcRemove.weightx = 1.0; 
        wrapperGbcRemove.weighty = 1.0; 
        wrapperGbcRemove.fill = GridBagConstraints.BOTH;
        removePetWrapperPanel.add(removePetPanel, wrapperGbcRemove);

        return removePetWrapperPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        label.setForeground(TEXT_DARK_GREY);
        return label;
    }

    private JTextField styleTextField(JTextField textField) {
        textField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GREY, 1),
                new EmptyBorder(7, 12, 7, 12) 
        ));
        textField.putClientProperty("JComponent.roundRectangle", true);
        return textField;
    }

    private JTextArea styleTextArea(JTextArea textArea, JScrollPane scrollPane) {
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        textArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GREY, 1),
                new EmptyBorder(7, 12, 7, 12) 
        ));
        textArea.putClientProperty("JComponent.roundRectangle", true);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        scrollPane.getViewport().setBackground(PANEL_WHITE); 
        return textArea;
    }

    private JComboBox<String> styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 15));
        comboBox.setBackground(PANEL_WHITE);
        comboBox.setForeground(TEXT_DARK_GREY);
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER_GREY, 1));
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setBorder(new EmptyBorder(7, 12, 7, 12)); 
                if (isSelected) {
                    renderer.setBackground(PRIMARY_BLUE_HOVER);
                    renderer.setForeground(Color.WHITE);
                } else {
                    renderer.setBackground(PANEL_WHITE);
                    renderer.setForeground(TEXT_DARK_GREY);
                }
                return renderer;
            }
        });
        return comboBox;
    }

    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setFont(new Font("SansSerif", Font.BOLD, 17));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 30, 12, 30));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                JButton btn = (JButton) c;
                int width = btn.getWidth();
                int height = btn.getHeight();
                int arc = 15; 

                if (btn.getModel().isArmed()) {
                    g2.setColor(bgColor.darker());
                } else if (btn.getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(bgColor);
                }
                g2.fillRoundRect(0, 0, width, height, arc, arc);

                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

                super.paint(g2, c);
                g2.dispose();
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });
    }

    private void saveOrUpdatePet() {
        try {
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            int months = Integer.parseInt(monthsField.getText().trim());
            String imagePath = imagePathField.getText().trim();
            String gender = (String) genderComboBox.getSelectedItem();
            String type = (String) typeComboBox.getSelectedItem();

            String color = colorField.getText().trim();
            String breed = breedField.getText().trim();
            String healthStatus = healthStatusField.getText().trim();
            String spayedNeutered = (String) spayedNeuteredComboBox.getSelectedItem();
            String vaccinationsDeworm = vaccinationsDewormField.getText().trim();
            String additionalDescription = descriptionArea.getText().trim(); 

            if (name.isEmpty() || imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Name and Image Path.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (age < 0 || months < 0) {
                 JOptionPane.showMessageDialog(this, "Age and Months cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder fullDescription = new StringBuilder();
            if (!color.isEmpty()) fullDescription.append("Color: ").append(color).append("\n");
            if (!breed.isEmpty()) fullDescription.append("Breed: ").append(breed).append("\n");
            if (!healthStatus.isEmpty()) fullDescription.append("Health Status: ").append(healthStatus).append("\n");
            if (spayedNeutered != null && !spayedNeutered.isEmpty()) fullDescription.append("Spayed/Neutered: ").append(spayedNeutered).append("\n");
            if (!vaccinationsDeworm.isEmpty()) fullDescription.append("Vaccinations & Deworm: ").append(vaccinationsDeworm).append("\n");
            if (!additionalDescription.isEmpty()) fullDescription.append("Description: ").append(additionalDescription).append("\n");
            
            String finalDescription = fullDescription.toString().trim();

            if (currentEditingPet == null) {
                // Polymorphism - Creating different pet types through common interface
                Pet newPet;
                if (Objects.equals(type, "Cat")) {
                    newPet = new Cat(name, age, months, finalDescription, imagePath, gender);
                } else {
                    newPet = new Dog(name, age, months, finalDescription, imagePath, gender);
                }
                allPetsPanel.addPet(newPet);
                JOptionPane.showMessageDialog(this, "Pet added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                currentEditingPet.setName(name);
                currentEditingPet.setAge(age);
                currentEditingPet.setMonths(months);
                currentEditingPet.setImagePath(imagePath);
                currentEditingPet.setGender(gender);
                currentEditingPet.setDescription(finalDescription);

                PetDataManager.savePets(allPetsPanel.getFullPetList());
                JOptionPane.showMessageDialog(this, "Pet updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

            clearManagePetForm(); 
            updatePetListDisplay(); 

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Age (Years) and Age (Months).", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving/updating pet: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void removeSelectedPet() {
        int selectedIndex = petJList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedPetString = petListModel.getElementAt(selectedIndex);
            String petName = selectedPetString.substring(0, selectedPetString.lastIndexOf(" ("));

            Pet petToRemove = null;
            for (Pet pet : allPetsPanel.getFullPetList()) {
                if (pet.getName().equals(petName)) {
                    petToRemove = pet;
                    break;
                }
            }

            if (petToRemove != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to remove " + petToRemove.getName() + "?",
                        "Confirm Removal",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    allPetsPanel.removePet(petToRemove);
                    updatePetListDisplay();
                    JOptionPane.showMessageDialog(this, "Pet removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearManagePetForm(); 
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selected pet not found in the list.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a pet to remove.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editSelectedPet() {
        int selectedIndex = petJList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedPetString = petListModel.getElementAt(selectedIndex);
            String petName = selectedPetString.substring(0, selectedPetString.lastIndexOf(" ("));

            for (Pet pet : allPetsPanel.getFullPetList()) {
                if (pet.getName().equals(petName)) {
                    currentEditingPet = pet;
                    populateManagePetForm(pet);
                    managePetTitle.setText("Edit Pet Details");
                    saveOrUpdateButton.setText("Update Pet");
                    typeComboBox.setEnabled(false); 
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a pet to edit.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void populateManagePetForm(Pet pet) {
        nameField.setText(pet.getName());
        ageField.setText(String.valueOf(pet.getAge()));
        monthsField.setText(String.valueOf(pet.getMonths()));
        imagePathField.setText(pet.getImagePath());
        genderComboBox.setSelectedItem(pet.getGender());
        typeComboBox.setSelectedItem(pet instanceof Cat ? "Cat" : "Dog");

        String description = pet.getDescription();
        Map<String, String> parsedDetails = parseDescription(description);

        colorField.setText(parsedDetails.getOrDefault("Color", ""));
        breedField.setText(parsedDetails.getOrDefault("Breed", ""));
        healthStatusField.setText(parsedDetails.getOrDefault("Health Status", ""));
        spayedNeuteredComboBox.setSelectedItem(parsedDetails.getOrDefault("Spayed/Neutered", "Not yet"));
        vaccinationsDewormField.setText(parsedDetails.getOrDefault("Vaccinations & Deworm", ""));
        descriptionArea.setText(parsedDetails.getOrDefault("Description", "")); 
    }

    private Map<String, String> parseDescription(String description) {
        Map<String, String> details = new HashMap<>();
        String[] lines = description.split("\n");
        StringBuilder remainingDescription = new StringBuilder();

        for (String line : lines) {
            if (line.startsWith("Color:")) {
                details.put("Color", line.substring("Color:".length()).trim());
            } else if (line.startsWith("Breed:")) {
                details.put("Breed", line.substring("Breed:".length()).trim());
            } else if (line.startsWith("Health Status:")) {
                details.put("Health Status", line.substring("Health Status:".length()).trim());
            } else if (line.startsWith("Spayed/Neutered:")) {
                details.put("Spayed/Neutered", line.substring("Spayed/Neutered:".length()).trim());
            } else if (line.startsWith("Vaccinations & Deworm:")) {
                details.put("Vaccinations & Deworm", line.substring("Vaccinations & Deworm:".length()).trim());
            } else if (line.startsWith("Description:")) {
                details.put("Description", line.substring("Description:".length()).trim());
            } else {
                if (remainingDescription.length() > 0) remainingDescription.append("\n");
                remainingDescription.append(line);
            }
        }
        if (details.get("Description") == null && remainingDescription.length() > 0) {
             details.put("Description", remainingDescription.toString().trim());
        } else if (details.get("Description") != null && remainingDescription.length() > 0) {
            details.put("Description", details.get("Description") + "\n" + remainingDescription.toString().trim());
        }
        return details;
    }

    private void clearManagePetForm() {
        currentEditingPet = null;
        managePetTitle.setText("Add New Pet");
        saveOrUpdateButton.setText("Add Pet");
        nameField.setText("");
        ageField.setText("");
        monthsField.setText("");
        imagePathField.setText("");
        genderComboBox.setSelectedIndex(0);
        typeComboBox.setSelectedIndex(0);
        typeComboBox.setEnabled(true); 

        colorField.setText("");
        breedField.setText("");
        healthStatusField.setText("");
        spayedNeuteredComboBox.setSelectedIndex(0);
        vaccinationsDewormField.setText("");
        descriptionArea.setText("");
    }

    public void updatePetListDisplay() {
        petListModel.clear();
        for (Pet pet : allPetsPanel.getFullPetList()) {
            String type = (pet instanceof Cat) ? "Cat" : "Dog";
            petListModel.addElement(pet.getName() + " (" + type + ")");
        }
    }
}