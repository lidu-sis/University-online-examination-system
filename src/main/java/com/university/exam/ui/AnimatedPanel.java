package com.university.exam.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class AnimatedPanel extends JPanel {
    private float alpha = 0f;
    private Timer fadeTimer;
    private boolean isShowing = false;

    public AnimatedPanel() {
        setOpaque(false);
        fadeTimer = new Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isShowing) {
                    alpha += 0.1f;
                    if (alpha >= 1f) {
                        alpha = 1f;
                        fadeTimer.stop();
                    }
                } else {
                    alpha -= 0.1f;
                    if (alpha <= 0f) {
                        alpha = 0f;
                        fadeTimer.stop();
                    }
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        // Create gradient background
        GradientPaint gp = new GradientPaint(
            0, 0, new Color(240, 248, 255),
            0, getHeight(), new Color(230, 240, 250)
        );
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Add subtle pattern
        g2d.setColor(new Color(255, 255, 255, 30));
        for (int i = 0; i < getHeight(); i += 20) {
            g2d.drawLine(0, i, getWidth(), i);
        }

        // Add corner decoration
        g2d.setColor(new Color(70, 130, 180, 50));
        g2d.setStroke(new BasicStroke(2f));
        int cornerSize = 40;
        g2d.drawArc(0, 0, cornerSize, cornerSize, 90, 90);
        g2d.drawArc(getWidth() - cornerSize, 0, cornerSize, cornerSize, 0, 90);
        g2d.drawArc(0, getHeight() - cornerSize, cornerSize, cornerSize, 180, 90);
        g2d.drawArc(getWidth() - cornerSize, getHeight() - cornerSize, cornerSize, cornerSize, 270, 90);

        g2d.dispose();
        super.paintComponent(g);
    }

    public void fadeIn() {
        isShowing = true;
        fadeTimer.start();
    }

    public void fadeOut() {
        isShowing = false;
        fadeTimer.start();
    }
}
