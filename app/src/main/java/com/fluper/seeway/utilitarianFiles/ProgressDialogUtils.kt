package com.fluper.seeway.utilitarianFiles

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.fluper.seeway.R

class ProgressDialogUtils {
    private var c: Context? = null
    private var mDialog: Dialog? = null

    companion object {
        var progressDialog: ProgressDialogUtils? = null
        fun getInstance(): ProgressDialogUtils {
            if (progressDialog == null) {
                progressDialog = ProgressDialogUtils()
            }
            return progressDialog as ProgressDialogUtils
        }
    }

    fun showProgress(context: Context, cancelable: Boolean) {
        if (mDialog == null || context != c) {
            mDialog = Dialog(context)
            c = context
            mDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mDialog?.setContentView(R.layout.custom_progress_dailog)
            mDialog?.setCancelable(cancelable)
            mDialog?.setCanceledOnTouchOutside(cancelable)
        }
        mDialog?.show()
    }

    fun hideProgress() {
        mDialog?.dismiss()
    }

    fun dispose() {
        if (progressDialog != null) {
            progressDialog == null
        }
    }
}