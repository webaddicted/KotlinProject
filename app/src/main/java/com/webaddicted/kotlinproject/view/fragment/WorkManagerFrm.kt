package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.work.*
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmWorkManagerBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.global.services.MyWorker
import com.webaddicted.kotlinproject.global.services.WorkManagerService
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.util.concurrent.TimeUnit


class WorkManagerFrm : BaseFragment(R.layout.frm_work_manager) {
    private lateinit var mBinding: FrmWorkManagerBinding

    companion object {
        val TAG = WorkManagerFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): WorkManagerFrm {
            val fragment = WorkManagerFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmWorkManagerBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.work_manager_title)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnOneTime.setOnClickListener(this)
        mBinding.btnPeriodicReq.setOnClickListener(this)
        mBinding.btnChainingWorks.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> backDispatcher.onBackPressed()
            R.id.btn_one_time -> oneTimeReq()
            R.id.btn_periodic_req -> periodicReq()
            R.id.btn_chaining_works -> chainingWorkReq()
        }
    }

    private fun oneTimeReq() {
        val constraints = Constraints.Builder()
//            .setRequiresCharging(true)
//            .setRequiresStorageNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
//            .setInputData(data)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueue(workRequest)
    }

    private fun periodicReq() {
        val photoCheckBuilder = PeriodicWorkRequest.Builder(
            WorkManagerService::class.java, 15, TimeUnit.MINUTES
        )
        val request = photoCheckBuilder.build()
        WorkManager.getInstance()
            .enqueueUniquePeriodicWork("hgghgj", ExistingPeriodicWorkPolicy.REPLACE, request)
    }

    private fun chainingWorkReq() {
        val constraints = Constraints.Builder()
//            .setRequiresCharging(true)
//            .setRequiresStorageNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
//            .setInputData(data)
            .setConstraints(constraints)
            .build()
        val workRequest1 = OneTimeWorkRequest.Builder(MyWorker::class.java)
//            .setInputData(data)
            .setConstraints(constraints)
            .build()
        val workRequest2 = OneTimeWorkRequest.Builder(MyWorker::class.java)
//            .setInputData(data)
            .setConstraints(constraints)
            .build()


        WorkManager.getInstance().
        beginWith(workRequest)
            .then(workRequest1)
            .then(workRequest2)
            .enqueue()
    }

}

