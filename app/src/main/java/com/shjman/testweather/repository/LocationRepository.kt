package com.shjman.testweather.repository

import com.shjman.testweather.data.CoordinateLatLon
import com.shjman.testweather.data.Location
import com.shjman.testweather.data.toLocalModel
import com.shjman.testweather.local.LocationDataSource
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun saveNewLocation(location: Location)
    fun getLocations(): Flow<List<CoordinateLatLon>>
}

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource,
) : LocationRepository {

    override suspend fun saveNewLocation(
        location: Location,
    ) {
        locationDataSource.saveNewLocation(location.toLocalModel())
    }

    override fun getLocations(): Flow<List<CoordinateLatLon>> {
        return locationDataSource.getLocations()
    }
}
