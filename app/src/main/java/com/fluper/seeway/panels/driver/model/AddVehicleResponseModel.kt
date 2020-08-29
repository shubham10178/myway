package com.fluper.seeway.panels.driver.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddVehicleResponseModel(
    val message: String? = "", // Vehicle add  Successfully
    val response: ArrayList<Response?>? = ArrayList()
) :Parcelable{
    @Parcelize
    data class Response(
        val _id: String = "", // 5f49a3e867fb861306b3078f
        val car_document: String? = "", // https://seewayapp.s3.ap-south-1.amazonaws.com/uploads/1598661608508/pro.png
        val certificate_date: String? = "", // 29-08-2020
        val description: String? = "", // jvjhhjh
        val isBlock: Int? = 0, // 0
        val is_saved: Int? = 0, // 1
        val is_verify: Int? = 0, // 0
        val no_of_seats: Int? = 0, // 2
        val relation_with: String? = "", // Owner
        val user_id: String = "", // 5f49980f67fb861306b3078e
        val vehicle_color: String? = "", // blue
        val vehicle_image: ArrayList<String?>? = ArrayList(),
        val vehicle_model: String? = "", // Bugatti veyron
        val vehicle_number: String? = "" // UP7824
    ):Parcelable
}