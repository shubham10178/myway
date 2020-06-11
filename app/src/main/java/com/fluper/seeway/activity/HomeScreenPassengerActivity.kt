package com.fluper.seeway.activity

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.Interface.ListAdapter
import com.fluper.seeway.R
import com.fluper.seeway.adapter.HomeAddressAdapter
import com.fluper.seeway.model.AddressModel
import com.fluper.seeway.model.UserTypeModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeScreenPassengerActivity : AppCompatActivity(), OnMapReadyCallback,View.OnClickListener {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen_passenger)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)


        initView()

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    private fun initView(){

        val recyclerView = findViewById(R.id.home_recycler) as RecyclerView




        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        val users = ArrayList<AddressModel>()


        users.add(AddressModel("Home", "383 joriseen st sunnyside Pretora", R.drawable.home_file))
        users.add(AddressModel("Work", "580 Paul grugre St. pretoria",R.drawable.work_file))
        users.add(AddressModel("Rosandra", "580 Paul grugre St. pretoria",R.drawable.star_file))




        var  adapter = HomeAddressAdapter(users, this)
        recyclerView.adapter = adapter
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}
