package com.example.bysykkelsjekker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bysykkelsjekker.R
import com.example.bysykkelsjekker.Station

class ItemAdapter(
    private val context: Context,
    private val dataset: List<Station>
    ) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.station_name)
        val idTextView: TextView = view.findViewById(R.id.station_id)
        val addressTextView: TextView = view.findViewById(R.id.station_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // New view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.station_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.nameTextView.text = item.name
        val idNum = "# " + item.station_id
        holder.idTextView.text = idNum
        holder.addressTextView.text = item.address
    }

    override fun getItemCount() = dataset.size
}