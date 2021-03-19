package com.fluper.seeway.panels.passenger.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.databinding.ActivitySetLocationBinding
import com.fluper.seeway.maputils.MapUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


class SetLocationActivity : BaseActivity() , LocationListener , GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private val CONNECTION_FAILURE_RESOLUTION_REQUEST: Int=100
    private lateinit var latlog: LatLng
    private val TAG: String = "SetLocationActivity::"
    private var binding: ActivitySetLocationBinding? = null


    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_set_location
        ) as ActivitySetLocationBinding
        binding!!.clickHandler = ClickHandler(this@SetLocationActivity)

        initLocation()

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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


    inner class ClickHandler(private val mActivity: Activity) {

        fun onClickBack(view: View) {
            Log.d(TAG, "onClickBack: ")
            setResult(Activity.RESULT_CANCELED)
            (mActivity as SetLocationActivity).finish()
        }

        fun onClickAddHome(view: View) {
            Log.d(TAG, "onClickAddHome: ")
            Intent(
                this@SetLocationActivity,
                ChooseAPlaceActivity::class.java
            ).apply { startActivity(this) }
        }

        fun onClickAddWork(view: View) {
            Log.d(TAG, "onClickAddWork: ")
            Intent(
                this@SetLocationActivity,
                SearchLocationActivity::class.java
            ).apply { startActivity(this) }
        }

        fun onClickSetlocationOnMap(view: View) {
            Log.d(TAG, "onClickSetlocationOnMap: ")
            Intent(
                this@SetLocationActivity,
                SetOnMapActivity::class.java
            ).apply { startActivity(this) }
        }


        fun onClickRemove(view: View) {
            Log.d(TAG, "onClickRemove: ")
            binding.apply {
                this?.ivRemoveStop?.visibility = View.GONE
                this?.ivAddStop?.visibility = View.VISIBLE
                this?.group1?.visibility = View.GONE
            }

        }

        fun onClickAdd(View: View) {
            Log.d(TAG, "onClickAdd: ")
            binding.apply {
                this?.ivRemoveStop?.visibility = android.view.View.VISIBLE
                this?.ivAddStop?.visibility = android.view.View.GONE
                this?.group1?.visibility = android.view.View.VISIBLE

            }
        }


        fun onClickSavedLocation(view: View) {
            Log.d(TAG, "onClickSavedLocation: ")
            Intent(this@SetLocationActivity, ChooseAPlaceActivity::class.java).apply {
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

            binding?.apply {
                tvPickupLocation.text=
                    MapUtils.getAddressFromLocation(
                        this@SetLocationActivity,
                        LatLng(currentLatitude,currentLongitude))
            }


        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
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
            } catch (e: SendIntentException) {
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