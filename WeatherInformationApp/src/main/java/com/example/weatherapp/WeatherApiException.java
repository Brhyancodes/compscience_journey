package com.example.weatherapp;

/**
 * Exception used when a weather API request cannot be completed.
 */
public class WeatherApiException extends Exception {

    public WeatherApiException(String message) {
        super(message);
    }

    public WeatherApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
