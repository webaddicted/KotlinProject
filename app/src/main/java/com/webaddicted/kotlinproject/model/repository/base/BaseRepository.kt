package com.webaddicted.kotlinproject.model.repository.base

import com.webaddicted.kotlinproject.global.db.dao.UserInfoDao
import com.webaddicted.kotlinproject.global.sharedpref.PreferenceMgr
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by Deepak Sharma on 01/07/19.
 */
open class BaseRepository : KoinComponent {
    protected val preferenceMgr: PreferenceMgr by inject()
    protected val userInfoDao: UserInfoDao by inject()

}