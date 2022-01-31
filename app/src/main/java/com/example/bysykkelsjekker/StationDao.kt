package com.example.bysykkelsjekker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StationDao {
    @Insert
    fun insertStation(station: Station)

    @Update
    fun updateStation(station: Station)

    @Query("SELECT * from station")
    fun getAll(): List<Station>

    @Query("SELECT * from station ORDER BY name")
    fun getLexicographicOrder(): List<Station>

    @Query("SELECT * from station WHERE name LIKE '%:input%'")
    fun findByName(input: String): List<Station>
}