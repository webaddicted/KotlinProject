package com.webaddicted.kotlinproject.global.db.migration

import android.database.Cursor
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.webaddicted.kotlinproject.global.db.entity.UserInfoEntity
import com.webaddicted.kotlinproject.model.repository.base.BaseRepository
import org.koin.core.KoinComponent


class DbMigrationHelper  : KoinComponent, BaseRepository(){
    fun migrateUserDataToPref(cursor: Cursor?) {
        cursor?.moveToFirst()
        val user = UserInfoEntity().apply {
            //String Variable
            id = cursor?.getInt(cursor.getColumnIndex("id")) ?: 0
            name= cursor?.getString(cursor.getColumnIndex("name")) ?: ""
            nickname = cursor?.getString(cursor.getColumnIndex("nickname")) ?: ""
            mobileno = cursor?.getString(cursor.getColumnIndex("mobileno")) ?: ""
            email = cursor?.getString(cursor.getColumnIndex("email")) ?: ""
            password = cursor?.getString(cursor.getColumnIndex("password")) ?: ""
        }
        insert(user)
    }
//    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("CREATE TABLE IF NOT EXISTS `Pet` (`name` TEXT NOT NULL, PRIMARY KEY(`name`))")
//        }
//    }
    fun insert(user: UserInfoEntity) {
        userInfoDao.insertUser(user)
//        mPref.prefSaveUserDat(user)
//        UserSession.user = user
    }

}