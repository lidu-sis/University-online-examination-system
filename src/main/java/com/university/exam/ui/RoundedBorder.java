package com.university.exam.ui;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class RoundedBorder extends AbstractBorder {
    private Color color;
    private int thickness;
    private int radius;
    private boolean drawShadow;
    private Insets insets;

    public RoundedBorder(Color color, int radius, boolean drawShadow) {
        this.color = color;
        this.radius = radius;
        this.thickness = 1;
        this.drawShadow = drawShadow;
        this.insets = new Insets(radius/2, radius/2, radius/2, radius/2);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw shadow if enabled
        if (drawShadow) {
            for (int i = 0; i < 5; i++) {
                float alpha = 0.1f * (5 - i);
                g2.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
                g2.setStroke(new BasicStroke(thickness + i));
                g2.draw(new RoundRectangle2D.Double(x + i, y + i, 
                                                  width - 1 - (2 * i), 
                                                  height - 1 - (2 * i), 
                                                  radius, radius));
            }
        }

        // Draw main border
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));

        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }
}
