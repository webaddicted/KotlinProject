package com.webaddicted.kotlinproject.model.fcmkit

import java.io.File
import java.io.Serializable

/**
 * Created by Deepak Sharma(webaddicted) on 30/03/2020
 */
class FcmSocialLoginRespoBean :Serializable{
    var userId: String? = ""
    var uuid: String? = ""
    var imei: String? = ""
    var tokenId: String? = ""
    var fcmToken: String? = ""
    var userName: String? = ""
    var userEmailId: String? = ""
    var userMobileno: String? = ""
    var userImage: String? = ""
    var dob: String? = ""
    var provider: String? = ""
    var profileImgFile: File? = null
    var password: String? = ""
    var isAdmin: Boolean = false

}