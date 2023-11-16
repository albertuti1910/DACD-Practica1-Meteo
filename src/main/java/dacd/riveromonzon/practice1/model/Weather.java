package dacd.riveromonzon.practice1.model;

import java.time.Instant;

public class Weather {
	private final Float temperature;
	private final Float rain;
	private final Integer humidity;
	private final Integer clouds;
	private final Float windSpeed;
	private final Location location;
	private final Instant timeStamp;

	public Weather(Float temperature, Float rain, Integer humidity, Integer clouds, Float windSpeed, Location location, Instant timeStamp) {
		this.temperature = temperature;
		this.rain = rain;
		this.humidity = humidity;
		this.clouds = clouds;
		this.windSpeed = windSpeed;
		this.location = location;
		this.timeStamp = timeStamp;
	}

	public Float getTemperature() {
		return temperature;
	}

	public Float getRain() {
		return rain;
	}

	public Integer getHumidity() {
		return humidity;
	}

	public Integer getClouds() {
		return clouds;
	}

	public Float getWindSpeed() {
		return windSpeed;
	}

	public Location getLocation() {
		return location;
	}

	public Instant getTimeStamp() {
		return timeStamp;
	}
}
