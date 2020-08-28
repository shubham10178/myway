package com.fluper.seeway.onBoard.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground


class ForgotPasswordActivity : BaseActivity() {

    lateinit var img_back :ImageView
    lateinit var btn_continue :Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.fragment_forgot_password)

        img_back = findViewById(R.id.img_back)
        btn_continue = findViewById(R.id.btn_continue)

        img_back.setOnClickListener {
            onBackPressed()
        }

        btn_continue.setOnClickListener{
            startActivity(Intent(this, OtpVerificationActivity::class.java).apply {
                putExtra(Constants.CameFrom,Constants.ForgotPassword)
                this@ForgotPasswordActivity.finish()
            })
        }
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}

