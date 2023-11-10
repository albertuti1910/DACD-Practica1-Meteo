import dacd.riveromonzon.Main;
import dacd.riveromonzon.practice1.control.OpenWeatherMapProvider;
import dacd.riveromonzon.practice1.control.WeatherProvider;
import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class Tests {
	@Test
	public void testLocations() {
		String filePath = "/locations.tsv";
		List<Location> locations = Main.loadLocationsFromFile(filePath);
		assertNotNull("Locations should not be null", locations);

		for (Location location : locations) {
			System.out.println("Name: " + location.getName() + ", Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
		}
	}
}
