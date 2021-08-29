package com.webaddicted.kotlinproject.global.annotationdef

import androidx.annotation.IntDef

class MediaPickerType {
    @IntDef(CAPTURE_IMAGE, SELECT_IMAGE, SCAN_DL)
    @Retention(AnnotationRetention.SOURCE)
    annotation class MediaType

    companion object {
        const val CAPTURE_IMAGE = 500
        const val SELECT_IMAGE = 501
        const val SCAN_DL = 502
    }
}