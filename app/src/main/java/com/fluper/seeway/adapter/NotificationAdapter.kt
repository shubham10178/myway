package com.fluper.seeway.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.model.AddressModel
import com.fluper.seeway.model.NotificationModel

class NotificationAdapter constructor (val userList: ArrayList<NotificationModel>, val mCtx: Context?) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(user: NotificationModel) {

            val txt_title  = itemView.findViewById(R.id.txt_notification) as TextView
            val txt_time  = itemView.findViewById(R.id.txt_time) as TextView


            txt_title.text = user.title
            txt_time.text = user.time


        }
    }


}