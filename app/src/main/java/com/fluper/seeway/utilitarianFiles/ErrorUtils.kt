package com.fluper.seeway.utilitarianFiles

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.fluper.seeway.R
import com.fluper.seeway.database.beans.ErrorBean
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorUtils {

    //val TAG = ErrorUtil::class.simpleName

    fun handlerGeneralError(context: Context?, throwable: Throwable) {
        //Log.e(TAG, "Error: ${throwable.message}")
        throwable.printStackTrace()

        if (context == null) return

        when (throwable) {
            //For Display Toast

            is ConnectException -> Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
            is SocketTimeoutException -> Toast.makeText(context, "Connection Lost", Toast.LENGTH_SHORT).show()
            is UnknownHostException -> Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
            is InternalError -> Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()

            is HttpException -> {
                try {
                    when (throwable.code()) {
                        401 -> {
                            //Logout
                            //forceLogout(context)
                            displayError(context, throwable)
                        }
                        403 -> {
                            displayError(context, throwable)
                        }
                        400 -> {
                            displayError(context, throwable)
                        }
                        500 ->{
                            showToast(context,"500 Internal server error")
                        }
                        else -> {
                            displayError(context, throwable)
                        }
                    }
                } catch (exception: Exception) {

                }
            } else -> {
            Toast.makeText(context, context.resources.getString(R.string.something_went_wrong),
                Toast.LENGTH_LONG).show()
        }
            //For Display SnackBar
            /*is HttpException -> {
                try {
                    when (throwable.code()) {
                        401 -> {
                            SnackbarUtils.displayError(view, throwable)
                            //logout(context)
                        }
                        else -> {
                            SnackbarUtils.displayError(view, throwable)
                        }
                    }
                } catch (exception: Exception) {
                    SnackbarUtils.somethingWentWrong(view)
                    exception.printStackTrace()
                }
            }
            is ConnectException -> SnackbarUtils.displayError(view, throwable)
            is SocketTimeoutException -> SnackbarUtils.displayError(view, throwable)
            else -> SnackbarUtils.somethingWentWrong(view)
       */

        }
    }

//    ** Perform logout for both the success and error case (force logout)

    private fun forceLogout(context: Context) {

        /*Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
        app.professionaltrainer.utils.MySharedPreference.getInstance(context).deletePreference()
        val intent = Intent(context, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)*/

        /*   SharedPreferenceUtil.getInstance(context).deletePreferences()
           val intent = Intent(context, SignInActivity::class.java)
           intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
           context.startActivity(intent)*/
    }

    private fun displayError(context: Context, exception: retrofit2.HttpException) {
        //Log.i(TAG, "displayError()")
        try {
            val errorBody = getGsonInstance().
                fromJson(exception.response()?.errorBody()?.charStream(),
                    ErrorBean::class.java)
            //  SnackbarUtils.displaySnackbar(view, errorBody.message)
            Log.e("ErrorMessage", errorBody.message)
            showToast(context,errorBody.message)
        } catch (e: Exception) {
            Log.e("MyExceptions", e.message!!)
            showToast(context,context.getString(R.string.error_exception))
        }
    }
}