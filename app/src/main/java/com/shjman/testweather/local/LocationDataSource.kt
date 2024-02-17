package com.shjman.testweather.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.shjman.testweather.data.CoordinateLatLon
import com.shjman.testweather.data.LocationLocalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LocationDataSource {
    suspend fun saveNewLocation(locationLocalModel: LocationLocalModel)
    fun getLocations(): Flow<List<CoordinateLatLon>>
}

class LocationDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : LocationDataSource {
    override suspend fun saveNewLocation(
        locationLocalModel: LocationLocalModel,
    ) {
        dataStore.edit { preferences ->
            val locationKey = stringPreferencesKey(locationLocalModel.key)
            preferences[locationKey] = locationLocalModel.coordinateLatLon
        }
    }

    override fun getLocations(): Flow<List<CoordinateLatLon>> {
        val coordinates = dataStore.data.map { preferences -> preferences.asMap().values.toList() }
            .map { items ->
                items.filterIsInstance<String>()
                    .map { item ->
                        val array = item.split("$").toTypedArray() // todo get localModel and convert it to present/domain model in the domain layer
                        CoordinateLatLon(array[0], array[1])
                    }
            }
        return coordinates
    }
}