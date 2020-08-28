package com.fluper.seeway.panels.passenger

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.fluper.seeway.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PassengerMainBottomSheetFragment : BottomSheetDialogFragment() {

    //private var propertyList: PropertyListModel.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //for making sheet background transparent
        //setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
        /*if (!arguments?.isEmpty!!)
            propertyList =
                arguments?.getParcelable<PropertyListModel.Data>(Constants.Property_List)!!*/
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.bottom_fragment_passenger_main, null)
        dialog.setContentView(view)

        /*if (!propertyList?.image.isNullOrEmpty())
            Picasso.get().load(propertyList?.image).into(dialog.ivPropertyImage)
        if (!propertyList?.property_title.isNullOrEmpty())
            dialog.tvPropertyTitle.text = propertyList?.property_title
        if (!propertyList?.location.isNullOrEmpty())
            dialog.tvPropertyLocation.text = propertyList?.location
        if (!propertyList?.property_type.isNullOrEmpty())
            dialog.tvPropertyType.text = propertyList?.property_type*/

        /*dialog.btnCancel.setOnClickListener {
            dismiss()
        }
        dialog.btnConfirm.setOnClickListener {
            dismiss()
        }*/
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO, true)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                /*Do it later*/
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when {
                    BottomSheetBehavior.STATE_EXPANDED == newState -> {
                        /*Do it later*/
                    }
                    BottomSheetBehavior.STATE_COLLAPSED == newState -> {
                        /*Do it later*/
                    }
                    BottomSheetBehavior.STATE_HIDDEN == newState -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING == newState -> {
                        /*Do it later*/
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED == newState -> {
                        /*Do it later*/
                    }
                    BottomSheetBehavior.STATE_SETTLING == newState -> {
                        /*Do it later*/
                    }
                    else -> {
                        /*Do it later*/
                    }
                }
            }
        })
        return dialog
    }
}