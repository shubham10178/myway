package com.fluper.seeway.utilitarianFiles

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Spinner
import com.fluper.seeway.R

object SpinnerUtil {
    //    Set item in spinner according to the position
    fun setItemAtPosition(spinner: Spinner, array: ArrayList<String>, value: String) {
        for (i in 0 until array.size) {
            if (array[i].equals(value, ignoreCase = true)) {
                spinner.setSelection(i)
                break
            } else
                spinner.setSelection(array.size - 1)
        }
    }

    //set the data to the spinner
    fun setSpinner(spinner: Spinner, array: ArrayList<String>, context: Context) {
        spinner.dropDownVerticalOffset = 100
        spinner.setPopupBackgroundResource(R.drawable.bg_spinner_popup)
        // Creating adapter for spinner
        val spinnerArrayAdapter: ArrayAdapter<String?> =
            object : ArrayAdapter<String?>(
                context, R.layout.spinner_item, array.toList()
            ) {
                // Disable the first item from Spinner
                // First item will be use for hint
                /*override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }*/

                /*override fun getDropDownView(
                    position: Int, convertView: View?,
                    parent: ViewGroup
                ): View {
                    var view =
                        super.getDropDownView(position, convertView, parent)
                    val tv = view as CheckedTextView
                    if (position == 0) {
                        // Set the hint text color gray
                        //tv.setTextColor(context.resources.getColor(R.color.colorAccent))
                        tv.isClickable = false
                        view = tv
                    }*//* else {
                        //tv.setTextColor(context.resources.getColor(R.color.black))
                        tv.visibility = View.VISIBLE
                        view = tv
                    }*//*
                    return view
                }*/
            }
        // Drop down layout style - list view with radio button
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Creating adapter for spinner
            //val dataAdapter = ArrayAdapter(context, R.layout.spinner_item, array)
        // Drop down layout style - list view with radio button
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        spinner.adapter = spinnerArrayAdapter
    }
}