package com.example.bysykkelsjekker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.example.bysykkelsjekker.adapter.ItemAdapter
import com.example.bysykkelsjekker.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadStations()
        viewModel.updateStations()

        val lastUpdatedText = binding.lastUpdated
        viewModel.getLastUpdated().observe(this) {
            lastUpdatedText.text = it
        }

        var myDataset = listOf<Station>()
        runBlocking {
            launch {
                myDataset = viewModel.getLexicographicOrder()
            }
        }

        val adapter = ItemAdapter(this, myDataset)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        val stationInput = binding.stationSearch

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
}

// result generated from /json

data class Base(val last_updated: Number?, val ttl: Number?, val version: String?, val data: Data?)

data class Data(val stations: List<Station>?)
