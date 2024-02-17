Android Developer Test Task
Description
Create a simple application to show weather forecasts for user locations.
Functionality
The application should contain the following screens:
- User locations
- Weather forecast
- Forecast details
User locations
- The user should be able to add any location
- All added locations are shown as a list of locations
- By clicking on the location item, the user navigates to the weather forecast screen
Weather forecast
- Show 7 days (from today) weather forecast for the selected location
- The weather forecast screen shows basic weather information, which includes:
- Forecast date
- Temperature
- Weather icon
- By clicking on the weather item, the user navigates to the weather details screen
Weather details screen
- Show weather details for the selected day
- The weather details screen should show the following information:
- Temperature day, night, feels like
- Weather icon
- Humidity
- Pressure
- UV index
- Sunrise and sunset time
Implementation and design
Architecture and used libraries selected by the developer. No strict requirements for application
design. By navigating back, the user should be returned to the previous screen. By navigating
back to the first application screen (user locations) application should be closed. The application
should work online by using the provided API, offline mode is not required.
API
API key: 4aff0d93fc6fb6fd2fd195632dc9bbc1
Weather API to use:
api.openweathermap.org/data/2.5/forecast/daily?lat={lat}&lon={lon}&cn
t={cnt}&appid={API key}
Weather API documentation:
Daily Forecast 16 Days - OpenWeatherMap
