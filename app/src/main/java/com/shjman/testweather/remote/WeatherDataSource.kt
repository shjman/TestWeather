package com.shjman.testweather.remote

import android.util.Log
import com.shjman.testweather.data.CoordinateLatLon
import com.shjman.testweather.data.ForecastResponseRemote

interface WeatherDataSource {
    suspend fun getForecast(
        coordinateLatLon: CoordinateLatLon,
    ): ForecastResponseRemote
}

class WeatherDataSourceImpl(
    private val api: WeatherApi,
) : WeatherDataSource {

    private val cache: MutableMap<CoordinateLatLon, ForecastResponseRemote> = mutableMapOf()

    override suspend fun getForecast(
        coordinateLatLon: CoordinateLatLon,
    ): ForecastResponseRemote {
        val cached = cache.getOrDefault(coordinateLatLon, null) // todo add logic to expire cache
        return if (cached != null) {
            Log.e("aaaa", "loaded from cache")
            cached
        } else {
            val forecastResponse = api.getForecast(
                lat = coordinateLatLon.lat.toDouble(),
                lon = coordinateLatLon.lon.toDouble(),
                appid = "4aff0d93fc6fb6fd2fd195632dc9bbc1",
                cnt = 7, // todo const or dynamic set for 7 days
            )
            cache[coordinateLatLon] = forecastResponse
            forecastResponse
        }
    }
}
