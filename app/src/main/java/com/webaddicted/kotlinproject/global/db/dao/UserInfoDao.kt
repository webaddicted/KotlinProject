package com.webaddicted.kotlinproject.global.db.dao

import androidx.room.*
import com.webaddicted.kotlinproject.global.db.entity.UserInfoEntity

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userInfo: UserInfoEntity)

    @Query("SELECT * FROM user_info")
    fun getUserInfo(): List<UserInfoEntity>

    @Query("SELECT * FROM user_info WHERE  email >= :emailId")
    fun getCouponsBySize(emailId: String): UserInfoEntity

    @Delete
    fun deleteUser(userInfo: UserInfoEntity)

    @Query("DELETE FROM user_info")
    fun cleatTable()
}