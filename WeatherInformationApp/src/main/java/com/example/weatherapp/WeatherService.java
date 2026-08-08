package com.example.weatherapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all communication with OpenWeather.
 *
 * The application first converts a city name to latitude/longitude using
 * OpenWeather's Geocoding API, then requests current weather and forecast
 * data using those coordinates.
 */
public class WeatherService {

    private static final String GEO_URL =
            "https://api.openweathermap.org/geo/1.0/direct";

    private static final String CURRENT_URL =
            "https://api.openweathermap.org/data/2.5/weather";

    private static final String FORECAST_URL =
            "https://api.openweathermap.org/data/2.5/forecast";

    private final String apiKey;
    private final HttpClient httpClient;

    public WeatherService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public WeatherData.WeatherBundle fetchWeatherByCity(
            String city,
            String units) throws IOException, InterruptedException, WeatherApiException {

        if (apiKey == null || apiKey.isBlank()) {
            throw new WeatherApiException(
                    "OpenWeather API key is missing. Set OPENWEATHER_API_KEY before running the app.");
        }

        if (city == null || city.isBlank()) {
            throw new WeatherApiException("Please enter a city name.");
        }

        String encodedCity = URLEncoder.encode(city.trim(), StandardCharsets.UTF_8);

        String geoUrl = GEO_URL
                + "?q=" + encodedCity
                + "&limit=1"
                + "&appid=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        JsonArray locations = JsonParser.parseString(get(geoUrl)).getAsJsonArray();

        if (locations.isEmpty()) {
            throw new WeatherApiException("Location not found. Please check the city name.");
        }

        JsonObject locationJson = locations.get(0).getAsJsonObject();

        String name = getString(locationJson, "name", city.trim());
        String country = getString(locationJson, "country", "");
        double lat = getDouble(locationJson, "lat", 0);
        double lon = getDouble(locationJson, "lon", 0);

        return fetchWeatherByCoordinates(
                new WeatherData.Location(name, country, lat, lon),
                units
        );
    }

    public WeatherData.WeatherBundle fetchWeatherByCoordinates(
            WeatherData.Location location,
            String units) throws IOException, InterruptedException, WeatherApiException {

        String currentUrl = CURRENT_URL
                + "?lat=" + location.getLatitude()
                + "&lon=" + location.getLongitude()
                + "&units=" + units
                + "&appid=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        String forecastUrl = FORECAST_URL
                + "?lat=" + location.getLatitude()
                + "&lon=" + location.getLongitude()
                + "&units=" + units
                + "&cnt=8"
                + "&appid=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        JsonObject currentJson =
                JsonParser.parseString(get(currentUrl)).getAsJsonObject();

        JsonObject forecastJson =
                JsonParser.parseString(get(forecastUrl)).getAsJsonObject();

        WeatherData.Current current = parseCurrent(currentJson, location);
        List<WeatherData.ForecastItem> forecast = parseForecast(forecastJson);

        return new WeatherData.WeatherBundle(current, forecast);
    }

    private WeatherData.Current parseCurrent(
            JsonObject json,
            WeatherData.Location location) throws WeatherApiException {

        try {
            JsonObject main = json.getAsJsonObject("main");
            JsonObject wind = json.getAsJsonObject("wind");
            JsonArray weatherArray = json.getAsJsonArray("weather");
            JsonObject sys = json.getAsJsonObject("sys");

            JsonObject weather = weatherArray.get(0).getAsJsonObject();

            double temperature = main.get("temp").getAsDouble();
            double feelsLike = main.get("feels_like").getAsDouble();
            int humidity = main.get("humidity").getAsInt();
            double windSpeed = wind.get("speed").getAsDouble();

            String condition = getString(weather, "main", "Unknown");
            String description = getString(weather, "description", "Unknown");
            String icon = getString(weather, "icon", "01d");

            long timestamp = getLong(json, "dt", 0);
            long sunrise = getLong(sys, "sunrise", 0);
            long sunset = getLong(sys, "sunset", 0);
            int timezoneOffset = getInt(json, "timezone", 0);

            return new WeatherData.Current(
                    location,
                    temperature,
                    feelsLike,
                    humidity,
                    windSpeed,
                    condition,
                    description,
                    icon,
                    timestamp,
                    sunrise,
                    sunset,
                    timezoneOffset
            );
        } catch (Exception ex) {
            throw new WeatherApiException(
                    "The weather service returned an unexpected response.", ex);
        }
    }

    private List<WeatherData.ForecastItem> parseForecast(JsonObject json)
            throws WeatherApiException {

        List<WeatherData.ForecastItem> items = new ArrayList<>();

        try {
            JsonArray list = json.getAsJsonArray("list");

            for (JsonElement element : list) {
                JsonObject item = element.getAsJsonObject();
                JsonObject main = item.getAsJsonObject("main");
                JsonArray weatherArray = item.getAsJsonArray("weather");

                JsonObject weather = weatherArray.get(0).getAsJsonObject();

                long timestamp = getLong(item, "dt", 0);
                double temperature = main.get("temp").getAsDouble();

                String condition = getString(weather, "main", "Unknown");
                String description = getString(weather, "description", "Unknown");
                String icon = getString(weather, "icon", "01d");

                int precipitationProbability = 0;
                if (item.has("pop") && !item.get("pop").isJsonNull()) {
                    precipitationProbability =
                            (int) Math.round(item.get("pop").getAsDouble() * 100);
                }

                items.add(new WeatherData.ForecastItem(
                        timestamp,
                        temperature,
                        condition,
                        description,
                        icon,
                        precipitationProbability
                ));
            }

            return items;
        } catch (Exception ex) {
            throw new WeatherApiException(
                    "The forecast service returned an unexpected response.", ex);
        }
    }

    private String get(String url)
            throws IOException, InterruptedException, WeatherApiException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String message = "Weather API request failed (HTTP "
                    + response.statusCode() + ").";

            try {
                JsonObject error = JsonParser.parseString(response.body()).getAsJsonObject();
                if (error.has("message")) {
                    message += " " + error.get("message").getAsString();
                }
            } catch (Exception ignored) {
                // Keep the generic HTTP message.
            }

            throw new WeatherApiException(message);
        }

        return response.body();
    }

    private static String getString(JsonObject object, String key, String fallback) {
        if (object.has(key) && !object.get(key).isJsonNull()) {
            return object.get(key).getAsString();
        }
        return fallback;
    }

    private static double getDouble(JsonObject object, String key, double fallback) {
        if (object.has(key) && !object.get(key).isJsonNull()) {
            return object.get(key).getAsDouble();
        }
        return fallback;
    }

    private static long getLong(JsonObject object, String key, long fallback) {
        if (object.has(key) && !object.get(key).isJsonNull()) {
            return object.get(key).getAsLong();
        }
        return fallback;
    }

    private static int getInt(JsonObject object, String key, int fallback) {
        if (object.has(key) && !object.get(key).isJsonNull()) {
            return object.get(key).getAsInt();
        }
        return fallback;
    }
}
