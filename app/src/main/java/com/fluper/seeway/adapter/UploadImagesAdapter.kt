package com.fluper.seeway.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.model.ImageUploadModel
import com.fluper.seeway.model.NotificationModel

class UploadImagesAdapter constructor (val userList: ArrayList<ImageUploadModel>, val mCtx: Context?) : RecyclerView.Adapter<UploadImagesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadImagesAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.image_upload, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: UploadImagesAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])

        if(userList.size.equals(0)){
            val largeIcon = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.dummy_dl)
            holder.img_upload.setImageBitmap(largeIcon)
        }else{
            holder.img_upload.setImageBitmap(userList[position].img)
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var img_upload : ImageView

        fun bindItems(user: ImageUploadModel) {

             img_upload  = itemView.findViewById(R.id.img_upload) as ImageView
            val img_cross  = itemView.findViewById(R.id.img_cross) as ImageView


        }
    }


}