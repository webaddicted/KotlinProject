package com.webaddicted.kotlinproject.global.annotationdef

import androidx.annotation.IntDef

class SortListType {
    @IntDef(DEFAULT, ASCENDING, DESCENDING)
    @Retention(AnnotationRetention.SOURCE)
    annotation class SortType

    companion object {
        const val DEFAULT = 80
        const val ASCENDING = 81
        const val DESCENDING = 82
    }
}