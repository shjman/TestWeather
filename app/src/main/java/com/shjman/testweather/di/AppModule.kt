package com.shjman.testweather.di

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shjman.testweather.data.CoordinateLatLon
import com.shjman.testweather.local.LocationDataSource
import com.shjman.testweather.local.LocationDataSourceImpl
import com.shjman.testweather.remote.WeatherApi
import com.shjman.testweather.remote.WeatherDataSource
import com.shjman.testweather.remote.WeatherDataSourceImpl
import com.shjman.testweather.repository.LocationRepository
import com.shjman.testweather.repository.LocationRepositoryImpl
import com.shjman.testweather.repository.WeatherRepository
import com.shjman.testweather.repository.WeatherRepositoryImpl
import com.shjman.testweather.ui.locations.LocationsViewModel
import com.shjman.testweather.ui.weather_details.WeatherDetailsViewModel
import com.shjman.testweather.ui.weather_forecast.WeatherViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {

    single<Gson> { GsonBuilder().create() }
    single<WeatherApi> { get<Retrofit>().create(WeatherApi::class.java) }
    single<Retrofit> { provideRetrofit(get()) }
    single { provideDataStore(get()) }
    single<LocationRepository> {
        LocationRepositoryImpl(
            locationDataSource = get(),
        )
    }
    single<WeatherRepository> {
        WeatherRepositoryImpl(
            weatherDataSource = get(),
        )
    }
    single<WeatherDataSource> {
        WeatherDataSourceImpl(
            api = get(),
        )
    }
    single<LocationDataSource> {
        LocationDataSourceImpl(
            dataStore = get(),
        )
    }
    viewModel { LocationsViewModel(get()) }
    viewModel { (coordinate: CoordinateLatLon) -> WeatherViewModel(coordinate, get()) }
    viewModel { (coordinate: CoordinateLatLon, date: String) -> WeatherDetailsViewModel(coordinate, date, get()) }
}

private fun provideDataStore(context: Context) = PreferenceDataStoreFactory.create(
    produceFile = { context.preferencesDataStoreFile("locations") }
)

private fun provideRetrofit(
    gson: Gson,
): Retrofit {

    return Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor { Log.v("interceptor", "it = $it") }
                    .apply { level = HttpLoggingInterceptor.Level.BODY })
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}
