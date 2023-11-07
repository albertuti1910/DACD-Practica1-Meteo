package dacd.riveromonzon.practice1.model;

import java.time.Instant;

public class Weather {
	private Float temperature;
	private Float rain;
	private Integer humidity;
	private Integer clouds;
	private Float windSpeed;
	private Location location;
	private Instant timeStamp;

	public Weather(Float temperature, Float rain, Integer humidity, Integer clouds, Float windSpeed, Location location, Instant timeStamp) {
		this.temperature = temperature;
		this.rain = rain;
		this.humidity = humidity;
		this.clouds = clouds;
		this.windSpeed = windSpeed;
		this.location = location;
		this.timeStamp = timeStamp;
	}
}
