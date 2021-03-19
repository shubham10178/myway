package com.fluper.seeway.panels.passenger.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.databinding.AdapterChooseSavedPlaceBinding

class SavedPlaceAdapter(private val arrayList: ArrayList<String>) :RecyclerView.Adapter<SavedPlaceAdapter.ChooseSavePlaceViewHolder>()  {


    inner class ChooseSavePlaceViewHolder(item: View): RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSavePlaceViewHolder {

      val binding= DataBindingUtil.inflate(LayoutInflater.from(parent.context),
          R.layout.adapter_choose_saved_place,parent,false)
              as AdapterChooseSavedPlaceBinding
        return ChooseSavePlaceViewHolder(binding.root)
        binding.lifecycleOwner
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ChooseSavePlaceViewHolder, position: Int) {


    }
}