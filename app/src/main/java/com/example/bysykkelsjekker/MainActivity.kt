package com.example.bysykkelsjekker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

}

// result generated from /json

data class BaseDto(val last_updated: Number?, val ttl: Number?, val version: String?, val dataDto: DataDto?)

data class DataDto(val stations: List<Station>?)
