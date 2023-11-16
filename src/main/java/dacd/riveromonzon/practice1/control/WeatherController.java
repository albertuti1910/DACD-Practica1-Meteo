package dacd.riveromonzon.practice1.control;

import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherController {
	private final Location location;
	private final Integer daysForecasted;
	private final WeatherProvider weatherProvider;
	private final WeatherStore weatherStore;
	private Timer timer;

	public WeatherController(Location location, int daysForecasted, WeatherProvider weatherProvider, WeatherStore weatherStore, Timer timer) {
		this.location = location;
		this.daysForecasted = daysForecasted;
		this.weatherProvider = weatherProvider;
		this.weatherStore = weatherStore;
		this.timer = new Timer();
	}

	public void execute(Instant timeStamp) {
		TimerTask task = new TimerTask() {
			public void run() {
				executeLogic(timeStamp);
			}
		};

		long period = 6 * 60 * 60 * 1000;
		timer.scheduleAtFixedRate(task, 0, period);
	}

	private void executeLogic(Instant timeStamp) {
		SQLiteWeatherStore.main();
		for (int day = 0; day < daysForecasted; day++) {
			Weather weatherData = weatherProvider.getWeatherData(location, timeStamp);
			weatherStore.storeWeatherData(weatherData);
			timeStamp = timeStamp.plus(Duration.ofDays(1));
		}
	}
}
