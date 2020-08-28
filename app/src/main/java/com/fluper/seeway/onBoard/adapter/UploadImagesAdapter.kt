package com.fluper.seeway.onBoard.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fluper.seeway.R
import com.fluper.seeway.onBoard.model.ImageUploadModel

class UploadImagesAdapter constructor(
    private val userList: ArrayList<ImageUploadModel>,
    private val mCtx: Context?,
    private val removePicListener:RemovePictures
) : RecyclerView.Adapter<UploadImagesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.image_upload, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position])

        if (userList.size.equals(0)) {
            val largeIcon = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.dummy_dl)
            if (mCtx != null) {
                Glide
                    .with(mCtx)
                    .load(largeIcon)
                    .override(300, 200)
                    .into(holder.img_upload)
            }

        } else {
            if (mCtx != null) {
                Glide
                    .with(mCtx)
                    .load(userList[position].img)
                    .override(300, 200)
                    .into(holder.img_upload)
            }

        }

        holder.img_cross.setOnClickListener {
            val newPosition = holder.adapterPosition
            userList.removeAt(newPosition)
            notifyItemRemoved(newPosition)
            notifyItemRangeChanged(newPosition, userList.size)
            removePicListener.removePictureId(userList.size)
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var img_upload: ImageView
        lateinit var img_cross: ImageView

        fun bindItems(user: ImageUploadModel) {

            img_upload = itemView.findViewById(R.id.img_upload) as ImageView
            img_cross = itemView.findViewById(R.id.img_cross) as ImageView


        }
    }


}

interface RemovePictures{
    fun removePictureId(picsCount:Int)
}