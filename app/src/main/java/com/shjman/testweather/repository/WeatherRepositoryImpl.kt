package com.shjman.testweather.repository

import android.util.Log
import com.shjman.testweather.data.CoordinateLatLon
import com.shjman.testweather.data.DetailedDayForecastPresentModel
import com.shjman.testweather.data.Forecast
import com.shjman.testweather.data.convertInstantToDateString
import com.shjman.testweather.data.toDetailedDayForecast
import com.shjman.testweather.data.toForecast
import com.shjman.testweather.remote.WeatherDataSource


class WeatherRepositoryImpl(
    private val weatherDataSource: WeatherDataSource,
) : WeatherRepository {

    override suspend fun getForecast(
        coordinateLatLon: CoordinateLatLon,
    ): Forecast {
        val forecastRemoteModel = weatherDataSource.getForecast(coordinateLatLon)
        Log.e("aaaa", " remote $forecastRemoteModel")
        return forecastRemoteModel.toForecast()
    }

    override suspend fun getDetailedForecast(
        coordinateLatLon: CoordinateLatLon,
        necessaryDate: String,
    ): DetailedDayForecastPresentModel {
        val forecastRemoteModel = weatherDataSource.getForecast(coordinateLatLon)
        val forecastForDayRemote = forecastRemoteModel.forecastsPerDaysRemote.firstOrNull {
            necessaryDate == it.date.convertInstantToDateString()
        }
        if (forecastForDayRemote != null) {
            return forecastForDayRemote.toDetailedDayForecast()
        } else {
            throw Exception("don't find necessary forecast")
        }
    }
}
