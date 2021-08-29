package com.webaddicted.kotlinproject.apiutils

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class ApiConstant {

    companion object {



        const val PHONE_AUTH_TIMEOUT: Long = 119

        /*********API BASE URL************/
        const val BASE_URL = "https://fcm.googleapis.com/"
        const val API_TIME_OUT: Long = 6000

        //        START FIREBASE CHILD
        const val FCM_DB_USERS = "Users"
        const val FCM_DB_CATEGORY = "Category"

        //        END FIREBASE CHILD

        //        START FIREBASE STORAGE FOLDER
        const val FCM_STORAGE_PROFILE = "UserProfileImages/"
        //        END FIREBASE STORAGE FOLDER

        //        START FIREBASE KEY
        const val FCM_USERS_EMAIL_ID = "userEmailId"
        const val FCM_USERS_MOBILE_NO = "userMobileno"
        const val FCM_USERS_PASSWORD = "password"
        const val FCM_USERS_FCM_TOKEN= "fcmToken"
        //        END FIREBASE KEY
        const val SERVER_KEY= "AAAAsi55X5Y:APA91bGoXntyJcBoW8evgk8DuZNDfhb6m7iu4Vs8Uf-cW2IqjlcM0GOu6DzqOZxbJkQdnXvq9E3pmdi9CieSBA9A0vU4un9ja6_KiFT9r8k9pk3QZIxxe7wLRJEqErxa1sS8O_ZMyjOK"
        const val FCM_TOPIC_NAME: String = "weather"
    }
}