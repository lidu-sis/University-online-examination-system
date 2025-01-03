package com.university.exam.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class ModernUI {
    // Modern color scheme
    public static final Color PRIMARY = new Color(75, 0, 130);      // Deep Purple
    public static final Color SECONDARY = new Color(147, 112, 219); // Medium Purple
    public static final Color ACCENT = new Color(255, 140, 0);      // Dark Orange
    public static final Color BACKGROUND = new Color(245, 245, 250); // Light Gray-Purple
    public static final Color CARD_BG = new Color(255, 255, 255);   // White
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33); // Dark Gray
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117); // Medium Gray
    public static final Color SUCCESS = new Color(46, 204, 113);    // Green
    public static final Color ERROR = new Color(231, 76, 60);       // Red
    public static final Color WARNING = new Color(241, 196, 15);    // Yellow

    // Create styled label with modern font
    public static JLabel createStyledLabel(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    // Modern button with hover effect and gradient
    public static class ModernButton extends JButton {
        private boolean isHovered = false;
        private Color gradientStart = PRIMARY;
        private Color gradientEnd = SECONDARY;
        private Timer pulseTimer;
        private float pulseAlpha = 0f;

        public ModernButton(String text) {
            super(text);
            setup();
        }

        private void setup() {
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Hover effect
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    startPulseAnimation();
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    stopPulseAnimation();
                    repaint();
                }
            });
        }

        private void startPulseAnimation() {
            if (pulseTimer != null) pulseTimer.stop();
            pulseTimer = new Timer(50, e -> {
                pulseAlpha += 0.1f;
                if (pulseAlpha > 1f) pulseAlpha = 0f;
                repaint();
            });
            pulseTimer.start();
        }

        private void stopPulseAnimation() {
            if (pulseTimer != null) {
                pulseTimer.stop();
                pulseAlpha = 0f;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Create gradient paint
            GradientPaint gradient = new GradientPaint(
                0, 0, gradientStart,
                getWidth(), getHeight(), gradientEnd
            );
            g2.setPaint(gradient);

            // Draw rounded rectangle background
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));

            // Draw hover effect
            if (isHovered) {
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                
                // Draw pulse effect
                g2.setColor(new Color(255, 255, 255, (int)(50 * pulseAlpha)));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            }

            // Draw text
            FontMetrics fm = g2.getFontMetrics(getFont());
            Rectangle2D r = fm.getStringBounds(getText(), g2);
            int x = (getWidth() - (int) r.getWidth()) / 2;
            int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
            
            g2.setFont(getFont());
            g2.setColor(getForeground());
            g2.drawString(getText(), x, y);

            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            return new Dimension(size.width + 40, 40);
        }
    }

    // Modern text field with floating label and animations
    public static class ModernTextField extends JTextField {
        private String placeholder;
        private Color placeholderColor = TEXT_SECONDARY;
        private boolean focused = false;
        private float labelY = 0f;
        private Timer animationTimer;
        private float borderAlpha = 0f;

        public ModernTextField(String placeholder) {
            this.placeholder = placeholder;
            setup();
        }

        private void setup() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(25, 15, 10, 15));
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(TEXT_PRIMARY);
            
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    focused = true;
                    startAnimation(true);
                }
                
                @Override
                public void focusLost(FocusEvent e) {
                    focused = false;
                    if (getText().isEmpty()) {
                        startAnimation(false);
                    }
                }
            });
        }

        private void startAnimation(boolean up) {
            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }
            
            float targetY = up ? 0f : 1f;
            float currentY = labelY;
            
            animationTimer = new Timer(16, e -> {
                float progress = 0f;
                progress += 0.1f;
                if (progress >= 1f) {
                    progress = 1f;
                    ((Timer)e.getSource()).stop();
                }
                
                labelY = currentY + (targetY - currentY) * progress;
                borderAlpha = up ? progress : 1f - progress;
                repaint();
            });
            
            animationTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background
            g2.setColor(CARD_BG);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            
            // Draw border
            if (focused) {
                g2.setColor(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), 
                    (int)(255 * borderAlpha)));
                g2.setStroke(new BasicStroke(2f));
            } else {
                g2.setColor(new Color(TEXT_SECONDARY.getRed(), TEXT_SECONDARY.getGreen(), 
                    TEXT_SECONDARY.getBlue(), 100));
                g2.setStroke(new BasicStroke(1f));
            }
            g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
            
            // Draw text
            super.paintComponent(g);
            
            // Draw placeholder/label
            if (getText().isEmpty() && !focused) {
                g2.setColor(placeholderColor);
                g2.setFont(getFont());
                g2.drawString(placeholder, 15, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 5);
            } else {
                g2.setColor(focused ? PRIMARY : TEXT_SECONDARY);
                g2.setFont(getFont().deriveFont(12f));
                g2.drawString(placeholder, 15, 15 + (labelY * 20));
            }
            
            g2.dispose();
        }
    }

    // Modern password field with the same styling as text field
    public static class ModernPasswordField extends JPasswordField {
        private String placeholder;
        private Color placeholderColor = TEXT_SECONDARY;
        private boolean focused = false;
        private float labelY = 0f;
        private Timer animationTimer;
        private float borderAlpha = 0f;

        public ModernPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setup();
        }

        private void setup() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(25, 15, 10, 15));
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(TEXT_PRIMARY);
            
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    focused = true;
                    startAnimation(true);
                }
                
                @Override
                public void focusLost(FocusEvent e) {
                    focused = false;
                    if (getPassword().length == 0) {
                        startAnimation(false);
                    }
                }
            });
        }

        private void startAnimation(boolean up) {
            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }
            
            float targetY = up ? 0f : 1f;
            float currentY = labelY;
            
            animationTimer = new Timer(16, e -> {
                float progress = 0f;
                progress += 0.1f;
                if (progress >= 1f) {
                    progress = 1f;
                    ((Timer)e.getSource()).stop();
                }
                
                labelY = currentY + (targetY - currentY) * progress;
                borderAlpha = up ? progress : 1f - progress;
                repaint();
            });
            
            animationTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background
            g2.setColor(CARD_BG);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            
            // Draw border
            if (focused) {
                g2.setColor(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), 
                    (int)(255 * borderAlpha)));
                g2.setStroke(new BasicStroke(2f));
            } else {
                g2.setColor(new Color(TEXT_SECONDARY.getRed(), TEXT_SECONDARY.getGreen(), 
                    TEXT_SECONDARY.getBlue(), 100));
                g2.setStroke(new BasicStroke(1f));
            }
            g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
            
            // Draw text
            super.paintComponent(g);
            
            // Draw placeholder/label
            if (getPassword().length == 0 && !focused) {
                g2.setColor(placeholderColor);
                g2.setFont(getFont());
                g2.drawString(placeholder, 15, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 5);
            } else {
                g2.setColor(focused ? PRIMARY : TEXT_SECONDARY);
                g2.setFont(getFont().deriveFont(12f));
                g2.drawString(placeholder, 15, 15 + (labelY * 20));
            }
            
            g2.dispose();
        }
    }
}
