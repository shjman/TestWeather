package com.shjman.testweather.ui.weather_details


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shjman.testweather.data.CoordinateLatLon
import com.shjman.testweather.data.DetailedDayForecastPresentModel
import com.shjman.testweather.repository.WeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherDetailsViewModel(
    private val coordinate: CoordinateLatLon,
    private val date: String,
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            Log.e("aaaa", "load details for coordinate $coordinate")
            _screenState.value = ScreenState.Loading
            delay(1000)
            try {
                val detailedDayForecast = weatherRepository.getDetailedForecast(coordinate, date)
                Log.e("aaaa", "getDetailedForecast load for coordinate $detailedDayForecast")
                _screenState.value = ScreenState.Success(detailedDayForecast)
            } catch (th: Throwable) {
                _screenState.value = ScreenState.Error(th.toString())
                Log.e("aaaa", " error while load forecast error ==  $th")
            }
        }
    }
}

sealed class ScreenState {
    data object Loading : ScreenState()
    data class Error(val error: String) : ScreenState()
    data class Success(
        val detailedDayForecastPresentModel: DetailedDayForecastPresentModel,
    ) : ScreenState()
}
