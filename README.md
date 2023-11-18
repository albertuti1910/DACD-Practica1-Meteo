# Weather Forecast Using OpenWeatherMap API

## Cover Page

- **Title:** Practice 1 - Data capture from external sources
- **Course:** Application Development for Data Science
- **Academic Year:** 2023-24
- **Degree:** Bachelor's Degree in Data Science and Engineering
- **School:** School of Computer Engineering
- **University:** University of Las Palmas de Gran Canaria
- **Author:** Alberto Rivero Monzón

## Functionality Summary

This project develops a Java application for real-time weather data processing and visualization using the OpenWeatherMap API. It involves fetching weather data for multiple locations, which are specified in a TSV file located in the resources folder. The application periodically retrieves weather data from the API, processes it, and stores it in a SQLite database for persistence. A Swing-based GUI displays the progress of data fetching and storage for every invidual location.

To run the project, the application expects command-line arguments for the OpenWeatherMap API key and other configurations. The argument format is the following: `[API Key] [API Forecast Hour Request] [Number of Days]`
- `API Key` : Your OpenWeatherMap API key.
- `API Forecast Hour Request` : The desired hour to get the data of each location (e.g. "12" - will get you how the weather forecast is at 12:00).
- `Number of Days` : Number of days to get forecast weather data of (e.g. "3" - will get you the forecast for 3 days). [Limited to 5 due to OpenWeatherMap limitations.]

The application reads locations from a TSV file named locations.tsv.
Ensure this file is in the correct path (/locations.tsv) and formatted properly. Each line should contain a location's name, latitude, and longitude, separated by tabs.

For each location in the TSV file, the application creates a WeatherController instance.
The WeatherController fetches weather data for the specified number of forecast days at the specified interval.
The data is then stored in a SQLite database. If there is no prior data stored, the application will INSERT the values into the database. On the other hand, if there are existing weather timestamp values, the application will UPDATE these values, storing the most recent predictions. After the code runs for several iterations (several hours), the database will start growing in size due to the INSERT of new values.

The ProgressGUI displays the progress of weather data fetching and storage.
It shows real-time updates on which location is being processed and the overall progress.

**Additional notes**
- Ensure that the locations.tsv file is accessible at the specified path and contains valid data.
- Adjust the file path or other configurations as needed to fit your environment or requirements.
- Remember to replace placeholders like the API key with actual values.


## Resources Used

- **Development Environments:** IntelliJ IDEA
- **Version Control Tools:** Git, GitHub
- **Documentation Tools:** GitHub text editor

## Design

### Design Patterns and Principles Used

The project employs several design patterns and principles:
- **MVC (Model-View-Controller) Pattern:** For organizing the application logic, GUI, and control flow.
- **Singleton Pattern:** Used for database connection management.
- **Observer Pattern:** Potentially utilized for updating the GUI based on changes in data fetching and processing.
- **SOLID Principles:** Ensuring the software design is robust, scalable, and maintainable.

### Class Diagram

![image](https://github.com/albertuti1910/DACD-Practica1-Meteo/blob/master/image.png)

### Dependency Relations

The application consists of several key classes:
- `Main`: Initializes the application and loads locations from the TSV file.
- `WeatherController`: Manages the fetching and storage of weather data for each location.
- `WeatherProvider`: Interfaces with the OpenWeatherMap API for data retrieval.
- `WeatherStore`: Handles the storage of weather data in the SQLite database.
- `ProgressGUI`: Displays real-time progress and status updates in the GUI.
- `Location`: Represents the data model for a geographical location.
- `Weather`: Represents the structure of the weather data (such as temperature, humidity, etc.) retrieved from the API. It is used by `WeatherStore` for storing the weather data in the SQLite database and by `WeatherController` to process and transfer data between `WeatherProvider` and `WeatherStore`.


Dependencies are primarily centered around the `WeatherController`, which orchestrates the main operations of the application, relying on services provided by `WeatherProvider` and `WeatherStore`, and updating the `ProgressGUI`.
