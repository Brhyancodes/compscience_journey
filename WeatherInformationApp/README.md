# Weather Information App

A Java Swing desktop application that retrieves real-time weather data using
OpenWeather and displays current conditions, a short-term forecast, weather
icons, unit conversion, search history, and a dynamic background.

## Features

- Java Swing graphical user interface
- Search by city name
- Real-time current weather
- Temperature
- Feels-like temperature
- Humidity
- Wind speed
- Weather condition and description
- OpenWeather weather icons with a text fallback
- Short-term forecast
- Celsius / meters per second
- Fahrenheit / miles per hour
- Invalid-location and API-request error handling
- Search history with timestamps
- History stored in `weather_history.csv`
- Dynamic background for morning, daytime, evening/sunset, and night
- OpenWeather attribution in the GUI
- API key is read from an environment variable rather than hard-coded

## Requirements

- JDK 17 or newer
- Maven 3.9+ recommended
- Internet connection
- An OpenWeather API key

## 1. Get an OpenWeather API key

Create an account and obtain an API key from OpenWeather.

Do NOT put your real API key into Java source code or commit it to GitHub.

## 2. Set the API key

### Windows PowerShell

For the current PowerShell session:

```powershell
$env:OPENWEATHER_API_KEY="YOUR_API_KEY_HERE"
```

Then run the application from the same terminal.

### Windows Command Prompt

```cmd
set OPENWEATHER_API_KEY=YOUR_API_KEY_HERE
```

### macOS / Linux

```bash
export OPENWEATHER_API_KEY="YOUR_API_KEY_HERE"
```

## 3. Open the project in VS Code

Open the folder containing `pom.xml`.

Recommended VS Code extensions:

- Extension Pack for Java
- Maven for Java

VS Code can run this project normally because the GUI uses Java Swing,
which is included with the JDK.

## 4. Run the application

From the VS Code terminal:

```bash
mvn clean compile exec:java
```

Alternatively, compile and run using Maven's Java execution support.

## How to use

1. Enter a city such as `Toronto`, `London`, or `New York`.
2. Click **Search**.
3. The application displays current conditions and a short forecast.
4. Use the unit selector to switch between metric and imperial units.
5. Click **History** to see recent searches and timestamps.
6. Search again to update the location.

## Implementation notes

The application uses the following flow:

1. City name -> OpenWeather Geocoding API
2. Geocoding result -> latitude/longitude
3. Coordinates -> Current Weather API
4. Coordinates -> 5-day / 3-hour Forecast API
5. JSON responses -> Java objects used by the Swing GUI

Network calls are performed in `SwingWorker` background threads so the GUI
does not freeze while waiting for the API.

## Project structure

```text
WeatherInformationApp/
├── pom.xml
├── README.md
├── .gitignore
├── src/
│   └── main/
│       └── java/
│           └── com/example/weatherapp/
│               ├── Main.java
│               ├── WeatherApp.java
│               ├── WeatherData.java
│               ├── WeatherService.java
│               ├── WeatherApiException.java
│               ├── WeatherIconUtil.java
│               ├── WeatherBackgroundPanel.java
│               └── SearchHistory.java
└── weather_history.csv  (created automatically after searches)
```

## Rubric coverage

### Code Style and Readability
- Classes have clear responsibilities.
- Comments explain the main API and GUI sections.
- No unnecessary repeated API code.

### Program Flow and Structure
- Separate model, API service, history, icon, background, and GUI classes.
- Meaningful variable and method names.
- Background threads are used for API operations.

### Output
- Complete Swing GUI.
- Weather information is visible in the application.
- History window is included.

### GUI Design
- City input field
- Search button
- Unit selector
- History button
- Current weather panel
- Forecast cards

### Logic and Computation
- API data is parsed and displayed.
- Temperature and wind units change with the selected unit system.
- Local time is calculated using the API's timezone offset.
- Background changes based on local time.

### API Integration
- Uses OpenWeather Geocoding, Current Weather, and Forecast APIs.
- Retrieves temperature, humidity, wind, conditions, and forecast data.
- API key is supplied through an environment variable.

## Important submission step

Before submitting, take a screenshot of the running GUI showing actual weather
data. The assignment rubric specifically asks for a screenshot of the GUI.

Also submit this README file with the Java source files.

## Academic note

Customize the GUI and comments to reflect your own understanding of the code.
Be prepared to explain how the API request, JSON parsing, Swing GUI, unit
selection, error handling, and history tracking work.
