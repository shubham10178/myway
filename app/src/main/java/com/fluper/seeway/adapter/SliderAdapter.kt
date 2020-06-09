package com.fluper.seeway.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.Constraints
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.fluper.seeway.activity.LoginActivity
import com.fluper.seeway.R

class SliderAdapter : PagerAdapter{

    var context : Context
    var image : Array<Int>
    var pager : ViewPager
    lateinit var layoutInflater : LayoutInflater


    constructor( context : Context, image : Array<Int>,pager : ViewPager):super(){
        this.context =context
        this.image = image
        this.pager = pager
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view==  `object` as RelativeLayout

    override fun getCount(): Int = image.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
       var imageView : ImageView


        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view : View= layoutInflater.inflate(R.layout.slider_image_item,container,false)
        imageView=view.findViewById(R.id.slider)


        imageView.setBackgroundResource(image[position])
        container!!.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container!!.removeView(`object` as RelativeLayout)
    }
}