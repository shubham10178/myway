package com.fluper.seeway.onBoard.activities

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.animation.AnimationUtils
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.hideStatusBarWithBackground
import kotlinx.android.synthetic.main.activity_splashscreen.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashScreen : BaseActivity() {
    private val splashTimeOut = 4000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        val topAnim = AnimationUtils.loadAnimation(this,R.anim.anim_top_splash)
        val bottomAnim= AnimationUtils.loadAnimation(this,R.anim.anim_bottom_splash)
        ivSplashLogo.animation = topAnim
        prefs = getSharedPreferences("com.fluper.seeway", Context.MODE_PRIVATE)
        hideStatusBarWithBackground()
        printHashKey(this)
        Handler().postDelayed(
            {
                if (prefs.getBoolean(Constants.FirstRun, true)) {
                    startActivity(Intent(this, WalkThroughActivity::class.java))
                }
                else {
                    startActivity(Intent(this, UserTypeActivity::class.java))
                }
                this.finish()
            }, splashTimeOut)
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
