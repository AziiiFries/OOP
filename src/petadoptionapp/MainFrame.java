// MainFrame.java - UPDATED
package petadoptionapp;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class MainFrame extends JFrame {
    // Encapsulation - Private fields with public access methods
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private AllPetsPanel allPetsPanel;
    private AdminPanel adminPanel;

    public static final String HOME_PANEL = "Home";
    public static final String ALL_PETS_PANEL = "Adopt";
    public static final String ADMIN_PANEL = "Admin";
    public static final String ABOUT_PANEL = "About";

    public MainFrame() {
        setTitle("FurGivers Paws of Hope");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 768));
        setPreferredSize(new Dimension(1200, 900));
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        getContentPane().setBackground(Color.decode("#F2F4F8"));
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1));

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(Color.WHITE);
        topWrapper.setBorder(new EmptyBorder(20, 60, 20, 60));

        JLabel logoLabel;
        try {
            URL imageUrl = getClass().getResource("/resources/1.png");
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image scaledLogo = originalIcon.getImage().getScaledInstance(300, 80, Image.SCALE_SMOOTH);
                logoLabel = new JLabel(new ImageIcon(scaledLogo));
            } else {
                logoLabel = new JLabel("FurGivers Paws of Hope");
                logoLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
                logoLabel.setForeground(Color.decode("#2B4576"));
            }
        } catch (Exception e) {
            logoLabel = new JLabel("FurGivers Paws of Hope");
            logoLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
            logoLabel.setForeground(Color.decode("#2B4576"));
        }
        logoLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        topWrapper.add(logoLabel, BorderLayout.WEST);

        JPanel navButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        navButtonsPanel.setOpaque(false);
        navButtonsPanel.setBorder(new EmptyBorder(25, 0, 25, 0));

        MenuButton homeButton = new MenuButton("Home");
        MenuButton aboutButton = new MenuButton("About");
        MenuButton donateButton = new MenuButton("Donate");
        MenuButton findAPetButton = new MenuButton("Find a Pet");
        MenuButton contactButton = new MenuButton("Contact");

        navButtonsPanel.add(homeButton);
        navButtonsPanel.add(aboutButton);
        navButtonsPanel.add(donateButton);
        navButtonsPanel.add(findAPetButton);
        navButtonsPanel.add(contactButton);

        topWrapper.add(navButtonsPanel, BorderLayout.EAST);
        add(topWrapper, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.decode("#F2F4F8"));
        cardPanel.setBorder(new EmptyBorder(30, 80, 30, 80));

        JPanel homePanel = createHomePanel(cardLayout, cardPanel);
        allPetsPanel = new AllPetsPanel(this);
        adminPanel = new AdminPanel(allPetsPanel);
        JPanel aboutUsPanel = createAboutUsPanel();

        cardPanel.add(homePanel, HOME_PANEL);
        cardPanel.add(allPetsPanel, ALL_PETS_PANEL);
        cardPanel.add(aboutUsPanel, ABOUT_PANEL);
        cardPanel.add(adminPanel, ADMIN_PANEL);

        add(cardPanel, BorderLayout.CENTER);

        homeButton.addActionListener(e -> cardLayout.show(cardPanel, HOME_PANEL));
        findAPetButton.addActionListener(e -> {
            cardLayout.show(cardPanel, ALL_PETS_PANEL);
            allPetsPanel.updatePetsDisplay();
        });
        aboutButton.addActionListener(e -> cardLayout.show(cardPanel, ABOUT_PANEL));
        contactButton.addActionListener(e -> {
            ContactDialog contactDialog = new ContactDialog(
                    MainFrame.this,
                    "\n+63 917 123 4567",
                    "+63 912 345 6789",
                    "\nfurgiversph@gmail.com");
            contactDialog.setVisible(true);
        });

        donateButton.addActionListener(e -> {
            final String DONATE_URL = "https://docs.google.com/forms/d/e/1FAIpQLSda_jQUn0XWPIzr3Eli5bVkoWOW10H_VVsiQDWC-dWoHyiPMQ/viewform";
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(DONATE_URL));
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot open browser. Please visit:\n" + DONATE_URL, "Browser Not Supported", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening link: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        String adminKey = "adminShortcut";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), adminKey);
        actionMap.put(adminKey, new AbstractAction() {
            // Polymorphism - Overriding actionPerformed method
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminLoginDialog loginDialog = new AdminLoginDialog(MainFrame.this);
                loginDialog.setVisible(true);
                if (loginDialog.isLoggedIn()) {
                    cardLayout.show(cardPanel, ADMIN_PANEL);
                    adminPanel.updatePetListDisplay();
                }
            }
        });

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.decode("#F2F4F8"));
        footerPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        JLabel footerLabel = new JLabel("2025 FurGivers Paws of Hope © All Rights Reserved");
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        footerLabel.setForeground(Color.decode("#757575"));
        footerPanel.add(footerLabel);

        add(footerPanel, BorderLayout.SOUTH);
        cardLayout.show(cardPanel, HOME_PANEL);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createHomePanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.decode("#F2F4F8"));
        panel.setBorder(new EmptyBorder(30, 0, 30, 0));

        JLabel mainTitle = new JLabel("FurGivers Paws of Hope");
        mainTitle.setFont(new Font("SansSerif", Font.BOLD, 50));
        mainTitle.setForeground(Color.decode("#333333"));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitle = new JLabel("COMPASSION AND RESPONSIBILITY FOR ANIMALS");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 22));
        subTitle.setForeground(Color.decode("#666666"));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(50));
        panel.add(mainTitle);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subTitle);
        panel.add(Box.createVerticalStrut(40));

        JPanel previewGridPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        previewGridPanel.setOpaque(false);
        previewGridPanel.setBorder(new EmptyBorder(0, 50, 0, 50));

        previewGridPanel.add(createHomePetPreview("/resources/dog_arian.png", "Hi I'm Arian, and I'm a very good boy!"));
        previewGridPanel.add(createHomePetPreview("/resources/cat_ash.png", "Hi I'm Ash, the cuddly cat."));
        previewGridPanel.add(createHomePetPreview("/resources/dog_brisket.png", "Hi I'm Brisket, will you Netflix and chill with me?"));

        panel.add(previewGridPanel);
        panel.add(Box.createVerticalStrut(50));

        JButton adoptButton = new JButton("ADOPT!");
        adoptButton.setFont(new Font("SansSerif", Font.BOLD, 28));
        adoptButton.setForeground(Color.WHITE);
        adoptButton.setBackground(Color.decode("#2B4576"));
        adoptButton.setFocusPainted(false);
        adoptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adoptButton.setBorder(new EmptyBorder(20, 60, 20, 60));
        adoptButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        adoptButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                JButton btn = (JButton) c;
                int width = btn.getWidth();
                int height = btn.getHeight();
                int arc = 25;

                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(5, 5, width - 5, height - 5, arc, arc);

                if (btn.getModel().isArmed()) {
                    g2.setColor(Color.decode("#2B4576").darker());
                } else if (btn.getModel().isRollover()) {
                    g2.setColor(Color.decode("#4A699A"));
                } else {
                    g2.setColor(Color.decode("#2B4576"));
                }
                g2.fillRoundRect(0, 0, width, height, arc, arc);

                super.paint(g2, c);
                g2.dispose();
            }
        });

        adoptButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                adoptButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                adoptButton.repaint();
            }
        });

        adoptButton.addActionListener(e -> {
            cardLayout.show(cardPanel, ALL_PETS_PANEL);
            if (allPetsPanel != null) {
                allPetsPanel.updatePetsDisplay();
            }
        });
        panel.add(adoptButton);

        return panel;
    }

    private JPanel createAboutUsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setBackground(Color.decode("#F2F4F8"));
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel carouselPanel = createImageCarousel();
        mainPanel.add(carouselPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.decode("#F2F4F8"));

        JLabel title = new JLabel("About us");
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(Color.decode("#2B4576"));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 30, 0));

        JTextPane content = new JTextPane();
        content.setContentType("text/html");
        String htmlContent = "<html><body style='width: 100%; text-align: justify; font-family: sans-serif'>" +
                "<div style='max-width: 600px'>" +
                "FurGivers Paws of Hope is a non-profit organization dedicated to the welfare of animals. " +
                "Established in 2023, our mission is to promote compassion and responsibility towards animals " +
                "through education, advocacy, and direct animal rescue and rehabilitation.<br><br>" +
                "We believe that every animal deserves a life free from cruelty, neglect, and abuse. " +
                "Our programs include: <br>" +
                "<b>Rescue and Rehabilitation:</b> Providing immediate care, medical attention, and rehabilitation for rescued animals.<br>" +
                "<b>Adoption Program:</b> Finding loving forever homes for our rescued cats and dogs.<br>" +
                "<b>Spay/Neuter Program:</b> Promoting population control through responsible pet ownership and accessible spay/neuter services.<br>" +
                "<b>Education and Advocacy:</b> Raising awareness about animal welfare issues and advocating for stronger animal protection laws.<br><br>" +
                "We rely heavily on the support of volunteers and donations to continue our vital work. " +
                "Join us in making a difference in the lives of countless animals!" +
                "</div></body></html>";
        content.setText(htmlContent);
        content.setFont(new Font("SansSerif", Font.PLAIN, 18));
        content.setForeground(Color.decode("#333333"));
        content.setBackground(Color.decode("#F2F4F8"));
        content.setEditable(false);
        content.setBorder(new EmptyBorder(0, 0, 0, 0));
        content.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        contentPanel.add(title);
        contentPanel.add(content);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createHomePetPreview(String imagePath, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setPreferredSize(new Dimension(280, 400));
        panel.setMaximumSize(new Dimension(280, 400));
        panel.setMinimumSize(new Dimension(280, 400));

        panel.setOpaque(false);
        panel.addMouseListener(new MouseAdapter() {
            private float scale = 1.0f;
            private Timer scaleTimer;
            private final int ANIMATION_STEPS = 5;
            private final int ANIMATION_DELAY = 20;

            @Override
            public void mouseEntered(MouseEvent evt) {
                startScaleAnimation(1.03f);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                startScaleAnimation(1.0f);
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
                        panel.repaint();
                    }
                });
                scaleTimer.start();
            }
        });

        JLabel imageLabel = new JLabel();
        try {
            URL imgUrl = getClass().getResource(imagePath);
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image scaledImg = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
            } else {
                imageLabel.setText("Image N/A");
                imageLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
                imageLabel.setForeground(Color.GRAY);
            }
        } catch (Exception e) {
            imageLabel.setText("Image Error");
            imageLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            imageLabel.setForeground(Color.GRAY);
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("SansSerif", Font.BOLD, 16));
        descArea.setForeground(Color.decode("#333333"));
        descArea.setBackground(Color.WHITE);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descArea.setMaximumSize(new Dimension(250, 60));
        descArea.setBorder(new EmptyBorder(5, 5, 5, 5));

        panel.add(imageLabel);
        panel.add(descArea);

        return panel;
    }

    private JPanel createImageCarousel() {
        String[] imagePaths = {
            "/resources/dog_billie.png",
            "/resources/cat_kitty.png",
            "/resources/dog_brisket.png",
            "/resources/cat_mimi.png",
            "/resources/dog_arian.png",
            "/resources/cat_ash.png",
            "/resources/dog_frankie.png",
            "/resources/cat_riley.png",
            "/resources/dog_bruno.png",
            "/resources/cat_siopao.png"
        };
        
        ImageIcon[] images = new ImageIcon[imagePaths.length];
        for (int i = 0; i < imagePaths.length; i++) {
            try {
                URL imgUrl = getClass().getResource(imagePaths[i]);
                if (imgUrl != null) {
                    ImageIcon originalIcon = new ImageIcon(imgUrl);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                    images[i] = new ImageIcon(scaledImage);
                } else {
                    images[i] = createPlaceholderIcon(400, 300, "Image " + (i + 1) + " Not Found");
                }
            } catch (Exception e) {
                images[i] = createPlaceholderIcon(400, 300, "Error Loading Image " + (i + 1));
            }
        }

        JPanel carouselPanel = new JPanel(new BorderLayout());
        carouselPanel.setPreferredSize(new Dimension(450, 350));
        carouselPanel.setBackground(Color.decode("#F2F4F8"));
        carouselPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#D1D9E6"), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JLabel imageLabel = new JLabel(images[0], SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton prevButton = createCarouselButton("◀");
        JButton nextButton = createCarouselButton("▶");

        JPanel indicatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        indicatorPanel.setOpaque(false);
        JLabel[] indicators = new JLabel[images.length];
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new JLabel("•");
            indicators[i].setFont(new Font("SansSerif", Font.PLAIN, 24));
            indicators[i].setForeground(i == 0 ? Color.BLUE : Color.GRAY);
            indicatorPanel.add(indicators[i]);
        }

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(prevButton, BorderLayout.WEST);
        buttonPanel.add(nextButton, BorderLayout.EAST);
        buttonPanel.add(indicatorPanel, BorderLayout.CENTER);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        carouselPanel.add(imageLabel, BorderLayout.CENTER);
        carouselPanel.add(buttonPanel, BorderLayout.SOUTH);

        AtomicInteger currentIndex = new AtomicInteger(0);

        // Abstraction - Using Consumer interface to hide implementation details
        Consumer<Integer> updateCarousel = (newIndex) -> {
            imageLabel.setIcon(null);
            Timer timer = new Timer(5, null);
            timer.addActionListener(new ActionListener() {
                float alpha = 0f;
                @Override
                public void actionPerformed(ActionEvent e) {
                    alpha += 0.1f;
                    if (alpha >= 1f) {
                        alpha = 1f;
                        timer.stop();
                    }
                    imageLabel.setIcon(images[newIndex]);
                    for (int i = 0; i < indicators.length; i++) {
                        indicators[i].setForeground(i == newIndex ? Color.BLUE : Color.GRAY);
                    }
                }
            });
            timer.start();
        };

        prevButton.addActionListener(e -> {
            int newIndex = (currentIndex.get() - 1 + images.length) % images.length;
            currentIndex.set(newIndex);
            updateCarousel.accept(newIndex);
        });

        nextButton.addActionListener(e -> {
            int newIndex = (currentIndex.get() + 1) % images.length;
            currentIndex.set(newIndex);
            updateCarousel.accept(newIndex);
        });

        carouselPanel.setFocusable(true);
        carouselPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    prevButton.doClick();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    nextButton.doClick();
                }
            }
        });

        Timer autoAdvanceTimer = new Timer(5000, e -> nextButton.doClick());
        autoAdvanceTimer.start();

        carouselPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                autoAdvanceTimer.stop();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                autoAdvanceTimer.restart();
            }
        });

        return carouselPanel;
    }

    private JButton createCarouselButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.BLUE);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.BLACK);
            }
        });

        return button;
    }

    private ImageIcon createPlaceholderIcon(int width, int height, String text) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(0, 0, Color.LIGHT_GRAY, width, height, Color.WHITE);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(1, 1, width-3, height-3);

        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (width - textWidth) / 2;
        int y = height / 2;

        g2d.setColor(Color.GRAY);
        g2d.drawString(text, x+1, y+1);
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x, y);

        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(width/4, height/4, 3*width/4, 3*height/4);
        g2d.drawLine(3*width/4, height/4, width/4, 3*height/4);

        g2d.dispose();
        return new ImageIcon(image);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}

// Inheritance - MenuButton extends JButton
class MenuButton extends JButton {
    public MenuButton(String text) {
        super(text);
        setFont(new Font("SansSerif", Font.PLAIN, 16));
        setForeground(Color.decode("#333333"));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                setForeground(Color.decode("#2B4576"));
            }

            public void mouseExited(MouseEvent evt) {
                setForeground(Color.decode("#333333"));
            }
        });
    }
}