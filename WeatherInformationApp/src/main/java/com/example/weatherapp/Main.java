package com.example.weatherapp;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Application entry point.
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // The default Swing look and feel is acceptable if the system style fails.
        }

        SwingUtilities.invokeLater(() -> {
            WeatherApp app = new WeatherApp();
            app.setVisible(true);
        });
    }
}
