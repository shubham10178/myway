package com.fluper.seeway.panels.passenger.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.databinding.ActivityVahicleTypeBinding
import com.fluper.seeway.panels.passenger.ui.adapter.VehicleTypeAdapter

class VahicleTypeActivity : BaseActivity() ,VehicleTypeAdapter.OnClickVehiclelistener{
    private lateinit var mContext: Context
    private var binding: ActivityVahicleTypeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vahicle_type)
        mContext=this;
        binding?.apply {
            clickHandler = ClickHandler(this@VahicleTypeActivity)
            rvVehicles.adapter = VehicleTypeAdapter(mContext)
        }


    }



    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    inner class ClickHandler(mContext: Context) {


        fun onclickBack(view: View) {
            finish()
        }

    }

    override fun onClickVehicle(position: Int) {

    }

}