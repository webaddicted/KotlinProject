package com.webaddicted.kotlinproject.global.koin


import com.webaddicted.kotlinproject.viewModel.common.CommonViewModel
import com.webaddicted.kotlinproject.viewModel.fcmkit.FcmFoodViewModel
import com.webaddicted.kotlinproject.viewModel.home.HomeViewModel
import com.webaddicted.kotlinproject.viewModel.list.NewsViewModel
import com.webaddicted.kotlinproject.viewModel.main.MainViewModel
import com.webaddicted.kotlinproject.viewModel.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Deepak Sharma on 01/07/19.
 */
val viewModelModule = module {
    viewModel { NewsViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { MapViewModel(get()) }
    viewModel { CommonViewModel() }
    viewModel { FcmFoodViewModel(get()) }
}