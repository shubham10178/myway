package com.fluper.seeway.utilitarianFiles

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

val String.isValidMobile: Boolean
    get() = this.length in 7..12

val String.isValidPassword: Boolean
    get() = this.length >= 8

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


fun EditText.firstSpaceEdt(){
    val it =this
    it.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(s!!.isNotEmpty()){

            }
        }

        override fun afterTextChanged(s: Editable?) {
            if (it.text.toString().startsWith(" ")) {
                it.setText("")
            } else {

            }
        }
    })
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

fun AppCompatActivity.showToast(message: String) {
    val str= if (message.isNotEmpty()) message else ""
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    val str= if (message.isNotEmpty()) message else ""
    Toast.makeText(this.activity, str, Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context?, message: String) {
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
            showToast(this.context,"Please enter mobile number")
            false
        }
        !this.getString().isDigitsOnly()-> {
            showToast(this.context,"Please enter valid mobile number")
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

fun AppCompatActivity.getRequestBody(value: String): RequestBody {
    return value.toRequestBody("text/plain".toMediaTypeOrNull())
}

fun <E> List<E>?.toGson(): String? {
    var gson = Gson()
    return gson.toJson(this)
}
fun getOldImages(imageList: ArrayList<Any>?): List<String>? {
    return imageList?.filter { it is String }?.map { it as String }
}

fun AppCompatActivity.getMultipartBodyArrayList(imageList: ArrayList<Bitmap>?,keyName: String): ArrayList<MultipartBody.Part>? {
    var multiPartBody = ArrayList<MultipartBody.Part>()
    imageList?.forEach {
        File(bitmapToFile(it,this).path!!).let { file ->
            multiPartBody.add(
                MultipartBody.Part.createFormData(
                    keyName,
                    file.name,
                    file.asRequestBody("image/*".toMediaTypeOrNull())
                )
            )
        }
    }
    return if (multiPartBody.size == 0) null else multiPartBody
}
fun AppCompatActivity.getMultipartBody(filePath: Bitmap, keyName: String): MultipartBody.Part? {
    return File(bitmapToFile(filePath,this).path!!).let {
        MultipartBody.Part.createFormData(
            keyName, it.name,
            it.asRequestBody("image/*".toMediaTypeOrNull())
        )
    }
}
fun bitmapToFile(bitmap: Bitmap, context: Context?): Uri {
    // Get the context wrapper
    val wrapper = ContextWrapper(context)
    // Initialize a new file instance to save bitmap object
    var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
    file = File(file, "${UUID.randomUUID()}.jpg")
    try {
        // Compress the bitmap and save in jpg format
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    // Return the saved bitmap uri
    return Uri.parse(file.absolutePath)
}