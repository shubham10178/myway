package com.fluper.seeway.Interface

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.activity.LoginActivity
import com.fluper.seeway.activity.ProfileCreationDriverActivity
import com.fluper.seeway.activity.ProfileCreationPassengerActivity
import com.fluper.seeway.model.UserTypeModel


class ListAdapter constructor (val userList: ArrayList<UserTypeModel>,val mCtx: Context?) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_type_item, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
        holder.main_cons.setOnClickListener {

            val strName = userList.get(position).name
            holder.main_cons.setBackgroundColor(Color.parseColor("#FFFFFF"));
            if(strName.equals("Passenger")) {

                val intent = Intent(mCtx, LoginActivity::class.java)
                intent.putExtra("type","Passenger")
                mCtx?.startActivity(intent)
            }else{

                val intent = Intent(mCtx, LoginActivity::class.java)
                intent.putExtra("type","Driver")
                mCtx?.startActivity(intent)
            }

        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

       lateinit var main_cons : RelativeLayout

        fun bindItems(user: UserTypeModel) {
            val img_User = itemView.findViewById(R.id.img_User) as ImageView
            val txt_user_type  = itemView.findViewById(R.id.txt_user_type) as TextView
             main_cons  = itemView.findViewById(R.id.main_cons) as RelativeLayout

            img_User.setBackgroundResource(user.img as Int)

            txt_user_type.text = user.name

            main_cons.setOnClickListener {

                if(user.name.equals("Passenger")){


                }
                if(user.equals("Driver")){

            }

            }

        }
    }




}