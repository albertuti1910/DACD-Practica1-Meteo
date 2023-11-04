package dacd.riveromonzon.practice1.control;

import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;

import java.time.Instant;

public interface WeatherStore {
	void storeWeatherData(Weather weather, Location location, Instant timestamp);
}
