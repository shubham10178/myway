package com.fluper.seeway.utilitarianFiles.customdialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.fluper.seeway.R

class AddTipForDriverDailog(context: Context) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dailog_add_tip_for_driver)


    }

}