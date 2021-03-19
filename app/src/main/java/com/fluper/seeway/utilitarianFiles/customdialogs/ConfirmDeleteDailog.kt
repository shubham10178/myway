package com.fluper.seeway.utilitarianFiles.customdialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.fluper.seeway.R

class ConfirmDeleteDailog(mContext: Context) : Dialog(mContext) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_delete_alert)
    }

}