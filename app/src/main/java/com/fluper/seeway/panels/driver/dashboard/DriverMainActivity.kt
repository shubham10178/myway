package com.fluper.seeway.panels.driver.dashboard

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.onBoard.activities.UserTypeActivity
import com.fluper.seeway.panels.driver.ChooseVehicleTypeActivity
import com.fluper.seeway.panels.driver.DriverViewModel
import com.fluper.seeway.panels.passenger.NotificationPassengerActivity
import com.fluper.seeway.utilitarianFiles.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_new_driver_nav.*
import kotlinx.android.synthetic.main.navigation_header.*
import kotlinx.android.synthetic.main.navigation_menu.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class DriverMainActivity : BaseActivity(), OnMapReadyCallback, View.OnClickListener {

    private var dl: DrawerLayout? = null
    private var t: ActionBarDrawerToggle? = null
    private lateinit var mMap: GoogleMap
    private var doubleBackToExitPressedOnce = false
    private var fusedLocationFetcher: FusedLocationFetcher? = null
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
        setContentView(R.layout.activity_new_driver_nav)
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
            sharedPreference.isLoggedIn = false
            sharedPreference.deletePreferences()
            drawerHandler()
            startActivity(
                Intent(this, UserTypeActivity::class.java).apply {
                    this@DriverMainActivity.finishAffinity()
                })
        })
        driverViewModel.throwable.observe(this, androidx.lifecycle.Observer {
            ProgressBarUtils.getInstance().hideProgress()
            ErrorUtils.handlerGeneralError(this, it)
        })
    }

    private fun initView() {
        if (sharedPreference.userFirstName.isNotEmpty())
            tvUserName.text = sharedPreference.userFirstName + " " + sharedPreference.userLastName
        else
            if (sharedPreference.userEmailId.isNotEmpty())
                tvUserName.text = sharedPreference.userEmailId
            else
                tvUserName.text = sharedPreference.userCountryCode + " " + sharedPreference.userMobile
        ll_earnings.visibility = View.VISIBLE
        img_seekbar.setOnClickListener {
            if (fusedLocationFetcher != null)
                fusedLocationFetcher?.getCurrentLocation()
            if (img_seekbar.drawable
                    .constantState == resources.getDrawable(R.drawable.toggle_off_all)
                    .constantState
            ) {
                ivProgress.visibility = View.VISIBLE
                tvFinding.visibility = View.VISIBLE
                val aniRotate = AnimationUtils.loadAnimation(this, R.anim.clock_wise)
                ivProgress.startAnimation(aniRotate)
                img_seekbar.setImageResource(R.drawable.toggle_on3x)
                txt_active_status.setTextColor(Color.parseColor("#02B509"))
                txt_active_status.text = "You are Online"
            } else {
                ivProgress.visibility = View.GONE
                tvFinding.visibility = View.GONE
                ivProgress.clearAnimation()
                img_seekbar.setImageResource(R.drawable.toggle_off_all)
                txt_active_status.setTextColor(Color.RED)
                txt_active_status.text = "You are Offline"
            }
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

    private fun initDrawer() {
        btnLogout.setOnClickListener(this)
        tvInviteEarn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnLogout -> {
                alertSubmit()
            }
            R.id.tvInviteEarn -> {
                drawerHandler()
            }
        }
    }

    private fun alertSubmit() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_logout)
        val btnLogoutD = dialog.findViewById<Button>(R.id.btnLogout)
        val btnCancelD = dialog.findViewById<Button>(R.id.btnCancel)
        btnLogoutD.setOnClickListener {
            if (NetworkUtils.isInternetAvailable(this)) {
                ProgressBarUtils.getInstance().showProgress(this, false)
                driverViewModel.logout(sharedPreference.accessToken!!)
            } else {
                showToast("Poor connection")
                drawerHandler()
            }
            dialog.dismiss()
        }
        btnCancelD.setOnClickListener {
            dialog.dismiss()
        }
        dialog.dismiss()
        dialog.show()
    }

    private fun drawerHandler() {
        if (nav_dra.isDrawerOpen(GravityCompat.START)) {
            nav_dra.closeDrawer(GravityCompat.START)
        } else {
            nav_dra.openDrawer(GravityCompat.START)
        }
    }

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
                        } catch (e: Exception) {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_screen_nav_driver, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_driver_chosse_vehicle -> {
                startActivity(Intent(this, ChooseVehicleTypeActivity::class.java))
                super.onOptionsItemSelected(item)
            }
            R.id.action_driver_notification -> {
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