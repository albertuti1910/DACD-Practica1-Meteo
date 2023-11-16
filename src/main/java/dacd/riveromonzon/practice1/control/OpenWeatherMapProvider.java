package dacd.riveromonzon.practice1.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dacd.riveromonzon.practice1.model.Location;
import dacd.riveromonzon.practice1.model.Weather;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class OpenWeatherMapProvider implements WeatherProvider {
	private static final String TEMPLATE_URL = "https://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&appid=%s&units=metric";
	private final String apiKey;
	private final int desiredHour;

	public OpenWeatherMapProvider(String apiKey, int desiredHour) {
		this.apiKey = apiKey;
		this.desiredHour = desiredHour;
	}

	@Override
	public Weather getWeatherData(Location location, Instant timeStamp) {
		String apiUrl = String.format(TEMPLATE_URL, location.getLatitude(), location.getLongitude(), apiKey);

		try {
			String jsonResponse = Jsoup.connect(apiUrl).ignoreContentType(true).execute().body();
			return parseWeatherData(jsonResponse, location, timeStamp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Weather parseWeatherData(String jsonResponse, Location location, Instant timeStamp) {
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
		JsonArray forecastList = jsonObject.getAsJsonArray("list");

		for (JsonElement element : forecastList) {
			JsonObject forecastEntry = element.getAsJsonObject();
			Instant forecastDateTime = Instant.ofEpochSecond(forecastEntry.get("dt").getAsLong());

			if (isDesiredForecastTime(forecastDateTime, timeStamp)) {
				//Weather weather = extractWeatherData(forecastEntry, location, forecastDateTime);
				return extractWeatherData(forecastEntry, location, forecastDateTime);
			}
		}
		return null;
	}

	private boolean isDesiredForecastTime(Instant forecastDateTime, Instant currentDateTime) {
		LocalDateTime forecastLocalDateTime = LocalDateTime.ofInstant(forecastDateTime, ZoneId.systemDefault());
		LocalDateTime currentLocalDateTime = LocalDateTime.ofInstant(currentDateTime, ZoneId.systemDefault());

		if (currentLocalDateTime.getHour() < desiredHour) {
			return forecastLocalDateTime.toLocalDate().equals(currentLocalDateTime.toLocalDate())
					&& forecastLocalDateTime.getHour() == desiredHour;
		} else {
			return forecastLocalDateTime.toLocalDate().equals(currentLocalDateTime.toLocalDate().plusDays(1))
					&& forecastLocalDateTime.getHour() == desiredHour;
		}
	}

	private Weather extractWeatherData(JsonObject forecastEntry, Location location, Instant timeStamp) {
		JsonObject mainObject = forecastEntry.getAsJsonObject("main");
		JsonObject cloudsObject = forecastEntry.getAsJsonObject("clouds");
		JsonObject windObject = forecastEntry.getAsJsonObject("wind");

		Float temperature = mainObject.get("temp").getAsFloat();
		Float pop = forecastEntry.get("pop").getAsFloat();
		Integer humidity = mainObject.get("humidity").getAsInt();
		Integer clouds = cloudsObject.get("all").getAsInt();
		Float windSpeed = windObject.get("speed").getAsFloat();

		//Weather weather = new Weather(temperature, pop, humidity, clouds, windSpeed, location, timeStamp);
		return new Weather(temperature, pop, humidity, clouds, windSpeed, location, timeStamp);
	}
}
