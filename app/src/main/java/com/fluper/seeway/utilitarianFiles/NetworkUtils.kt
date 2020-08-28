package com.fluper.seeway.utilitarianFiles

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
    fun isInternetAvailable(context: Context): Boolean {
        var status: Boolean = false
        return try {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            //should check null because in airplane mode it will be null
            status = netInfo != null && netInfo.isConnected
            //  if (!status) Toast.makeText(context,context.resources.getString(R.string.error_no_internet_connection),Toast.LENGTH_SHORT).show()
            status
        } catch (e: NullPointerException) {
            e.printStackTrace()
            false
        }
    }
}