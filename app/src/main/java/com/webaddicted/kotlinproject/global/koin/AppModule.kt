package com.webaddicted.kotlinproject.global.koin

import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.webaddicted.kotlinproject.BuildConfig
import com.webaddicted.kotlinproject.apiutils.ApiConstant
import com.webaddicted.kotlinproject.apiutils.ApiServices
import com.webaddicted.kotlinproject.apiutils.ReflectionUtil
import com.webaddicted.kotlinproject.global.common.AppApplication
import com.webaddicted.kotlinproject.global.constant.DbConstant
import com.webaddicted.kotlinproject.global.db.database.AppDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Deepak Sharma on 01/07/19.
 */
val appModule = module {

    /* PROVIDE GSON SINGLETON */
    single<Gson> {
        val builder =
            GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        builder.setLenient().create()
    }

    /* PROVIDE RETROFIT SINGLETON */
    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)httpClient.addInterceptor(loggingInterceptor)
        httpClient.connectTimeout(ApiConstant.API_TIME_OUT, TimeUnit.MILLISECONDS)
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder().build()
            chain.proceed(request)
        }
        val okHttpClient = httpClient.build()

        Retrofit.Builder()
            .baseUrl(ApiConstant.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(get() as Gson))
            .build()
    }

    /*Provide API Service Singleton */
    single {
        (get<Retrofit>()).create(ApiServices::class.java)
    }
    single {
        Room.databaseBuilder(
            (androidApplication() as AppApplication),
            AppDatabase::class.java,
            DbConstant.DB_NAME
        ).allowMainThreadQueries().build()
        //.addMigrations(migration4To5, migration5To6).build()
    }
    single { (get() as AppDatabase).userInfoDao() }

//    single {
//        PreferenceHelper(
//            (androidApplication() as ShopAtHomeApplication).getSharedPreferences(
//                PreferenceConstants.PREFERENCE_NAME,
//                Context.MODE_PRIVATE
//            )
//        )
//    }


//    single {
//        ValidationHelper()
//    }
//
//    single {
//        Validator(get())
//    }

    single { ReflectionUtil(get()) }
}