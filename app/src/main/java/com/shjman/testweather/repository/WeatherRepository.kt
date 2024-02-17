package com.shjman.testweather.repository

import com.shjman.testweather.data.CoordinateLatLon
import com.shjman.testweather.data.DetailedDayForecastPresentModel
import com.shjman.testweather.data.Forecast


interface WeatherRepository {
    suspend fun getForecast(coordinateLatLon: CoordinateLatLon): Forecast
    suspend fun getDetailedForecast(coordinateLatLon: CoordinateLatLon, necessaryDate: String): DetailedDayForecastPresentModel
}
