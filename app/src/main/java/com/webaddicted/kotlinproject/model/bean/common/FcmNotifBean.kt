package com.webaddicted.kotlinproject.model.bean.common

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Deepak Sharma(webaddicted) on 23-03-2020.
 */
class FcmNotifBean {
    @SerializedName("to")
    var to: String = ""

    @SerializedName("data")
    var data =DataNoti()

    open class DataNoti : Serializable {
        @SerializedName("title")
        var title: String = ""

        @SerializedName("message")
        var message: String = ""
    }
}
