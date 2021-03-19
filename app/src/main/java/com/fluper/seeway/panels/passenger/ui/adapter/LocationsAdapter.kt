package com.fluper.seeway.panels.passenger.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R

const val SET_MAP_VIEW = 1
const val SET_PLACE_VIEW = 2

class LocationsAdapter(private val list: ArrayList<String>) : RecyclerView.Adapter<LocationsAdapter.LocationCiewHolder>() {

    inner class LocationCiewHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationCiewHolder {

        val view: View;
        if (viewType.equals(SET_PLACE_VIEW)) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_locations, parent, false)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_locations_using_map, parent, false)
        }
        return LocationCiewHolder(view)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: LocationCiewHolder, position: Int) {

    }
    override fun getItemViewType(position: Int): Int {
        if (list.size-1 == position) {
            return SET_MAP_VIEW
        }
        return SET_PLACE_VIEW
    }
}