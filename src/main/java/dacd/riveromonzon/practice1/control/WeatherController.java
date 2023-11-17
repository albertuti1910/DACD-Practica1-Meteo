package dacd.riveromonzon.practice1.control;

import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;
import dacd.riveromonzon.practice1.view.ProgressGUI;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherController {
	private final Location location;
	private final Integer daysForecasted;
	private final WeatherProvider weatherProvider;
	private final WeatherStore weatherStore;
	private final Timer timer;
	private int completedTasks;
	private final Object lock = new Object();
	private final ProgressGUI progressGUI;

	public WeatherController(Location location, int daysForecasted, WeatherProvider weatherProvider, WeatherStore weatherStore, Timer timer, ProgressGUI progressGUI) {
		this.location = location;
		this.daysForecasted = daysForecasted;
		this.weatherProvider = weatherProvider;
		this.weatherStore = weatherStore;
		this.timer = timer;
		this.completedTasks = 0;
		this.progressGUI = progressGUI;
	}

	public void execute(Instant timeStamp) {
		TimerTask task = new TimerTask() {
			public void run() {
				executeLogic(timeStamp);
			}
		};

		long period = 6 * 60 * 60 * 1000;
		timer.scheduleAtFixedRate(task, 0, period);

		resetProgress();
	}

	private void executeLogic(Instant timeStamp) {
		SQLiteWeatherStore.createOrInitialise();
		for (int day = 0; day < daysForecasted; day++) {
			Weather weatherData = weatherProvider.getWeatherData(location, timeStamp);
			weatherStore.storeWeatherData(weatherData);
			timeStamp = timeStamp.plus(Duration.ofDays(1));

			signalCompletion();
		}
	}

	private void signalCompletion() {
		synchronized (lock) {
			completedTasks++;
			if (progressGUI != null) {
				int progress = (completedTasks * 100) / daysForecasted;
				progressGUI.updateProgress(progress, "Processing: " + location.getName());
			}
			if (completedTasks >= daysForecasted) {
				resetProgress();
			}
		}
	}

	private void resetProgress() {
		synchronized (lock) {
			completedTasks = 0;
		}
	}
}
