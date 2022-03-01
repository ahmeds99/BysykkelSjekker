package com.example.bysykkelsjekker

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class Datasource(private val stationDao: StationDao) {
    private val path = "https://gbfs.urbansharing.com/oslobysykkel.no"
    private val stationURL = "$path/station_information.json"
    private val realTimeDataURL = "$path/station_status.json"
    private val gson = Gson()

    suspend fun fetchInformation() {
        try {
            val response = gson.fromJson(Fuel.get(stationURL).awaitString(), BaseDto::class.java)
            val stationList = response.dataDto?.stations

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

    suspend fun fetchRealTimeData(): String {
        try {
            val response = gson.fromJson(Fuel.get(realTimeDataURL).awaitString(), BaseDto::class.java)
            val stationList = response.dataDto?.stations

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
            val date = constructDateNew(response.last_updated?.toLong())
            return "Last updated: $date"
        } catch (exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
            return ""
        }
    }

    // Change posix/Unix timestamp to readable date-string
    private fun constructDateNew(unixTime: Long?): String? {
        if (unixTime != null) {
            val date = Date(unixTime * 1000)
            val df = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN).format(date)
            return df.toString()
        }
        return null
    }
}