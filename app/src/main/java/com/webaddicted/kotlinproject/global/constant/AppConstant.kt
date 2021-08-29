package com.webaddicted.kotlinproject.global.constant

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class AppConstant {
    companion object {
        const val PAGINATION_SIZE: Int = 10
        const val SPLASH_DELAY: Long = 4000
        const val PICK_IMAGE = 5000
        const val GALLERY_IMAGE_ = 5001
        const val PICK_IMAGE_MULTIPLE = 5002
        const val REQUEST_CODE_SPEECH_INPUT = 5003
        const val DATE_FORMAT_Y_M_D = "yyyy-MM-dd"
        const val DATE_FORMAT_D_M_Y = "dd/MM/yyyy"
        const val DATE_FORMAT_D_M_Y_H = "dd/MMMM/yyyy"
        const val DATE_FORMAT_SRC = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val DATE_FORMAT_DST = "dd MMM yyyy hh:mm:ss a"
        const val SELECTED_POSITION = "selected_position"
        const val GALLERY_IMAGE = "gallery_image"
        const val GEOFENCE_RADIUS= 50f // in meter
        const val GEOFENCE_REQ_ID = "My Geofence"
        const val GEOFENCE_REQ_CODE = 0
        const val NOTIFICATION_CHANNEL_ID = "com.icc.cruuui"
        const val IS_USER_COME_FROM_SYSTEM_APPS = 1
        const val IS_USER_COME_FROM_USER_APPS = 2
    }

}