package com.example.weatherapp;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * Loads OpenWeather condition icons.
 */
public final class WeatherIconUtil {

    private WeatherIconUtil() {
    }

    public static ImageIcon load(String iconCode, int size) {
        String url = "https://openweathermap.org/img/wn/"
                + iconCode + "@2x.png";

        try {
            URL imageUrl = URI.create(url).toURL();
            Image image = ImageIO.read(imageUrl);

            if (image == null) {
                return null;
            }

            Image scaled = image.getScaledInstance(
                    size,
                    size,
                    Image.SCALE_SMOOTH
            );

            return new ImageIcon(scaled);
        } catch (IOException | IllegalArgumentException ex) {
            return null;
        }
    }
}
