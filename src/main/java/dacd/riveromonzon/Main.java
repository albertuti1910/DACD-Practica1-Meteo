package dacd.riveromonzon;

import dacd.riveromonzon.practice1.model.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		//TODO: Load locations from file
		List<Location> locations = loadLocationsFromFile("locations.tsv");

		//TODO: Create controllers
		//TODO: Create periodical task
		//TODO: Execute periodical task
	}

	private static List<Location> loadLocationsFromFile(String filePath) {
		List<Location> locations = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
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