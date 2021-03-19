package com.fluper.seeway.panels.passenger.ui.activity

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.fluper.seeway.R
import com.fluper.seeway.databinding.ActivitySearchLocationBinding
import com.fluper.seeway.panels.passenger.ui.adapter.LocationsAdapter

class SearchLocationActivity : AppCompatActivity() {

    private var binding: ActivitySearchLocationBinding? = null
    val arrayList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_search_location
        ) as ActivitySearchLocationBinding

        binding?.clickHander = ClickHander(this@SearchLocationActivity)

        arrayList.add("India")
        arrayList.add("India")
        arrayList.add("India")
        binding?.listitem = arrayList



    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    inner class ClickHander(private val mContext: Context) {

        fun onBackClick(view: View) {
            setResult(Activity.RESULT_CANCELED)
            (mContext as SearchLocationActivity).finish()
        }


    }

}