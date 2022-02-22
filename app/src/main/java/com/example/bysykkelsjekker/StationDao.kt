package com.example.bysykkelsjekker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station: Station)

    @Query("UPDATE station SET " +
            "available_bikes = :currentBikes, available_parking = :currentParking " +
            "WHERE station_id = :station_id")
    suspend fun updateStation(currentBikes: Int?, currentParking: Int?, station_id: String?)

    @Query("SELECT * FROM station")
    suspend fun getAll(): List<Station>

    @Query("SELECT * FROM station ORDER BY name")
    suspend fun getLexicographicOrder(): List<Station>

    @Query("SELECT * FROM station WHERE name LIKE '%' || :input || '%'")
    suspend fun findByName(input: String): List<Station>

    @Query("SELECT * FROM station WHERE station_id = :stationId")
    suspend fun findById(stationId: String?): Station
}