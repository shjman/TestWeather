package com.shjman.testweather.data

data class Location(
    val key: String,
    val coordinateLatLon: CoordinateLatLon,
)

data class CoordinateLatLon(
    val lat: String,
    val lon: String,
)

data class LocationLocalModel(
    val key: String,
    val coordinateLatLon: String,
)

fun Location.toLocalModel(): LocationLocalModel {
    return LocationLocalModel(
        key = key,
        coordinateLatLon = coordinateLatLon.lat + "$" + coordinateLatLon.lon,
    )
}
