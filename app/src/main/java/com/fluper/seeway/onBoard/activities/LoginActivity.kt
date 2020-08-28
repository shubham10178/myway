package com.fluper.seeway.onBoard.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import com.rilixtech.CountryCodePicker
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_continue
import kotlinx.android.synthetic.main.activity_login.ccp
import kotlinx.android.synthetic.main.activity_login.edt_User_Email
import kotlinx.android.synthetic.main.activity_login.edt_phone_number


class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        statusBarFullScreenWithBackground()
        prefs = getSharedPreferences("com.fluper.seeway", Context.MODE_PRIVATE)

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

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.edt_User_Email,R.id.emailClick -> {
                startActivityForResult(Intent(this, EmailRegisterActivity::class.java).apply {
                    putExtra("email",edt_User_Email.text.toString())
                }, 1)
            }
            R.id.edt_phone_number,R.id.mobileClick,R.id.ccp -> {
                startActivityForResult(Intent(this, MobileRegisterActivity::class.java).apply {
                    putExtra("ccp",ccp.selectedCountryNameCode)
                    putExtra("mobile",edt_phone_number.text.toString())
                }, 2)
            }
            R.id.btn_continue -> {
                showDialog()
            }
            R.id.tvForgot -> {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
            }
            R.id.btnFace -> {
            }
            R.id.btnFingerPrint -> {
            }
            R.id.btnPin -> {
            }
        }
    }

    private fun showDialog() {
        val dialog = this.let { it1 -> Dialog(it1) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window?.attributes = layoutParams
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_alert_layout)

        val btn_cont = dialog.findViewById<Button>(R.id.btn_cont) as TextView

        val btn_cancel = dialog.findViewById<View>(R.id.btn_cancel) as Button
        btn_cancel.setOnClickListener { dialog.dismiss() }

        btn_cont.setOnClickListener {
            startActivity(Intent(this, OtpVerificationActivity::class.java).apply {
                putExtra(Constants.CameFrom, prefs.getString(Constants.UserType, ""))
            })
            dialog.dismiss()
        }

        dialog.dismiss()
        dialog.show()
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
