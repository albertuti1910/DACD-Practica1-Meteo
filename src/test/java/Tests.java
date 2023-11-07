import dacd.riveromonzon.Main;
import dacd.riveromonzon.practice1.model.Location;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class Tests {
	@Test
	public void testLocations() {  //FIXME: Arreglar filepath para unicamente poner locations.tsv en lugar de todo el path.
		String filePath = "/locations.tsv";
		List<Location> locations = Main.loadLocationsFromFile(filePath);
		assertNotNull("Locations should not be null", locations);

		for (Location location : locations) {
			System.out.println("Name: " + location.getName() + ", Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
		}
	}
}
