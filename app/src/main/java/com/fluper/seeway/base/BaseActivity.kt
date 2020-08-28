package com.fluper.seeway.base

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    /*lateinit var userViewModel: UserViewModel*/
    lateinit var prefs: SharedPreferences
    /*val apiInterface: ApiInterface by lazy {
        RetrofitUtil.createBaseApiService()
    }*/

    override fun onStart() {
        super.onStart()
        Log.e("BaseCallback", "OnStartCalled")
    }

    override fun onStop() {
        super.onStop()
        Log.e("BaseCallback", "OnStopCalled")
    }
}