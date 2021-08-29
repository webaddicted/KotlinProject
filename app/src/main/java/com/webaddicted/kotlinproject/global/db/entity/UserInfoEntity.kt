package com.webaddicted.kotlinproject.global.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.webaddicted.kotlinproject.global.constant.DbConstant

@Entity(tableName = DbConstant.USER_INFO_TABLE)
class UserInfoEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var name: String? = null
    var nickname: String? = null
    var mobileno: String? = null
    var email: String? = null
    var password: String? = null
}