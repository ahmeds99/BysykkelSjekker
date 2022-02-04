package com.example.bysykkelsjekker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.bysykkelsjekker.adapter.ItemAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://gbfs.urbansharing.com/oslobysykkel.no/station_information.json"
        val realTimeData = "https://gbfs.urbansharing.com/oslobysykkel.no/station_status.json"
        val gson = Gson()

        // Maybe refactor to ViewBinding
        //val lastUpdatedText = findViewById<TextView>(R.id.last_updated)

        // Database
        val stationDao = initiateDataBase()
        fetchInformation(url, gson, stationDao)
        fetchRealTimeData(realTimeData, gson, stationDao)

        var myDataset = listOf<Station>()
        runBlocking {
            launch {
                myDataset = stationDao.getLexicographicOrder()
            }
        }

        val adapter = ItemAdapter(this, myDataset)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        // Only for testing purposes
        runBlocking {
            launch {
                //testStation(stationDao)
                val stationTest = stationDao.findByName("akersgata")
                Log.d("DB TEST", stationTest.toString())
            }
        }

        val stationInput = findViewById<SearchView>(R.id.station_search)

        stationInput.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        // TODO: Create personal buttons that goes to activity which display three most nearby stations
        // TODO: Refactor (model for data classes), and create methods instead of everything in main
    }

    // Only for testing
    /*
    private suspend fun testStation(stationDao: StationDao) {
        val test = Station("1", "testStasjon", "blindern 1", 200.0, 100.0, 4, 2, 1)
        stationDao.insertStation(test)
        val testStation = stationDao.findById("1")
        testStation.address?.let { Log.d("TEST TEST DB", it) }

        val alleStasjoner = stationDao.findByName("oslo")

        for (stasjon in alleStasjoner) {
            stasjon.name?.let { Log.d("AlleStasjoner", it) }
        }
    }
     */

    private fun showToast(text: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }

    private fun initiateDataBase(): StationDao {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "stations"
        ).fallbackToDestructiveMigration().build()
        return db.stationDao()
    }
}

fun fetchRealTimeData(url: String, gson: Gson, stationDao: StationDao) {
    runBlocking {
        launch {
            try {
                val response = gson.fromJson(Fuel.get(url).awaitString(), Base::class.java)
                val stationList = response.data?.stations
                Log.d("STATIONS REALTIME", stationList.toString())

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
}

fun fetchInformation(url: String, gson: Gson, stationDao: StationDao) {
    runBlocking {
        launch {
            try {
                val response = gson.fromJson(Fuel.get(url).awaitString(), Base::class.java)
                val stationList = response.data?.stations

                if (stationList != null) {
                    for (station in stationList) {
                        station.bicycleLogo = R.drawable.bycycle
                        station.parkingLogo = R.drawable.parking
                        stationDao.insertStation(station)
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
