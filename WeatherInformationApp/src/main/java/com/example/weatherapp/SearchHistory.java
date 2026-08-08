package com.example.weatherapp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores recent weather searches in a small CSV file.
 */
public class SearchHistory {

    private static final Path FILE = Path.of("weather_history.csv");

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    private final List<HistoryEntry> entries = new ArrayList<>();

    public SearchHistory() {
        load();
    }

    public void add(String city, double latitude, double longitude) {
        entries.add(0, new HistoryEntry(
                city,
                latitude,
                longitude,
                FORMATTER.format(Instant.now())
        ));

        // Keep the history useful and compact.
        if (entries.size() > 20) {
            entries.remove(entries.size() - 1);
        }

        save();
    }

    public List<HistoryEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    private void load() {
        if (!Files.exists(FILE)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(FILE, StandardCharsets.UTF_8);

            for (String line : lines) {
                if (line.isBlank() || line.startsWith("timestamp,city")) {
                    continue;
                }

                String[] parts = line.split(",", -1);

                if (parts.length >= 4) {
                    try {
                        entries.add(new HistoryEntry(
                                parts[1],
                                Double.parseDouble(parts[2]),
                                Double.parseDouble(parts[3]),
                                parts[0]
                        ));
                    } catch (NumberFormatException ignored) {
                        // Skip malformed history entries.
                    }
                }
            }
        } catch (IOException ignored) {
            // The app can still work without previous history.
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        lines.add("timestamp,city,latitude,longitude");

        for (HistoryEntry entry : entries) {
            String safeCity = entry.city().replace(",", " ");
            lines.add(String.format(
                    "%s,%s,%s,%s",
                    entry.timestamp(),
                    safeCity,
                    entry.latitude(),
                    entry.longitude()
            ));
        }

        try {
            Files.write(
                    FILE,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException ignored) {
            // A history write failure should not stop the weather app.
        }
    }

    public record HistoryEntry(
            String city,
            double latitude,
            double longitude,
            String timestamp) {
    }
}
