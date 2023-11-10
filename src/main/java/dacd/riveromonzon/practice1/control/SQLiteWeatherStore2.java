package dacd.riveromonzon.practice1.control;

import dacd.riveromonzon.practice1.control.WeatherStore;
import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;

public class SQLiteWeatherStore2 implements WeatherStore {
	public void storeWeatherData(Weather weather, Location location, Instant timeStamp) {
		String dbPath = "storage/weather.db";
		new File(dbPath).getParentFile().mkdirs();

		try (Connection connection = connect(dbPath)) {
			// Use PreparedStatement to avoid SQL injection
			String query = "INSERT INTO weather_data (location_name, latitude, longitude, temperature, rain, humidity, clouds, wind_speed, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, location.getName());
				preparedStatement.setDouble(2, location.getLatitude());
				preparedStatement.setDouble(3, location.getLongitude());
				preparedStatement.setFloat(4, weather.getTemperature());
				preparedStatement.setFloat(5, weather.getRain());
				preparedStatement.setInt(6, weather.getHumidity());
				preparedStatement.setInt(7, weather.getClouds());
				preparedStatement.setFloat(8, weather.getWindSpeed());
				preparedStatement.setTimestamp(9, java.sql.Timestamp.from(timeStamp));

				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static Connection connect(String dbPath) {
		try {
			String url = "jdbc:sqlite:" + dbPath;
			return DriverManager.getConnection(url);
		} catch (SQLException e) {
			throw new RuntimeException("Error connecting to SQLite database", e);
		}
	}
}