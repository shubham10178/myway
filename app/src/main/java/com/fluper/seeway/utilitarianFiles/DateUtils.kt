package com.fluper.seeway.utilitarianFiles

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {

    @JvmStatic
    fun getDateTimeFromIsoString(isoString:String):String{
        /*val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())*/
        val isoFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")
        val timestamp = isoFormat.parse(isoString)!!.time
        val date : Date = Date(timestamp)

        val differenceInMillisecond = Calendar.getInstance().timeInMillis - date.time

        val diffInDays: Long = TimeUnit.MILLISECONDS.toDays(differenceInMillisecond)
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(differenceInMillisecond)
        val diffInMin: Long = TimeUnit.MILLISECONDS.toMinutes(differenceInMillisecond)
        val diffInSec: Long = TimeUnit.MILLISECONDS.toSeconds(differenceInMillisecond)

        /*val expectedFormat = SimpleDateFormat("dd MMM YYYY '|' h:mm a",Locale.getDefault())
        return  expectedFormat.format(date).toString()*/

        return when{
            diffInSec<=60 -> { "$diffInSec Seconds Ago" }
            diffInMin<=60 -> { "$diffInMin Minutes Ago" }
            diffInHours<=24 -> { "$diffInHours Hours Ago" }
            diffInDays<=365 -> { "$diffInDays Days Ago" }
            else -> {"NAN"}
        }
    }
}