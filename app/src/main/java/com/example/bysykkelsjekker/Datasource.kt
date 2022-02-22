package com.example.bysykkelsjekker

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

class Datasource(private val stationDao: StationDao) {
    private val path = "https://gbfs.urbansharing.com/oslobysykkel.no"
    private val stationURL = "$path/station_information.json"
    private val realTimeDataURL = "$path/station_status.json"
    private val gson = Gson()

    suspend fun fetchInformation() {
        try {
            val response = gson.fromJson(Fuel.get(stationURL).awaitString(), Base::class.java)
            val stationList = response.data?.stations

            if (stationList != null) {
                for (station in stationList) {
                    station.bicycleLogo = R.drawable.bycycle
                    station.parkingLogo = R.drawable.parking
                    stationDao.insertStation(station)
                }
            }
            fetchRealTimeData()
        } catch (exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
        }
    }

    suspend fun fetchRealTimeData() {
        try {
            val response = gson.fromJson(Fuel.get(realTimeDataURL).awaitString(), Base::class.java)
            val stationList = response.data?.stations

            if (stationList != null) {
                for (station in stationList) {
                    stationDao.updateStation(
                        station.num_bikes_available,
                        station.num_docks_available,
                        station.station_id
                    )
                }
            }
            //val date = constructDate(response.last_updated?.toLong())
            //val update = "Last updated: $date"
            //lastUpdated.text = update
        } catch (exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
        }
    }
}