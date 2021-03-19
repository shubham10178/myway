package com.fluper.seeway.onBoard.activities

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.panels.driver.DriverViewModel
import com.fluper.seeway.utilitarianFiles.*
import kotlinx.android.synthetic.main.fragment_reset_password.*

class ResetPasswordActivity : BaseActivity(){
    private lateinit var driverViewModel: DriverViewModel
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.fragment_reset_password)
        driverViewModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        myObserver()
        btn_submit.setOnClickListener {
                if(isValidPasswords()) {
                    if(NetworkUtils.isInternetAvailable(this)){
                        ProgressBarUtils.getInstance().showProgress(this, false)
                        driverViewModel.resetPassword(
                            sharedPreference.userId,
                            edtConPassword.getString()
                        )
                    } else
                        showToast("Poor Connection")
                }
        }
        img_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun myObserver() {
        driverViewModel.resetPassword.observe(this, Observer {
            ProgressBarUtils.getInstance().hideProgress()
            showToast(it.message!!)
            onBackPressed()
        })
        driverViewModel.throwable.observe(this, Observer {
            ProgressBarUtils.getInstance().hideProgress()
            ErrorUtils.handlerGeneralError(this, it)
        })
    }

    private fun isValidPasswords(): Boolean {
        return when {
            edt_password.getString().isEmpty() -> {
                showToast("Please enter password")
                false
            }
            !edt_password.getString().isValidPassword -> {
                showToast("Please enter valid password")
                false
            }
            edtConPassword.getString().isEmpty() -> {
                showToast("Please enter confirm password")
                false
            }
            (edtConPassword.getString() != edt_password.getString()) -> {
                showToast("Password and confirm password should be same")
                false
            }
            else -> true
        }
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}