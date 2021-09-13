package com.webaddicted.kotlinproject.global.db.migration

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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










//@Database(entities = [
//    ActionLog::class,
//    Customer::class,
//    Visit::class,
//    AssignTo::class,
//    WheelCategory::class,
//    ActionToBeTaken::class,
//    ActionLogRecordType::class,
//    Product::class,
//    Cart::class,
//    CommitmentSheet::class,
//    BizCard::class,
//    Bco::class,
//    MappedDealer::class,
//    FleetInfo::class,
//    VehicleTracking::class,
//    Recommendation::class,
//    TyreTracking::class,
//    TyreMaster::class,
//    TyreInspection::class,
//    ScrapTyre::class
//],
//    version = 14)
//abstract class DbMigrationHelper : RoomDatabase() {
//
//    abstract fun actionLogDao(): ActionLogDao
//    abstract fun commitmentSheetDao(): CommitmentSheetDao
//    abstract fun customerDao(): CustomerDao
//    abstract fun assignToDao(): AssignToDao
//    abstract fun visitDao(): VisitDao
//    abstract fun wheelCatDao(): WheelCategoryDao
//    abstract fun actionToBeTakenDao(): ActionToBeTakenDao
//    abstract fun actionLogRecordType(): ActionLogRecordTypeDao
//    abstract fun productDao(): ProductDao
//    abstract fun cartDao(): CartDao
//    abstract fun bizCardDao(): BizCardDao
//    abstract fun bcoDao(): BcoDao
//    abstract fun dealerDao(): DealerDao
//    abstract fun fleetDao(): FleetDao
//    abstract fun vehicleDao(): VehicleDao
//    abstract fun recommendationDao(): RecommendationDao
//    abstract fun tyreTrackingDao(): TyreTrackingDao
//    abstract fun tyreMasterDao() : TyreMasterDao
//    abstract fun tyreInspectionDao() : TyreInspectionDao
//    abstract fun scrapTyreDao() : ScrapTyreDao
//
//    companion object {
//        private var INSTANCE: DbMigrationHelper? = null
//
//        fun getInstance(context: Context): DbMigrationHelper? {
//            if (INSTANCE == null) {
//                synchronized(DbMigrationHelper::class) {
//                    INSTANCE = Room.databaseBuilder(context.applicationContext,
//                        DbMigrationHelper::class.java, "pjp.db")
//                        .addMigrations(
//                            MIGRATION_1_2,
//                            MIGRATION_2_3,
//                            MIGRATION_3_4,
//                            MIGRATION_4_5,
//                            MIGRATION_5_6,
//                            MIGRATION_6_7,
//                            MIGRATION_7_8,
//                            MIGRATION_8_9,
//                            MIGRATION_9_10,
//                            MIGRATION_10_11,
//                            MIGRATION_11_12,
//                            MIGRATION_12_13,
//                            MIGRATION_13_14,
//
//                            )
//                        .build()
//                }
//            }
//            return INSTANCE
//        }
//
//        fun destroyInstance() {
//            INSTANCE = null
//        }
//
//        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//
//                database.execSQL("""
//                CREATE TABLE commitmentSheet (
//                    id TEXT PRIMARY KEY NOT NULL,
//                    commitmentType TEXT NOT NULL,
//                    commitmentDetails TEXT NOT NULL,
//                    targetDate TEXT NOT NULL,
//                    signImg TEXT NOT NULL,
//                    isImgSynced INTEGER NOT NULL
//                )
//                """.trimIndent())
//            }
//        }
//
//        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//
//                database.execSQL("""
//                ALTER TABLE visit ADD COLUMN isCheckedOut INTEGER NOT NULL DEFAULT 0
//                """.trimIndent())
//
//                database.execSQL("""
//                ALTER TABLE visit ADD COLUMN offlineId TEXT DEFAULT ''
//                """.trimIndent())
//            }
//        }
//
//        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//
//                database.execSQL("""
//                ALTER TABLE customer ADD COLUMN latitude REAL NOT NULL DEFAULT 0
//                """.trimIndent())
//
//                database.execSQL("""
//                ALTER TABLE customer ADD COLUMN longitude REAL NOT NULL DEFAULT 0
//                """.trimIndent())
//            }
//        }
//
//        private val MIGRATION_4_5: Migration = object : Migration(4, 5) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//
//                database.execSQL("""
//                ALTER TABLE visit ADD COLUMN checkInReason TEXT DEFAULT ''
//                """.trimIndent())
//
//                database.execSQL("""
//                ALTER TABLE visit ADD COLUMN isRemoteCheckIn INTEGER NOT NULL DEFAULT 0
//                """.trimIndent())
//            }
//        }
//
//        private val MIGRATION_5_6: Migration = object : Migration(5, 6) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//
//                database.execSQL("""
//                ALTER TABLE actionLog ADD COLUMN productId TEXT DEFAULT ''
//                """.trimIndent())
//
//                database.execSQL("""
//                ALTER TABLE actionLog ADD COLUMN productName TEXT DEFAULT ''
//                """.trimIndent())
//
//                database.execSQL("""
//                ALTER TABLE actionLog ADD COLUMN complaint TEXT DEFAULT ''
//                """.trimIndent())
//
//                database.execSQL("""
//                ALTER TABLE actionLog ADD COLUMN reportName TEXT DEFAULT ''
//                """.trimIndent())
//
//                database.execSQL("""
//                ALTER TABLE actionLog ADD COLUMN platform TEXT DEFAULT ''
//                """.trimIndent())
//            }
//        }
//
//        private val MIGRATION_6_7: Migration = object : Migration(6, 7) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("""
//                ALTER TABLE customer ADD COLUMN isEfleet INTEGER NOT NULL DEFAULT 0
//                """.trimIndent())
//
//                database.execSQL("""
//                ALTER TABLE customer ADD COLUMN directIndirect TEXT DEFAULT ''
//                """.trimIndent())
//
//                database.execSQL("""
//                ALTER TABLE customer ADD COLUMN salesGrp TEXT DEFAULT ''
//                """.trimIndent())
//
//                database.execSQL("""
//                ALTER TABLE customer ADD COLUMN recordTypeName TEXT DEFAULT ''
//                """.trimIndent())
//
//                database.execSQL("""
//                CREATE TABLE businessCard (
//                    iD TEXT PRIMARY KEY NOT NULL,
//                    cardNo TEXT,
//                    monthYr TEXT NOT NULL,
//                    customerName TEXT,
//                    createdDate TEXT,
//                    isSynced INTEGER NOT NULL DEFAULT 0,
//                    offlineId TEXT
//                )
//                """.trimIndent())
//
//                database.execSQL("""
//                CREATE TABLE bco (
//                    iD TEXT PRIMARY KEY NOT NULL,
//                    bcoNo TEXT,
//                    name TEXT NOT NULL,
//                    brand TEXT,
//                    tyreClass TEXT,
//                    offtake TEXT,
//                    associatedDealer TEXT,
//                    prospectDealer TEXT,
//                    bizCardId TEXT,
//                    customerId TEXT,
//                    isSynced INTEGER NOT NULL,
//                    createdDate TEXT,
//                    offlineId TEXT
//                )
//                """.trimIndent())
//
//                database.execSQL("""
//                CREATE TABLE mappedDealer (
//                    iD TEXT PRIMARY KEY NOT NULL,
//                    name TEXT,
//                    dealerId TEXT NOT NULL,
//                    dealerName TEXT,
//                    prospectDealer TEXT,
//                    customerId TEXT,
//                    sapCode TEXT,
//                    recordTypeName TEXT,
//                    isSynced INTEGER NOT NULL,
//                    createdDate TEXT,
//                    offlineId TEXT
//                )
//                """.trimIndent())
//            }
//        }
//
//        private val MIGRATION_7_8: Migration = object : Migration(7, 8) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                try {
//                    val c = database.query("SELECT * FROM customer")
//                    c.use {
//                        val cvList = ArrayList<ContentValues>()
//                        while (c.moveToNext()) {
//                            val cv = ContentValues()
//                            cv.put("id", c.getString(c.getColumnIndex("id")))
//                            cv.put("isSynced", c.getInt(c.getColumnIndex("isSynced")))
//                            cv.put("isActive", c.getInt(c.getColumnIndex("isActive")))
//                            cv.put("businessUnit", c.getString(c.getColumnIndex("businessUnit")))
//                            cv.put("custGrp", c.getString(c.getColumnIndex("custGrp")))
//                            cv.put("custSegment", c.getString(c.getColumnIndex("custSegment")))
//                            cv.put("custScore", c.getString(c.getColumnIndex("custScore")))
//                            cv.put("dealerType", c.getString(c.getColumnIndex("dealerType")))
//                            cv.put("sapCode", c.getString(c.getColumnIndex("sapCode")))
//                            cv.put("name", c.getString(c.getColumnIndex("name")))
//                            cv.put("recordType", c.getString(c.getColumnIndex("recordType")))
//                            cv.put("territory", c.getString(c.getColumnIndex("territory")))
//                            cv.put("town", c.getString(c.getColumnIndex("town")))
//                            cv.put("type", c.getString(c.getColumnIndex("type")))
//                            cv.put("uniqueIdentifier", c.getString(c.getColumnIndex("uniqueIdentifier")))
//                            cv.put("rating", c.getString(c.getColumnIndex("rating")))
//                            cv.put("infuencerType", c.getString(c.getColumnIndex("infuencerType")))
//                            cv.put("email", c.getString(c.getColumnIndex("email")))
//                            cv.put("contact1", c.getString(c.getColumnIndex("contact1")))
//                            cv.put("contact2", c.getString(c.getColumnIndex("contact2")))
//                            cv.put("address", c.getString(c.getColumnIndex("address")))
//                            cv.put("latitude", c.getDouble(c.getColumnIndex("latitude")))
//                            cv.put("longitude", c.getDouble(c.getColumnIndex("longitude")))
//                            cv.put("isEfleet", c.getInt(c.getColumnIndex("isEfleet")))
//                            cv.put("directIndirect", c.getString(c.getColumnIndex("directIndirect")))
//                            cv.put("salesGrp", c.getString(c.getColumnIndex("salesGrp")))
//                            cv.put("recordTypeName", c.getString(c.getColumnIndex("recordTypeName")))
//                            cvList.add(cv)
//                        }
//                        database.execSQL("DROP TABLE IF EXISTS `customer`")
//                        createCustomerTable(database)
//                        for (row in cvList) {
//                            database.insert("customer", 0, row)
//                        }
//                    }
//                } catch (e: SQLiteException) {
//                    Log.e(e.message, "SQLiteException in migrate from database version 7 to version 8")
//                } catch (e: Exception) {
//                    Log.e(e.message, "Failed to migrate database version 7 to version 8")
//                }
//
//                //bizCard
//                try {
//                    val c = database.query("SELECT * FROM businessCard")
//                    c.use {
//                        val cvList = ArrayList<ContentValues>()
//                        while (c.moveToNext()) {
//                            val cv = ContentValues()
//                            cv.put("id", c.getString(c.getColumnIndex("id")))
//                            cv.put("cardNo", c.getString(c.getColumnIndex("cardNo")))
//                            cv.put("monthYr", c.getString(c.getColumnIndex("monthYr")))
//                            cv.put("customerName", c.getString(c.getColumnIndex("customerName")))
//                            cv.put("createdDate", c.getString(c.getColumnIndex("createdDate")))
//                            cv.put("isSynced", c.getInt(c.getColumnIndex("isSynced")))
//                            cv.put("offlineId", c.getString(c.getColumnIndex("offlineId")))
//                            cvList.add(cv)
//                        }
//                        database.execSQL("DROP TABLE IF EXISTS `businessCard`")
//                        createBizCardTable(database)
//                        for (row in cvList) {
//                            database.insert("businessCard", 0, row)
//                        }
//                    }
//                } catch (e: SQLiteException) {
//                    Log.e(e.message, "SQLiteException in migrate from database version 7 to version 8")
//                } catch (e: Exception) {
//                    Log.e(e.message, "Failed to migrate database version 7 to version 8")
//                }
//
//                //bco
//                try {
//                    val c = database.query("SELECT * FROM bco")
//                    c.use {
//                        val cvList = ArrayList<ContentValues>()
//                        while (c.moveToNext()) {
//                            val cv = ContentValues()
//                            cv.put("id", c.getString(c.getColumnIndex("id")))
//                            cv.put("name", c.getString(c.getColumnIndex("name")))
//                            cv.put("brand", c.getString(c.getColumnIndex("brand")))
//                            cv.put("tyreClass", c.getString(c.getColumnIndex("tyreClass")))
//                            cv.put("offtake", c.getString(c.getColumnIndex("offtake")))
//                            cv.put("associatedDealer", c.getString(c.getColumnIndex("associatedDealer")))
//                            cv.put("prospectDealer", c.getString(c.getColumnIndex("prospectDealer")))
//                            cv.put("bizCardId", c.getString(c.getColumnIndex("bizCardId")))
//                            cv.put("customerId", c.getString(c.getColumnIndex("customerId")))
//                            cv.put("createdDate", c.getString(c.getColumnIndex("createdDate")))
//                            cv.put("isSynced", c.getInt(c.getColumnIndex("isSynced")))
//                            cv.put("offlineId", c.getString(c.getColumnIndex("offlineId")))
//                            cvList.add(cv)
//                        }
//                        database.execSQL("DROP TABLE IF EXISTS `bco`")
//                        createBCOTable(database)
//                        for (row in cvList) {
//                            database.insert("bco", 0, row)
//                        }
//                    }
//                } catch (e: SQLiteException) {
//                    Log.e(e.message, "SQLiteException in migrate from database version 7 to version 8")
//                } catch (e: Exception) {
//                    Log.e(e.message, "Failed to migrate database version 7 to version 8")
//                }
//
//
//                //mapped dealer
//                try {
//                    val c = database.query("SELECT * FROM mappedDealer")
//                    c.use {
//                        val cvList = ArrayList<ContentValues>()
//                        while (c.moveToNext()) {
//                            val cv = ContentValues()
//                            cv.put("id", c.getString(c.getColumnIndex("id")))
//                            cv.put("name", c.getString(c.getColumnIndex("name")))
//                            cv.put("dealerId", c.getString(c.getColumnIndex("dealerId")))
//                            cv.put("dealerName", c.getString(c.getColumnIndex("dealerName")))
//                            cv.put("prospectDealer", c.getString(c.getColumnIndex("prospectDealer")))
//                            cv.put("customerId", c.getString(c.getColumnIndex("customerId")))
//                            cv.put("sapCode", c.getString(c.getColumnIndex("sapCode")))
//                            cv.put("recordTypeName", c.getString(c.getColumnIndex("recordTypeName")))
//                            cv.put("createdDate", c.getString(c.getColumnIndex("createdDate")))
//                            cv.put("isSynced", c.getInt(c.getColumnIndex("isSynced")))
//                            cv.put("offlineId", c.getString(c.getColumnIndex("offlineId")))
//                            cvList.add(cv)
//                        }
//                        database.execSQL("DROP TABLE IF EXISTS `mappedDealer`")
//                        createDealerTable(database)
//                        for (row in cvList) {
//                            database.insert("mappedDealer", 0, row)
//                        }
//                    }
//                } catch (e: SQLiteException) {
//                    Log.e(e.message, "SQLiteException in migrate from database version 7 to version 8")
//                } catch (e: Exception) {
//                    Log.e(e.message, "Failed to migrate database version 7 to version 8")
//                }
//            }
//        }
//
//        fun createCustomerTable(database: SupportSQLiteDatabase) {
//            database.execSQL("""CREATE TABLE IF NOT EXISTS `customer` (
//                            `id` TEXT NOT NULL,
//                            `isSynced` INTEGER NOT NULL,
//                            `isActive` INTEGER NOT NULL,
//                            `businessUnit` TEXT NOT NULL,
//                            `custGrp` TEXT NOT NULL,
//                            `custSegment` TEXT NOT NULL,
//                            `custScore` TEXT NOT NULL,
//                            `dealerType` TEXT NOT NULL,
//                            `sapCode` TEXT NOT NULL,
//                            `name` TEXT NOT NULL,
//                            `recordType` TEXT NOT NULL,
//                            `territory` TEXT NOT NULL,
//                            `town` TEXT NOT NULL,
//                            `type` TEXT NOT NULL,
//                            `uniqueIdentifier` TEXT NOT NULL,
//                            `rating` TEXT NOT NULL,
//                            `infuencerType` TEXT NOT NULL,
//                            `email` TEXT NOT NULL,
//                            `contact1` TEXT NOT NULL,
//                            `contact2` TEXT NOT NULL,
//                            `address` TEXT NOT NULL,
//                            `latitude` REAL NOT NULL,
//                            `longitude` REAL NOT NULL,
//                            `isEfleet` INTEGER NOT NULL DEFAULT 0,
//                            `directIndirect` TEXT NOT NULL DEFAULT '',
//                            `salesGrp` TEXT NOT NULL DEFAULT '',
//                            `recordTypeName` TEXT NOT NULL DEFAULT '',
//                            PRIMARY KEY(`id`))""".trimIndent())
//        }
//
//        fun createBizCardTable(database: SupportSQLiteDatabase) {
//            database.execSQL("""CREATE TABLE IF NOT EXISTS `businessCard` (
//                            `id` TEXT NOT NULL DEFAULT '',
//                            `cardNo` TEXT NOT NULL DEFAULT '',
//                            `monthYr` TEXT NOT NULL DEFAULT '',
//                            `customerName` TEXT NOT NULL DEFAULT '',
//                            `createdDate` TEXT NOT NULL DEFAULT '',
//                            `isSynced` INTEGER NOT NULL DEFAULT 0,
//                            `offlineId` TEXT NOT NULL DEFAULT '',
//                            PRIMARY KEY(`id`))""".trimIndent())
//        }
//
//        fun createBCOTable(database: SupportSQLiteDatabase) {
//            database.execSQL("""CREATE TABLE IF NOT EXISTS `bco` (
//                            `id` TEXT NOT NULL DEFAULT '',
//                            `name` TEXT NOT NULL DEFAULT '',
//                            `brand` TEXT NOT NULL DEFAULT '',
//                            `tyreClass` TEXT NOT NULL DEFAULT '',
//                            `offtake` TEXT NOT NULL DEFAULT '',
//                            `associatedDealer` TEXT NOT NULL DEFAULT '',
//                            `prospectDealer` TEXT NOT NULL DEFAULT '',
//                            `bizCardId` TEXT NOT NULL DEFAULT '',
//                            `customerId` TEXT NOT NULL DEFAULT '',
//                            `createdDate` TEXT NOT NULL DEFAULT '',
//                            `isSynced` INTEGER NOT NULL DEFAULT 0,
//                            `offlineId` TEXT NOT NULL DEFAULT '',
//                            PRIMARY KEY(`id`))""".trimIndent())
//        }
//
//        fun createDealerTable(database: SupportSQLiteDatabase) {
//            database.execSQL("""CREATE TABLE IF NOT EXISTS `mappedDealer` (
//                            `id` TEXT NOT NULL DEFAULT '',
//                            `name` TEXT NOT NULL DEFAULT '',
//                            `dealerId` TEXT NOT NULL DEFAULT '',
//                            `dealerName` TEXT NOT NULL DEFAULT '',
//                            `prospectDealer` TEXT NOT NULL DEFAULT '',
//                            `customerId` TEXT NOT NULL DEFAULT '',
//                            `sapCode` TEXT NOT NULL DEFAULT '',
//                            `recordTypeName` TEXT NOT NULL DEFAULT '',
//                            `createdDate` TEXT NOT NULL DEFAULT '',
//                            `isSynced` INTEGER NOT NULL DEFAULT 0,
//                            `offlineId` TEXT NOT NULL DEFAULT '',
//                            PRIMARY KEY(`id`))""".trimIndent())
//        }
//
//        private val MIGRATION_8_9: Migration = object : Migration(8, 9) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("""
//                ALTER TABLE customer ADD COLUMN fleetSize TEXT NOT NULL DEFAULT ''
//                """.trimIndent())
//
//                database.execSQL("""CREATE TABLE IF NOT EXISTS `fleet` (
//                            `id` TEXT NOT NULL DEFAULT '',
//                            `name` TEXT NOT NULL DEFAULT '',
//                            `application` TEXT NOT NULL DEFAULT '',
//                            `leadHaulType` TEXT NOT NULL DEFAULT '',
//                            `loadClassification` TEXT NOT NULL DEFAULT '',
//                            `noOfVehicles` INTEGER NOT NULL DEFAULT '',
//                            `materialCarried` TEXT NOT NULL DEFAULT '',
//                            `frontBias` TEXT NOT NULL DEFAULT '',
//                            `frontRadial` TEXT NOT NULL DEFAULT '',
//                            `rearBias` TEXT NOT NULL DEFAULT '',
//                            `rearRadial` TEXT NOT NULL DEFAULT '',
//                            `transporterName` TEXT NOT NULL DEFAULT '',
//                            `createdDate` TEXT NOT NULL DEFAULT '',
//                            `isSynced` INTEGER NOT NULL DEFAULT 0,
//                            `offlineId` TEXT NOT NULL DEFAULT '',
//                            PRIMARY KEY(`id`))""".trimIndent())
//
//                database.execSQL("""CREATE TABLE IF NOT EXISTS `vehicle-tracking` (
//                            `id` TEXT NOT NULL DEFAULT '',
//                            `name` TEXT NOT NULL DEFAULT '',
//                            `registrationNo` TEXT NOT NULL DEFAULT '',
//                            `truckMake` TEXT NOT NULL DEFAULT '',
//                            `wheelType` TEXT NOT NULL DEFAULT '',
//                            `itmExpectation` REAL NOT NULL DEFAULT '',
//                            `retreadExpectation` TEXT NOT NULL DEFAULT '',
//                            `associatedFleet` TEXT NOT NULL DEFAULT '',
//                            `customerId` TEXT NOT NULL DEFAULT '',
//                            `createdDate` TEXT NOT NULL DEFAULT '',
//                            `isSynced` INTEGER NOT NULL DEFAULT 0,
//                            `offlineId` TEXT NOT NULL DEFAULT '',
//                            PRIMARY KEY(`id`))""".trimIndent())
//
//                database.execSQL("""CREATE TABLE IF NOT EXISTS `recommendation` (
//                            `id` TEXT NOT NULL DEFAULT '',
//                            `recommendation` TEXT NOT NULL DEFAULT '',
//                            `application` TEXT NOT NULL DEFAULT '',
//                            `leadHaulType` TEXT NOT NULL DEFAULT '',
//                            `loadClassification` TEXT NOT NULL DEFAULT '',
//                            `tyreClass` TEXT NOT NULL DEFAULT '',
//                            `axle` TEXT NOT NULL DEFAULT '',
//                            `createdDate` TEXT NOT NULL DEFAULT '',
//                            PRIMARY KEY(`id`))""".trimIndent())
//            }
//        }
//
//        private val MIGRATION_9_10: Migration = object : Migration(9, 10) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("""CREATE TABLE IF NOT EXISTS `tyre-tracking` (
//                            `id` TEXT NOT NULL DEFAULT '',
//                            `name` TEXT NOT NULL DEFAULT '',
//                            `tyreSrNo` TEXT NOT NULL DEFAULT '',
//                            `wheelType` TEXT NOT NULL DEFAULT '',
//                            `tyrePos` TEXT NOT NULL DEFAULT '',
//                            `tyreBrand` TEXT NOT NULL DEFAULT '',
//                            `tyreClass` TEXT NOT NULL DEFAULT '',
//                            `pattern` TEXT NOT NULL DEFAULT '',
//                            `tyreSize` TEXT NOT NULL DEFAULT '',
//                            `trackingLifeFor` TEXT NOT NULL DEFAULT '',
//                            `tyreType` TEXT NOT NULL DEFAULT '',
//                            `fitmentDate` TEXT NOT NULL DEFAULT '',
//                            `odoMeterReading` TEXT NOT NULL DEFAULT '',
//                            `airPressure` TEXT NOT NULL DEFAULT '',
//                            `relatedFleetVehicle` TEXT NOT NULL DEFAULT '',
//                            `customerId` TEXT NOT NULL DEFAULT '',
//                            `createdDate` TEXT NOT NULL DEFAULT '',
//                            `isSynced` INTEGER NOT NULL DEFAULT 0,
//                            `offlineId` TEXT NOT NULL DEFAULT '',
//                            `syncError` TEXT NOT NULL DEFAULT '',
//                            PRIMARY KEY(`id`))""".trimIndent())
//
//                database.execSQL("""CREATE TABLE IF NOT EXISTS `tyre-master` (
//                            `id` TEXT NOT NULL DEFAULT '',
//                            `name` TEXT NOT NULL DEFAULT '',
//                            `tyreBrand` TEXT NOT NULL DEFAULT '',
//                            `tyreClass` TEXT NOT NULL DEFAULT '',
//                            `tyrePattern` TEXT NOT NULL DEFAULT '',
//                            `tyreSize` TEXT NOT NULL DEFAULT '',
//                            `originalNsd` TEXT NOT NULL DEFAULT '',
//                            `createdDate` TEXT NOT NULL DEFAULT '',
//                            PRIMARY KEY(`id`))""".trimIndent())
//
//                database.execSQL("""CREATE TABLE IF NOT EXISTS `tyre-inspection` (
//                            `id` TEXT NOT NULL DEFAULT '',
//                            `name` TEXT NOT NULL DEFAULT '',
//                            `inspectionDate` TEXT NOT NULL DEFAULT '',
//                            `odometerReading` TEXT NOT NULL DEFAULT '',
//                            `latestNsd` TEXT NOT NULL DEFAULT '',
//                            `airPressure` TEXT NOT NULL DEFAULT '',
//                            `remarks` TEXT NOT NULL DEFAULT '',
//                            `isRemove` INTEGER NOT NULL DEFAULT 0,
//                            `removalReason` TEXT NOT NULL DEFAULT '',
//                            `customerId` TEXT NOT NULL DEFAULT '',
//                            `vehicleId` TEXT NOT NULL DEFAULT '',
//                            `tyreId` TEXT NOT NULL DEFAULT '',
//                            `serialNo` TEXT NOT NULL DEFAULT '',
//                            `createdDate` TEXT NOT NULL DEFAULT '',
//                            `isSynced` INTEGER NOT NULL DEFAULT 0,
//                            `offlineId` TEXT NOT NULL DEFAULT '',
//                            `syncError` TEXT NOT NULL DEFAULT '',
//                            PRIMARY KEY(`id`))""".trimIndent())
//
//                database.execSQL("""CREATE TABLE IF NOT EXISTS `scrap-tyre` (
//                            `id` TEXT NOT NULL DEFAULT '',
//                            `name` TEXT NOT NULL DEFAULT '',
//                            `tyreSrNo` TEXT NOT NULL DEFAULT '',
//                            `inspectionDate` TEXT NOT NULL DEFAULT '',
//                            `tyreBrand` TEXT NOT NULL DEFAULT '',
//                            `tyreClass` TEXT NOT NULL DEFAULT '',
//                            `pattern` TEXT NOT NULL DEFAULT '',
//                            `tyreSize` TEXT NOT NULL DEFAULT '',
//                            `actualNsd` TEXT NOT NULL DEFAULT '',
//                            `nsdShoulder` TEXT NOT NULL DEFAULT '',
//                            `defectType` TEXT NOT NULL DEFAULT '',
//                            `customerId` TEXT NOT NULL DEFAULT '',
//                            `createdDate` TEXT NOT NULL DEFAULT '',
//                            `isSynced` INTEGER NOT NULL DEFAULT 0,
//                            `offlineId` TEXT NOT NULL DEFAULT '',
//                            `syncError` TEXT NOT NULL DEFAULT '',
//                            PRIMARY KEY(`id`))""".trimIndent())
//            }
//        }
//
//        private val MIGRATION_10_11: Migration = object : Migration(10, 11) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("""
//                ALTER TABLE customer ADD COLUMN orderLock TEXT NOT NULL DEFAULT ''
//                """.trimIndent())
//            }
//        }
//
//        private val MIGRATION_11_12: Migration = object : Migration(11, 12) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("""
//                ALTER TABLE customer ADD COLUMN orderLockType TEXT NOT NULL DEFAULT ''
//                """.trimIndent())
//            }
//        }
//
//        private val MIGRATION_12_13: Migration = object : Migration(12, 13) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("""
//                ALTER TABLE customer ADD COLUMN whatsAppNo TEXT NOT NULL DEFAULT ''
//                """.trimIndent())
//            }
//        }
//        private val MIGRATION_13_14: Migration = object : Migration(13, 14) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("""
//                ALTER TABLE Product ADD COLUMN stockAlertThreshold INTEGER
//                """.trimIndent())
//            }
////            override fun migrate(database: SupportSQLiteDatabase) {
////                database.execSQL("""
////                ALTER TABLE Product ADD COLUMN stockAlertThreshold INTEGER NOT NULL DEFAULT 0
////                """.trimIndent())
////            }
//        }
//    }
//}