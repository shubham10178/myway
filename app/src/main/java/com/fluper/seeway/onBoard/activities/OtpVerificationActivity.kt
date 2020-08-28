package com.fluper.seeway.onBoard.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.panels.driver.ProfileCreationDriverActivity
import com.fluper.seeway.panels.passenger.ProfileCreationPassengerActivity
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.showToast
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.fragment_otp_verification.*


class OtpVerificationActivity : BaseActivity() {

    lateinit var btn_otp_con : Button
    var timerCompleted: Boolean = false
    var sec: Long? = null
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.fragment_otp_verification)
        setTimer()
        btn_otp_con = findViewById(R.id.btn_otp_con)
        val type : String? = intent?.getStringExtra(Constants.CameFrom)

        btn_otp_con.setOnClickListener {
            if(type.equals(Constants.ForgotPassword)){
                startActivity(Intent(this, ResetPasswordActivity::class.java).apply {
                    this@OtpVerificationActivity.finish()
                })
            }else {
                if(type.equals(Constants.Passenger)){
                    startActivity(Intent(this, ProfileCreationPassengerActivity::class.java).apply {
                        this@OtpVerificationActivity.finish()
                    })
                }else if (type.equals(Constants.Driver)){
                    startActivity(Intent(this, ProfileCreationDriverActivity::class.java).apply {
                        this@OtpVerificationActivity.finish()
                    })
                }
            }
        }
        tvResendOtp.setOnClickListener {
            if (timerCompleted){
                setTimer()
            }else{
                showToast("Please wait...")
            }
        }
    }
    private fun setTimer() {
        if (timer == null) {
            timer = object : CountDownTimer(50000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    sec = millisUntilFinished / 1000
                    tvTimer.text = "00:"+sec.toString()
                    timerCompleted = false
                    //here you can have your logic to set text to edittext
                }

                override fun onFinish() {
                    tvTimer.text = "00:00"
                    showToast("Please do request a new OTP.")
                    timerCompleted = true
                    sec = null
                }
            }
        }
        timer?.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }
    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}
