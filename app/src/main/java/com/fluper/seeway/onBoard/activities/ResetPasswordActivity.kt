package com.fluper.seeway.onBoard.activities

import android.os.Bundle
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.fragment_reset_password.*

class ResetPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.fragment_reset_password)

        btn_submit.setOnClickListener{
            onBackPressed()
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