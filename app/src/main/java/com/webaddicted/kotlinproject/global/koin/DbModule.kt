package com.webaddicted.kotlinproject.global.koin

import androidx.room.Room
import com.webaddicted.kotlinproject.global.common.AppApplication
import com.webaddicted.kotlinproject.global.constant.DbConstant
import com.webaddicted.kotlinproject.global.db.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Created by Deepak Sharma on 01/07/19.
 */
val dbModule = module(override = true) {

    single {
//        val migration5To6 = object : Migration(5, 6) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                Lg.d("MIGRATION", "MIGRATION CALLED 5--6")
//                val cursor = database.query("SELECT * FROM User")
//                if (cursor.count > 0) {
//                    get<DbMigrationHelper>().migrateUserDataToPref(cursor)
//                }
//                database.execSQL("DROP TABLE user_info")
////                    database.execSQL("DROP TABLE Test_table")
////                    database.execSQL("DROP TABLE my_test")
//            }
//        }
//        val migration4To5 = object : Migration(4, 5) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                Lg.d("MIGRATION", "MIGRATION CALLED 4--5")
//            }
//        }
        Room.databaseBuilder(
            (androidApplication() as AppApplication),
            AppDatabase::class.java,
            DbConstant.DB_NAME
        ).allowMainThreadQueries().build()
        //.addMigrations(migration4To5, migration5To6).build()
    }

    single { (get() as AppDatabase).userInfoDao() }
}