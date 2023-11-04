package dacd.riveromonzon.practice1.control;

import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;

public class OpenWeatherMapProvider implements WeatherProvider {
	private static final String API_URL_TEMPLATE = "https://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&appid=%s&units=metric";
	private static final String API_KEY_FILE_PATH = "api_key.txt";
	private static String API_KEY = readApiKeyFromFile();

	private static String readApiKeyFromFile() {
		try (BufferedReader br = new BufferedReader(new FileReader(API_KEY_FILE_PATH))) {
			return br.readLine().trim();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public Weather getWeather(Location location, Instant timeStamp) {
		//TODO: request Json from API and scrap desired data.
	}
}
