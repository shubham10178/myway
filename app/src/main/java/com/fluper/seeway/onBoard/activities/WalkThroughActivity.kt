package com.fluper.seeway.onBoard.activities

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.onBoard.adapter.WalkThroughAdapter
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.activity_walkthrough.*
import me.relex.circleindicator.CircleIndicator


class WalkThroughActivity : BaseActivity() {

    lateinit var btn_next: Button
    lateinit var btn_skip: Button
    lateinit var btn_next1: Button

    var image: Array<Int> = arrayOf(
        R.drawable.walk_1,
        R.drawable.walk_2,
        R.drawable.walk_3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)
        statusBarFullScreenWithBackground()
        val adapter: PagerAdapter = WalkThroughAdapter(this, image, pager)
        pager.adapter = WalkThroughAdapter(this, image, pager)

        btn_next = findViewById(R.id.btn_next)
        btn_skip = findViewById(R.id.btn_skip)
        btn_next1 = findViewById(R.id.btn_next1)


        val indicator: CircleIndicator = findViewById(R.id.indicator)
        indicator.createIndicators(5, 0)

        indicator.animatePageSelected(0)
        indicator.setViewPager(pager)
        pager.adapter = adapter

        btn_next1.setOnClickListener {
            startActivity(Intent(this, UserTypeActivity::class.java).apply {
                sharedPreference.firstRun = false
                this@WalkThroughActivity.finish()
            })
        }

        btn_next.setOnClickListener {
            pager.setCurrentItem(pager.currentItem + 1, true)
        }

        btn_skip.setOnClickListener {
            startActivity(Intent(this, UserTypeActivity::class.java).apply {
                sharedPreference.firstRun = false
                this@WalkThroughActivity.finish()
            })
        }

        pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                if (position == 2) {
                    btn_skip.visibility = View.INVISIBLE
                    btn_next1.visibility = View.VISIBLE
                    btn_next.visibility = View.GONE

                } else {
                    btn_skip.visibility = View.VISIBLE
                    btn_next1.visibility = View.GONE
                    btn_next.visibility = View.VISIBLE

                }
            }

            override fun onPageSelected(position: Int) {
                // if you want the second page, for example


            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    protected fun setFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (fragment != null) {
            fragmentTransaction.add(android.R.id.content, fragment)
        }
        fragmentTransaction
            .addToBackStack(fragment?.javaClass!!.name).commit()
    }

    /*private fun replaceFragment(
        fragment: Fragment,
        addToBackStack: Boolean
    ): Boolean {
        //val selectedFragment = fragment
        val manager = supportFragmentManager
        // Begin the fragment transition using support fragment manager
        val transaction = manager.beginTransaction()
        // Replace the fragment on container
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.replace(R.id.navHostFragment, fragment)
            if (addToBackStack)
                transaction.addToBackStack(fragment.javaClass.name)
            // Finishing the transition
            transaction.commit()
        }
        return true
    }*/

}



