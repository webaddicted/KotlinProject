package com.webaddicted.kotlinproject.global.services

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.webaddicted.kotlinproject.global.common.AppApplication
import com.webaddicted.kotlinproject.global.common.GlobalUtility

/**
 * Created by Deepak Sharma(webaddicted) on 04/04/2020
 */
class WorkManagerService(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        GlobalUtility.showOfflineNotification(AppApplication.context, "WorkManagerService", "work descrip")
        return Result.Success()
    }

}