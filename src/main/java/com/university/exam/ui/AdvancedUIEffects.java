package com.university.exam.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class AdvancedUIEffects {
    // Glass pane effect for modal overlays
    public static class GlassPane extends JComponent {
        private float alpha = 0.5f;
        
        public GlassPane() {
            setOpaque(false);
            setBackground(new Color(0, 0, 0, 128));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    // Modern tooltip with rounded corners
    public static class ModernTooltip extends JToolTip {
        public ModernTooltip() {
            setBackground(new Color(50, 50, 50));
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // Ripple effect for buttons
    public static class RippleEffect {
        private Point origin;
        private float radius;
        private Color color;
        private Timer timer;
        private JComponent target;

        public RippleEffect(JComponent target, Point origin) {
            this.target = target;
            this.origin = origin;
            this.radius = 0;
            this.color = new Color(255, 255, 255, 100);
            
            timer = new Timer(16, e -> {
                radius += 5;
                if (radius > target.getWidth()) {
                    timer.stop();
                    radius = 0;
                }
                target.repaint();
            });
            timer.start();
        }

        public void paint(Graphics2D g2) {
            g2.setColor(color);
            float alpha = 1 - (radius / (float) target.getWidth());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.fill(new Ellipse2D.Double(origin.x - radius, origin.y - radius, radius * 2, radius * 2));
        }
    }

    // Card panel with shadow and hover effect
    public static class ModernCard extends JPanel {
        private boolean hovered = false;
        private int shadowSize = 10;
        private float shadowOpacity = 0.3f;
        private Color shadowColor = new Color(0, 0, 0, 50);

        public ModernCard() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(shadowSize, shadowSize, shadowSize, shadowSize));
            
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    shadowSize = 15;
                    repaint();
                }
                
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    shadowSize = 10;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw shadow
            for (int i = 0; i < shadowSize; i++) {
                float opacity = shadowOpacity * (1 - (float) i / shadowSize);
                g2.setColor(new Color(0, 0, 0, (int) (opacity * 255)));
                g2.fillRoundRect(i, i, getWidth() - (i * 2), getHeight() - (i * 2), 20, 20);
            }

            // Draw card background
            g2.setColor(getBackground());
            g2.fillRoundRect(shadowSize, shadowSize, 
                           getWidth() - (shadowSize * 2), 
                           getHeight() - (shadowSize * 2), 15, 15);

            if (hovered) {
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(shadowSize, shadowSize, 
                               getWidth() - (shadowSize * 2), 
                               getHeight() - (shadowSize * 2), 15, 15);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
