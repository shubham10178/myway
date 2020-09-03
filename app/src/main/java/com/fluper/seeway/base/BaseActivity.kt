package com.fluper.seeway.base

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fluper.seeway.networks.ApiInterface
import com.fluper.seeway.networks.RetrofitUtil
import com.fluper.seeway.database.model.SharedPreferenceUtils

open class BaseActivity : AppCompatActivity() {
    /*lateinit var userViewModel: UserViewModel*/
    val apiInterface : ApiInterface by lazy {
        RetrofitUtil.createBaseApiService()
    }
    val sharedPreference: SharedPreferenceUtils by lazy {
        SharedPreferenceUtils.getInstance(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        Log.e("BaseCallback", "OnStartCalled")
    }

    override fun onStop() {
        super.onStop()
        Log.e("BaseCallback", "OnStopCalled")
    }
}