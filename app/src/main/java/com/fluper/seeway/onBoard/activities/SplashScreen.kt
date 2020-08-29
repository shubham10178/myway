package com.fluper.seeway.onBoard.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.animation.AnimationUtils
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.panels.driver.dashboard.DriverMainActivity
import com.fluper.seeway.panels.passenger.dashboard.PassengerMainActivity
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.hideStatusBarWithBackground
import com.fluper.seeway.utilitarianFiles.showToast
import kotlinx.android.synthetic.main.activity_splashscreen.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashScreen : BaseActivity() {
    private val splashTimeOut = 4000L

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        val topAnim = AnimationUtils.loadAnimation(this, R.anim.anim_top_splash)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.anim_bottom_splash)
        ivSplashLogo.animation = topAnim
        hideStatusBarWithBackground()
        sharedPreference.deviceUniqueId = Settings.Secure.getString(
            this.applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        printHashKey(this)
        Handler().postDelayed(
            {
                if (sharedPreference.firstRun) {
                    startActivity(Intent(this, WalkThroughActivity::class.java))
                } else {
                    if (sharedPreference.isLoggedIn) {
                        when (sharedPreference.userType) {
                            Constants.Passenger -> {
                                startActivity(
                                    Intent(
                                        this,
                                        PassengerMainActivity::class.java
                                    ).apply {
                                        this@SplashScreen.finishAffinity()
                                    })
                            }
                            Constants.Driver -> {
                                startActivity(Intent(this, DriverMainActivity::class.java).apply {
                                    this@SplashScreen.finishAffinity()
                                })
                            }
                            /*Constants.Tenant->{}
                            Constants.Renter->{}
                            Constants.Parcel->{}
                            Constants.DeliveryBoy->{}
                            Constants.UserMasterUser->{}*/
                            else -> {
                                sharedPreference.isLoggedIn = false
                                sharedPreference.deletePreferences()
                                showToast("Session expired.")
                                startActivity(Intent(this, UserTypeActivity::class.java))
                            }
                        }
                    } else {
                        startActivity(Intent(this, UserTypeActivity::class.java))
                    }
                }
                this.finish()
            }, splashTimeOut
        )
    }

    private fun printHashKey(pContext: Context) {
        try {
            val info = pContext.packageManager
                .getPackageInfo(pContext.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("FragmentActivity.TAG", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("FragmentActivity.TAG", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("FragmentActivity.TAG", "printHashKey()", e)
        }

    }
}
