package com.fluper.seeway.networks

import com.fluper.seeway.database.beans.MessageResponse
import com.fluper.seeway.panels.driver.model.AddVehicleResponseModel
import com.fluper.seeway.panels.driver.model.GetVehicleTypesResponseModel
import com.fluper.seeway.panels.driver.model.RegisterResponseModel
import com.fluper.seeway.utilitarianFiles.ApiConstants
import com.fluper.seeway.utilitarianFiles.Constants
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST(ApiConstants.ForgotPassword)
    fun forgotPassword(
        @Field("country_code")country_code:String,
        @Field("mobile_number")mobile_number:String,
        @Field("email")email:String,
        @Field("user_type")user_type:String
    ): Observable<RegisterResponseModel>

    @FormUrlEncoded
    @POST(ApiConstants.VerifyOtp)
    fun verifyOtp(
        @Field("user_id")user_id:String,
        @Field("verificationCode")verificationCode:String
    ): Observable<RegisterResponseModel>

    @FormUrlEncoded
    @POST(ApiConstants.ResendOtp)
    fun resendOtp(
        @Field("mobile_number")mobile_number:String,
        @Field("country_code")country_code:String
    ): Observable<RegisterResponseModel>

    @FormUrlEncoded
    @POST(ApiConstants.ResetPassword)
    fun resetPassword(
        @Field("user_id")user_id:String,
        @Field("password")password:String
    ): Observable<RegisterResponseModel>

    @FormUrlEncoded
    @POST(ApiConstants.Register)
    fun signInSignUp(
        @Field("email")email:String,
        @Field("country_code")country_code:String,
        @Field("mobile_number")mobile_number:String,
        @Field("device_token")device_token:String,
        @Field("password")password:String,
        @Field("device_type")device_type:String,
        @Field("user_type")user_type:String
    ): Observable<RegisterResponseModel>

    @FormUrlEncoded
    @POST(ApiConstants.Logout)
    fun logout(
        @Header("access_token")access_token:String
    ):Observable<MessageResponse>

    @FormUrlEncoded
    @POST(ApiConstants.AcceptTermsConditions)
    fun acceptTermsAndConditions(
        @Field("user_id")user_id:String,
        @Field("value")value:String
    ): Observable<RegisterResponseModel>

    @GET(ApiConstants.GetVehicleType)
    fun getVehicleType():Observable<GetVehicleTypesResponseModel>

    @Multipart
    @POST(ApiConstants.AddVehicleType)
    fun addVehicle(
        @Header("access_token") access_token: String,
        @Part("vehicle_number")vehicle_number :RequestBody,
        @Part("vehicle_model")vehicle_model :RequestBody,
        @Part("vehicle_color")vehicle_color :RequestBody,
        @Part vehicle_imgae :ArrayList<MultipartBody.Part>? = null,
        @Part("no_of_seats")no_of_seats :RequestBody,
        @Part car_document :MultipartBody.Part? = null,
        @Part("certificate_date")certificate_date :RequestBody,
        @Part("relation_with")relation_with :RequestBody,
        @Part("description")description :RequestBody
    ):Observable<AddVehicleResponseModel>

    @FormUrlEncoded
    @POST(ApiConstants.DeleteVehicle)
    fun deleteVehicle(
        @Field("_id")_id:String,
        @Field("user_id")user_id:String
    ):Observable<AddVehicleResponseModel>

    @Multipart
    @POST(ApiConstants.CompleteProfile)
    fun profile(
        @Header("access_token") access_token: String,
        @Part("first_name")first_name :RequestBody,
        @Part("last_name")last_name :RequestBody,
        @Part("city")city :RequestBody,
        @Part id_proof :MultipartBody.Part? = null,

        @Part("account_holdar_name")account_holdar_name :RequestBody,
        @Part("account_number")account_number :RequestBody,
        @Part("ifsc_code")ifsc_code :RequestBody,
        @Part("branch_name")branch_name :RequestBody,

        @Part("business_name")business_name :RequestBody,
        @Part("business_address1")business_address1 :RequestBody,
        @Part("business_address2")business_address2 :RequestBody,
        @Part("business_city")business_city :RequestBody,
        @Part("business_country")business_country :RequestBody,
        @Part("vat_number")vat_number :RequestBody,

        @Part("card_number")card_number :RequestBody,
        @Part("expiry_date")expiry_date :RequestBody,
        @Part("cvv")cvv :RequestBody,
        @Part("gexpay_account")gexpay_account :RequestBody,

        @Part("gender")gender :RequestBody,
        @Part("somking_status")somking_status :RequestBody,
        @Part("vehicle_type_id")vehicle_type_id :RequestBody,
        @Part driving_licence :MultipartBody.Part? = null,

        @Part("user_id")user_id :RequestBody,
        @Part("user_permission")user_permission :RequestBody,
        @Part upload_permission :MultipartBody.Part? = null,
        @Part profile_image :MultipartBody.Part? = null
    ):Observable<RegisterResponseModel>

}