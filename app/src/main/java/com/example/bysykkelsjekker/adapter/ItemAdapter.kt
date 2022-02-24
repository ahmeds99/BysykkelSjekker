package com.example.bysykkelsjekker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bysykkelsjekker.Station
import com.example.bysykkelsjekker.databinding.StationItemBinding

class ItemAdapter(
    private val context: Context,
    private var dataset: List<Station>
    ) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(), Filterable {

    private var filteredStations: List<Station> = dataset

    class ItemViewHolder(binding: StationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameTextView: TextView = binding.stationName
        val idTextView: TextView = binding.stationId
        val addressTextView: TextView = binding.stationAddress
        val availableBikesTextView: TextView = binding.availableBikes
        val availableParkingTextView: TextView = binding.availableParking
        val bicycleView: ImageView = binding.bicycle
        val parkingView: ImageView = binding.parking
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            StationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = filteredStations[position]
        holder.nameTextView.text = item.name
        val idNum = "# " + item.station_id
        holder.idTextView.text = idNum
        holder.addressTextView.text = item.address

        val availableBikes = "Available bikes: " + item.num_bikes_available
        holder.availableBikesTextView.text = availableBikes

        val availableParking = "Available parking: " + item.num_docks_available
        holder.availableParkingTextView.text = availableParking

        holder.bicycleView.setImageResource(item.bicycleLogo)
        holder.parkingView.setImageResource(item.parkingLogo)
    }

    override fun getItemCount() = filteredStations.size

    // Inspiration: https://johncodeos.com/how-to-add-search-in-recyclerview-using-kotlin/
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val search = p0.toString()

                filteredStations = if (search.isEmpty())
                    dataset
                else {
                    // Can this code be sped up? Not noticing signs of slow response-time but still
                    val resultList = ArrayList<Station>()
                    for (station in dataset) {
                        if (station.name?.lowercase()?.contains(search.lowercase()) == true) {
                            resultList.add(station)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredStations
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredStations = if (p1 != null ) p1.values as ArrayList<Station> else emptyList()
                notifyDataSetChanged()
            }
        }
    }
}