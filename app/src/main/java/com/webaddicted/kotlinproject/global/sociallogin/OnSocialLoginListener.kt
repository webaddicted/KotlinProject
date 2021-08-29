package com.webaddicted.kotlinproject.global.sociallogin

import com.webaddicted.kotlinproject.global.sociallogin.model.SocialLoginResponse

/**
 * Created by Deepak Sharma(webaddicted) on 29/03/2020
 */

interface OnSocialLoginListener {
    fun onSocialLoginSuccess(loginResponse: SocialLoginResponse?)
    fun onSocialLoginSuccess(success: String?)
    fun onSocialLoginFailure(failure: String?)
}
