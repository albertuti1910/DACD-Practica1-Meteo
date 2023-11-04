package dacd.riveromonzon.practice1.control;

import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;

import java.time.Instant;

public interface WeatherProvider {
	Weather getWeather(Location location, Instant timeStamp);
}
