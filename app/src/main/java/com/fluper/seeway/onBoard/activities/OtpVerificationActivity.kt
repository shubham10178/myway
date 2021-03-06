package com.fluper.seeway.onBoard.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.panels.driver.DriverViewModel
import com.fluper.seeway.panels.driver.ProfileCreationDriverActivity
import com.fluper.seeway.panels.passenger.ProfileCreationPassengerActivity
import com.fluper.seeway.utilitarianFiles.*
import kotlinx.android.synthetic.main.fragment_otp_verification.*


class OtpVerificationActivity : BaseActivity(), View.OnClickListener {

    var timerCompleted: Boolean = false
    var sec: Long? = null
    private var timer: CountDownTimer? = null
    private lateinit var driverViewModel: DriverViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.fragment_otp_verification)

        Log.e("Bharat",sharedPreference.loginWith)
        when(sharedPreference.loginWith){
            Constants.LoginWithEmail->{
                tvTextDescription.text = getString(R.string.verification_txt_email)
            }
            Constants.LoginWithMobile->{
                tvTextDescription.text = getString(R.string.verification_txt)
            }
        }


        driverViewModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        setTimer()
        myObserver()
        btn_otp_con.setOnClickListener(this)
        tvResendOtp.setOnClickListener(this)

    }

    private fun myObserver() {
        driverViewModel.otpVerify.observe(this, Observer {
            ProgressBarUtils.getInstance().hideProgress()
            showToast(it.message!!)
            if (!it.response?._id.isNullOrEmpty())
                sharedPreference.userId = it.response?._id!!
            else
                sharedPreference.userId = ""
            if (!it.response?.mobile_number.isNullOrEmpty())
                sharedPreference.userMobile = it.response?.mobile_number!!
            else
                sharedPreference.userMobile = ""
            if (!it.response?.country_code.isNullOrEmpty())
                sharedPreference.userCountryCode = it.response?.country_code!!
            else
                sharedPreference.userCountryCode = ""
            if (!it.response?.access_token.isNullOrEmpty())
                sharedPreference.accessToken = it.response?.access_token!!
            else
                sharedPreference.accessToken = ""
            if (!it.response?.profile_image.isNullOrEmpty())
                sharedPreference.profileImage = it.response?.profile_image!!
            else
                sharedPreference.profileImage = ""
            if (!it.response?.email.isNullOrEmpty())
                sharedPreference.userEmailId = it.response?.email!!
            else
                sharedPreference.userEmailId = ""
            if (!it.response?.first_name.isNullOrEmpty())
                sharedPreference.userFirstName = it.response?.first_name!!
            else
                sharedPreference.userFirstName = ""
            if (!it.response?.last_name.isNullOrEmpty())
                sharedPreference.userLastName = it.response?.last_name!!
            else
                sharedPreference.userLastName = ""

            if (intent.hasExtra(Constants.CameFrom)) {
                when {
                    intent.getStringExtra(Constants.CameFrom).equals(Constants.ForgotPassword) -> startActivity(Intent(this, ResetPasswordActivity::class.java).apply {
                        this@OtpVerificationActivity.finish()
                    })
                    intent.getStringExtra(Constants.CameFrom).equals(Constants.SignUp) -> startActivity(Intent(this, ChooseSecurityActivity::class.java).apply {
                        putExtra(Constants.UserType, sharedPreference.userType)
                        this@OtpVerificationActivity.finish()
                    })
                    else -> onBackPressed()
                }
            } else {
                if (intent.hasExtra(Constants.UserType)) {
                    when (intent.getStringExtra(Constants.UserType)) {
                        Constants.Passenger -> {
                            startActivity(
                                Intent(
                                    this,
                                    ProfileCreationPassengerActivity::class.java
                                ).apply {
                                    this@OtpVerificationActivity.finish()
                                })
                        }
                        Constants.Driver -> {
                            startActivity(
                                Intent(
                                    this,
                                    ProfileCreationDriverActivity::class.java
                                ).apply {
                                    this@OtpVerificationActivity.finish()
                                })
                        }
                        else -> onBackPressed()
                    }
                } else
                    onBackPressed()
            }
        })
        driverViewModel.resendOtp.observe(this, Observer {
            ProgressBarUtils.getInstance().hideProgress()
            showToast(it.message!!)
            if (!it.response?._id.isNullOrEmpty())
                sharedPreference.userId = it.response?._id!!
            else
                sharedPreference.userId = ""
            if (!it.response?.mobile_number.isNullOrEmpty())
                sharedPreference.userMobile = it.response?.mobile_number!!
            else
                sharedPreference.userMobile = ""
            if (!it.response?.country_code.isNullOrEmpty())
                sharedPreference.userCountryCode = it.response?.country_code!!
            else
                sharedPreference.userCountryCode = ""
            if (!it.response?.access_token.isNullOrEmpty())
                sharedPreference.accessToken = it.response?.access_token!!
            else
                sharedPreference.accessToken = ""
            if (!it.response?.profile_image.isNullOrEmpty())
                sharedPreference.profileImage = it.response?.profile_image!!
            else
                sharedPreference.profileImage = ""
            if (!it.response?.email.isNullOrEmpty())
                sharedPreference.userEmailId = it.response?.email!!
            else
                sharedPreference.userEmailId = ""
            if (!it.response?.first_name.isNullOrEmpty())
                sharedPreference.userFirstName = it.response?.first_name!!
            else
                sharedPreference.userFirstName = ""
            if (!it.response?.last_name.isNullOrEmpty())
                sharedPreference.userLastName = it.response?.last_name!!
            else
                sharedPreference.userLastName = ""
        })

        driverViewModel.throwable.observe(this, Observer {
            ProgressBarUtils.getInstance().hideProgress()
            ErrorUtils.handlerGeneralError(this, it)
        })
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_otp_con -> {
                if (pinview.value.isNotEmpty()) {
                    if (pinview.value.length == 4) {
                        if (NetworkUtils.isInternetAvailable(this)) {
                            ProgressBarUtils.getInstance().showProgress(this, false)
                            driverViewModel.otpVerify(
                                sharedPreference.userId,
                                pinview.value.trim()
                            )
                        } else
                            showToast("Poor Connection")
                    } else
                        showToast("Please enter complete otp")
                } else
                    showToast("Please enter otp")
            }
            R.id.tvResendOtp -> {
                if (timerCompleted) {
                    setTimer()
                    if (NetworkUtils.isInternetAvailable(this)) {
                        ProgressBarUtils.getInstance().showProgress(this, false)
                        driverViewModel.resendOtp(
                            sharedPreference.userId
                        )
                    }
                } else {
                    showToast("Please wait...")
                }
            }
        }
    }

    private fun setTimer() {
        if (timer == null) {
            timer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    sec = millisUntilFinished / 1000
                    if(sec.toString().length==1){
                        tvTimer.text = "00:" + "0"+sec.toString()
                    }else{
                        tvTimer.text = "00:" + sec.toString()
                    }
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
