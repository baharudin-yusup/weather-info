# Weather Info App

This is a simple weather application that provides current weather conditions and up to 4 days of weather forecast for multiple cities. The application utilizes the OpenWeatherMap API to fetch weather data.

## Project Specifications

- The project is implemented using **Kotlin**.
- Utilizes the **MVVM** design pattern and **Clean Architecture**.

## How to Use

1. Clone the repository:

```shell
git clone https://github.com/baharudin-yusup/weather-info.git
```

2. Open the project in Android Studio.

3. Create a `local.properties` file in the root directory of the project if it doesn't exist.

4. Add your OpenWeather API key to the `local.properties` file as follows:

```properties
API_KEY = your_api_key_here
```

5. Build and run the project on an emulator or a physical device.

## Key Dependencies

This project relies on several essential dependencies and libraries to enhance its functionality:

- `androidx.core:core-ktx` for core Android functionality.
- `androidx.appcompat:appcompat` for application compatibility.
- `com.google.android.material:material` for Material Design components.
- `com.squareup.retrofit2:retrofit` and related libraries for handling API requests and responses.
- `org.jetbrains.kotlinx:kotlinx-coroutines-core` for asynchronous programming.
- `androidx.lifecycle:lifecycle-viewmodel-ktx` for managing UI-related data.
- `androidx.room:room-runtime` for local data storage.
- `com.google.dagger:hilt-android` for dependency injection.
- Other relevant dependencies as specified in the project's `build.gradle` files.

For a comprehensive list of dependencies and versions, please refer to the project's build files.

## Author

Baharudin Yusup

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments

- [OpenWeatherMap](https://openweathermap.org/api) for providing the weather data API.