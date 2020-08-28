package com.fluper.seeway.onBoard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.fluper.seeway.R

class WalkThroughAdapter(var context: Context, var image: Array<Int>, var pager: ViewPager) :
    PagerAdapter() {

    lateinit var layoutInflater: LayoutInflater


    override fun isViewFromObject(view: View, `object`: Any): Boolean =
        view == `object` as ConstraintLayout

    override fun getCount(): Int = image.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView: ImageView


        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.slider_image_item, container, false)
        imageView = view.findViewById(R.id.slider)


        imageView.setBackgroundResource(image[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}