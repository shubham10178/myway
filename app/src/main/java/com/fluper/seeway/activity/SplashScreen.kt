package com.fluper.seeway.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.fluper.seeway.R

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 5000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        Handler().postDelayed(
            {
                val i = Intent(this, IntroductionActivity::class.java)
                startActivity(i)
                finish()
            }, SPLASH_TIME_OUT)

    }
}
