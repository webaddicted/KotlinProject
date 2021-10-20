package com.webaddicted.kotlinproject.viewModel.common

import com.webaddicted.kotlinproject.global.db.entity.UserInfoEntity
import com.webaddicted.kotlinproject.viewModel.base.BaseViewModel

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class CommonViewModel : BaseViewModel() {

    /**
     * insert user info
     */
    fun insertUser(userInfo: UserInfoEntity) {
        userInfoDao.insertUser(userInfo)
    }

    fun getUserInfo() {
        userInfoDao.getUserInfo()
    }

    fun getEmailId(emailId: String): UserInfoEntity {
        return userInfoDao.getCouponsBySize(emailId)
    }

    fun deleteUser(userInfo: UserInfoEntity) {
        userInfoDao.deleteUser(userInfo)
    }

    fun cleatTable() {
        userInfoDao.cleatTable()
    }
}