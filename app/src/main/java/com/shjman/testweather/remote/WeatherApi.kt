package com.shjman.testweather.remote

import com.shjman.testweather.data.ForecastResponseRemote
import retrofit2.http.*

interface WeatherApi {

    @GET("data/2.5/forecast/daily?")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: Int,
        @Query("appid") appid: String,
    ): ForecastResponseRemote
}
