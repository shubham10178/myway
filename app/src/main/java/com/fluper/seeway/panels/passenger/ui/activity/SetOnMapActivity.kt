package com.fluper.seeway.panels.passenger.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.fluper.seeway.R
import com.fluper.seeway.databinding.ActivitySetLocationBinding
import com.fluper.seeway.databinding.ActivitySetOnMapBinding
import com.fluper.seeway.maputils.MapUtils
import com.fluper.seeway.utilitarianFiles.FusedLocationFetcher
import com.fluper.seeway.utilitarianFiles.NetworkUtils
import com.fluper.seeway.utilitarianFiles.showToast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.io.IOException
import java.util.*


class SetOnMapActivity : AppCompatActivity(), OnMapReadyCallback , LocationListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var  autocompleteFragment: AutocompleteSupportFragment
    private var placesClient: PlacesClient? = null
    private val TAG = "SetOnMapActivity::"

    private val CONNECTION_FAILURE_RESOLUTION_REQUEST: Int=100
    private lateinit var latlog: LatLng
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
    private var binding: ActivitySetOnMapBinding? = null
    private lateinit var fusedLocationFetcher: FusedLocationFetcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_set_on_map
        ) as ActivitySetOnMapBinding


        initLocation()

        if (!Places.isInitialized())
            Places.initialize(applicationContext, resources.getString(R.string.google_maps_key))
        placesClient = Places.createClient(this)
        if (!NetworkUtils.isInternetAvailable(this.applicationContext))
            showToast("Poor internet connection.")


        binding?.clickHander = ClickHander(this)

        initilizeMap()
//        addEditTextListener()


     autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: ${place.name}, ${place.id}")

                autocompleteFragment.setText(MapUtils.getAddressFromLocation(this@SetOnMapActivity,
                    place.latLng!!
                ))

            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })

    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient!!.connect()
    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        //Disconnect from API onPause()
        if (mGoogleApiClient!!.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
            mGoogleApiClient!!.disconnect()
        }
    }

    private fun initLocation(){
        mGoogleApiClient =
            GoogleApiClient.Builder(this) // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this) //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build()

        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000)        // 10 seconds, in milliseconds
            .setFastestInterval(1 * 1000);
    }


    private fun initilizeMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_set_location) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        removeLocationUpdates()
    }


    override fun onMapReady(mGoogleMap: GoogleMap?) {
        mGoogleMap?.apply {
            fusedLocationFetcher =
                FusedLocationFetcher(
                    this@SetOnMapActivity,
                    object : FusedLocationFetcher.LocationChangeListener {
                        override fun onLocationChange(latitude: Double, longitude: Double) {
                            // Add a marker in Sydney and move the camera
                            val sydney = LatLng(latitude, longitude)
                            val cameraPosition =
                                CameraPosition.Builder().target(sydney).zoom(18f).build()
                            animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                            setOnCameraIdleListener {
                                val midLong: LatLng? =
                                    cameraPosition.target//map's center position latitude & longitude
                                try {
                                    getAddressFromLocation(midLong!!)
                                } catch (e: java.lang.Exception) {
                                }
                            }
                            removeLocationUpdates()
                        }
                    },
                    true
                )
            fusedLocationFetcher?.getCurrentLocation()

        }
    }

    private fun removeLocationUpdates() {
        fusedLocationFetcher.fusedLocationProviderClient.removeLocationUpdates(
            fusedLocationFetcher.locationCallback
        )
        fusedLocationFetcher.status = false
    }


    private fun getAddressFromLocation(currentLatLng: LatLng) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses =
                geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1)
            if (addresses != null && addresses.size > 0) {
                val address = addresses[0].getAddressLine(0)
                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            }
        } catch (e: IOException) {
            Log.d(TAG, "getAddressFromLocation:::$e")
        }

    }

    inner class ClickHander(private val mContext: Context) {

        fun onBackClick(view: View) {
            setResult(Activity.RESULT_CANCELED)
            (mContext as SetOnMapActivity).finish()
        }

        fun onClickDone(view: View) {
            Intent(this@SetOnMapActivity, ChooseAPlaceActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onLocationChanged(mLocation: Location) {
        latlog=LatLng(mLocation.latitude,mLocation.longitude)
        Log.d(TAG, "onLocationChanged: $latlog")
    }

    override fun onConnected(p0: Bundle?) {
        val location =
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }else{

                LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
            }

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            )
        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.latitude
            currentLongitude = location.longitude

            autocompleteFragment.setText(MapUtils.getAddressFromLocation(this@SetOnMapActivity,
                LatLng(currentLatitude,currentLongitude)
            ))


        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST
                )
                /*tv_pickup_location
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (e: IntentSender.SendIntentException) {
                // Log the error
                e.printStackTrace()
            }
        } else {
            /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e(
                "Error",
                "Location services connection failed with code " + connectionResult.getErrorCode()
            )
        }
    }


}