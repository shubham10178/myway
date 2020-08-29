package com.fluper.seeway.onBoard.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.panels.driver.dashboard.DriverMainActivity
import com.fluper.seeway.panels.passenger.dashboard.PassengerMainActivity
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.activity_patterns_lock.*

class PatternLockActivity: BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.activity_patterns_lock)
        img_back_addv.setOnClickListener(this)
        btn_otp_con.setOnClickListener(this)
    }
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.img_back_addv->{
                onBackPressed()
            }
            R.id.btn_otp_con->{
                /*if (intent.hasExtra(Constants.UserType)) {
                    when (intent.getStringExtra(Constants.UserType)) {
                        Constants.Passenger -> {
                            startActivity(Intent(this, PassengerMainActivity::class.java).apply {
                                this@PatternLockActivity.finishAffinity()
                            })
                        }
                        Constants.Driver -> {
                            startActivity(Intent(this, DriverMainActivity::class.java).apply {
                                this@PatternLockActivity.finishAffinity()
                            })
                        }
                        else -> onBackPressed()
                    }
                } else
                    onBackPressed()*/
            }
        }
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}