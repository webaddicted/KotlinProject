package com.webaddicted.kotlinproject.global.sharedpref

import com.webaddicted.kotlinproject.global.constant.PreferenceConstant
import com.webaddicted.kotlinproject.model.bean.language.LanguageBean
import com.webaddicted.kotlinproject.model.bean.preference.PreferenceBean
import com.webaddicted.kotlinproject.model.fcmkit.FcmSocialLoginRespoBean

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class PreferenceMgr constructor(var preferenceUtils: PreferenceUtils) {
    /**
     * set user session info
     */
    fun setUserInfo(preferenceBean: PreferenceBean) {
        preferenceUtils.setPreference(PreferenceConstant.PREF_USER_NAME, preferenceBean.name)
        preferenceUtils.setPreference(PreferenceConstant.PREF_USER_AGE, preferenceBean.age)
        preferenceUtils.setPreference(PreferenceConstant.PREF_USER_GENDER, preferenceBean.gender)
        preferenceUtils.setPreference(PreferenceConstant.PREF_USER_WEIGHT, preferenceBean.weight)
        preferenceUtils.setPreference(
            PreferenceConstant.PREF_USER_IS_MARRIED,
            preferenceBean.isMarried
        )
    }

    /**
     * get user session info
     */
    fun getUserInfo(): PreferenceBean {
        val preferenceBean = PreferenceBean()
        preferenceBean.name = preferenceUtils.getPreference(PreferenceConstant.PREF_USER_NAME, "")!!
        preferenceBean.gender =
            preferenceUtils.getPreference(PreferenceConstant.PREF_USER_GENDER, "")!!
        preferenceBean.age = preferenceUtils.getPreference(PreferenceConstant.PREF_USER_AGE, 0)!!
        preferenceBean.weight =
            preferenceUtils.getPreference(PreferenceConstant.PREF_USER_WEIGHT, 0L)!!
        preferenceBean.isMarried =
            preferenceUtils.getPreference(PreferenceConstant.PREF_USER_IS_MARRIED, false)!!
        return preferenceBean
    }

    /**
     * set user session info
     */
    fun setLanguage(languageBean: LanguageBean) {
        preferenceUtils.setPreference(PreferenceConstant.PREF_LANGUAGE_ID, languageBean.id)
        preferenceUtils.setPreference(
            PreferenceConstant.PREF_LANGUAGE_NAME,
            languageBean.languageName
        )
        preferenceUtils.setPreference(
            PreferenceConstant.PREF_LANGUAGE_CODE,
            languageBean.languageCode
        )
    }

    /**
     * get user session info
     */
    fun getLanguageInfo(): LanguageBean {
        return LanguageBean().apply {
            id = preferenceUtils.getPreference(PreferenceConstant.PREF_LANGUAGE_ID, "")!!
            languageName =
                preferenceUtils.getPreference(PreferenceConstant.PREF_LANGUAGE_NAME, "")!!
            languageCode =
                preferenceUtils.getPreference(PreferenceConstant.PREF_LANGUAGE_CODE, "")!!
        }
    }


    fun removeKey(removeKey: String) {
        preferenceUtils.removeKey(removeKey)
    }

    fun clearPref() {
        preferenceUtils.clearAllPreferences()
    }

    fun setFcmUserInfo(fcmUser: FcmSocialLoginRespoBean) {
        preferenceUtils.setPreference(PreferenceConstant.PREF_FCM_USERID, fcmUser.userId)
        preferenceUtils.setPreference(PreferenceConstant.PREF_FCM_UUID, fcmUser.uuid)
        preferenceUtils.setPreference(PreferenceConstant.PREF_FCM_IMEI, fcmUser.imei)
        preferenceUtils.setPreference(PreferenceConstant.PREF_FCM_TOKENID, fcmUser.tokenId)
        preferenceUtils.setPreference(PreferenceConstant.PREF_FCM_FCMTOKEN, fcmUser.fcmToken)
        preferenceUtils.setPreference(PreferenceConstant.PREF_FCM_NAME, fcmUser.userName)
        preferenceUtils.setPreference(PreferenceConstant.PREF_FCM_EMAILID, fcmUser.userEmailId)
        preferenceUtils.setPreference(PreferenceConstant.PREF_FCM_MOBILENO, fcmUser.userMobileno)
        preferenceUtils.setPreference(PreferenceConstant.PREF_FCM_IMAGE, fcmUser.userImage)
        preferenceUtils.setPreference(PreferenceConstant.PREF_FCM_DOB, fcmUser.dob)
        preferenceUtils.setPreference(PreferenceConstant.PREF_FCM_PROVIDER, fcmUser.provider)
//        preferenceUtils?.setPreference(PreferenceConstant.PREF_FCM_password, fcmUser.password)
    }

    fun getFcmUserInfo(): FcmSocialLoginRespoBean {
       return FcmSocialLoginRespoBean().apply {
            userId = preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_USERID, "")!!
            uuid =
                preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_UUID, "")!!
            imei =
                preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_IMEI, "")!!
            tokenId =
                preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_TOKENID, "")!!
            fcmToken =
                preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_FCMTOKEN, "")!!
            userName =
                preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_NAME, "")!!
            userEmailId =
                preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_EMAILID, "")!!
            userMobileno =
                preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_MOBILENO, "")!!
            userImage =
                preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_IMAGE, "")!!
            dob =
                preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_DOB, "")!!
            provider =
                preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_PROVIDER, "")!!
//           password =
//               preferenceUtils.getPreference(PreferenceConstant.PREF_FCM_PASSWORD, "")!!
       }
    }
}