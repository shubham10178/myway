package com.fluper.seeway.Interface

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.fragment.ProfileCreationPassengerFragment
import com.fluper.seeway.model.UserTypeModel


class ListAdapter constructor (val userList: ArrayList<UserTypeModel>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_type_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: UserTypeModel) {
            val img_User = itemView.findViewById(R.id.img_User) as ImageView
            val txt_user_type  = itemView.findViewById(R.id.txt_user_type) as TextView
            val main_cons  = itemView.findViewById(R.id.main_cons) as ConstraintLayout
//            textViewName.text = user.name
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