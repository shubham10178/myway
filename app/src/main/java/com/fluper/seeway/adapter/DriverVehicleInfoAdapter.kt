package com.fluper.seeway.adapter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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

       holder.txt_name_item.setText(userList[position].vehicleName)
       holder.txt_number_item.setText(userList[position].vehicleNum)

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var txt_name_item :TextView
        lateinit var txt_number_item :TextView

        fun bindItems(vinfo: VehicleInfoModel) {

             txt_name_item  = itemView.findViewById(R.id.txt_name_item) as TextView
             txt_number_item  = itemView.findViewById(R.id.txt_number_item) as TextView


        }
    }



}