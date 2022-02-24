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
    private var lastUpdated = MutableLiveData<String>()

    fun loadStations() {
        viewModelScope.launch(Dispatchers.IO) {
            datasource.fetchInformation()
        }
    }

    fun updateStations() {
        viewModelScope.launch(Dispatchers.IO) {
            lastUpdated.postValue(datasource.fetchRealTimeData())
        }
    }

    fun getLastUpdated(): LiveData<String> {
        return lastUpdated
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