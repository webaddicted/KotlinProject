package com.webaddicted.kotlinproject.view.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
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
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.Marker
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.common.PermissionHelper
import com.webaddicted.kotlinproject.global.constant.AppConstant
import com.webaddicted.kotlinproject.global.constant.AppConstant.Companion.GEOFENCE_REQ_CODE
import com.webaddicted.kotlinproject.global.constant.AppConstant.Companion.GEOFENCE_REQ_ID
import com.webaddicted.kotlinproject.global.services.GeofenceTransitionsIntentService
import java.util.*

/**
 * Created by deepaksharma
 */
abstract class BaseLocation(layoutId: Int) : BaseActivity(layoutId), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener,
    PermissionHelper.Companion.PermissionListener {
    private var mFancyMarker: Marker? = null
    private var mGeofencePendingIntent: PendingIntent? = null
    private var isUpdateLocation = false
    private var isShowAddress = false
    private var mGeofenceList: ArrayList<Geofence>? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    /*** provide user current location single time
     */
    protected fun getLocation() {
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
    fun getLocation( timeInterval: Long,  fastInterval: Long,  displacement: Long) {
        stopLocationUpdates()
        this.isUpdateLocation = true
        if (timeInterval > 0) INTERVAL *= timeInterval
        if (fastInterval > 0) FASTEST_INTERVAL *= fastInterval
        //        if (displacement > 0)
        MIN_DISTANCE_CHANGE_FOR_UPDATES *= displacement
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermission()

    }

    /**
     * check Gps status & location permission
     */
    private fun checkPermission() {
        mGeofenceList = ArrayList()
            val locationList = ArrayList<String>()
            locationList.add(Manifest.permission.ACCESS_FINE_LOCATION)
            locationList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            PermissionHelper.requestMultiplePermission(this, locationList, this)

    }

    /**
     * check play services & enable gps
     */
    private fun checkGpsLocation() {
        if (checkPlayServices()) {
            buildGoogleApiClient()
        } else {
            Toast.makeText(this, "Play service not available.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * check Google play services status.
     *
     * @return
     */
    private fun checkPlayServices(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 1000)?.show()
            } else {
                Toast.makeText(
                    applicationContext,
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
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
        mLocationRequest = createLocationRequest()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest!!)

        val result =
            LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient!!, builder.build())

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
                        status.startResolutionForResult(this@BaseLocation, 2000)
                    } catch (e: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }

                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Lg.d(
                    TAG,
                    "onResult: SETTINGS_CHANGE_UNAVAILABLE"
                )
                LocationSettingsStatusCodes.CANCELED -> Lg.d(TAG, "onResult: CANCELED")
            }//                        getLocation();
        }
    }

    fun startGeoFencing(location: Location, fancyMarker: Marker?) {
        mFancyMarker = fancyMarker
        createGeofences(location.latitude, location.longitude)
        try {
            mGoogleApiClient?.let {
                LocationServices.GeofencingApi.addGeofences(
                    it,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()!!
                ).setResultCallback { status ->
                    if (status.isSuccess) {
                        Lg.i(TAG, "Saving Geofence")
                    } else {
                        Lg.e(TAG, "Registering geofence failed: ${status.statusMessage} ${status.statusCode}"                        )
                    }
                }
            }

        } catch (securityException: SecurityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            Lg.e(TAG, "Error")
        }
    }

    /**
     * Create a Geofence list
     */
    fun createGeofences(latitude: Double, longitude: Double) {
        mGeofenceList?.clear()

        val fence = Geofence.Builder()
            .setRequestId(GEOFENCE_REQ_ID)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setCircularRegion(latitude, longitude, AppConstant.GEOFENCE_RADIUS)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
        mGeofenceList?.add(fence)
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        val builder = GeofencingRequest.Builder()
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        builder.addGeofences(mGeofenceList!!)
        return builder.build()
    }

    private fun getGeofencePendingIntent(): PendingIntent? {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent
        }
        val intent = Intent(this, GeofenceTransitionsIntentService::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    // Clear Geofence
     fun stopGeoFencing(fancyMarker: Marker?, geoFenceCircle: Circle?) {
            LocationServices.GeofencingApi.removeGeofences(
                mGoogleApiClient!!,
                getGeofencePendingIntent()!!
            ).setResultCallback { status ->
                if (status.isSuccess) {
                    fancyMarker?.remove()
                    geoFenceCircle?.remove()
                    // remove drawing
        //                removeGeofenceDraw()
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

    protected fun stopLocationUpdates() {
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
        mGoogleApiClient!!.connect()
    }


    //        [Permission Start]
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,  grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    override fun onPermissionGranted(mCustomPermission: List<String>) {
        checkGpsLocation()
        initLocationCallBack()
    }

    override fun onPermissionDenied(mCustomPermission: List<String>) {

    }
    //       [Permission Stop]

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

     fun initLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.let { super.onLocationResult(it) }
                locationResult.lastLocation?.let { getCurrentLocation(it, null) }
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
                    fusedLocationProviderClient.requestLocationUpdates(mLocationRequest!!, locationCallback, Looper.myLooper()!!)
                } else {
                    getCurrentLocation(location, null)
                }
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
            mLocationRequest.smallestDisplacement= MIN_DISTANCE_CHANGE_FOR_UPDATES
        }
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return mLocationRequest
    }

    override fun onLocationChanged(location: Location) {
        if (isShowAddress)
            getAddress(location)
        else
            getCurrentLocation(location, null)
    }

    /**
     * provide user current address on the bases of lat long
     *
     * @param location
     */
    private fun getAddress( location: Location) {
        var strAddress = ""
        val geocoder = Geocoder(this, Locale.getDefault())
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
                Lg.d(TAG, "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Lg.d(TAG, "Can't get Address! - $e")
        }

        getCurrentLocation(location, strAddress)
    }

    protected abstract fun getCurrentLocation( location: Location, address: String?)

    protected fun isAddressEnabled(showAddress: Boolean) {
        isShowAddress = showAddress
    }

    fun cleanUpLocation() {
        mGoogleApiClient = null
        mLocationRequest = null
        isShowAddress = false
        isUpdateLocation = false
        INTERVAL = 1000
        FASTEST_INTERVAL = 1000
        MIN_DISTANCE_CHANGE_FOR_UPDATES = 1F
    }

    companion object {
        private val TAG = BaseLocation::class.java.simpleName
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
            val sindLat = Math.sin(dLat / 2)
            val sindLng = Math.sin(dLng / 2)
            val a = Math.pow(sindLat, 2.0) + (Math.pow(sindLng, 2.0)
                    * Math.cos(Math.toRadians(currlat)) * Math.cos(Math.toRadians(givenlat)))
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            return earthRadius * c // output distance, in MILES
        }
    }

}
