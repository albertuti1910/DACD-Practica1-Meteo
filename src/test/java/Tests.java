import dacd.riveromonzon.Main;
import dacd.riveromonzon.practice1.model.Location;
import org.junit.Test;

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

	@Test
	public void printWeather() {

	}
}
