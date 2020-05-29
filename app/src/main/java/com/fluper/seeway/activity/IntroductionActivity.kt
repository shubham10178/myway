package com.fluper.seeway.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.fluper.seeway.R
import com.fluper.seeway.adapter.SliderAdapter
import kotlinx.android.synthetic.main.activity_introduction.*

class IntroductionActivity : AppCompatActivity() {

    var image : Array<Int> = arrayOf(
        R.drawable.walk_1,
        R.drawable.walk_2,
        R.drawable.walk_3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        var adapter : PagerAdapter = SliderAdapter(this, image,pager)
        pager.setAdapter(SliderAdapter(this, image,pager))

        pager.adapter = adapter
    }


}
