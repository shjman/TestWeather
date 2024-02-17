package com.shjman.testweather.data

import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


// todo prepare remoteModel domainModel presenterModel
data class ForecastRequest(
    val lat: Double,
    val lon: Double,
    val appid: String,
)

data class ForecastResponseRemote(
    @SerializedName("list") val forecastsPerDaysRemote: List<ForecastForDayRemote>,
)

data class Forecast(
    val forecastsPerDays: List<ForecastPerDay>,
)

fun ForecastResponseRemote.toForecast(): Forecast {
    return Forecast(
        forecastsPerDays = this.forecastsPerDaysRemote.map { it.toForecastPerDay() }
    )
}

data class ForecastPerDay(
    val date: String,
    val temp: Celsius,
    val iconUrl: String,
)

fun ForecastForDayRemote.toDetailedDayForecast(): DetailedDayForecastPresentModel {
    return DetailedDayForecastPresentModel(
        sunrise = this.sunrise.convertInstantToTimeString(),
        sunset = this.sunset.convertInstantToTimeString(),
        humidity = this.humidity,
        iconUrl = convertToIconUrl(this.weathersRemote.first().icon),
        tempDay = convertFromKelvinToCelsius(this.tempRemote.day),
        tempNight = convertFromKelvinToCelsius(this.tempRemote.night),
        tempFeelsLike = convertFromKelvinToCelsius(this.tempRemote.night),
        tempDayFeelsLike = convertFromKelvinToCelsius(this.feelsLikeRemote.day),
        tempNightFeelsLike = convertFromKelvinToCelsius(this.feelsLikeRemote.night),
    )
}

@JvmInline
value class Celsius(private val value: Int)

fun convertFromKelvinToCelsius(kelvin: Double): Celsius = Celsius((kelvin - 273.15).toInt())

fun Long.convertInstantToTimeString(): String {
    val instant: Instant = Instant.fromEpochMilliseconds(this * 1000)
    return instant.toLocalDateTime(TimeZone.UTC).time.toString()
}

fun Long.convertInstantToDateString(): String {
    val instant: Instant = Instant.fromEpochMilliseconds(this * 1000)
    return instant.toLocalDateTime(TimeZone.UTC).date.toString()
}

data class DetailedDayForecastPresentModel(
    val sunrise: String,
    val sunset: String,
    val humidity: Int,
    val iconUrl: String,
    val tempDay: Celsius,
    val tempNight: Celsius,
    val tempFeelsLike: Celsius,
    val tempDayFeelsLike: Celsius,
    val tempNightFeelsLike: Celsius,
)

fun ForecastForDayRemote.toForecastPerDay(): ForecastPerDay {
    return ForecastPerDay(
        date = this.date.convertInstantToDateString(),
        temp = convertFromKelvinToCelsius(this.tempRemote.day),
        iconUrl = convertToIconUrl(this.weathersRemote.first().icon),
    )
}

fun convertToIconUrl(icon: String): String {
    return "https://openweathermap.org/img/wn/${icon}@2x.png"
}

data class ForecastForDayRemote(
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("dt") val date: Long,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long,
    @SerializedName("temp") val tempRemote: TempRemote,
    @SerializedName("feels_like") val feelsLikeRemote: FeelsLikeRemote,
    @SerializedName("weather") val weathersRemote: List<WeatherRemote>,
)

data class FeelsLikeRemote(
    val day: Double,
    val night: Double,
)

data class TempRemote(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double,
)

data class WeatherRemote(
    val icon: String,
)

