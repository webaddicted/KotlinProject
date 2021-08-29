package com.webaddicted.kotlinproject.model.repository.news

import androidx.lifecycle.MutableLiveData
import com.webaddicted.kotlinproject.apiutils.ApiResponse
import com.webaddicted.kotlinproject.apiutils.ApiServices
import com.webaddicted.kotlinproject.apiutils.DataFetchCall
import com.webaddicted.kotlinproject.model.bean.newschannel.NewsChanelRespo
import com.webaddicted.kotlinproject.model.repository.base.BaseRepository
import kotlinx.coroutines.Deferred
import retrofit2.Response
/**
 * Created by Deepak Sharma on 01/07/19.
 */
class NewsRepository constructor(private val apiServices: ApiServices) : BaseRepository() {
    fun getNewsChannel(strUrl: String, loginResponse: MutableLiveData<ApiResponse<NewsChanelRespo>>) {
        object : DataFetchCall<NewsChanelRespo>(loginResponse) {
            override fun createCallAsync(): Deferred<Response<NewsChanelRespo>> {
                return apiServices.getChannelListName(strUrl)
            }

            override fun shouldFetchFromDB(): Boolean {
                return false
            }
        }.execute()
    }
}