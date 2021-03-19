package com.fluper.seeway.onBoard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fluper.seeway.R
import com.fluper.seeway.onBoard.model.AddressModel

class HomeAddressAdapter constructor (val userList: ArrayList<AddressModel>, val mCtx: Context?) : RecyclerView.Adapter<HomeAddressAdapter.ViewHolder>() {




    private lateinit var mClickItemListener:ClickItemListener

    interface ClickItemListener{
       fun onClickItem(position:Int);
    }

    init {
        mClickItemListener =mCtx as ClickItemListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.home_address_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position])
        mCtx?.let { Glide.with(it).load(userList[position].img).into(holder.img_icon_home) }

        holder.ll_adapter.setOnClickListener {
            mClickItemListener.onClickItem(position)
        }
    }

    //this method is giving the size of the list

    override fun getItemCount(): Int {
        return userList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var main_cons : ConstraintLayout
        lateinit var img_icon_home : ImageView
        lateinit var ll_adapter : LinearLayout

        fun bindItems(user: AddressModel) {
            img_icon_home = itemView.findViewById(R.id.img_icon_home) as ImageView
            val txt_title  = itemView.findViewById(R.id.txt_title) as TextView
            val txt_add  = itemView.findViewById(R.id.txt_add) as TextView
            ll_adapter  = itemView.findViewById(R.id.ll_adapter) as LinearLayout

            txt_title.text = user.title
            txt_add.text = user.address



        }


    }


}