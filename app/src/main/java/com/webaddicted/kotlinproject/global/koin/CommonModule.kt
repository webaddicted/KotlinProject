package com.webaddicted.kotlinproject.global.koin

import com.webaddicted.kotlinproject.global.common.MediaPickerUtils
import com.webaddicted.kotlinproject.global.sharedpref.PreferenceMgr
import com.webaddicted.kotlinproject.global.sharedpref.PreferenceUtils
import org.koin.dsl.module

/**
 * Created by Deepak Sharma on 01/07/19.
 */
val commonModelModule = module {
    single { PreferenceUtils() }
    single { PreferenceMgr(get()) }
    single { MediaPickerUtils() }

//    single { AppViewModelFactory() }
//    single { ThemeColors(get(), get()) }
}