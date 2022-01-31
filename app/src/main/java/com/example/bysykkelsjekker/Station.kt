package com.example.bysykkelsjekker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity
data class Station (
    @PrimaryKey val station_id: String,
    val name: String?,
    val address: String?,
    @ColumnInfo(name = "latitude") val lat: Double?,
    @ColumnInfo(name = "longitude") val lon: Double?,
    val capacity: Int?,
    @ColumnInfo(name = "available_bikes") var num_bikes_available: Int?,
    @ColumnInfo(name = "available_parking") var num_docks_available: Int?
)

class Converter {

}