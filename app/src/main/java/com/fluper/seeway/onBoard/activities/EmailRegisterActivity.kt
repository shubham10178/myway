package com.fluper.seeway.onBoard.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.utilitarianFiles.getString
import com.fluper.seeway.utilitarianFiles.isValidEmail
import com.fluper.seeway.utilitarianFiles.showToast
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
            if (isValidEmailId()) {
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

    private fun isValidEmailId(): Boolean {
        return when {
            edt_User_Email.getString().isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(edt_User_Email.getString()).matches() -> {
                showToast("Please enter valid email address")
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
