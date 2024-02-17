package com.shjman.testweather.ui.weather_forecast


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shjman.testweather.data.CoordinateLatLon
import com.shjman.testweather.data.ForecastPerDay
import com.shjman.testweather.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(
    private val coordinate: CoordinateLatLon,
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private val _onDayClick = MutableSharedFlow<String>()
    val onDayClick = _onDayClick.asSharedFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("aaaa", "load for coordinate $coordinate")
                _screenState.value = ScreenState.Loading
//                delay(1000) to show progress
                try {
                    val forecast = weatherRepository.getForecast(coordinate)
                    _screenState.value = ScreenState.Success(forecast.forecastsPerDays)
                } catch (th: Throwable) {
                    _screenState.value = ScreenState.Error(error = th.toString())
                    Log.e("aaaa", " error while load forecast error ==  $th")
                }
            }
        }
    }

    fun onDayClick(date: String) {
        viewModelScope.launch {
            _onDayClick.emit(date)
        }
    }
}

sealed class ScreenState {
    data object Loading : ScreenState()

    data class Error(
        val error: String,
    ) : ScreenState()

    data class Success(
        val forecastsPerDay: List<ForecastPerDay>,
    ) : ScreenState()
}
