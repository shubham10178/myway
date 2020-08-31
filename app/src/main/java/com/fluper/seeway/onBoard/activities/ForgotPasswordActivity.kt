package com.fluper.seeway.onBoard.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.panels.driver.DriverViewModel
import com.fluper.seeway.utilitarianFiles.*
import com.rilixtech.CountryCodePicker
import kotlinx.android.synthetic.main.fragment_forgot_password.*


class ForgotPasswordActivity : BaseActivity() {

    private lateinit var driverViewModel: DriverViewModel
    private var email = ""
    private var mobile = ""
    private var countryCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.fragment_forgot_password)
        val type = Typeface.createFromAsset(assets, "font/avenir_black.ttf")
        (ccp as CountryCodePicker).typeFace = type
        driverViewModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        handleEditTextView()
        myObserver()

        img_back.setOnClickListener {
            onBackPressed()
        }

        btn_continue.setOnClickListener {
            if (sharedPreference.userType.equals(Constants.Driver)) {
                if (isInputs()) {
                    if (NetworkUtils.isInternetAvailable(this)) {
                        if (edtEmailPhone.getString().isDigitsOnly()) {
                            mobile = edtEmailPhone.getString()
                            countryCode = ccp.selectedCountryCodeWithPlus
                            email = ""
                        } else {
                            email = edtEmailPhone.getString()
                            mobile = ""
                            countryCode = ""
                        }
                        ProgressBarUtils.getInstance().showProgress(this, false)
                        driverViewModel.forgotPassword(
                            countryCode,
                            mobile,
                            email,
                            "2"
                        )
                    } else
                        showToast("Poor Connection")
                }
            } else {
                startActivity(Intent(this, OtpVerificationActivity::class.java).apply {
                    putExtra(Constants.CameFrom, Constants.ForgotPassword)
                    this@ForgotPasswordActivity.finish()
                })
            }
        }
    }

    private fun isInputs(): Boolean {
        return when {
            edtEmailPhone.getString().isEmpty() -> {
                showToast("please enter e-mail/ mobile number")
                false
            }
            edtEmailPhone.getString()
                .isDigitsOnly() && !edtEmailPhone.getString().isValidMobile -> {
                showToast("Please enter valid mobile number")
                false
            }
            !edtEmailPhone.getString().isDigitsOnly() && !Patterns.EMAIL_ADDRESS.matcher(
                edtEmailPhone.getString()
            ).matches() -> {
                showToast("Please enter valid email address")
                false
            }
            else -> true
        }
    }

    private fun myObserver() {
        driverViewModel.forgotPassword.observe(this, Observer {
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

            startActivity(Intent(this, OtpVerificationActivity::class.java).apply {
                putExtra(Constants.CameFrom, Constants.ForgotPassword)
                this@ForgotPasswordActivity.finish()
            })
        })

        driverViewModel.throwable.observe(this, Observer {
            ProgressBarUtils.getInstance().hideProgress()
            ErrorUtils.handlerGeneralError(this, it)
        })
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }

    private fun handleEditTextView() {
        edtEmailPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrBlank()) {
                    //ivforgetMethod.setBackgroundResource(R.drawable.email)
                    ccp.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isDigitsOnly() || s.toString()[0].toString().equals("+")) {
                    //ivforgetMethod.setBackgroundResource(R.drawable.call)
                    ccp.visibility = View.VISIBLE
                    if (s.toString().isNotEmpty() && s.toString()[0].toString().equals("+")) {
                        edtEmailPhone.setText(
                            s.toString().removePrefix(ccp.selectedCountryCodeWithPlus)
                        )
                        edtEmailPhone.setSelection(edtEmailPhone.getString().length)
                    }
                } else {
                    //ivforgetMethod.setBackgroundResource(R.drawable.email)
                    ccp.visibility = View.GONE
                }
            }
        })
    }
}

