package com.fluper.seeway.onBoard.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.utilitarianFiles.getString
import com.fluper.seeway.utilitarianFiles.isValidEmail
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.fragment_email_register.*


class EmailRegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.fragment_email_register)

        edt_User_Email.setText(
            if (intent.hasExtra("email"))
                intent.getStringExtra("email")
            else ""
        )

        btn_continue.setOnClickListener {
            if (edt_User_Email.isValidEmail()) {
                val intent = Intent()
                intent.putExtra("email", edt_User_Email.getString())
                setResult(1, intent)
                onBackPressed()
            }
        }

        img_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}
