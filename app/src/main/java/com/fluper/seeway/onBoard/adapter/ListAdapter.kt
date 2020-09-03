package com.fluper.seeway.onBoard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.onBoard.model.UserTypeModel

class ListAdapter constructor(val userList: ArrayList<UserTypeModel>, val context: Context?,val userType: UserType) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_type_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position])
        holder.main_cons.setOnClickListener {
            val strName = userList[position].name
            userType.userTypeId(strName)
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var main_cons: ConstraintLayout

        fun bindItems(user: UserTypeModel) {
            val img_User = itemView.findViewById(R.id.img_User) as ImageView
            val txt_user_type = itemView.findViewById(R.id.txt_user_type) as TextView
            main_cons = itemView.findViewById(R.id.main_cons) as ConstraintLayout

            img_User.setBackgroundResource(user.img)

            txt_user_type.text = user.name

            main_cons.setOnClickListener {
            }
        }
    }
}
interface UserType{
    fun userTypeId(userType: String)
}