package com.webaddicted.kotlinproject.global.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.webaddicted.kotlinproject.global.constant.DbConstant
import com.webaddicted.kotlinproject.global.db.dao.UserInfoDao
import com.webaddicted.kotlinproject.global.db.entity.UserInfoEntity
import org.koin.core.KoinComponent

@Database(entities = [UserInfoEntity::class], version = DbConstant.DB_VERSION,
exportSchema = true)
abstract class AppDatabase : KoinComponent, RoomDatabase() {

    abstract fun userInfoDao(): UserInfoDao
}