package dacd.riveromonzon.practice1.model;

public class Location {
	private final String name;
	private final Float latitude;
	private final Float longitude;

	public Location(String name, Float latitude, Float longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public Float getLatitude() {
		return latitude;
	}

	public Float getLongitude() {
		return longitude;
	}
}
