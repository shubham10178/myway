package com.fluper.seeway.onBoard.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.panels.driver.dashboard.DriverMainActivity
import com.fluper.seeway.panels.passenger.dashboard.PassengerMainActivity
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.showToast
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.activity_finger_lock.*

class FingerPrintLockActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.activity_finger_lock)
        img_back_addv.setOnClickListener(this)
        ivFinger.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.img_back_addv -> {
                onBackPressed()
            }
            R.id.ivFinger -> {
                if (intent.hasExtra(Constants.UserType)) {
                    when (intent.getStringExtra(Constants.UserType)) {
                        Constants.Passenger -> {
                            startActivity(Intent(this, PassengerMainActivity::class.java).apply {
                                this@FingerPrintLockActivity.finishAffinity()
                            })
                        }
                        Constants.Driver -> {
                            if (intent.hasExtra(Constants.CameFrom) && intent.getStringExtra(
                                    Constants.CameFrom
                                ).equals(Constants.SignIn)
                            ) {
                                showToast("This service is under development")
                                onBackPressed()
                            } else
                                startActivity(Intent(this, DriverMainActivity::class.java).apply {
                                    this@FingerPrintLockActivity.finishAffinity()
                                })
                        }
                        else -> onBackPressed()
                    }
                } else
                    onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}