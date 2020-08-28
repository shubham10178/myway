package com.fluper.seeway.panels.passenger

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.onBoard.adapter.NotificationAdapter
import com.fluper.seeway.onBoard.model.NotificationModel
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.fragment_notification_passenger.*

class NotificationPassengerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_notification_passenger)
        statusBarFullScreenWithBackground()
        initView()
        img_back.setOnClickListener {
            onBackPressed()
        }
    }


    private fun initView(){
        val recyclerView = findViewById<RecyclerView>(R.id.notification_recycler)
        /*val imgBack = findViewById<ImageView>(R.id.img_back)
        imgBack.setOnClickListener {
            this.onBackPressed()
        }*/
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val users = ArrayList<NotificationModel>()
        users.clear()
        val  adapter = NotificationAdapter(users, this)
        recyclerView.adapter = adapter
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}
