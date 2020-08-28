package com.fluper.seeway.onBoard.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.onBoard.adapter.ListAdapter
import com.fluper.seeway.onBoard.adapter.UserType
import com.fluper.seeway.onBoard.model.UserTypeModel
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.showToast
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.fragment_user_type.*


class UserTypeActivity : BaseActivity() {
    protected lateinit var rootView: View

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.fragment_user_type)
        initView()
    }


    private fun initView() {

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val layoutManager: GridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = layoutManager
        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = 0
        layoutManager.startSmoothScroll(smoothScroller)

        val users = ArrayList<UserTypeModel>()

        users.add(UserTypeModel("Passenger", R.drawable.passenger))
        users.add(UserTypeModel("Driver", R.drawable.driver2x))
        users.add(UserTypeModel("Tenant", R.drawable.tenant2x))
        users.add(UserTypeModel("Renter", R.drawable.renter))
        users.add(UserTypeModel("Parcel Send or Receive", R.drawable.parcel_send_receive3x))
        users.add(UserTypeModel("Delivery Boy", R.drawable.deliveryboy2x))
        users.add(UserTypeModel("User/ Master User", R.drawable.user_master_user2x))

        val adapter = ListAdapter(users, this, object : UserType {
            override fun userTypeId(userType: String) {
                when {
                    userType.equals(Constants.Passenger) || userType.equals(Constants.Driver) -> {
                        startActivity(Intent(this@UserTypeActivity, LoginActivity::class.java).apply {
                            sharedPreference.userType = userType
                        })
                    }
                    else -> {
                        Toast.makeText(this@UserTypeActivity, "Under Development", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        recyclerView.adapter = adapter
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        this.showToast("Back again to exit")
//            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}