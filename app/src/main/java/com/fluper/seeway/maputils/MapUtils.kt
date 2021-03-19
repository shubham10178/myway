package com.fluper.seeway.maputils

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

object MapUtils {

    private val TAG: String = "MapUtils:::"


    fun getAddressFromLocation(mContext: Context, currentLatLng: LatLng) :String{
        val geocoder = Geocoder(mContext, Locale.getDefault())
        var address = ""
        try {
            val addresses =
                geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1)
            if (addresses != null && addresses.size > 0) {
                address = addresses[0].getAddressLine(0)
                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            }
        } catch (e: IOException) {
            Log.d(TAG, "getAddressFromLocation:::$e")
        }
        return address
    }
}