package com.fluper.seeway.onBoard.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class AddVehicleInfoModel(
    var vehicleNumber: String? = "",
    var vehicleModelName: String? = "",
    var viArrayList: @RawValue ArrayList<ImageUploadModel>? = ArrayList(),
    var vehicleColor: String? = "",
    var vehicleRegistrationDate: String? = "",
    var vehicleAvailSheets: String? = "",
    var vdArrayList: @RawValue ArrayList<ImageUploadModel>? = ArrayList(),
    var vehicleRelationship: String? = "",
    var vehicleRelationDescribed: String? = ""
) : Parcelable
/*@Parcelize
class AddVehicleInfoModel(
    val vehicleNumber: ArrayList<String>? = ArrayList(),
    val vehicleModelName: ArrayList<String>? = ArrayList(),
    val viArrayList: ArrayList<ImageUploadModel>? = ArrayList(),
    val vehicleColor: ArrayList<String>? = ArrayList(),
    val vehicleRegistrationDate: ArrayList<String>? = ArrayList(),
    val vehicleAvailSheets: ArrayList<String>? = ArrayList(),
    val vdArrayList: ArrayList<ImageUploadModel>? = ArrayList(),
    val vehicleRelationship: ArrayList<ImageUploadModel>? = ArrayList(),
    val vehicleRelationDescribed: ArrayList<String>? = ArrayList()
) : Parcelable*/
