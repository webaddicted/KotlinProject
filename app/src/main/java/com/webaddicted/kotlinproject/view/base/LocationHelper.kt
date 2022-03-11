package com.webaddicted.kotlinproject.view.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*


/**
 * Created by deepaksharma
 */
abstract class LocationHelper {
    companion object : GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
        private lateinit var locationChangeListener: LocationChangeListener
        private lateinit var activity: Activity
        private var isUpdateLocation = false
        private var isShowAddress = false
        private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
        private lateinit var locationCallback: LocationCallback


        /*** provide user current location single time
         */
        fun getLocation(
            activity: Activity,
            locationChangeListener: LocationChangeListener
        ) {
            this.activity = activity
            this.locationChangeListener = locationChangeListener
            stopLocationUpdates()
            checkPermission()
        }

        /**
         * provide user current location after a perticular time
         *
         * @param timeInterval - location update after time intervel in sec
         * @param fastInterval - fast time interval
         * @param displacement - location update after a perticular distance
         */
        fun getLocation(
            activity: Activity,
            @NonNull timeInterval: Long,
            @NonNull fastInterval: Long,
            @NonNull displacement: Long,
            locationChangeListener: LocationChangeListener
        ) {
            this.activity = activity
            this.locationChangeListener = locationChangeListener
            stopLocationUpdates()
            this.isUpdateLocation = true
            if (timeInterval > 0) INTERVAL *= timeInterval
            if (fastInterval > 0) FASTEST_INTERVAL *= fastInterval
            //        if (displacement > 0)
            MIN_DISTANCE_CHANGE_FOR_UPDATES *= displacement
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
            checkPermission()
        }

        /**
         * check Gps status & location permission
         */
        private fun checkPermission() {
            val locationList = ArrayList<String>()
            locationList.add(Manifest.permission.ACCESS_FINE_LOCATION)
            locationList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            PermissionHelper.requestMultiplePermission(
                activity,
                locationList,
                object : PermissionHelper.Companion.PermissionListener {
                    override fun onPermissionGranted(mCustomPermission: List<String>) {
                        checkGpsLocation()
                        initLocationCallBack()
                    }

                    override fun onPermissionDenied(mCustomPermission: List<String>) {
                        locationChangeListener.onError("permissionDenied")
                    }

                })
        }

        /**
         * check play services & enable gps
         */
        private fun checkGpsLocation() {
            if (checkPlayServices()) {
                buildGoogleApiClient()
            } else {
                locationChangeListener.onError("Play service not available.")
                Toast.makeText(activity, "Play service not available.", Toast.LENGTH_SHORT).show()
            }
        }

        /**
         * check Google play services status.
         *
         * @return
         */
        private fun checkPlayServices(): Boolean {
            val googleApiAvailability = GoogleApiAvailability.getInstance()
            val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(activity)

            if (resultCode != ConnectionResult.SUCCESS) {
                if (googleApiAvailability.isUserResolvableError(resultCode)) {
                    googleApiAvailability.getErrorDialog(activity, resultCode, 1000)?.show()
                } else {
                    locationChangeListener.onError("This device is not supported.")
                    Toast.makeText(
                        activity,
                        "This device is not supported.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return false
            }
            return true
        }

        @Synchronized
        private fun buildGoogleApiClient() {
            mGoogleApiClient = GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
            mGoogleApiClient!!.connect()
            mLocationRequest = createLocationRequest()
            val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest!!)

            val result =
                LocationServices.SettingsApi.checkLocationSettings(
                    mGoogleApiClient!!,
                    builder.build()
                )

            result.setResultCallback { locationSettingsResult ->
                val status = locationSettingsResult.status

                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS ->
                        // All location settings are satisfied. The client can initialize location requests here
                        Lg.d(TAG, "onResult: SUCCESS")
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Lg.d(TAG, "onResult: RESOLUTION_REQUIRED")
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, 2000)

                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Lg.d(
                        TAG,
                        "onResult: SETTINGS_CHANGE_UNAVAILABLE"
                    )
                    LocationSettingsStatusCodes.CANCELED -> Lg.d(TAG, "onResult: CANCELED")
                }
            }
        }

        override fun onConnected(arg0: Bundle?) {
            // Once connected with google api, get the location
            startLocationUpdates()
        }

        @SuppressLint("MissingPermission")
        private fun startLocationUpdates() {
            //        Criteria criteria = new Criteria();
            //        criteria.setAltitudeRequired(false);
            //        criteria.setBearingRequired(true);
            //        criteria.setSpeedRequired(false);
            //        criteria.setPowerRequirement(Criteria.POWER_LOW);
            //        criteria.setAccuracy(Criteria.ACCURACY_FINE);
            //        criteria.setCostAllowed(true);
            if (mGoogleApiClient!!.isConnected) {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient!!,
                    mLocationRequest!!,
                    this
                )
            } else {
                buildGoogleApiClient()
            }
        }

        fun stopLocationUpdates() {
            if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient!!,
                    this as LocationListener
                )
            }
            cleanUpLocation()
        }

        override fun onConnectionSuspended(arg0: Int) {
            Lg.d(TAG, "onConnectionSuspended: ")
            mGoogleApiClient!!.connect()
        }

        override fun onConnectionFailed(result: ConnectionResult) {
            Lg.i(
                TAG,
                "login  Connection failed: ConnectionResult.getErrorCode() = " + result.errorCode
            )
            locationChangeListener.onError("Connection failed")
            mGoogleApiClient!!.connect()

        }

        private fun initLocationCallBack() {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    locationChangeListener.onUpdatedLocation(locationResult?.lastLocation!!, "")
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                }
            }
        }

        /**
         * gps is not enabled then it gives last location
         *
         * @return
         */
        @SuppressLint("MissingPermission")
        fun getKnownLocation() {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location = it.result
                if (location == null) {
                    fusedLocationProviderClient.requestLocationUpdates(
                        mLocationRequest!!,
                        locationCallback,
                        Looper.myLooper()!!
                    )
                } else locationChangeListener.onUpdatedLocation(location, "")
            }
        }

        /**
         * location update interval
         */
        private fun createLocationRequest(): LocationRequest {
            val mLocationRequest = LocationRequest()
            if (isUpdateLocation) {
                mLocationRequest.interval = INTERVAL
                mLocationRequest.fastestInterval = FASTEST_INTERVAL
                mLocationRequest.smallestDisplacement = MIN_DISTANCE_CHANGE_FOR_UPDATES
            }
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            return mLocationRequest
        }

        override fun onLocationChanged(location: Location) {
            if (isShowAddress) getAddress(location)
            else locationChangeListener.onUpdatedLocation(location, "")
        }

        /**
         * provide user current address on the bases of lat long
         *
         * @param location
         */
        private fun getAddress(@NonNull location: Location) {
            var strAddress = ""
            val geocoder = Geocoder(activity, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses != null) {
                    val returnedAddress = addresses[0]
                    val strReturnedAddress = StringBuilder("")

                    for (i in 0..returnedAddress.maxAddressLineIndex) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    }
                    strAddress = strReturnedAddress.toString()
                    Lg.d(TAG, "Current address - $strReturnedAddress")
                } else {
                    locationChangeListener.onError("No Address returned!")
                    Lg.d(TAG, "No Address returned!")
                }
            } catch (e: Exception) {
                locationChangeListener.onError("Can't get Address! - $e")
                e.printStackTrace()
                Lg.d(TAG, "Can't get Address! - $e")
            }
            locationChangeListener.onUpdatedLocation(location, strAddress)
//            getCurrentLocation(location, strAddress)
        }

//        protected fun getCurrentLocation(
//            @NonNull location: Location,
//            @NonNull address: String?
//        )

        fun isAddressEnabled(showAddress: Boolean) {
            isShowAddress = showAddress
        }

        private fun cleanUpLocation() {
            mGoogleApiClient = null
            mLocationRequest = null
            isShowAddress = false
            isUpdateLocation = false
            INTERVAL = 1000
            FASTEST_INTERVAL = 1000
            MIN_DISTANCE_CHANGE_FOR_UPDATES = 1F
        }

        private val TAG = LocationHelper::class.java.simpleName

        // The minimum distance to change Updates in meters
        private var INTERVAL: Long = 1000 // 1 sec

        // The minimum time between updates in milliseconds
        private var FASTEST_INTERVAL: Long = 1000 // 1 sec

        // The minimum distance to change Updates in meters
        private var MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 1F // 1 meters

        private var mGoogleApiClient: GoogleApiClient? = null

        private var mLocationRequest: LocationRequest? = null

        /**
         * get distance between two lat long
         *
         * @param currlat
         * @param currlng
         * @param givenlat
         * @param givenlng
         * @return distane in miles
         */
        fun checkDistance(
            currlat: Double,
            currlng: Double,
            givenlat: Double,
            givenlng: Double
        ): Double {
            val earthRadius = 3958.75 // in miles, change to 6371 for kilometer output
            val dLat = Math.toRadians(givenlat - currlat)
            val dLng = Math.toRadians(givenlng - currlng)
            val sindLat = sin(dLat / 2)
            val sindLng = sin(dLng / 2)
            val a = sindLat.pow(2.0) + (sindLng.pow(2.0)
                    * cos(Math.toRadians(currlat)) * cos(Math.toRadians(givenlat)))
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return earthRadius * c // output distance, in MILES
        }

        interface LocationChangeListener {
            fun onUpdatedLocation(location: Location, address: String)
            fun onError(string: String)
        }
    }

}
