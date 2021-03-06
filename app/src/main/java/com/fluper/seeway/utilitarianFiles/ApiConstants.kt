package com.fluper.seeway.utilitarianFiles

interface ApiConstants {
    companion object{
        const val BaseUrl = "http://15.207.219.174:3000/"

        const val LogIn = "user/login"
        const val SelectUserType = "user/select_user_type"
        const val UploadImage = "/api/upload_image"

        const val Logout = "user/logout"
        const val Register = "user/register"
        const val ResendOtp = "user/resend_otp"
        const val VerifyOtp = "user/otp_verify"
        const val ForgotPassword = "user/forget_password"
        const val ResetPassword = "user/reset_password"
        const val CompleteProfile = "user/complete_profile"
        const val GetVehicleType = "user/get_vehicle_type"
        const val AddVehicleType = "user/add_vehicle"
        const val DeleteVehicle = "user/delete_vehicle"
        const val AcceptTermsConditions = "user/accept_term_condition"
    }
}