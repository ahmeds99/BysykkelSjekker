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

    fun getLexicographicOrder(): List<Station> {
        var dataset = listOf<Station>()
        viewModelScope.launch(Dispatchers.IO) {
            dataset = stationDao.getLexicographicOrder()
        }
        return dataset
    }

    private fun initiateDataBase(): StationDao {
        val db = Room.databaseBuilder(
            getApplication(),
            AppDatabase::class.java, "stations"
        ).fallbackToDestructiveMigration().build()
        return db.stationDao()
    }
}