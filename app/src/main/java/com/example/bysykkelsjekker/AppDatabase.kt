package com.example.bysykkelsjekker

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Station::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
}