package com.fluper.seeway.database.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetVehicleTypesResponseModel(
    val message: String? = "", // AddOns Variants List
    val response: ArrayList<Response?>? = ArrayList()
):Parcelable {
    @Parcelize
    data class Response(
        val _id: String? = "", // 5f4764eb957e8914dcd6bf40
        val name: String? = "" // vehicle type1
    ):Parcelable
}