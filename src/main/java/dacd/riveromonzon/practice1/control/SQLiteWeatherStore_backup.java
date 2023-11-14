package dacd.riveromonzon.practice1.control;

import dacd.riveromonzon.Main;
import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;

import java.io.File;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Quitar abstact y verificar override
public abstract class SQLiteWeatherStore_backup implements WeatherStore{
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

	public static Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
	}

	private static void createWeatherDataTable(Statement statement, List<Location> locations) throws SQLException {
		for (Location location : locations) {
			statement.execute("CREATE TABLE IF NOT EXISTS " + getSafeTableName(location.getName()) + "_weather_data (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"temperature REAL," +
					"rain REAL," +
					"humidity INTEGER," +
					"clouds INTEGER," +
					"wind_speed REAL," +
					"timestamp TEXT)");
		}
	}

	private static String getSafeTableName(String tableName) {
		return tableName.replaceAll("[^a-zA-Z0-9]", "_");
	}

	//@Override
	public void storeWeatherData(Weather weather, Location location, Instant timestamp) {
		String tableName = getSafeTableName(location.getName());
		Instant storeTimeStamp = weather.getTimeStamp();

		if (recordExists(tableName, timestamp)) {
			updateWeatherData(tableName, weather, timestamp);
		} else {
			insertWeatherData(tableName, weather, timestamp);
		}
	}

	private String formatTimestamp(Instant timestamp) {
		ZonedDateTime zdt = ZonedDateTime.ofInstant(timestamp, ZoneId.of("UTC"));
		return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").format(zdt);
	}

	private boolean recordExists(String tableName, Instant timestamp) {
		String query = "SELECT COUNT(*) FROM " + tableName + "_weather_forecast WHERE timestamp = ?";
		try (Connection connection = connect();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			String formattedTimestamp = formatTimestamp(timestamp);
			preparedStatement.setString(1, formattedTimestamp);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void updateWeatherData(String tableName, Weather weather, Instant timestamp) {
		String updateSQL = "UPDATE " + tableName + "_weather_data SET " +
				"temperature = ?, rain = ?, humidity = ?, clouds = ?, wind_speed = ? " +
				"WHERE timestamp = ?";

		try (Connection connection = connect();
			 PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

			preparedStatement.setFloat(1, Math.round(weather.getTemperature()));
			preparedStatement.setFloat(2, weather.getRain());
			preparedStatement.setInt(3, weather.getHumidity());
			preparedStatement.setInt(4, weather.getClouds());
			preparedStatement.setFloat(5, weather.getWindSpeed());
			preparedStatement.setString(6, weather.getTimeStamp().toString());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void insertWeatherData(String tableName, Weather weather, Instant timestamp) {
		String insertSQL = "INSERT INTO " + tableName + "_weather_data " +
				"(temperature, rain, humidity, clouds, wind_speed, timestamp) " +
				"VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = connect();
			 PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

			preparedStatement.setFloat(1, weather.getTemperature());
			preparedStatement.setFloat(2, weather.getRain());
			preparedStatement.setInt(3, weather.getHumidity());
			preparedStatement.setInt(4, weather.getClouds());
			preparedStatement.setFloat(5, weather.getWindSpeed());
			preparedStatement.setString(6, weather.getTimeStamp().toString());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
