package dacd.riveromonzon.practice1.control;

import dacd.riveromonzon.practice1.model.Location;

public class WeatherController {
	private Location location;
	private Integer days;
	private WeatherProvider weatherProvider;
	private WeatherStore weatherStore;

	public WeatherController(Location location, int days, WeatherProvider weatherProvider, WeatherStore weatherStore) {
		this.location = location;
		this.days = days;
		this.weatherProvider = weatherProvider;
		this.weatherStore = weatherStore;
	}

	public void execute() {
	}
}
