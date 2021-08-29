package com.webaddicted.kotlinproject.global.sociallogin.model

import java.io.Serializable

/**
 * Created by Deepak Sharma(webaddicted) on 29/03/2020
 */
class SocialLoginResponse :Serializable{
     var userid: String? = ""
     var tokenId: String? = ""
     var userName: String? = ""
     var userEmailId: String? = ""
     var userMobileno: String? = ""
     var userImage: String? = ""
     var dob: String? = ""
     var provider: String? = ""

}
