package com.webaddicted.kotlinproject.global.common

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData

class CommonDataRespo {
    companion object {
        var selfieMutableImg = MutableLiveData<Bitmap>()
        var dlMutableImg = MutableLiveData<Bitmap>()
    }
}