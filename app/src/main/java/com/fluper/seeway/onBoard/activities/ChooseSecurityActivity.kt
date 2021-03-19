package com.fluper.seeway.onBoard.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.panels.driver.dashboard.DriverMainActivity
import com.fluper.seeway.panels.driver.ProfileCreationDriverActivity
import com.fluper.seeway.panels.passenger.dashboard.PassengerMainActivity
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.showToast
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.fragment_chosse_security.*
import java.util.concurrent.Executor

class ChooseSecurityActivity : BaseActivity(), View.OnClickListener {
    var isEnabled=0

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chosse_security)
        statusBarFullScreenWithBackground()
        /*if (intent.hasExtra(Constants.UserType) && intent.getStringExtra(Constants.UserType).equals(Constants.Driver)) {
            showAlertSubmit()
        } else {
        }*/

        verfiyingBioMetricExistence()
        InitBioMetric()
        initClickListener()
    }

    private fun InitBioMetric(){
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setConfirmationRequired(false)
            .setAllowedAuthenticators(DEVICE_CREDENTIAL or BIOMETRIC_STRONG)

            .build()
    }

    private fun verfiyingBioMetricExistence() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->{
                println("App can authenticate using biometrics.")
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
            println("No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
            println("Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->{
                println("The user hasn't associated any biometric credentials with their account.")
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                }
                startActivityForResult(enrollIntent, 101)
            }
        }
    }

    private fun initClickListener() {
        txt_skip.setOnClickListener(this)
        btn_proceed.setOnClickListener(this)
        btnFace.setOnClickListener(this)
        btnFingerPrint.setOnClickListener(this)
        btnPin.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.txt_skip, R.id.btn_proceed -> {
                if (isEnabled!=0){
                    sharedPreference.isEnabled=isEnabled
                    when (intent.getStringExtra(Constants.UserType)) {
                        Constants.Passenger -> {
                            startActivity(Intent(this, PassengerMainActivity::class.java).apply {
                                this@ChooseSecurityActivity.finishAffinity()
                            })
                        }
                        Constants.Driver -> {
                            startActivity(Intent(this, DriverMainActivity::class.java).apply {
                                this@ChooseSecurityActivity.finishAffinity()
                            })
                        }
                        else -> onBackPressed()
                    }
                } else
                   showToast("Please choose atleast one security.")
            }
            R.id.btnFace -> {
                isEnabled=1
                biometricPrompt.authenticate(promptInfo)
                /*startActivity(Intent(this,FaceLockActivity::class.java).apply {
                    putExtra(Constants.UserType,intent.getStringExtra(Constants.UserType))
                })*/
            }
            R.id.btnFingerPrint -> {
                isEnabled=2
                biometricPrompt.authenticate(promptInfo)
            /*startActivity(Intent(this,FingerPrintLockActivity::class.java).apply {
                    putExtra(Constants.UserType,intent.getStringExtra(Constants.UserType))
                })*/
            }
            R.id.btnPin -> {
                isEnabled=3
                biometricPrompt.authenticate(promptInfo)
            /* startActivity(Intent(this,PatternLockActivity::class.java).apply {
                    putExtra(Constants.UserType,intent.getStringExtra(Constants.UserType))
                })*/
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
