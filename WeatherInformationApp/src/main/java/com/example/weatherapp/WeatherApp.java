package com.example.weatherapp;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main Swing user interface for the Weather Information App.
 */
public class WeatherApp extends JFrame {

    private static final String METRIC = "Celsius (°C, m/s)";
    private static final String IMPERIAL = "Fahrenheit (°F, mph)";

    private final WeatherService weatherService;
    private final SearchHistory searchHistory;
    private final WeatherBackgroundPanel backgroundPanel;

    private final JTextField locationField;
    private final JButton searchButton;
    private final JComboBox<String> unitCombo;
    private final JLabel statusLabel;

    private final JLabel locationLabel;
    private final JLabel currentIconLabel;
    private final JLabel temperatureLabel;
    private final JLabel conditionLabel;
    private final JLabel feelsLikeLabel;
    private final JLabel humidityLabel;
    private final JLabel windLabel;
    private final JLabel coordinatesLabel;

    private final JPanel forecastPanel;

    private WeatherData.Location lastLocation;

    public WeatherApp() {
        String apiKey = System.getenv("OPENWEATHER_API_KEY");

        weatherService = new WeatherService(apiKey);
        searchHistory = new SearchHistory();

        setTitle("Weather Information App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 720));
        setSize(1150, 780);
        setLocationRelativeTo(null);

        backgroundPanel = new WeatherBackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout(15, 15));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 12, 18));
        setContentPane(backgroundPanel);

        JPanel topPanel = buildTopPanel();
        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = buildCenterPanel();
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = buildBottomPanel();
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        locationField = findLocationField(topPanel);
        searchButton = findSearchButton(topPanel);
        unitCombo = findUnitCombo(topPanel);
        statusLabel = findStatusLabel(bottomPanel);

        locationLabel = findLabel(centerPanel, "location");
        currentIconLabel = findLabel(centerPanel, "currentIcon");
        temperatureLabel = findLabel(centerPanel, "temperature");
        conditionLabel = findLabel(centerPanel, "condition");
        feelsLikeLabel = findLabel(centerPanel, "feelsLike");
        humidityLabel = findLabel(centerPanel, "humidity");
        windLabel = findLabel(centerPanel, "wind");
        coordinatesLabel = findLabel(centerPanel, "coordinates");

        forecastPanel = findForecastPanel(centerPanel);

        // Default text before the first search.
        locationLabel.setText("Search for a city");
        temperatureLabel.setText("--°");
        conditionLabel.setText("Weather information will appear here");
        feelsLikeLabel.setText("Feels like: --");
        humidityLabel.setText("Humidity: --");
        windLabel.setText("Wind: --");
        coordinatesLabel.setText("Coordinates: --");

        installListeners();
    }

    private JPanel buildTopPanel() {
        JPanel outer = new JPanel(new BorderLayout(10, 10));
        outer.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Weather Information App");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitle = new JLabel(
                "Real-time weather, short forecast, search history and unit conversion");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subtitle);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        controls.setOpaque(false);

        JTextField field = new JTextField(18);
        field.setName("locationField");
        field.setToolTipText("Enter a city, e.g. Toronto");
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton button = new JButton("Search");
        button.setName("searchButton");
        button.setFocusPainted(false);

        JComboBox<String> units = new JComboBox<>(new String[]{METRIC, IMPERIAL});
        units.setName("unitCombo");
        units.setPreferredSize(new Dimension(150, 28));

        JButton historyButton = new JButton("History");
        historyButton.setFocusPainted(false);
        historyButton.addActionListener(e -> showHistoryDialog());

        controls.add(field);
        controls.add(button);
        controls.add(units);
        controls.add(historyButton);

        outer.add(titlePanel, BorderLayout.WEST);
        outer.add(controls, BorderLayout.EAST);

        return outer;
    }

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);

        JPanel currentPanel = new JPanel(new BorderLayout(20, 10));
        currentPanel.setName("currentPanel");
        currentPanel.setOpaque(true);
        currentPanel.setBackground(new Color(255, 255, 255, 225));
        currentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120)),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));

        JPanel mainInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        mainInfo.setOpaque(false);

        JLabel icon = new JLabel();
        icon.setName("currentIcon");
        icon.setPreferredSize(new Dimension(100, 100));
        icon.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel location = new JLabel();
        location.setName("location");
        location.setFont(new Font("SansSerif", Font.BOLD, 25));
        location.setAlignmentX(LEFT_ALIGNMENT);

        JLabel temp = new JLabel();
        temp.setName("temperature");
        temp.setFont(new Font("SansSerif", Font.BOLD, 48));
        temp.setAlignmentX(LEFT_ALIGNMENT);

        JLabel condition = new JLabel();
        condition.setName("condition");
        condition.setFont(new Font("SansSerif", Font.PLAIN, 17));
        condition.setAlignmentX(LEFT_ALIGNMENT);

        text.add(location);
        text.add(Box.createVerticalStrut(5));
        text.add(temp);
        text.add(Box.createVerticalStrut(2));
        text.add(condition);

        mainInfo.add(icon);
        mainInfo.add(text);

        JPanel details = new JPanel(new GridLayout(2, 2, 10, 10));
        details.setOpaque(false);
        details.setPreferredSize(new Dimension(390, 110));

        JLabel feels = detailLabel("Feels like: --", "feelsLike");
        JLabel humidity = detailLabel("Humidity: --", "humidity");
        JLabel wind = detailLabel("Wind: --", "wind");
        JLabel coords = detailLabel("Coordinates: --", "coordinates");

        details.add(feels);
        details.add(humidity);
        details.add(wind);
        details.add(coords);

        currentPanel.add(mainInfo, BorderLayout.CENTER);
        currentPanel.add(details, BorderLayout.EAST);

        JPanel forecastWrapper = new JPanel(new BorderLayout(5, 5));
        forecastWrapper.setOpaque(false);

        JLabel forecastTitle = new JLabel("Short-Term Forecast");
        forecastTitle.setForeground(Color.WHITE);
        forecastTitle.setFont(new Font("SansSerif", Font.BOLD, 19));

        JPanel forecasts = new JPanel(new GridLayout(1, 6, 8, 0));
        forecasts.setName("forecastPanel");
        forecasts.setOpaque(false);

        for (int i = 0; i < 6; i++) {
            forecasts.add(createForecastCard());
        }

        forecastWrapper.add(forecastTitle, BorderLayout.NORTH);
        forecastWrapper.add(forecasts, BorderLayout.CENTER);

        center.add(currentPanel, BorderLayout.NORTH);
        center.add(forecastWrapper, BorderLayout.CENTER);

        return center;
    }

    private JLabel detailLabel(String text, String name) {
        JLabel label = new JLabel(text);
        label.setName(name);
        label.setOpaque(true);
        label.setBackground(new Color(245, 248, 252));
        label.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return label;
    }

    private JPanel createForecastCard() {
        JPanel card = new JPanel();
        card.setName("forecastCard");
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 225));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 238)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel time = new JLabel("--:--");
        time.setName("forecastTime");
        time.setAlignmentX(CENTER_ALIGNMENT);
        time.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel icon = new JLabel();
        icon.setName("forecastIcon");
        icon.setPreferredSize(new Dimension(64, 64));
        icon.setAlignmentX(CENTER_ALIGNMENT);
        icon.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel temp = new JLabel("--°");
        temp.setName("forecastTemp");
        temp.setAlignmentX(CENTER_ALIGNMENT);
        temp.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel condition = new JLabel("—");
        condition.setName("forecastCondition");
        condition.setAlignmentX(CENTER_ALIGNMENT);
        condition.setHorizontalAlignment(SwingConstants.CENTER);
        condition.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JLabel rain = new JLabel("Rain: --");
        rain.setName("forecastRain");
        rain.setAlignmentX(CENTER_ALIGNMENT);
        rain.setFont(new Font("SansSerif", Font.PLAIN, 11));

        card.add(time);
        card.add(Box.createVerticalStrut(4));
        card.add(icon);
        card.add(Box.createVerticalStrut(2));
        card.add(temp);
        card.add(Box.createVerticalStrut(3));
        card.add(condition);
        card.add(Box.createVerticalStrut(4));
        card.add(rain);

        return card;
    }

    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(8, 5));
        bottom.setOpaque(false);

        JLabel status = new JLabel("Ready");
        status.setName("statusLabel");
        status.setForeground(Color.WHITE);

        JLabel attribution = new JLabel("Weather data provided by OpenWeather");
        attribution.setForeground(Color.WHITE);
        attribution.setHorizontalAlignment(SwingConstants.RIGHT);

        bottom.add(new JSeparator(), BorderLayout.NORTH);
        bottom.add(status, BorderLayout.WEST);
        bottom.add(attribution, BorderLayout.EAST);

        return bottom;
    }

    private void installListeners() {
        searchButton.addActionListener(this::performSearch);

        locationField.addActionListener(this::performSearch);

        unitCombo.addActionListener(e -> {
            if (lastLocation != null) {
                refreshForUnitChange();
            }
        });
    }

    private void performSearch(ActionEvent event) {
        String city = locationField.getText().trim();

        if (city.isBlank()) {
            showError("Please enter a city name.");
            return;
        }

        searchButton.setEnabled(false);
        statusLabel.setText("Loading weather data...");

        String units = getSelectedUnits();

        SwingWorker<WeatherData.WeatherBundle, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected WeatherData.WeatherBundle doInBackground()
                            throws Exception {
                        return weatherService.fetchWeatherByCity(city, units);
                    }

                    @Override
                    protected void done() {
                        searchButton.setEnabled(true);

                        try {
                            WeatherData.WeatherBundle bundle = get();
                            displayWeather(bundle);

                            WeatherData.Location location =
                                    bundle.getCurrent().getLocation();

                            lastLocation = location;
                            searchHistory.add(
                                    location.getName(),
                                    location.getLatitude(),
                                    location.getLongitude()
                            );

                            statusLabel.setText(
                                    "Weather updated successfully."
                            );
                        } catch (Exception ex) {
                            Throwable cause = ex.getCause() != null
                                    ? ex.getCause()
                                    : ex;

                            showError(cause.getMessage() != null
                                    ? cause.getMessage()
                                    : "Unable to retrieve weather data.");
                        }
                    }
                };

        worker.execute();
    }

    private void refreshForUnitChange() {
        searchButton.setEnabled(false);
        statusLabel.setText("Changing units...");

        String units = getSelectedUnits();

        SwingWorker<WeatherData.WeatherBundle, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected WeatherData.WeatherBundle doInBackground()
                            throws Exception {
                        return weatherService.fetchWeatherByCoordinates(
                                lastLocation,
                                units
                        );
                    }

                    @Override
                    protected void done() {
                        searchButton.setEnabled(true);

                        try {
                            displayWeather(get());
                            statusLabel.setText("Units updated.");
                        } catch (Exception ex) {
                            Throwable cause = ex.getCause() != null
                                    ? ex.getCause()
                                    : ex;

                            showError(cause.getMessage() != null
                                    ? cause.getMessage()
                                    : "Unable to change units.");
                        }
                    }
                };

        worker.execute();
    }

    private void displayWeather(WeatherData.WeatherBundle bundle) {
        WeatherData.Current current = bundle.getCurrent();

        String country = current.getLocation().getCountry();
        String locationText = current.getLocation().getName();

        if (!country.isBlank()) {
            locationText += ", " + country;
        }

        locationLabel.setText(locationText);

        String temperatureUnit = isMetric() ? "°C" : "°F";
        String windUnit = isMetric() ? "m/s" : "mph";

        temperatureLabel.setText(
                String.format("%.1f%s", current.getTemperature(), temperatureUnit)
        );

        conditionLabel.setText(capitalize(current.getDescription()));

        feelsLikeLabel.setText(
                String.format(
                        "Feels like: %.1f%s",
                        current.getFeelsLike(),
                        temperatureUnit
                )
        );

        humidityLabel.setText(
                "Humidity: " + current.getHumidity() + "%"
        );

        windLabel.setText(
                String.format(
                        "Wind: %.1f %s",
                        current.getWindSpeed(),
                        windUnit
                )
        );

        coordinatesLabel.setText(
                String.format(
                        "Coordinates: %.3f, %.3f",
                        current.getLocation().getLatitude(),
                        current.getLocation().getLongitude()
                )
        );

        ImageIconWithFallback icon =
                loadIconWithFallback(current.getIcon(), 100);

        currentIconLabel.setIcon(icon.icon());
        currentIconLabel.setText(icon.fallbackText());

        int localHour = getLocalHour(current.getTimezoneOffset());
        backgroundPanel.setLocalHour(localHour);

        updateForecast(bundle.getForecast(), current.getTimezoneOffset());
    }

    private void updateForecast(
            List<WeatherData.ForecastItem> forecast,
            int timezoneOffset) {

        if (forecastPanel == null) {
            return;
        }

        int count = Math.min(6, forecast.size());

        for (int i = 0; i < forecastPanel.getComponentCount(); i++) {
            JPanel card = (JPanel) forecastPanel.getComponent(i);

            if (i >= count) {
                clearForecastCard(card);
                continue;
            }

            WeatherData.ForecastItem item = forecast.get(i);

            JLabel time = findLabel(card, "forecastTime");
            JLabel icon = findLabel(card, "forecastIcon");
            JLabel temp = findLabel(card, "forecastTemp");
            JLabel condition = findLabel(card, "forecastCondition");
            JLabel rain = findLabel(card, "forecastRain");

            ZonedDateTime dateTime =
                    Instant.ofEpochSecond(item.getTimestamp())
                            .plusSeconds(timezoneOffset)
                            .atZone(ZoneOffset.UTC);

            time.setText(
                    dateTime.format(DateTimeFormatter.ofPattern("EEE HH:mm"))
            );

            ImageIconWithFallback weatherIcon =
                    loadIconWithFallback(item.getIcon(), 60);

            icon.setIcon(weatherIcon.icon());
            icon.setText(weatherIcon.fallbackText());

            String temperatureUnit = isMetric() ? "°C" : "°F";

            temp.setText(
                    String.format("%.1f%s", item.getTemperature(), temperatureUnit)
            );

            condition.setText(
                    "<html><center>" +
                            capitalize(item.getDescription()) +
                            "</center></html>"
            );

            rain.setText(
                    "Rain: " + item.getPrecipitationProbability() + "%"
            );
        }

        forecastPanel.revalidate();
        forecastPanel.repaint();
    }

    private void clearForecastCard(JPanel card) {
        findLabel(card, "forecastTime").setText("--:--");
        findLabel(card, "forecastIcon").setIcon(null);
        findLabel(card, "forecastIcon").setText("☁");
        findLabel(card, "forecastTemp").setText("--°");
        findLabel(card, "forecastCondition").setText("—");
        findLabel(card, "forecastRain").setText("Rain: --");
    }

    private ImageIconWithFallback loadIconWithFallback(
            String iconCode,
            int size) {

        var icon = WeatherIconUtil.load(iconCode, size);

        if (icon != null) {
            return new ImageIconWithFallback(icon, "");
        }

        // Text fallback keeps the GUI useful if image hosting is unavailable.
        return new ImageIconWithFallback(null, weatherSymbol(iconCode));
    }

    private int getLocalHour(int timezoneOffset) {
        return Instant.now()
                .plusSeconds(timezoneOffset)
                .atZone(ZoneOffset.UTC)
                .getHour();
    }

    private void showHistoryDialog() {
        JDialog dialog = new JDialog(this, "Recent Weather Searches", true);
        dialog.setSize(650, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        String[] columns = {
                "Timestamp",
                "City",
                "Latitude",
                "Longitude"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (SearchHistory.HistoryEntry entry : searchHistory.getEntries()) {
            model.addRow(new Object[]{
                    entry.timestamp(),
                    entry.city(),
                    String.format("%.4f", entry.latitude()),
                    String.format("%.4f", entry.longitude())
            });
        }

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(closeButton);

        dialog.add(buttons, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private String getSelectedUnits() {
        return isMetric() ? "metric" : "imperial";
    }

    private boolean isMetric() {
        return METRIC.equals(unitCombo.getSelectedItem());
    }

    private void showError(String message) {
        statusLabel.setText("Error");
        JOptionPane.showMessageDialog(
                this,
                message,
                "Weather App Error",
                JOptionPane.ERROR_MESSAGE
        );
        searchButton.setEnabled(true);
    }

    private static String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        return Character.toUpperCase(value.charAt(0))
                + value.substring(1);
    }

    private static String weatherSymbol(String iconCode) {
        if (iconCode == null) {
            return "☁";
        }

        if (iconCode.startsWith("01")) {
            return iconCode.endsWith("n") ? "☾" : "☀";
        }

        if (iconCode.startsWith("02")) {
            return "🌤";
        }

        if (iconCode.startsWith("03") || iconCode.startsWith("04")) {
            return "☁";
        }

        if (iconCode.startsWith("09") || iconCode.startsWith("10")) {
            return "🌧";
        }

        if (iconCode.startsWith("11")) {
            return "⛈";
        }

        if (iconCode.startsWith("13")) {
            return "❄";
        }

        if (iconCode.startsWith("50")) {
            return "🌫";
        }

        return "☁";
    }

    private static JLabel findLabel(JPanel root, String name) {
        if (root.getName() != null && root.getName().equals(name)) {
            return (JLabel) root;
        }

        for (java.awt.Component component : root.getComponents()) {
            if (component instanceof JLabel label
                    && name.equals(label.getName())) {
                return label;
            }

            if (component instanceof JPanel panel) {
                JLabel found = findLabel(panel, name);
                if (found != null) {
                    return found;
                }
            }
        }

        throw new IllegalStateException("Label not found: " + name);
    }

    private static JPanel findForecastPanel(JPanel root) {
        for (java.awt.Component component : root.getComponents()) {
            if (component instanceof JPanel panel) {
                if ("forecastPanel".equals(panel.getName())) {
                    return panel;
                }

                JPanel nested = findForecastPanel(panel);
                if (nested != null) {
                    return nested;
                }
            }
        }

        throw new IllegalStateException("Forecast panel not found.");
    }

    private static JTextField findLocationField(JPanel root) {
        for (java.awt.Component component : root.getComponents()) {
            if (component instanceof JTextField field
                    && "locationField".equals(field.getName())) {
                return field;
            }

            if (component instanceof JPanel panel) {
                JTextField found = findLocationField(panel);
                if (found != null) {
                    return found;
                }
            }
        }

        throw new IllegalStateException("Location field not found.");
    }

    private static JButton findSearchButton(JPanel root) {
        for (java.awt.Component component : root.getComponents()) {
            if (component instanceof JButton button
                    && "searchButton".equals(button.getName())) {
                return button;
            }

            if (component instanceof JPanel panel) {
                JButton found = findSearchButton(panel);
                if (found != null) {
                    return found;
                }
            }
        }

        throw new IllegalStateException("Search button not found.");
    }

    @SuppressWarnings("unchecked")
    private static JComboBox<String> findUnitCombo(JPanel root) {
        for (java.awt.Component component : root.getComponents()) {
            if (component instanceof JComboBox<?> combo
                    && "unitCombo".equals(combo.getName())) {
                return (JComboBox<String>) combo;
            }

            if (component instanceof JPanel panel) {
                JComboBox<String> found = findUnitCombo(panel);
                if (found != null) {
                    return found;
                }
            }
        }

        throw new IllegalStateException("Unit combo box not found.");
    }

    private static JLabel findStatusLabel(JPanel root) {
        for (java.awt.Component component : root.getComponents()) {
            if (component instanceof JLabel label
                    && "statusLabel".equals(label.getName())) {
                return label;
            }
        }

        throw new IllegalStateException("Status label not found.");
    }

    private record ImageIconWithFallback(
            javax.swing.ImageIcon icon,
            String fallbackText) {
    }
}
