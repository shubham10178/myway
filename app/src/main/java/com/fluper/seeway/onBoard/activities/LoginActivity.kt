package com.fluper.seeway.onBoard.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.panels.driver.DriverViewModel
import com.fluper.seeway.panels.driver.ProfileCreationDriverActivity
import com.fluper.seeway.panels.driver.dashboard.DriverMainActivity
import com.fluper.seeway.panels.passenger.ProfileCreationPassengerActivity
import com.fluper.seeway.panels.passenger.dashboard.PassengerMainActivity
import com.fluper.seeway.utilitarianFiles.*
import com.rilixtech.CountryCodePicker
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity(), View.OnClickListener {

    private var dialog: Dialog? = null
    private lateinit var driverViewModel: DriverViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        statusBarFullScreenWithBackground()
        driverViewModel = ViewModelProvider(this).get(DriverViewModel::class.java)

        initClickListener()
        myObserver()
        val type = Typeface.createFromAsset(assets, "font/avenir_black.ttf")
        (ccp as CountryCodePicker).typeFace = type
        edt_password.onFocusChangeListener = OnFocusChangeListener { view, isFocused ->
            if (isFocused) {
                //                edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                //                password_textInputLayout.setPasswordVisibilityToggleEnabled(false)
                edt_password.typeface = type

            } else {
                //                edt_password.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                edt_password.typeface = type
                //                password_textInputLayout.setPasswordVisibilityToggleEnabled(true)
            }
        }
    }

    private fun myObserver() {
        driverViewModel.signInSignUp.observe(this, Observer { it ->
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

            it.response?.let {
                if (it.is_term_accept == 0 && it.is_verified?.trim()
                        ?.toInt() == 0 && it.isProfileCreated?.trim()?.toInt() == 0
                ) {
                    showDialog(it._id!!)
                } else if (it.is_term_accept == 1 && it.is_verified?.trim()
                        ?.toInt() == 0 && it.isProfileCreated?.trim()?.toInt() == 0
                ) {
                    startActivity(Intent(this, OtpVerificationActivity::class.java).apply {
                        putExtra(Constants.UserType, sharedPreference.userType)
                    })
                } else if (it.is_term_accept == 1 && it.is_verified?.trim()
                        ?.toInt() == 1 && it.isProfileCreated?.trim()?.toInt() == 0
                ) {
                    when (sharedPreference.userType) {
                        Constants.Passenger -> {
                            startActivity(
                                Intent(
                                    this,
                                    ProfileCreationPassengerActivity::class.java
                                ).apply {})
                        }
                        Constants.Driver -> {
                            startActivity(
                                Intent(
                                    this,
                                    ProfileCreationDriverActivity::class.java
                                ).apply {})
                        }
                        else -> onBackPressed()
                    }
                } else {
                    when (sharedPreference.userType) {
                        Constants.Passenger -> {
                            startActivity(Intent(this, PassengerMainActivity::class.java).apply {
                                this@LoginActivity.finishAffinity()
                            })
                        }
                        Constants.Driver -> {
                            startActivity(Intent(this, DriverMainActivity::class.java).apply {
                                this@LoginActivity.finishAffinity()
                            })
                        }
                        else -> onBackPressed()
                    }
                }
            }
        })

        driverViewModel.termsAndConditions.observe(this, Observer {
            if (dialog != null) {
                dialog?.dismiss()
                dialog = null
            }
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

            startActivity(Intent(this, OtpVerificationActivity::class.java).apply {
                putExtra(Constants.UserType, sharedPreference.userType)
            })
        })
        driverViewModel.throwable.observe(this, Observer {
            ProgressBarUtils.getInstance().hideProgress()
            ErrorUtils.handlerGeneralError(this, it)
        })
    }

    private fun initClickListener() {
        edt_User_Email.setOnClickListener(this)
        emailClick.setOnClickListener(this)
        edt_phone_number.setOnClickListener(this)
        mobileClick.setOnClickListener(this)
        ccp.setOnClickListener(this)
        btn_continue.setOnClickListener(this)
        tvForgot.setOnClickListener(this)
        btnFace.setOnClickListener(this)
        btnFingerPrint.setOnClickListener(this)
        btnPin.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.edt_User_Email, R.id.emailClick -> {
                startActivityForResult(Intent(this, EmailRegisterActivity::class.java).apply {
                    putExtra("email", edt_User_Email.text.toString())
                }, 1)
            }
            R.id.edt_phone_number, R.id.mobileClick, R.id.ccp -> {
                startActivityForResult(Intent(this, MobileRegisterActivity::class.java).apply {
                    putExtra("ccp", ccp.selectedCountryNameCode)
                    putExtra("mobile", edt_phone_number.text.toString())
                }, 2)
            }
            R.id.btn_continue -> {
                if (sharedPreference.userType.equals(Constants.Driver)) {
                    if (isValidCredentials()) {
                        if (NetworkUtils.isInternetAvailable(this)) {
                            ProgressBarUtils.getInstance().showProgress(this, false)
                            driverViewModel.signInSignUp(
                                edt_User_Email.getString(),
                                ccp.selectedCountryCodeWithPlus,
                                edt_phone_number.getString(),
                                sharedPreference.deviceUniqueId!!,
                                edt_password.getString(),
                                Constants.DeviceTypeAndroid,
                                Constants.UserValueDriver,
                            )
                        } else
                            showToast("Poor Connection")
                    }
                } else {
                    startActivity(Intent(this, OtpVerificationActivity::class.java).apply {
                        putExtra(Constants.UserType, sharedPreference.userType)
                    })
                }
            }
            R.id.tvForgot -> {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
            }
            R.id.btnFace -> {
                startActivity(Intent(this, FaceLockActivity::class.java).apply {
                    putExtra(Constants.UserType, sharedPreference.userType)
                    putExtra(Constants.CameFrom,Constants.SignIn)
                })
            }
            R.id.btnFingerPrint -> {
                startActivity(Intent(this, FingerPrintLockActivity::class.java).apply {
                    putExtra(Constants.UserType, sharedPreference.userType)
                    putExtra(Constants.CameFrom,Constants.SignIn)
                })
            }
            R.id.btnPin -> {
                startActivity(Intent(this, PatternLockActivity::class.java).apply {
                    putExtra(Constants.UserType, sharedPreference.userType)
                    putExtra(Constants.CameFrom,Constants.SignIn)
                })
            }
        }
    }

    private fun isValidCredentials(): Boolean {
        return when {
            edt_User_Email.getString().isEmpty() && edt_phone_number.getString().isEmpty() -> {
                showToast("Please enter email or mobile")
                false
            }
            edt_password.getString().isEmpty() -> {
                showToast("Please enter password")
                false
            }
            !edt_password.getString().isValidPassword -> {
                showToast("Please enter valid password")
                false
            }
            else -> true
        }
    }

    private fun showDialog(str: String) {
        if (dialog == null)
            dialog = Dialog(this)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = layoutParams
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.custom_alert_layout)

        val btn_cont = dialog?.findViewById<Button>(R.id.btn_cont) as TextView

        val btn_cancel = dialog?.findViewById<View>(R.id.btn_cancel) as Button
        btn_cancel.setOnClickListener {
            if (dialog != null) {
                dialog?.dismiss()
                dialog = null
            }
        }

        btn_cont.setOnClickListener {
            if (NetworkUtils.isInternetAvailable(this)) {
                driverViewModel.termsAndCondition(str, "1")
            } else
                showToast("Poor Connection")
        }
        dialog?.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> {
                if (requestCode == resultCode) {
                    edt_User_Email.setText(
                        if (data != null && data.hasExtra("email"))
                            data.getStringExtra("email")
                        else ""
                    )
                }
            }
            2 -> {
                if (requestCode == resultCode) {
                    val ccpCode = if (data != null && data.hasExtra("ccp"))
                        data.getStringExtra("ccp")
                    else ""
                    if (!ccpCode.isNullOrEmpty()) {
                        ccp.setDefaultCountryUsingNameCode(ccpCode)
                        ccp.resetToDefaultCountry()
                    }
                    edt_phone_number.setText(
                        if (data != null && data.hasExtra("mobile"))
                            data.getStringExtra("mobile")
                        else ""
                    )
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        /*val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)*/
        this.finish()
        super.onBackPressed()
    }
}
