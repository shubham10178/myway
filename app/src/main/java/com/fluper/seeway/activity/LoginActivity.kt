package com.fluper.seeway.activity

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.fluper.seeway.R
import com.fluper.seeway.fragment.ForgotPassword
import com.fluper.seeway.fragment.ResetPassword


class LoginActivity : AppCompatActivity() {

    lateinit var forgotPassword_txt : TextView
    lateinit var btn_continue : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        forgotPassword_txt = findViewById(R.id.forgotPass_txt)
        btn_continue = findViewById(R.id.btn_continue)

        val fragmentDemo: ForgotPassword? =
            supportFragmentManager.findFragmentById(R.id.frame_container) as ForgotPassword?


       forgotPassword_txt.setOnClickListener {

           setFragment(ForgotPassword())

       }



        btn_continue.setOnClickListener {

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.custom_alert_layout)

            val btn_cont = dialog.findViewById<Button>(R.id.btn_cont) as TextView

            val btn_cancel = dialog.findViewById<View>(R.id.btn_cancel) as Button
            btn_cancel.setOnClickListener { dialog.dismiss() }
            btn_cont.setOnClickListener { dialog.dismiss() }

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
            .addToBackStack("ForgotPassword").commit()
    }


    override fun onBackPressed() {

            super.onBackPressed() //replaced

    }
}
