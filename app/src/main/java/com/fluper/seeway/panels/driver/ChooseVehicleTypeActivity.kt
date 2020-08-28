package com.fluper.seeway.panels.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.base.BaseFragment
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.fragment_choose_vehicle_type.*

class ChooseVehicleTypeActivity : BaseActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.fragment_choose_vehicle_type)
        initClickListener()
    }

    private fun initClickListener() {
        img_back.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.img_back->{
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}