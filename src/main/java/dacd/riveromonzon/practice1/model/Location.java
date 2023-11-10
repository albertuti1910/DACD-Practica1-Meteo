package dacd.riveromonzon.practice1.model;

public class Location {
	private String name;
	private Float latitude;
	private Float longitude;

	public Location(String name, Float latitude, Float longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
