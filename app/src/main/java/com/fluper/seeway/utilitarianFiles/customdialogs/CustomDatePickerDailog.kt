package com.fluper.seeway.utilitarianFiles.customdialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.fluper.seeway.R
import kotlinx.android.synthetic.main.custom_date_picker.*
import kotlinx.android.synthetic.main.driver_profile_approval_alert1.*

class CustomDatePickerDailog(context: Context) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_date_picker);

        button_no.setOnClickListener {
            dismiss()
        }
        button_yes.setOnClickListener {

        }

    }


    fun getCurrentDate():String{
        return "${datePicker.dayOfMonth}/${datePicker.month+1}+/+${datePicker.year}"
    }
}