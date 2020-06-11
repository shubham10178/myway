package com.fluper.seeway.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.model.VehicleInfoModel

class DriverVehicleInfoAdapter constructor (val userList: ArrayList<VehicleInfoModel>, val mCtx: Context?) : RecyclerView.Adapter<DriverVehicleInfoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverVehicleInfoAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_info_item, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: DriverVehicleInfoAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(vinfo: VehicleInfoModel) {

            val txt_name_item  = itemView.findViewById(R.id.txt_name_item) as TextView
            val txt_number_item  = itemView.findViewById(R.id.txt_number_item) as TextView

            txt_name_item.text = vinfo.vehicleName
            txt_number_item.text = vinfo.vehicleNum


        }
    }


}