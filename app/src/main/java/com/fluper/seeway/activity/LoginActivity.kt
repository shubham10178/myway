package com.fluper.seeway.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.fluper.seeway.R
import com.fluper.seeway.fragment.ForgotPassword
import com.fluper.seeway.fragment.OtpVerificationFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rilixtech.CountryCodePicker


class LoginActivity : AppCompatActivity() {

    lateinit var forgotPassword_txt : TextView
    lateinit var btn_continue : Button
    lateinit var edt_User_Email : EditText
    lateinit var edt_password : TextInputEditText
    lateinit var edt_phone_number : AppCompatEditText
    lateinit var password_textInputLayout : TextInputLayout
    var ccp: CountryCodePicker? = null
    var edtPhoneNumber: AppCompatEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        forgotPassword_txt = findViewById(R.id.forgotPass_txt)
        btn_continue = findViewById(R.id.btn_continue)
        edt_User_Email = findViewById(R.id.edt_User_Email)
        edt_password = findViewById(R.id.edt_password)
        edt_phone_number = findViewById(R.id.edt_phone_number)
        password_textInputLayout = findViewById(R.id.password_textInputLayout)

        ccp  = findViewById(R.id.ccp)
        edtPhoneNumber = findViewById(R.id.phone_number_edt)
        val type = Typeface.createFromAsset(getAssets(),"font/avenir_black.ttf");
        (ccp as CountryCodePicker).setTypeFace(type)


        forgotPassword_txt.setOnClickListener {

           setFragment(ForgotPassword())

       }

        edt_password.setOnFocusChangeListener(OnFocusChangeListener { view, isFocused ->
            if (isFocused) {
//                edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
//                password_textInputLayout.setPasswordVisibilityToggleEnabled(false)
                edt_password.setTypeface(type)

            } else {
//                edt_password.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                edt_password.setTypeface(type)
//                password_textInputLayout.setPasswordVisibilityToggleEnabled(true)
            }
        })

        btn_continue.setOnClickListener {
            val dialog = this?.let { it1 -> Dialog(it1) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.getWindow()?.getAttributes())
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            dialog.getWindow()?.setAttributes(layoutParams)
            dialog?.setCancelable(false)
            dialog?.setContentView(R.layout.custom_alert_layout)

            val btn_cont = dialog?.findViewById<Button>(R.id.btn_cont) as TextView

            val btn_cancel = dialog.findViewById<View>(R.id.btn_cancel) as Button
            btn_cancel.setOnClickListener { dialog.dismiss() }


            btn_cont.setOnClickListener {

              setFragment(OtpVerificationFragment())
                dialog?.dismiss()
            }

            dialog?.dismiss()
            dialog.show()

        }


    }

    protected fun setFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (fragment != null) {
            fragmentTransaction.add(android.R.id.content, fragment)
        }
        fragmentTransaction
            .addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)

    }

}
