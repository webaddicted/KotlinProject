package com.webaddicted.kotlinproject.apiutils

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
/**
 * Created by Deepak Sharma on 01/07/19.
 */
abstract class DataFetchCall<ResultType>(private val responseLiveData: MutableLiveData<ApiResponse<ResultType>>) {
    abstract fun createCallAsync(): Deferred<Response<ResultType>>
    abstract fun shouldFetchFromDB(): Boolean
    open fun loadFromDB(){
    }
    open fun saveResult(result: ResultType){

    }

     fun execute() {
        if (shouldFetchFromDB()) {
            loadFromDB()
        } else {
            GlobalScope.launch {
                try {
                    responseLiveData.postValue(ApiResponse.loading())
                    val request = createCallAsync()
                    val response = request.await()
                    if (response.body() != null) {
                        saveResult(response.body()!!)
                        responseLiveData.postValue(ApiResponse.success(response.body()!!))
                    } else {
                        responseLiveData.postValue(ApiResponse.error(Throwable(response.errorBody().toString())))
                    }
                } catch (exception: Exception) {
                    responseLiveData.postValue(ApiResponse.error(exception))
                }
            }
        }
    }

}