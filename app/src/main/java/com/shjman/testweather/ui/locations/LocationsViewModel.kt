package com.shjman.testweather.ui.locations


import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shjman.testweather.R
import com.shjman.testweather.data.CoordinateLatLon
import com.shjman.testweather.data.Location
import com.shjman.testweather.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class LocationsViewModel(
    private val locationRepository: LocationRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CurrentState())
    val state = _state.asStateFlow()

    private val _onCoordinateClick = MutableSharedFlow<CoordinateLatLon>()
    val onCoordinateClick = _onCoordinateClick.asSharedFlow()

    private val _sideEffects = MutableSharedFlow<SideEffect>()
    val sideEffects = _sideEffects.asSharedFlow()

    init {
        locationRepository.getLocations().onEach {// todo handle errors
            _state.value = _state.value.copy(
                locations = it
            )
        }.launchIn(viewModelScope)
    }

    fun onLatChanged(newLat: String) {
        _state.value = _state.value.copy(
            enteredLat = newLat
        )
    }

    fun onLonChanged(newLon: String) {
        _state.value = _state.value.copy(
            enteredLon = newLon
        )
    }

    fun onSaveLocationClick() { // todo handle quick double click . check if already exist this location
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val enteredLat = _state.value.enteredLat.toDoubleOrNull()
                if (enteredLat == null || enteredLat < -90 || enteredLat > 90) {
                    Log.e("aaaa", "enteredLat == null || enteredLat < -90")
                    _sideEffects.emit(SideEffect.ShowError(R.string.wrong_lat))
                    return@withContext
                }
                val enteredLon = _state.value.enteredLon.toDoubleOrNull()
                if (enteredLon == null || enteredLon < -180 || enteredLon > 180) {
                    Log.e("aaaa", "enteredLon == null || enteredLon < -180 || enteredLon > 180")
                    _sideEffects.emit(SideEffect.ShowError(R.string.wrong_lon))
                    return@withContext
                }
                locationRepository.saveNewLocation( // todo add loading .error state. show success message
                    Location(
                        key = Clock.System.now().toString(),
                        coordinateLatLon = CoordinateLatLon(enteredLat.toString(), enteredLon.toString()),
                    )
                )
                Log.e("aaaa", "done locationRepository.saveNewLocation")
            }
        }
    }

    fun onCoordinateClick(coordinateLatLon: CoordinateLatLon) {
        viewModelScope.launch {
            _onCoordinateClick.emit(coordinateLatLon)
        }
    }
}

sealed class SideEffect {
    data class ShowError(
        @StringRes
        val message: Int,
    ) : SideEffect()
}

data class CurrentState(
    val enteredLat: String = "",
    val enteredLon: String = "",
    val locations: List<CoordinateLatLon> = emptyList(),
)
