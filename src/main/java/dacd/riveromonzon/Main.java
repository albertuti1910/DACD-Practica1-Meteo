package dacd.riveromonzon;

import dacd.riveromonzon.practice1.control.*;
import dacd.riveromonzon.practice1.model.Location;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		List<Location> locations = loadLocationsFromFile("/locations.tsv");

		WeatherProvider weatherProvider = new OpenWeatherMapProvider(args[0], Integer.parseInt(args[1]));
		WeatherStore weatherStore = new SQLiteWeatherStore();

		for (Location location : locations) {
			WeatherController controller = new WeatherController(location, Integer.parseInt(args[2]), weatherProvider, weatherStore);

			controller.execute();
		}
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