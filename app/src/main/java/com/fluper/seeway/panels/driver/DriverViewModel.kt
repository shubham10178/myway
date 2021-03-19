package com.fluper.seeway.panels.driver

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.fluper.seeway.base.BaseViewModel
import com.fluper.seeway.database.beans.MessageResponse
import com.fluper.seeway.database.model.AddVehicleResponseModel
import com.fluper.seeway.database.model.GetVehicleTypesResponseModel
import com.fluper.seeway.database.model.ProfileCreationResponse
import com.fluper.seeway.database.model.RegisterResponseModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DriverViewModel : BaseViewModel() {
    var throwable = MutableLiveData<Throwable>()
    var forgotPassword = MutableLiveData<ProfileCreationResponse>()
    var otpVerify = MutableLiveData<ProfileCreationResponse>()
    var resendOtp = MutableLiveData<ProfileCreationResponse>()
    var resetPassword = MutableLiveData<ProfileCreationResponse>()

    var signInSignUp = MutableLiveData<RegisterResponseModel>()
    var termsAndConditions = MutableLiveData<RegisterResponseModel>()
    var getVehicleTypes = MutableLiveData<GetVehicleTypesResponseModel>()
    var addVehicles = MutableLiveData<AddVehicleResponseModel>()
    var deleteVehicles = MutableLiveData<AddVehicleResponseModel>()
    var profileCreation = MutableLiveData<ProfileCreationResponse>()

    var logout = MutableLiveData<MessageResponse>()

    private fun onErrors(it: Throwable) {
        throwable.value = it
    }

    @SuppressLint("CheckResult")
    fun logout(access_token: String) {
        apiInterface.logout(
            access_token = access_token
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onLogout(it) },
                { onErrors(it) }
            )
    }

    private fun onLogout(it: MessageResponse) {
        logout.value = it
    }

    @SuppressLint("CheckResult")
    fun forgotPassword(
        country_code: String,
        mobile_number: String,
        email: String,
        user_type: String
    ) {
        apiInterface.forgotPassword(
            country_code = country_code,
            mobile_number = mobile_number,
            email = email,
            user_type = user_type
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onForgotPassword(it) },
                { onErrors(it) }
            )
    }

    private fun onForgotPassword(it: ProfileCreationResponse) {
        forgotPassword.value = it
    }


    @SuppressLint("CheckResult")
    fun otpVerify(
        user_id: String,
        verificationCode: String
    ) {
        apiInterface.verifyOtp(
            user_id = user_id,
            verificationCode = verificationCode
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onOtpVerify(it) },
                { onErrors(it) }
            )
    }

    private fun onOtpVerify(it: ProfileCreationResponse) {
        otpVerify.value = it
    }


    @SuppressLint("CheckResult")
    fun resendOtp(
        user_id: String
    ) {
        apiInterface.resendOtp(
            user_id = user_id
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onOtpResend(it) },
                { onErrors(it) }
            )
    }

    private fun onOtpResend(it: ProfileCreationResponse) {
        resendOtp.value = it
    }


    @SuppressLint("CheckResult")
    fun resetPassword(
        user_id: String,
        password: String
    ) {
        apiInterface.resetPassword(
            user_id = user_id,
            password = password
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onResetPassword(it) },
                { onErrors(it) }
            )
    }

    private fun onResetPassword(it: ProfileCreationResponse) {
        resetPassword.value = it
    }


    @SuppressLint("CheckResult")
    fun termsAndCondition(
        user_id: String,
        value: String
    ) {
        apiInterface.acceptTermsAndConditions(
            user_id = user_id,
            value = value
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onTermsAndConds(it) },
                { onErrors(it) }
            )
    }

    private fun onTermsAndConds(it: RegisterResponseModel) {
        termsAndConditions.value = it
    }


    @SuppressLint("CheckResult")
    fun signInSignUp(
        email: String,
        country_code: String,
        mobile_number: String,
        device_token: String,
        password: String,
        device_type: String,
        user_type: String
    ) {
        apiInterface.signInSignUp(
            email = email,
            country_code = country_code,
            mobile_number = mobile_number,
            device_token = device_token,
            password = password,
            device_type = device_type,
            user_type = user_type
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSignInSignUp(it) },
                { onErrors(it) }
            )
    }

    private fun onSignInSignUp(it: RegisterResponseModel) {
        signInSignUp.value = it
    }


    @SuppressLint("CheckResult")
    fun getVehicleType() {
        apiInterface.getVehicleType().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onVehicleTypes(it) },
                { onErrors(it) }
            )
    }

    private fun onVehicleTypes(it: GetVehicleTypesResponseModel) {
        getVehicleTypes.value = it
    }

    @SuppressLint("CheckResult")
    fun addVehicles(
        access_token: String,
        vehicle_number: RequestBody,
        vehicle_model: RequestBody,
        vehicle_color: RequestBody,
        vehicle_imgae: ArrayList<MultipartBody.Part>?,
        no_of_seats: RequestBody,
        car_document: MultipartBody.Part?,
        certificate_date: RequestBody,
        relation_with: RequestBody,
        description: RequestBody
    ) {
        apiInterface.addVehicle(
            access_token = access_token,
            vehicle_number = vehicle_number,
            vehicle_model = vehicle_model,
            vehicle_color = vehicle_color,
            vehicle_imgae = vehicle_imgae,
            no_of_seats = no_of_seats,
            car_document = car_document,
            certificate_date = certificate_date,
            relation_with = relation_with,
            description = description
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onAddVehicles(it) },
                { onErrors(it) }
            )
    }

    private fun onAddVehicles(it: AddVehicleResponseModel) {
        addVehicles.value = it
    }

    @SuppressLint("CheckResult")
    fun deleteVehicle(
        _id: String,
        user_id: String
    ) {
        apiInterface.deleteVehicle(
            _id = _id,
            user_id = user_id
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onDeleteVehicle(it) },
                { onErrors(it) }
            )
    }

    private fun onDeleteVehicle(it: AddVehicleResponseModel) {
        deleteVehicles.value = it
    }

    @SuppressLint("CheckResult")
    fun profile(
        access_token: String,
        user_type: RequestBody,
        first_name: RequestBody,
        last_name: RequestBody,
        city: RequestBody,
        id_proof: MultipartBody.Part?,

        account_holdar_name: RequestBody,
        account_number: RequestBody,
        ifsc_code: RequestBody,
        branch_name: RequestBody,

        business_name: RequestBody,
        business_address1: RequestBody,
        business_address2: RequestBody,
        business_city: RequestBody,
        business_country: RequestBody,
        vat_number: RequestBody,

        card_number: RequestBody,
        expiry_date: RequestBody,
        cvv: RequestBody,
        gexpay_account: RequestBody,

        gender: RequestBody,
        smoking_status: RequestBody,
        vehicle_type_id: RequestBody?,
        driving_licence: MultipartBody.Part?,

        user_id: RequestBody,
        user_permission: RequestBody,
        upload_permission: MultipartBody.Part?,
        profile_image: MultipartBody.Part?,

        email: RequestBody?,
        country_code: RequestBody?,
        mobile_number: RequestBody?
    ) {
        apiInterface.profile(
            access_token = access_token,
            user_type = user_type,
            first_name = first_name,
            last_name = last_name,
            city = city,
            id_proof = id_proof,

            account_holdar_name = account_holdar_name,
            account_number = account_number,
            ifsc_code = ifsc_code,
            branch_name = branch_name,

            business_name = business_name,
            business_address1 = business_address1,
            business_address2 = business_address2,
            business_city = business_city,
            business_country = business_country,
            vat_number = vat_number,

            card_number = card_number,
            expiry_date = expiry_date,
            cvv = cvv,
            gexpay_account = gexpay_account,

            gender = gender,
            somking_status = smoking_status,
            vehicle_type_id = vehicle_type_id,
            driving_licence = driving_licence,

            user_id = user_id,
            user_permission = user_permission,
            upload_permission = upload_permission,
            profile_image = profile_image,

            email = email,
            country_code = country_code,
            mobile_number = mobile_number
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onProfileCreate(it) },
                { onErrors(it) }
            )
    }

    private fun onProfileCreate(it: ProfileCreationResponse) {
        profileCreation.value = it
    }
}