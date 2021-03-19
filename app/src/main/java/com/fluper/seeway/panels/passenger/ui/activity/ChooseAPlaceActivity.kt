package com.fluper.seeway.panels.passenger.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.fluper.seeway.R
import com.fluper.seeway.databinding.ActivityChooseAPlaceBinding

class ChooseAPlaceActivity : AppCompatActivity() {

    private var binding: ActivityChooseAPlaceBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_a_place)
        binding?.clickHandler =ClickHandler(this@ChooseAPlaceActivity)
        binding?.list = ArrayList();

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    inner class ClickHandler(private val mActivity: Activity) {

        fun onClickBack(view: View) {
            setResult(Activity.RESULT_CANCELED)
            (mActivity as ChooseAPlaceActivity).finish()
        }


    }

}