package com.webaddicted.kotlinproject.model.bean.common

import android.graphics.Bitmap

/**
 * Created by Deepak Sharma(webaddicted) on 23-03-2020.
 */
class ContactBean {
    var contactId: String = ""
    var contactName: String = ""
    var contactNumber: String = ""
    var contactEmail: String = ""
    var contactPhoto: Bitmap? = null
    var contactInfo: String = ""
    var checked: String = ""
}
