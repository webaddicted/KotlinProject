package com.webaddicted.kotlinproject.global.caranim

import android.animation.ValueAnimator
import android.location.Location
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class CarAnimationHelper(
    googleMap: GoogleMap?,
    duration: Long,
    updateLocation: UpdateLocationCallBack
) {
    private val updateLocation: UpdateLocationCallBack = updateLocation
    private var valueAnimator: ValueAnimator? = null
    private val googleMap: GoogleMap? = googleMap
    private val animationDuration: Long = duration
    fun animateMarker(
        destination: Location,
//        oldLocation: Location?,
        marker: Marker?
    ) {
        if (marker != null) {
            val startPosition = marker.position
            val endPosition = LatLng(destination.latitude, destination.longitude)
            if (valueAnimator != null) valueAnimator!!.end()
            val latLngInterpolator: CarAnimationUtils.LatLngInterpolator =
                CarAnimationUtils.LatLngInterpolator.LinearFixed()
            valueAnimator = ValueAnimator.ofFloat(0f, 1f)
            valueAnimator?.duration = animationDuration // duration 1 second
            valueAnimator?.interpolator = LinearInterpolator()
            valueAnimator?.addUpdateListener { animation ->
                try {
                    val v = animation.animatedFraction
                    val newPosition: LatLng =
                        latLngInterpolator.interpolate(v, startPosition, endPosition)!!
                    marker.position = newPosition
                    //marker.setRotation(computeRotation(v, startRotation, destination.getBearing()));
                    marker.rotation = CarAnimationUtils.computeRotation(
                        v, marker.rotation,
                        CarAnimationUtils.bearingBetweenLocations(
                            startPosition,
                            newPosition
                        ).toFloat()
                    )
                    marker.setAnchor(0.5f, 0.5f)
                    marker.isFlat = true
                    // add new location into old location
                    updateLocation.onUpdatedLocation(destination)
                    //when marker goes out from screen it automatically move into center
                    if (googleMap != null) {
                        if (!CarAnimationUtils.isMarkerVisible(googleMap, newPosition)) {
                            googleMap.animateCamera(
                                CameraUpdateFactory
                                    .newCameraPosition(
                                        CameraPosition.Builder()
                                            .target(newPosition)
                                            .zoom(googleMap.cameraPosition.zoom)
                                            .build()
                                    )
                            )
                        } else {
                            try {
                                googleMap.animateCamera(
                                    CameraUpdateFactory
                                        .newCameraPosition(
                                            CameraPosition.Builder()
                                                .target(newPosition)
                                                .tilt(0f)
                                                .zoom(googleMap.cameraPosition.zoom)
                                                .build()
                                        )
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    // handle exception here
                }
            }
            valueAnimator?.start()
        }
    }

    interface UpdateLocationCallBack {
        fun onUpdatedLocation(updatedLocation: Location?)
    }

}
