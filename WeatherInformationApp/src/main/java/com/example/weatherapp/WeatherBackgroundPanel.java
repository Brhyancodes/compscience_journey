package com.example.weatherapp;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Changes the background according to the selected location's local time.
 */
public class WeatherBackgroundPanel extends JPanel {

    private int localHour = 12;

    public void setLocalHour(int localHour) {
        this.localHour = localHour;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );

        Color top;
        Color bottom;

        if (localHour >= 5 && localHour < 11) {
            // Morning
            top = new Color(104, 155, 196);
            bottom = new Color(245, 205, 139);
        } else if (localHour >= 11 && localHour < 17) {
            // Day
            top = new Color(70, 150, 205);
            bottom = new Color(183, 222, 244);
        } else if (localHour >= 17 && localHour < 21) {
            // Evening / sunset
            top = new Color(72, 74, 130);
            bottom = new Color(231, 142, 106);
        } else {
            // Night
            top = new Color(18, 28, 61);
            bottom = new Color(53, 67, 110);
        }

        g2.setPaint(new GradientPaint(
                0,
                0,
                top,
                getWidth(),
                getHeight(),
                bottom
        ));

        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
