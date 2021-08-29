package com.webaddicted.kotlinproject.model.repository.fcmkit

import androidx.lifecycle.MutableLiveData
import com.webaddicted.kotlinproject.apiutils.ApiResponse
import com.webaddicted.kotlinproject.apiutils.ApiServices
import com.webaddicted.kotlinproject.apiutils.DataFetchCall
import com.webaddicted.kotlinproject.model.bean.common.FcmNotifBean
import com.webaddicted.kotlinproject.model.bean.common.NotiRespo
import com.webaddicted.kotlinproject.model.fcmkit.FcmSocialLoginRespoBean
import com.webaddicted.kotlinproject.model.repository.base.BaseRepository
import kotlinx.coroutines.Deferred
import retrofit2.Response

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class FcmFoodRepository constructor(private val apiServices: ApiServices) : BaseRepository() {
    fun setFcmFoodUserInfo(fcmUser: FcmSocialLoginRespoBean) {
        preferenceMgr.setFcmUserInfo(fcmUser)
    }

    fun getFcmFoodUserInfo(): FcmSocialLoginRespoBean {
        return preferenceMgr.getFcmUserInfo()
    }

    fun getFcmNoti(req1: FcmNotifBean,loginResponse: MutableLiveData<ApiResponse<NotiRespo>>) {
        object : DataFetchCall<NotiRespo>(loginResponse) {
            override fun createCallAsync(): Deferred<Response<NotiRespo>> {
                return apiServices.getFcmNoti(req1)
            }

            override fun shouldFetchFromDB(): Boolean {
                return false
            }
        }.execute()
    }
}