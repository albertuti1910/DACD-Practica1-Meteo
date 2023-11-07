package dacd.riveromonzon.practice1.control;

import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		executeLogic();
		scheduler.scheduleAtFixedRate(this::executeLogic, 6, 6, TimeUnit.HOURS);
	}

	private void executeLogic() {
		Instant timeStamp = Instant.now();
		for (int day = 0; day < days; day++) {
			Weather weatherData = weatherProvider.getWeatherData(location, timeStamp);
			weatherStore.storeWeatherData(weatherData, location, timeStamp);
			timeStamp = timeStamp.plusSeconds(24 * 60 * 60); // 24 hours in seconds
		}
	}
}
