package com.example.bysykkelsjekker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bysykkelsjekker.R
import com.example.bysykkelsjekker.Station

class ItemAdapter(
    private val context: Context,
    private var dataset: List<Station>
    ) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(), Filterable {

    private var filteredStations: List<Station> = dataset

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.station_name)
        val idTextView: TextView = view.findViewById(R.id.station_id)
        val addressTextView: TextView = view.findViewById(R.id.station_address)
        val availableBikesTextView: TextView = view.findViewById(R.id.available_bikes)
        val availableParkingTextView: TextView = view.findViewById(R.id.available_parking)
        val bicycleView: ImageView = view.findViewById(R.id.bicycle)
        val parkingView: ImageView = view.findViewById(R.id.parking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // New view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.station_item, parent, false)

        return ItemViewHolder(adapterLayout)
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
                filteredStations = p1?.values as ArrayList<Station>
                notifyDataSetChanged()
            }
        }
    }
}