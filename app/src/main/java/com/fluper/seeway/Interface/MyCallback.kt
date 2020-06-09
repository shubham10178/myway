package com.fluper.seeway.Interface

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.fluper.seeway.activity.LoginActivity

interface MyCallback {
    fun onItemClicked()

    val context: Context

    fun toast(message: String) {
      
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

var listener: MyCallback? = null

fun setOnItemClickListener(callback: MyCallback?) {
    listener = callback

}