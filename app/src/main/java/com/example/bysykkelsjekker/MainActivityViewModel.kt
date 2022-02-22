package com.example.bysykkelsjekker

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    // database
    private val stationDao = initiateDataBase()
    private val datasource = Datasource(stationDao)

    fun loadStations() {
        viewModelScope.launch(Dispatchers.IO) {
            datasource.fetchInformation()
        }
    }

    fun updateStations() {
        viewModelScope.launch(Dispatchers.IO) {
            datasource.fetchRealTimeData()
        }
    }

    suspend fun getLexicographicOrder(): List<Station> {
        return stationDao.getLexicographicOrder()
    }

    private fun initiateDataBase(): StationDao {
        val db = Room.databaseBuilder(
            getApplication(),
            AppDatabase::class.java, "stations"
        ).fallbackToDestructiveMigration().build()
        return db.stationDao()
    }
}