package com.fluper.seeway.onBoard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.fluper.seeway.R
import com.fluper.seeway.panels.driver.model.AddVehicleResponseModel
import kotlinx.android.synthetic.main.vehicle_info_item.view.*


class DriverVehicleInfoAdapter constructor(
    private val userList: ArrayList<AddVehicleResponseModel.Response?>?,
    val mCtx: Context,
    private val removeVehicle: RemoveVehicle
) : RecyclerView.Adapter<DriverVehicleInfoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.vehicle_info_item, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!userList.isNullOrEmpty()) {
            holder.bindItems(userList[position])
            holder.txt_name_item.text = userList[position]?.vehicle_model
            holder.txt_number_item.text = userList[position]?.vehicle_number
            holder.itemView.tvRemoveVehicle.setOnClickListener {
                val newPosition = holder.adapterPosition
                removeVehicle.removeVehicleId(
                    userList[newPosition]?._id,
                    userList[newPosition]?.user_id
                )
            }
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return if (!userList.isNullOrEmpty())
            userList.size
        else
            0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var txt_name_item: TextView
        lateinit var txt_number_item: TextView
        fun bindItems(vinfo: AddVehicleResponseModel.Response?) {
            txt_name_item = itemView.findViewById(R.id.txt_name_item) as TextView
            txt_number_item = itemView.findViewById(R.id.txt_number_item) as TextView
        }
    }
}

interface RemoveVehicle {
    fun removeVehicleId(vehicleId:String?,userId:String?)
}