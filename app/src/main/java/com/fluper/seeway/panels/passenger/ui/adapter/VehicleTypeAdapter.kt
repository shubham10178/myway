package com.fluper.seeway.panels.passenger.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.databinding.ActivityVahicleTypeBinding
import com.fluper.seeway.databinding.AdapterVehicleTypeBinding

class VehicleTypeAdapter(
    val mContext: Context,
) : RecyclerView.Adapter<VehicleTypeAdapter.VehicleTypeViewHolder>() {

    private var mOnClickVehiclelistener :OnClickVehiclelistener?=null

    interface OnClickVehiclelistener {
        fun onClickVehicle(position: Int)
    }

    init {
        mOnClickVehiclelistener=mContext as  OnClickVehiclelistener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleTypeViewHolder {

        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_vehicle_type,
            parent,
            false
        ) as AdapterVehicleTypeBinding
        return VehicleTypeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: VehicleTypeViewHolder, position: Int) {
        holder.binding.clVehicle.setOnClickListener {
            mOnClickVehiclelistener?.onClickVehicle(position)
        }
    }


    inner class VehicleTypeViewHolder(var binding: AdapterVehicleTypeBinding) :
        RecyclerView.ViewHolder(binding.root)
}