package dacd.riveromonzon.practice1.control;

import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherController {
	private final Location location;
	private final Integer daysForecasted;
	private final WeatherProvider weatherProvider;
	private final WeatherStore weatherStore;

	public WeatherController(Location location, int daysForecasted, WeatherProvider weatherProvider, WeatherStore weatherStore) {
		this.location = location;
		this.daysForecasted = daysForecasted;
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
		SQLiteWeatherStore.main();
		for (int day = 0; day < daysForecasted; day++) {
			System.out.println("Timestamp for day " + (day + 1) + ": " + timeStamp);
			Weather weatherData = weatherProvider.getWeatherData(location, timeStamp);
			//weatherStore.storeWeatherData(weatherData, location, timeStamp);
			weatherStore.storeWeatherData(weatherData);
			timeStamp = timeStamp.plus(Duration.ofDays(1));
		}
	}
}
