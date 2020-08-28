package com.fluper.seeway.onBoard.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.panels.driver.DriverMainActivity
import com.fluper.seeway.panels.driver.ProfileCreationDriverActivity
import com.fluper.seeway.panels.passenger.PassengerMainActivity
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.fragment_chosse_security.*

class ChooseSecurityActivity : BaseActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chosse_security)
        statusBarFullScreenWithBackground()

        /*if (intent.hasExtra(Constants.UserType) && intent.getStringExtra(Constants.UserType).equals(Constants.Driver)) {
            showAlertSubmit()
        } else {
        }*/

        initClickListener()
    }

    private fun initClickListener() {
        txt_skip.setOnClickListener(this)
        btn_proceed.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.txt_skip,R.id.btn_proceed->{
                if (intent.hasExtra(Constants.UserType) && intent.getStringExtra(Constants.UserType).equals(Constants.Passenger)) {
                    startActivity(Intent(this, PassengerMainActivity::class.java).apply {
                        this@ChooseSecurityActivity.finishAffinity()
                    })
                } else if (intent.hasExtra(Constants.UserType) && intent.getStringExtra(Constants.UserType).equals(Constants.Driver)) {
                    startActivity(Intent(this, DriverMainActivity::class.java).apply {
                        this@ChooseSecurityActivity.finishAffinity()
                    })
                }
            }
        }
    }

    private fun showAlertSubmit() {
        val dialog = this.let { it1 -> Dialog(it1) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.driver_profile_approval_alert1)
        val txt_msg = dialog.findViewById<View>(R.id.txt_msg) as TextView
        val btn_no = dialog.findViewById<View>(R.id.btn_no) as Button
        val btn_yes = dialog.findViewById<View>(R.id.btn_yes) as Button
        txt_msg.setText(R.string.alert_driver_profile1)
        btn_no.setOnClickListener {
            val i = Intent(this, ProfileCreationDriverActivity::class.java)
            startActivity(i)

            dialog.dismiss()
        }

        btn_yes.setOnClickListener {

            showAlertYes()
            dialog.dismiss()
        }
        dialog.dismiss()
        dialog.show()
    }

    private fun showAlertYes() {
        val dialog = this.let { it1 -> Dialog(it1) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.driver_profile_approval_alert1)

        val txt_msg = dialog.findViewById<View>(R.id.txt_msg) as TextView
        val btn_no = dialog.findViewById<View>(R.id.btn_no) as Button
        val btn_yes = dialog.findViewById<View>(R.id.btn_yes) as Button
        txt_msg.setText(R.string.alert_driver_profile2)

        btn_no.setOnClickListener {
            val i = Intent(this, ProfileCreationDriverActivity::class.java)
            startActivity(i)
            dialog.dismiss()
        }

        btn_yes.setOnClickListener {

            dialog.dismiss()
        }
        dialog.dismiss()
        dialog.show()
    }
}
