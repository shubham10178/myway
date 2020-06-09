package com.fluper.seeway.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fluper.seeway.Interface.ListAdapter
import com.fluper.seeway.R
import com.fluper.seeway.activity.ProfileCreationPassengerActivity
import com.fluper.seeway.model.AddressModel
import com.fluper.seeway.model.UserTypeModel
import com.squareup.picasso.Picasso

class HomeAddressAdapter constructor (val userList: ArrayList<AddressModel>, val mCtx: Context?) : RecyclerView.Adapter<HomeAddressAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAddressAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.home_address_item, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: HomeAddressAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
        mCtx?.let { Glide.with(it).load(userList[position].img).into(holder.img_icon_home) }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var main_cons : ConstraintLayout
        lateinit var img_icon_home : ImageView

        fun bindItems(user: AddressModel) {
             img_icon_home = itemView.findViewById(R.id.img_icon_home) as ImageView
            val txt_title  = itemView.findViewById(R.id.txt_title) as TextView
            val txt_add  = itemView.findViewById(R.id.txt_add) as TextView



            txt_title.text = user.title
            txt_add.text = user.address


        }
    }


}