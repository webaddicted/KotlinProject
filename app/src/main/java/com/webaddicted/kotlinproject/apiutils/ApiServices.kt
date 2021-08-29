package com.webaddicted.kotlinproject.apiutils

import com.webaddicted.kotlinproject.model.bean.common.FcmNotifBean
import com.webaddicted.kotlinproject.model.bean.common.NotiRespo
import com.webaddicted.kotlinproject.model.bean.newschannel.NewsChanelRespo
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
interface ApiServices {
    @GET
    fun getChannelListName(@Url strUrl: String): Deferred<Response<NewsChanelRespo>>//Call<CategoryDetails>
    @Headers("Authorization: key=${ApiConstant.SERVER_KEY}" , "Content-Type:application/json")
    @POST("fcm/send")
    fun getFcmNoti(@Body req1: FcmNotifBean): Deferred<Response<NotiRespo>>
}