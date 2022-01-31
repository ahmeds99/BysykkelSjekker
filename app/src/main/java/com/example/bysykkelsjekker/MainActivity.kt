package com.example.bysykkelsjekker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://gbfs.urbansharing.com/oslobysykkel.no/station_information.json"
        val realTimeData = "https://gbfs.urbansharing.com/oslobysykkel.no/station_status.json"
        val gson = Gson()

        val lastUpdatedText = findViewById<TextView>(R.id.last_updated)
        val stationInput = findViewById<EditText>(R.id.station_input)
        val searchButton = findViewById<Button>(R.id.search_station)
        val stationCard = findViewById<TextView>(R.id.current_station)
        val allStations = HashMap<String?, Station>()
        val idMap = HashMap<String?, Station>()

        fetchInformation(url, gson, idMap, allStations)
        fetchRealTimeData(realTimeData, gson, idMap, lastUpdatedText)

        searchButton.setOnClickListener {
            fetchRealTimeData(realTimeData, gson, idMap, lastUpdatedText)
            val input = stationInput.text.toString().lowercase().trim()
            val station = allStations[input]

            try {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (exception: NullPointerException) {}

            if (station != null) {
                val info = station.name + "\nAddress: " + station.address + "\nCapacity: " +
                        station.capacity + "\nBikes available: " + station.num_bikes_available +
                        "\nParking available: " + station.num_docks_available
                stationCard.text = info
            } else {
                val notFound = "Could not find: $input"
                showToast(notFound)
                stationCard.text = ""
            }
        }

        // TODO: Maybe create DB? That lets me have search-functionality
        // TODO: Create personal buttons that goes to activity which display three most nearby stations
        // TODO: Refactor, and create methods instead of everything in main
    }

    private fun showToast(text: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }
}

fun fetchRealTimeData(url: String, gson: Gson, idMap: HashMap<String?, Station>,
                      lastUpdated: TextView) {
    runBlocking {
        launch {
            try {
                val response = gson.fromJson(Fuel.get(url).awaitString(), Base::class.java)
                val stationList = response.data?.stations
                Log.d("STATIONS REALTIME", stationList.toString())

                if (stationList != null) {
                    for (station in stationList) {
                        val curStation = idMap[station.station_id]
                        if (curStation != null) {
                            curStation.num_bikes_available = station.num_bikes_available
                            curStation.num_docks_available = station.num_docks_available
                        }
                    }
                }
                val date = constructDate(response.last_updated?.toLong())
                val update = "Last updated: $date"
                lastUpdated.text = update
            } catch (exception: Exception) {
                println("A network request exception was thrown: ${exception.message}")
            }
        }
    }
}

fun fetchInformation(url: String, gson: Gson,
                     idMap: HashMap<String?, Station>, allStations: HashMap<String?, Station>) {
    runBlocking {
        launch {
            try {
                val response = gson.fromJson(Fuel.get(url).awaitString(), Base::class.java)
                val stationList = response.data?.stations

                if (stationList != null) {
                    for (station in stationList) {
                        // use lowercase for easing the search-process later
                        allStations[station.name?.lowercase()] = station
                        idMap[station.station_id] = station
                    }
                }
            } catch (exception: Exception) {
                println("A network request exception was thrown: ${exception.message}")
            }
        }
    }
}

// Change posix/Unix timestamp to readable date-string
fun constructDate(unixTime: Long?): String? {
    if (unixTime != null) {
        val date = Date(unixTime * 1000)
        val df = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(date)
        val minutes = if (date.minutes < 10) "0" + date.minutes else "" + date.minutes
        return "$df " + date.hours + ":" + minutes
    }
    return null
}

// result generated from /json

data class Base(val last_updated: Number?, val ttl: Number?, val version: String?, val data: Data?)

data class Data(val stations: List<Station>?)

data class RentalUris(val android: String?, val ios: String?)

