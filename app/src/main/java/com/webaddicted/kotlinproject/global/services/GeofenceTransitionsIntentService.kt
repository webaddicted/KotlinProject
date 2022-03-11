package com.webaddicted.kotlinproject.global.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.webaddicted.kotlinproject.global.common.GlobalUtility.Companion.showOfflineNotification
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent


class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionsIntentService") {

    override fun onHandleIntent(intent: Intent?) {

        Log.i(TAG, "onHandleIntent")

        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        if (geofencingEvent!!.hasError()) {
            //String errorMessage = GeofenceErrorMessages.getErrorString(this,
            //      geofencingEvent.getErrorCode());
            Log.e(TAG, "Goefencing Error " + geofencingEvent.errorCode)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        Log.i(
            TAG,
            "geofenceTransition = " + geofenceTransition + " Enter : " + Geofence.GEOFENCE_TRANSITION_ENTER + "Exit : " + Geofence.GEOFENCE_TRANSITION_EXIT
        )
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            showOfflineNotification(this,"Entered", "Entered the Location")
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.i(TAG, "Showing Notification...")
            showOfflineNotification(this,"Exited", "Exited the Location")
        } else {
            // Log the error.
            showOfflineNotification(this,"Error", "Error")
            Log.e(TAG, "Error ")
        }
    }



    companion object {

        private val TAG = "GeofenceTransitions"
    }
}
