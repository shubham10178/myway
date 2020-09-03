package com.fluper.seeway.database.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ProfileCreationResponse(
    val message: String? = "", // Login Successful
    val response: Response? = Response()
) : Parcelable {
    @Parcelize
    data class Response(
        val _id: String? = "", // 5f49980f67fb861306b3078e
        val access_token: String? = "", // cf2897f64dbb73902c3c83d1b3c8af13
        val bank_detail: @RawValue BankDetail? = BankDetail(),
        val business_detail: @RawValue BusinessDetail? = BusinessDetail(),
        val country_code: String? = "", // +91
        val created_on: String? = "", // 1598625332062
        val device_token: String? = "", // dsfdsf
        val device_type: Int? = 0, // 1
        val driving_licence: String? = "", // https://seewayapp.s3.ap-south-1.amazonaws.com/uploads/1598662483956/pro%20-%20Copy.png
        val email: String? = "", // aksh@gmail.com
        val first_name: String? = "", // Aman
        val gender: String? = "", // Male
        val gexpay_account: String? = "", // null
        val id_proof: String? = "", // https://seewayapp.s3.ap-south-1.amazonaws.com/uploads/1598662483063/pro%20-%20Copy.png
        val isBlock: Int? = 0, // 0
        val isProfileCreated: String? = "", // 1
        val is_document_verified: Int? = 0, // 0
        val is_term_accept: Int? = 0, // 1
        val is_mobile_verified: String? = "", // 1
        val is_email_verified: String? = "", //1
        val last_name: String? = "", // Kumar
        val latitude: Double? = 0.0, // 77.7440948
        val location: @RawValue Location? = Location(),
        val longitude: Double? = 0.0, // 27.4642568
        val mobile_number: String? = "", // 2582582580
        val password: String? = "", // 3c989d2de553b47af88d96bf752c3266
        val profile_image: String? = "", // https://seewayapp.s3.ap-south-1.amazonaws.com/uploads/1598662485268/pro.png
        val social_type: String? = "", // 0
        val somking_status: String? = "", // 2
        val upload_permission: String? = "", // https://seewayapp.s3.ap-south-1.amazonaws.com/uploads/1598662484813/pro%20-%20Copy.png
        val user_permission: Int? = 0, // 1
        val user_type: Int? = 0, // 2
        val vat_number: String? = "", // null
        val vehicleDetails: @RawValue List<VehicleDetail?>? = listOf(),
        val vehicle_type_id: String? = "",//is coming as String in profile completion
        val verificationCode: String? = ""
    ) :Parcelable{
        @Parcelize
        data class BankDetail(
            val account_holdar_name: String? = "", // Aman kumar
            val account_number: String? = "", // null
            val branch_name: String? = "", // noidabranch
            val ifsc_code: String? = "" // 4321indus
        ):Parcelable
        @Parcelize
        data class BusinessDetail(
            val busines_address1: String? = "", // noida1
            val busines_address2: String? = "", // noida1
            val business_city: String? = "", // noida3
            val business_country: String? = "", // india
            val business_name: String? = "" // geeked
        ):Parcelable
        @Parcelize
        data class Location(
            val coordinates: List<Double?>? = listOf(),
            val type: String? = "" // Point
        ):Parcelable
        @Parcelize
        data class VehicleDetail(
            val _id: String? = "", // 5f49a3e867fb861306b3078f
            val car_document: String? = "", // https://seewayapp.s3.ap-south-1.amazonaws.com/uploads/1598661608508/pro.png
            val certificate_date: String? = "", // 29-08-2020
            val description: String? = "", // jvjhhjh
            val isBlock: Int? = 0, // 0
            val is_saved: Int? = 0, // 1
            val is_verify: Int? = 0, // 0
            val no_of_seats: Int? = 0, // 2
            val relation_with: String? = "", // Owner
            val user_id: String? = "", // 5f49980f67fb861306b3078e
            val vehicle_color: String? = "", // blue
            val vehicle_image: List<String?>? = listOf(),
            val vehicle_model: String? = "", // Bugatti veyron
            val vehicle_number: String? = "" // UP7824
        ):Parcelable
        @Parcelize
        data class VehicleTypeId(
            val _id: String? = "", // 5f4764eb957e8914dcd6bf40
            val name: String? = "" // vehicle type1
        ):Parcelable
    }
}