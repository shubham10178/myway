package com.fluper.seeway.utilitarianFiles

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import org.w3c.dom.CDATASection
import org.w3c.dom.CharacterData
import org.w3c.dom.Text

fun AppCompatActivity.hideStatusBarWithBackground() {
    window.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        } else {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun AppCompatActivity.statusBarFullScreenWithBackground() {
    window.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //or View.SYSTEM_UI_FLAG_FULLSCREEN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        } else {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun AppCompatActivity.statusBarColor(color: Int) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        if (color == android.R.color.white) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                // View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR: make the text of status bar to black
                this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                this.window.statusBarColor = this.resources.getColor(color)
            }
        } else {
            this.window.statusBarColor = this.resources.getColor(color)
        }
    }
}

fun AppCompatActivity.statusBarTextColor() {

}

var toast: Toast? = null
fun AppCompatActivity.showToast(message: String) {
    if (toast != null) toast!!.cancel()
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun Fragment.showToast(message: String) {
    if (toast != null) toast!!.cancel()
    Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
}

var toast1: Toast? = null
fun showToast(context: Context?, message: String) {
    if (toast1 != null) toast1!!.cancel()
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

var gson: Gson? = null
fun getGsonInstance(): Gson {

    if (gson == null)
        gson = Gson()
    return gson!!
}

fun EditText.getString(): String {
    return if (this.text.toString().trim().isNotEmpty())
        this.text.toString()
    else ""
}
fun TextView.getString(): String {
    return if (this.text.toString().trim().isNotEmpty())
        this.text.toString()
    else ""
}
fun EditText.isValidName():Boolean{
    return if (this.getString().isEmpty()) {
        showToast(this.context,"Please enter name")
        false
    }else
        true
}
fun EditText.isValidNumbersWithCharacters() :Boolean{
    return if (this.getString().isEmpty()) {
        showToast(this.context,"Please enter valid number")
        false
    }else
        true
}
fun EditText.isValidNumbersOnly():Boolean{
    return when {
        this.getString().isEmpty() -> {
            showToast(this.context,"Please enter number")
            false
        }
        !this.getString().isDigitsOnly()-> {
            showToast(this.context,"Please enter valid numbers")
            false
        }
        else -> true
    }
}
fun EditText.isValidEmail(): Boolean {
    return when {
        this.getString().trim().isEmpty()-> {
            showToast(this.context,"Please enter email")
            false
        }
        !Patterns.EMAIL_ADDRESS.matcher(this.getString()).matches() -> {
            showToast(this.context,"Please enter valid email address")
            false
        }
        else -> true
    }
}
fun EditText.isValidMobileNumber(): Boolean{
    return when{
        this.getString().isEmpty()->{
            showToast(this.context,"Please enter contact number")
            false
        }
        !Patterns.PHONE.matcher(this.getString()).matches() ->{
            showToast(this.context,"Please enter valid contact number")
            false
        }
        else -> true
    }
}
