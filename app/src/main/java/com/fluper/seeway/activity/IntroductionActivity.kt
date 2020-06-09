package com.fluper.seeway.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.GONE
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.fluper.seeway.R
import com.fluper.seeway.adapter.SliderAdapter
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_introduction.*
import me.relex.circleindicator.CircleIndicator


class IntroductionActivity : AppCompatActivity() {

    lateinit var btn_next : Button
    lateinit var btn_skip : Button
    lateinit var btn_next1 : Button

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

        btn_next= findViewById(R.id.btn_next)
        btn_skip= findViewById(R.id.btn_skip)
        btn_next1= findViewById(R.id.btn_next1)


        var indicator : CircleIndicator = findViewById(R.id.indicator)
        indicator.createIndicators(5,0);

        indicator.animatePageSelected(0)
        indicator.setViewPager(pager);
        pager.adapter = adapter


        btn_next1.setOnClickListener {

                val i  = Intent(this@IntroductionActivity,LoginActivity::class.java)
                startActivity(i)

        }

        btn_next.setOnClickListener {
            pager.setCurrentItem(pager.currentItem+1, true);
        }

        btn_skip.setOnClickListener {

            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
        }


        pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                if(position == 2){
                    btn_skip.setVisibility(View.INVISIBLE)
                    btn_next1.setVisibility(View.VISIBLE)
                    btn_next.setVisibility(View.GONE)

                }
                else
                {
                    btn_skip.setVisibility(View.VISIBLE)
                    btn_next1.setVisibility(View.GONE)
                    btn_next.setVisibility(View.VISIBLE)

                }
            }

            override fun onPageSelected(position: Int) {
                // if you want the second page, for example


            }

            override fun onPageScrollStateChanged(state: Int) {

            }


        })

    }
    }



