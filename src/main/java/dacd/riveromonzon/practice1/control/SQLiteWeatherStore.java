package dacd.riveromonzon.practice1.control;

import dacd.riveromonzon.Main;
import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;

import java.io.File;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SQLiteWeatherStore implements WeatherStore {
	private static final String DATABASE_PATH = "storage/weather.db";

	public static void main() {
		createDatabaseIfNotExists();
		initializeDatabase();
	}

	private static void createDatabaseIfNotExists() {
		File directory = new File("storage");
		if (!directory.exists()) {
			if (directory.mkdirs()) {
				System.out.println("Directory 'storage' created.");
			} else {
				throw new RuntimeException("Failed to create 'storage' directory.");
			}
		}
	}

	private static void initializeDatabase() {
		try (Connection connection = connect()) {
			Statement statement = connection.createStatement();
			List<Location> locations = Main.loadLocationsFromFile("/locations.tsv");
			createWeatherDataTable(statement, locations);
		} catch (SQLException e) {
			throw new RuntimeException("Error initializing the database", e);
		}
	}

	private static Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
	}

	private static void createWeatherDataTable(Statement statement, List<Location> locations) throws SQLException {
		for (Location location : locations) {
			String tableName = getSafeTableName(location.getName());
			String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + "_weather_data (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"temperature REAL," +
					"rain REAL," +
					"humidity INTEGER," +
					"clouds INTEGER," +
					"wind_speed REAL," +
					"weather_timestamp TIMESTAMP UNIQUE," +
					"store_timestamp TIMESTAMP)";

			statement.execute(createTableSQL);
		}
	}

	private static String getSafeTableName(String tableName) {
		return tableName.replaceAll("[^a-zA-Z0-9]", "_");
	}

	@Override
	public void storeWeatherData(Weather weather) {
		Location location = weather.getLocation();
		String tableName = getSafeTableName(location.getName());
		Instant nowTimeStamp = Instant.now();
		updateOrInsertWeatherData(tableName, weather, nowTimeStamp);
	}

	private String formatTimestamp(Instant timestamp) {
		return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(timestamp.atZone(ZoneId.of("UTC")));
	}

	private void updateOrInsertWeatherData(String tableName, Weather weather, Instant storeInstant) {
		String insertSQL = "INSERT OR REPLACE INTO " + tableName + "_weather_data " +
				"(temperature, rain, humidity, clouds, wind_speed, weather_timestamp, store_timestamp) " +
				"VALUES (ROUND(?,2), ?, ?, ?, ROUND(?,2), ?, ?)";

		try (Connection connection = connect();
			 PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

			preparedStatement.setDouble(1, weather.getTemperature());
			preparedStatement.setFloat(2, weather.getRain());
			preparedStatement.setInt(3, weather.getHumidity());
			preparedStatement.setInt(4, weather.getClouds());
			preparedStatement.setDouble(5, weather.getWindSpeed());
			preparedStatement.setString(6, formatTimestamp(weather.getTimeStamp()));
			preparedStatement.setString(7, formatTimestamp(storeInstant));

			preparedStatement.executeUpdate();
			System.out.println("Stored");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
