package com.webaddicted.kotlinproject.viewModel.fcmkit

import androidx.lifecycle.MutableLiveData
import com.webaddicted.kotlinproject.apiutils.ApiResponse
import com.webaddicted.kotlinproject.model.bean.common.FcmNotifBean
import com.webaddicted.kotlinproject.model.bean.common.NotiRespo
import com.webaddicted.kotlinproject.model.fcmkit.FcmSocialLoginRespoBean
import com.webaddicted.kotlinproject.model.repository.fcmkit.FcmFoodRepository
import com.webaddicted.kotlinproject.viewModel.base.BaseViewModel

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class FcmFoodViewModel(private val projectRepository: FcmFoodRepository) : BaseViewModel() {
    fun getFcmFoodUserInfo(): FcmSocialLoginRespoBean {
        return projectRepository.getFcmFoodUserInfo()
    }

    fun setFcmFoodUserInfo(userInfo: FcmSocialLoginRespoBean) {
        projectRepository.setFcmFoodUserInfo(userInfo)
    }

    private var fcmNotif = MutableLiveData<ApiResponse<NotiRespo>>()

    fun getFcmNoti(): MutableLiveData<ApiResponse<NotiRespo>> {
        return fcmNotif
    }

    fun notiApi(fcmNotifBean: FcmNotifBean) {
        projectRepository.getFcmNoti(fcmNotifBean, fcmNotif)
    }
}