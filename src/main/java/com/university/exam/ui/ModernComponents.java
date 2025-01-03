package com.university.exam.ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class ModernComponents {
    // Colors
    public static final Color PRIMARY_DARK = new Color(41, 128, 185);
    public static final Color PRIMARY_LIGHT = new Color(52, 152, 219);
    public static final Color SECONDARY = new Color(155, 89, 182);
    public static final Color SUCCESS = new Color(46, 204, 113);
    public static final Color WARNING = new Color(241, 196, 15);
    public static final Color ERROR = new Color(231, 76, 60);
    public static final Color BACKGROUND = new Color(236, 240, 241);
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141);

    public static class ModernCard extends JPanel {
        private String title;
        private Color accentColor;

        public ModernCard(String title, Color accentColor) {
            this.title = title;
            this.accentColor = accentColor;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw shadow
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 15, 15);

            // Draw card background
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 15, 15);

            // Draw accent line
            g2.setColor(accentColor);
            g2.fillRoundRect(0, 0, getWidth() - 3, 5, 2, 2);

            // Draw title
            g2.setColor(TEXT_PRIMARY);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(title, 15, 30);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class RoundedBorder extends AbstractBorder {
        private Color color;
        private int radius;
        private boolean hasShadow;

        public RoundedBorder(Color color, int radius, boolean hasShadow) {
            this.color = color;
            this.radius = radius;
            this.hasShadow = hasShadow;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (hasShadow) {
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(x + 2, y + 2, width - 4, height - 4, radius, radius);
            }

            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
    }

    public static class ModernProgressBar extends JProgressBar {
        private Color progressColor;

        public ModernProgressBar(Color progressColor) {
            this.progressColor = progressColor;
            setOpaque(false);
            setBorderPainted(false);
            setStringPainted(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw background
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

            // Draw progress
            g2.setColor(progressColor);
            int progressWidth = (int) ((getWidth() - 2) * ((double) getValue() / getMaximum()));
            g2.fillRoundRect(1, 1, progressWidth, getHeight() - 2, getHeight() - 2, getHeight() - 2);

            // Draw text
            String progressText = getString();
            if (progressText != null) {
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(progressText);
                int textHeight = fm.getHeight();
                g2.drawString(progressText,
                    (getWidth() - textWidth) / 2,
                    (getHeight() + textHeight / 2) / 2);
            }

            g2.dispose();
        }
    }

    public static class CircularProgressBar extends JComponent {
        private int progress;
        private Color progressColor;

        public CircularProgressBar(Color progressColor) {
            this.progressColor = progressColor;
            setPreferredSize(new Dimension(100, 100));
        }

        public void setProgress(int progress) {
            this.progress = progress;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw background circle
            g2.setColor(new Color(230, 230, 230));
            g2.setStroke(new BasicStroke(10));
            g2.drawArc(10, 10, getWidth() - 20, getHeight() - 20, 0, 360);

            // Draw progress arc
            g2.setColor(progressColor);
            g2.drawArc(10, 10, getWidth() - 20, getHeight() - 20, 90, -(progress * 360 / 100));

            // Draw progress text
            g2.setColor(TEXT_PRIMARY);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            String text = progress + "%";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text,
                (getWidth() - fm.stringWidth(text)) / 2,
                (getHeight() + fm.getHeight() / 2) / 2);

            g2.dispose();
        }
    }

    // Modern button with hover effect and ripple
    public static class ModernButton extends JButton {
        private Color hoverBackground = new Color(70, 130, 180);
        private boolean isHovered = false;
        private AdvancedUIEffects.RippleEffect ripple;

        public ModernButton(String text) {
            super(text);
            setup();
        }

        private void setup() {
            setOpaque(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    ripple = new AdvancedUIEffects.RippleEffect(ModernButton.this, e.getPoint());
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw button background
            if (isHovered) {
                g2.setColor(hoverBackground);
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            
            // Draw ripple effect if present
            if (ripple != null) {
                ripple.paint(g2);
            }
            
            // Draw text
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(getText(), g2);
            int x = (getWidth() - (int) r.getWidth()) / 2;
            int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
            
            g2.setColor(getForeground());
            g2.drawString(getText(), x, y);
            
            g2.dispose();
        }
    }

    // Modern text field with floating label
    public static class ModernTextField extends JTextField {
        private String placeholder;
        private Color placeholderColor = new Color(150, 150, 150);
        private boolean focused = false;
        private float labelY = 0f;
        private Timer animationTimer;

        public ModernTextField(String placeholder) {
            this.placeholder = placeholder;
            setup();
        }

        private void setup() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 10));
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(Color.WHITE);
            
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
            
            animationTimer = new Timer(16, new ActionListener() {
                float progress = 0f;
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    progress += 0.1f;
                    if (progress >= 1f) {
                        progress = 1f;
                        animationTimer.stop();
                    }
                    
                    labelY = currentY + (targetY - currentY) * progress;
                    repaint();
                }
            });
            
            animationTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            
            // Draw text
            super.paintComponent(g);
            
            // Draw placeholder/label
            if (getText().isEmpty() && !focused) {
                g2.setColor(placeholderColor);
                g2.setFont(getFont());
                g2.drawString(placeholder, 10, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 5);
            } else {
                g2.setColor(focused ? Color.WHITE : placeholderColor);
                g2.setFont(getFont().deriveFont(12f));
                g2.drawString(placeholder, 10, 15 + (labelY * 20));
            }
            
            // Draw bottom line
            if (focused) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
            } else {
                g2.setColor(new Color(200, 200, 200));
                g2.setStroke(new BasicStroke(1f));
            }
            g2.drawLine(5, getHeight() - 5, getWidth() - 5, getHeight() - 5);
            
            g2.dispose();
        }
    }
}
