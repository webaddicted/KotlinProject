package com.webaddicted.kotlinproject.global.caranim

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

object CarAnimationUtils {
    fun isMarkerVisible(googleMap: GoogleMap, newLocation: LatLng?): Boolean {
        return googleMap.projection.visibleRegion.latLngBounds.contains(newLocation)
    }

    fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Double {
        val PI = 3.14159
        val lat1 = latLng1.latitude * PI / 180
        val long1 = latLng1.longitude * PI / 180
        val lat2 = latLng2.latitude * PI / 180
        val long2 = latLng2.longitude * PI / 180
        val dLon = long2 - long1
        val y = sin(dLon) * cos(lat2)
        val x =
            cos(lat1) * sin(lat2) - (sin(lat1)
                    * cos(lat2) * cos(dLon))
        var brng = atan2(y, x)
        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360
        return brng
    }

    fun computeRotation(
        fraction: Float,
        start: Float,
        end: Float
    ): Float {
        val normalizeEnd = end - start // rotate start to 0
        val normalizedEndAbs = (normalizeEnd + 360) % 360
        val direction =
            if (normalizedEndAbs > 180) (-1).toFloat() else 1.toFloat() // -1 = anticlockwise, 1 = clockwise
        val rotation: Float
        rotation = if (direction > 0) {
            normalizedEndAbs
        } else {
            normalizedEndAbs - 360
        }
        val result = fraction * rotation + start
        return (result + 360) % 360
    }

    interface LatLngInterpolator {
        fun interpolate(fraction: Float, a: LatLng?, b: LatLng?): LatLng?
        class LinearFixed : LatLngInterpolator {
            override fun interpolate(fraction: Float, a: LatLng?, b: LatLng?): LatLng {
                val lat = (b?.latitude!! - a?.latitude!!) * fraction + a.latitude
                var lngDelta = b.longitude - a.longitude
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360
                }
                val lng = lngDelta * fraction + a.longitude
                return LatLng(lat, lng)
            }
        }
    }
}
