package com.fluper.seeway.onBoard.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.utilitarianFiles.showToast
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.fragment_email_register.*
import kotlinx.android.synthetic.main.fragment_email_register.btn_continue
import kotlinx.android.synthetic.main.fragment_email_register.edt_User_Email


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
            if (edt_User_Email.text.toString().isNotEmpty()){
                if(checkForEmail(edt_User_Email.text.toString())) {
                    val intent=Intent()
                    intent.putExtra("email",edt_User_Email.text.toString())
                    setResult(1,intent)
                    onBackPressed()
                }
            }else{
                Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show()
            }
        }

        img_back.setOnClickListener {
            onBackPressed()
        }
    }
    private fun checkForEmail(email: String): Boolean {
        return if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            true
        }else {
            showToast("Please enter valid email address")
            false
        }
    }
    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}
