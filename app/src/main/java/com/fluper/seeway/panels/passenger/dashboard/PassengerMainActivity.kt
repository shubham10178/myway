package com.fluper.seeway.panels.passenger.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.onBoard.activities.UserTypeActivity
import com.fluper.seeway.onBoard.adapter.HomeAddressAdapter
import com.fluper.seeway.onBoard.model.AddressModel
import com.fluper.seeway.panels.driver.DriverViewModel
import com.fluper.seeway.panels.passenger.NotificationPassengerActivity
import com.fluper.seeway.panels.passenger.PassengerMainBottomSheetFragment
import com.fluper.seeway.utilitarianFiles.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_new_passenger_nav.*
import kotlinx.android.synthetic.main.navigation_header.*
import kotlinx.android.synthetic.main.navigation_menu.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class PassengerMainActivity : BaseActivity(), OnMapReadyCallback, View.OnClickListener {
    private lateinit var mMap: GoogleMap
    private val bottomSheetDialogFragment by lazy { PassengerMainBottomSheetFragment() }
    private var doubleBackToExitPressedOnce = false
    private var fusedLocationFetcher: FusedLocationFetcher? = null
    private var dl: DrawerLayout? = null
    private var t: ActionBarDrawerToggle? = null
    private val STORAGE_PERMISSION_CODE = 23
    val MULTIPLE_PERMISSIONS = 10 // code you want.
    var permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_NOTIFICATION_POLICY,
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private lateinit var driverViewModel: DriverViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_passenger_nav)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        statusBarFullScreenWithBackground()
        sharedPreference.isLoggedIn = true
        driverViewModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        if (!Places.isInitialized())
            Places.initialize(applicationContext, resources.getString(R.string.google_maps_key))
        if (!NetworkUtils.isInternetAvailable(this.applicationContext))
            showToast("Poor internet connection.")
        myObserver()
        bottomSheetFragment()
        initMap()
        setToolBar()
        initDrawer()
        initView()
        if (isReadStorageAllowed()) {
            return
        } else {
            requestStoragePermission()
        }
    }

    private fun myObserver() {
        driverViewModel.logout.observe(this, androidx.lifecycle.Observer {
            ProgressBarUtils.getInstance().hideProgress()
            showToast(it.message!!)
            startActivity(
                Intent(
                    this@PassengerMainActivity,
                    UserTypeActivity::class.java
                ))
            sharedPreference.isLoggedIn = false
            sharedPreference.deletePreferences()
            this@PassengerMainActivity.finishAffinity()
        })
        driverViewModel.throwable.observe(this, androidx.lifecycle.Observer {
            ProgressBarUtils.getInstance().hideProgress()
            ErrorUtils.handlerGeneralError(this, it)
        })
    }

    private fun bottomSheetFragment() {
        /*val bundle = Bundle()
        //bundle.putParcelable(Constants.Property_List, propertyList)
        bottomSheetDialogFragment.arguments = bundle
        bottomSheetDialogFragment.show(
            supportFragmentManager,
            bottomSheetDialogFragment.tag
        )*/
        val behavior = BottomSheetBehavior.from(bottomSheet)
        //behavior.isFitToContents = false
        //behavior.halfExpandedRatio = 0.7f
        //behavior.isGestureInsetBottomIgnored = true
        behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
                Log.e("onSlide", "onSlide")
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when {
                    BottomSheetBehavior.STATE_EXPANDED == newState -> {
                        /*Do it later*/
                    }
                    BottomSheetBehavior.STATE_COLLAPSED == newState -> {
                        /*Do it later*/
                    }
                    BottomSheetBehavior.STATE_HIDDEN == newState -> {
                        /*Do it later*/
                    }
                    BottomSheetBehavior.STATE_DRAGGING == newState -> {
                        /*Do it later*/
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED == newState -> {
                        /*Do it later*/
                    }
                    BottomSheetBehavior.STATE_SETTLING == newState -> {
                        /*Do it later*/
                    }
                    else -> {
                        /*Do it later*/
                    }
                }
            }
        })
    }

    private fun initDrawer() {
        btnLogout.setOnClickListener(this)
        tvInviteEarn.setOnClickListener(this)
    }

    private fun initView() {
        if (sharedPreference.profileImage.isNotEmpty())
            Picasso.get().load(sharedPreference.profileImage)
                .placeholder(R.drawable.profile_placeholder2x)
                .error(R.drawable.profile_placeholder2x).into(ivUserImage)
        if (sharedPreference.userFirstName.isNotEmpty())
            tvUserName.text = sharedPreference.userFirstName+" "+sharedPreference.userLastName
        ll_earnings.visibility = View.GONE
        btnCurrentLocation.setOnClickListener {
            if (fusedLocationFetcher != null)
                fusedLocationFetcher?.getCurrentLocation()
        }
        home_recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val users = ArrayList<AddressModel>()
        users.add(AddressModel("Home", "383 joriseen st sunnyside Pretora", R.drawable.home_file))
        users.add(AddressModel("Work", "580 Paul grugre St. pretoria", R.drawable.work_file))
        users.add(AddressModel("Rosandra", "580 Paul grugre St. pretoria", R.drawable.star_file))
        users.add(AddressModel("Rosandra", "580 Paul grugre St. pretoria", R.drawable.star_file))
        val adapter = HomeAddressAdapter(users, this)
        home_recycler.adapter = adapter
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnLogout -> {
                startActivity(
                    Intent(
                        this@PassengerMainActivity,
                        UserTypeActivity::class.java
                    ))
                sharedPreference.isLoggedIn = false
                sharedPreference.deletePreferences()
                this@PassengerMainActivity.finishAffinity()
                /*drawerHandler()
                ProgressBarUtils.getInstance().showProgress(this, false)
                driverViewModel.logout(sharedPreference.accessToken!!)*/
            }
            R.id.tvInviteEarn -> {
                drawerHandler()
            }
        }
    }

    private fun drawerHandler() {
        if (nav_dra.isDrawerOpen(GravityCompat.START)) {
            nav_dra.closeDrawer(GravityCompat.START)
        } else {
            nav_dra.openDrawer(GravityCompat.START)
        }
    }

    private fun initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap
            enableMyLocation()
            getLocation()
            // Add a marker in Sydney and move the camera
            /*val sydney = LatLng(-34.0, 151.0)
            mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        } else
            showToast("Error in map loading...")
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (!::mMap.isInitialized) return
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.uiSettings.isMyLocationButtonEnabled = false
            mMap.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            if (isReadStorageAllowed()) {
                return
            } else {
                requestStoragePermission()
            }
        }
    }

    fun getLocation() {
        fusedLocationFetcher =
            FusedLocationFetcher(this, object : FusedLocationFetcher.LocationChangeListener {
                override fun onLocationChange(latitude: Double, longitude: Double) {
                    // Add a marker in Sydney and move the camera
                    val sydney = LatLng(latitude, longitude)
                    val cameraPosition = CameraPosition.Builder().target(sydney).zoom(18f).build()
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    mMap.setOnCameraIdleListener {
                        val midLong: LatLng? =
                            mMap.cameraPosition.target//map's center position latitude & longitude
                        try {
                            getAddressFromLocation(midLong!!)
                        } catch (e: java.lang.Exception) {
                        }
                    }
                    removeLocationUpdates()
                }
            }, true)
        fusedLocationFetcher?.getCurrentLocation()
    }

    private fun getAddressFromLocation(currentLatLng: LatLng) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses =
                geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1)
            if (addresses != null && addresses.size > 0) {
                val address =
                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                /*addressLocation = address
                tv_loc.setText(addressLocation)
                currentLat = currentLatLng.latitude
                currentLng = currentLatLng.longitude*/
                // getLatLngFromAddress(tv_loc.text.toString())
            }
        } catch (e: IOException) {

        }
    }

    private fun removeLocationUpdates() {
        if (fusedLocationFetcher != null) {
            fusedLocationFetcher?.fusedLocationProviderClient?.removeLocationUpdates(
                fusedLocationFetcher?.locationCallback
            )
            fusedLocationFetcher?.status = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeLocationUpdates()
    }

    private fun setToolBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        dl = findViewById<View>(R.id.nav_dra) as DrawerLayout
        t = ActionBarDrawerToggle(this, dl, toolbar, 0, 0)
        dl!!.addDrawerListener(t!!)
        t!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.menu)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_screen_nav_passenger, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnNotification -> {
                startActivity(Intent(this, NotificationPassengerActivity::class.java))
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isReadStorageAllowed(): Boolean {
        var result: Int
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    //Requesting permission
    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) &&
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) &&
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) &&
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.SEND_SMS
            ) &&
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) &&
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            ) &&
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            ) &&
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    //This method will be called when the user will tap on allow or deny
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ): Unit {
        when (requestCode) {
            MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty()) {
                    var permissionsDenied = ""
                    for (per in permissions) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "$per".trimIndent()
                        }
                    }
                }
                return
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        this.showToast("Back again to exit")
//            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}