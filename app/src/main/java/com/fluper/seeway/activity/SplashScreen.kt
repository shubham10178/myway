package com.fluper.seeway.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.fluper.seeway.R

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 5000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        Handler().postDelayed(
            {
                val i = Intent(this, IntroductionActivity::class.java)
                startActivity(i)
                finish()
            }, SPLASH_TIME_OUT)

    }
}
