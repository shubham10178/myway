package com.fluper.seeway.onBoard.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Toast
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.utilitarianFiles.getString
import com.fluper.seeway.utilitarianFiles.isValidMobile
import com.fluper.seeway.utilitarianFiles.showToast
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import com.rilixtech.CountryCodePicker
import kotlinx.android.synthetic.main.fragment_mobile_r_egister.btn_continue
import kotlinx.android.synthetic.main.fragment_mobile_r_egister.ccp
import kotlinx.android.synthetic.main.fragment_mobile_r_egister.edt_phone_number
import kotlinx.android.synthetic.main.fragment_mobile_r_egister.img_back

class MobileRegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.fragment_mobile_r_egister)

        val type = Typeface.createFromAsset(assets, "font/avenir_black.ttf")
        (ccp as CountryCodePicker).typeFace = type

        val ccpCode = if (intent.hasExtra("ccp"))
            intent.getStringExtra("ccp")
        else ""
        if (!ccpCode.isNullOrEmpty()) {
            ccp.setDefaultCountryUsingNameCode(ccpCode)
            ccp.resetToDefaultCountry()
        }
        edt_phone_number.setText(
            if (intent.hasExtra("mobile"))
                intent.getStringExtra("mobile")
            else ""
        )

        btn_continue.setOnClickListener {
            if (edt_phone_number.getString().isNotEmpty()){
                if(checkForMobile(edt_phone_number.getString())) {
                    val intent= Intent()
                    intent.putExtra("ccp",ccp.selectedCountryNameCode)
                    intent.putExtra("mobile",edt_phone_number.getString())
                    setResult(2,intent)
                    onBackPressed()
                }
            }else{
                Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show()
            }
        }
        img_back.setOnClickListener {
            onBackPressed()
        }
    }
    private fun checkForMobile(mobile: String): Boolean {
        return if (!mobile.isValidMobile){
            showToast("Please enter valid mobile number")
            false
        }else {
            true
        }
    }
    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}
