package com.example.bysykkelsjekker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Station(
    @PrimaryKey val station_id: String?,
    val name: String?,
    val address: String?,
    val rental_uris: RentalUris?,
    @ColumnInfo(name = "latitude") val lat: Number?,
    @ColumnInfo(name = "longitude") val lon: Number?,
    val capacity: Number?,
    @ColumnInfo(name = "available_bikes") var num_bikes_available: Number?,
    @ColumnInfo(name = "available_parking") var num_docks_available: Number?
    )