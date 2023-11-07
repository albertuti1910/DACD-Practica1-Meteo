package dacd.riveromonzon;

import dacd.riveromonzon.practice1.control.*;
import dacd.riveromonzon.practice1.model.Location;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		List<Location> locations = loadLocationsFromFile("/locations.tsv");

		WeatherProvider weatherProvider = new OpenWeatherMapProvider();
		WeatherStore weatherStore = new SQLiteWeatherStore();

		for (Location location : locations) {
			WeatherController controller = new WeatherController(location, 5, weatherProvider, weatherStore);
			controller.execute();
		}
		//TODO: Create periodical task (IN CONTROLLER)
		//TODO: Execute periodical task (IN CONTROLLER)
	}

	public static List<Location> loadLocationsFromFile(String filePath) {
		InputStream resourceAsStream = Main.class.getResourceAsStream(filePath);
		List<Location> locations = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\t");
				if (parts.length == 3) {
					String name = parts[0];
					float latitude = Float.parseFloat(parts[1]);
					float longitude = Float.parseFloat(parts[2]);
					locations.add(new Location(name, latitude, longitude));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return locations;
	}
}