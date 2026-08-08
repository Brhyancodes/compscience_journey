package com.example.weatherapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Model classes used by the weather application.
 */
public final class WeatherData {

    private WeatherData() {
    }

    public static class Location {
        private final String name;
        private final String country;
        private final double latitude;
        private final double longitude;

        public Location(String name, String country, double latitude, double longitude) {
            this.name = name;
            this.country = country;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public static class Current {
        private final Location location;
        private final double temperature;
        private final double feelsLike;
        private final int humidity;
        private final double windSpeed;
        private final String condition;
        private final String description;
        private final String icon;
        private final long timestamp;
        private final long sunrise;
        private final long sunset;
        private final int timezoneOffset;

        public Current(
                Location location,
                double temperature,
                double feelsLike,
                int humidity,
                double windSpeed,
                String condition,
                String description,
                String icon,
                long timestamp,
                long sunrise,
                long sunset,
                int timezoneOffset) {

            this.location = location;
            this.temperature = temperature;
            this.feelsLike = feelsLike;
            this.humidity = humidity;
            this.windSpeed = windSpeed;
            this.condition = condition;
            this.description = description;
            this.icon = icon;
            this.timestamp = timestamp;
            this.sunrise = sunrise;
            this.sunset = sunset;
            this.timezoneOffset = timezoneOffset;
        }

        public Location getLocation() {
            return location;
        }

        public double getTemperature() {
            return temperature;
        }

        public double getFeelsLike() {
            return feelsLike;
        }

        public int getHumidity() {
            return humidity;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public String getCondition() {
            return condition;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public int getTimezoneOffset() {
            return timezoneOffset;
        }
    }

    public static class ForecastItem {
        private final long timestamp;
        private final double temperature;
        private final String condition;
        private final String description;
        private final String icon;
        private final int precipitationProbability;

        public ForecastItem(
                long timestamp,
                double temperature,
                String condition,
                String description,
                String icon,
                int precipitationProbability) {

            this.timestamp = timestamp;
            this.temperature = temperature;
            this.condition = condition;
            this.description = description;
            this.icon = icon;
            this.precipitationProbability = precipitationProbability;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public double getTemperature() {
            return temperature;
        }

        public String getCondition() {
            return condition;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

        public int getPrecipitationProbability() {
            return precipitationProbability;
        }
    }

    public static class WeatherBundle {
        private final Current current;
        private final List<ForecastItem> forecast;

        public WeatherBundle(Current current, List<ForecastItem> forecast) {
            this.current = current;
            this.forecast = new ArrayList<>(forecast);
        }

        public Current getCurrent() {
            return current;
        }

        public List<ForecastItem> getForecast() {
            return forecast;
        }
    }
}
