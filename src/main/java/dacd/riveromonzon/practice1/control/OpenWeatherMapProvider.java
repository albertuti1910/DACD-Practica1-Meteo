package dacd.riveromonzon.practice1.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;
import org.jsoup.Jsoup;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;

public class OpenWeatherMapProvider implements WeatherProvider {
	private static final String TEMPLATE_URL = "https://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&appid=%s&units=metric";
	private static final String API_KEY_FILE_PATH = "/api_key.txt";
	private static String API_KEY = readApiKeyFromFile();
	private final int desiredHour;

	public OpenWeatherMapProvider(int desiredHour) {
		this.desiredHour = desiredHour;
	}

	private static String readApiKeyFromFile() {
		InputStream resourceAsStream = OpenWeatherMapProvider.class.getResourceAsStream(API_KEY_FILE_PATH);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
			return br.readLine().trim();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Weather getWeatherData(Location location, Instant timeStamp) {
		String apiUrl = String.format(TEMPLATE_URL, location.getLatitude(), location.getLongitude(), API_KEY);
		try	{
			String jsonResponse = Jsoup.connect(apiUrl).ignoreContentType(true).execute().body();

			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

			if (jsonObject != null) {
				JsonArray list = jsonObject.getAsJsonArray("list");
				for (JsonElement element : list) {
					JsonObject object = element.getAsJsonObject();

					JsonObject mainObject = object.getAsJsonObject("main");
					JsonObject cloudsObject = object.getAsJsonObject("clouds");
					JsonObject windObject = object.getAsJsonObject("wind");

					Float temperature = mainObject.get("temp").getAsFloat();
					Float pop = object.get("pop").getAsFloat();
					Integer humidity = mainObject.get("humidity").getAsInt();
					Integer clouds = cloudsObject.get("all").getAsInt();
					Float windSpeed = windObject.get("speed").getAsFloat();

					Instant forecastTimestamp = Instant.ofEpochSecond(object.get("dt").getAsLong());

					// Check if the forecast is for the desired hour
					if (isDesiredHour(forecastTimestamp)) {
						return new Weather(temperature, pop, humidity, clouds, windSpeed, location, forecastTimestamp);
					}
					return new Weather(temperature, pop, humidity, clouds, windSpeed, location, timeStamp);
				}
			}

		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	private boolean isDesiredHour(Instant timestamp) {
		// Extract the hour from the timestamp and check if it matches the desired hour
		return timestamp.atZone(ZoneId.systemDefault()).toLocalTime().getHour() == desiredHour;
	}
}
