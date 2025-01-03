package com.university.exam.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import static com.university.exam.ui.ModernUI.*;

public class ModernCard extends JPanel {
    private String title;
    private Color accentColor;
    private boolean isHovered = false;
    private float shadowAlpha = 0.2f;
    private Timer hoverTimer;
    private float hoverProgress = 0f;

    public ModernCard(String title, Color accentColor) {
        this.title = title;
        this.accentColor = accentColor;
        setup();
    }

    private void setup() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                startHoverAnimation(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                startHoverAnimation(false);
            }
        });
    }

    private void startHoverAnimation(boolean hovering) {
        if (hoverTimer != null && hoverTimer.isRunning()) {
            hoverTimer.stop();
        }

        float targetProgress = hovering ? 1f : 0f;
        float startProgress = hoverProgress;

        hoverTimer = new Timer(16, e -> {
            float diff = targetProgress - startProgress;
            hoverProgress = startProgress + (diff * 0.2f);

            if (Math.abs(targetProgress - hoverProgress) < 0.01f) {
                hoverProgress = targetProgress;
                ((Timer)e.getSource()).stop();
            }

            repaint();
        });

        hoverTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw shadow
        int shadowSize = 20;
        for (int i = 0; i < shadowSize; i++) {
            float alpha = shadowAlpha * (1f - (float)i / shadowSize) * (1f + hoverProgress * 0.5f);
            g2.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(
                shadowSize - i, 
                shadowSize - i, 
                getWidth() - 2 * (shadowSize - i) - 1,
                getHeight() - 2 * (shadowSize - i) - 1,
                20, 20));
        }

        // Draw card background
        GradientPaint gradient = new GradientPaint(
            0, 0, CARD_BG,
            0, getHeight(), new Color(
                CARD_BG.getRed(),
                CARD_BG.getGreen(),
                CARD_BG.getBlue(),
                240
            )
        );
        g2.setPaint(gradient);
        g2.fill(new RoundRectangle2D.Float(
            shadowSize, shadowSize,
            getWidth() - 2 * shadowSize,
            getHeight() - 2 * shadowSize,
            20, 20));

        // Draw accent line
        g2.setColor(accentColor);
        g2.setStroke(new BasicStroke(3f));
        g2.draw(new Line2D.Float(
            shadowSize + 20,
            shadowSize + 40,
            shadowSize + 60,
            shadowSize + 40
        ));

        // Draw title
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.setColor(TEXT_PRIMARY);
        g2.drawString(title, shadowSize + 20, shadowSize + 30);

        // Draw hover effect
        if (hoverProgress > 0) {
            g2.setColor(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(),
                (int)(30 * hoverProgress)));
            g2.fill(new RoundRectangle2D.Float(
                shadowSize, shadowSize,
                getWidth() - 2 * shadowSize,
                getHeight() - 2 * shadowSize,
                20, 20));
        }

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        return new Dimension(
            Math.max(300, size.width + 40),
            Math.max(200, size.height + 40)
        );
    }
}
