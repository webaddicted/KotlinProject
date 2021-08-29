package com.webaddicted.kotlinproject.global.services

import android.content.Context
import com.firebase.jobdispatcher.*
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.Lg

/**
 * Created by Deepak Sharma(webaddicted) on 03/04/2020
 */
class ScheduledJobService : JobService() {
    companion object {
        var TAG = ScheduledJobService::class.java.simpleName

        fun scheduleJob(context: Context?) {
            //creating new firebase job dispatcher
            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))
            //creating new job and adding it with dispatcher
            val job = createJob(dispatcher)
            dispatcher.mustSchedule(job)
        }

        private fun createJob(dispatcher: FirebaseJobDispatcher): Job {
//            val periodicity =
//                TimeUnit.HOURS.toSeconds(1).toInt() // Every 1 hour periodicity expressed as seconds
//            val toleranceInterval =
//                TimeUnit.MINUTES.toSeconds(15).toInt() // a small(ish) window of time when triggering is OK

            return dispatcher.newJobBuilder() //persist the task across boots
                .setLifetime(Lifetime.FOREVER) //.setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                //call this service when the criteria are met.
                .setService(ScheduledJobService::class.java) //unique id of the task
                .setTag("ScheduledJobService") //don't overwrite an existing job with the same tag
                .setReplaceCurrent(false) // We are mentioning that the job is periodic.
                .setRecurring(true) // Run between 30 - 60 seconds from now.
                .setTrigger(Trigger.executionWindow(30, 60)) // retry with exponential backoff
//                .setTrigger(Trigger.executionWindow(periodicity, toleranceInterval)) // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR) //.setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setLifetime(Lifetime.FOREVER)
                //Run this job only when the network is available.
                .setConstraints(
                    Constraint.ON_ANY_NETWORK,
                    Constraint.DEVICE_CHARGING
                )
                .build()
        }

        fun updateJob(dispatcher: FirebaseJobDispatcher): Job? {
            return dispatcher.newJobBuilder() //update if any task with the given tag exists.
                .setReplaceCurrent(true) //Integrate the job you want to start.
                .setService(ScheduledJobService::class.java)
                .setTag("UniqueTagForYourJob") // Run between 30 - 60 seconds from now.
                .setTrigger(Trigger.executionWindow(30, 60))
                .build()
        }

    }

    override fun onStartJob(params: JobParameters?): Boolean {
        //Offloading work to a new thread.
        try {
            GlobalUtility.showOfflineNotification(applicationContext, "Job Desp", "test My job")
        } catch (exp: Exception) {
            exp.printStackTrace()
        }
        Thread { codeYouWantToRun() }.start()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    private fun codeYouWantToRun() {
        try {
            Lg.d(TAG, "completeJob: " + "jobStarted")
            //This task takes 2 seconds to complete.
            Thread.sleep(2000)
            Lg.d(TAG, "completeJob: " + "jobFinished")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            //Tell the framework that the job has completed and doesnot needs to be reschedule
//            jobFinished(parameters!!, true)
        }
    }
}
