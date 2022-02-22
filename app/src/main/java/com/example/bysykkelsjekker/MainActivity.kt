package com.example.bysykkelsjekker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.bysykkelsjekker.adapter.ItemAdapter
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.loadStations()
        viewModel.updateStations()

        // Maybe refactor to ViewBinding
        //val lastUpdatedText = findViewById<TextView>(R.id.last_updated)

        val myDataset = viewModel.getLexicographicOrder()

        val adapter = ItemAdapter(this, myDataset)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

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
