package com.fluper.seeway.panels.passenger.bindingutils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.panels.passenger.ui.adapter.SavedPlaceAdapter
import com.fluper.seeway.panels.passenger.ui.adapter.LocationsAdapter

class RecyclerBindingAdapter {



    companion object{

        @BindingAdapter("layoutmanager")
        @JvmStatic
        fun setlayoutmaager(recyclerView: RecyclerView, isHorizontal: Boolean) {
            recyclerView.layoutManager = LinearLayoutManager(
                recyclerView.context,
                if (isHorizontal) LinearLayoutManager.HORIZONTAL else LinearLayoutManager.VERTICAL,
                false
            )
        }

        @BindingAdapter( "locationsItems")
        @JvmStatic
        fun setRecyclerLocationsData(recyclerView: RecyclerView, array:ArrayList<String>) {
            var adapter = recyclerView.adapter

            if (adapter == null) {
                adapter =LocationsAdapter(array)
            } else {
                adapter.notifyDataSetChanged()
            }
            recyclerView.adapter = adapter
        }


        @BindingAdapter( "savedLocationItems")
        @JvmStatic
        fun setRecyclerSavedlocationData(recyclerView: RecyclerView, array:ArrayList<String>) {
            var adapter = recyclerView.adapter

            if (adapter == null) {
                adapter =SavedPlaceAdapter(array)
            } else {
                adapter.notifyDataSetChanged()
            }
            recyclerView.adapter = adapter
        }
    }


}