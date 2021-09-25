package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmCoroutineBinding
import com.webaddicted.kotlinproject.global.common.showToast
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

class CoroutineFrm : BaseFragment(R.layout.frm_coroutine) {
    private lateinit var mBinding: FrmCoroutineBinding
    private lateinit var job: Job

    companion object {
        val TAG = CoroutineFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): CoroutineFrm {
            val fragment = CoroutineFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmCoroutineBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.coroutine_title)
        job = Job()
    }

    private fun clickListener() {
        mBinding.btnLaunch.setOnClickListener(this)
        mBinding.btnSequentially.setOnClickListener(this)
        mBinding.btnParallel.setOnClickListener(this)
        mBinding.btnLaunchTimeout.setOnClickListener(this)
        mBinding.btnExceptionHandler.setOnClickListener(this)
        mBinding.btnLifecycleAware.setOnClickListener(this)
        mBinding.btnAndroidScoped.setOnClickListener(this)
        mBinding.btnCancel.setOnClickListener(this)
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_launch -> launchCoroutine(mBinding.txtLaunch)
            R.id.btn_sequentially -> launchSequentially(mBinding.txtSequentially)
            R.id.btn_parallel -> launchParallel(mBinding.txtParallel)
            R.id.btn_launch_timeout -> launchLaunchTimeout(mBinding.txtLaunchTimeout)
            R.id.btn_exception_handler -> launchExceptionHandler(mBinding.txtExceptionHandler)
            R.id.btn_lifecycle_aware -> launchLifecycleAware()
            R.id.btn_android_scoped -> launchAndroidScope()
            R.id.btn_cancel -> {
                job.cancel()
                activity?.showToast(getString(R.string.job_cancel))
                mBinding.txtCancel.text = getString(R.string.job_cancel)
            }
        }
    }

    private fun launchCoroutine(textView: TextView) {
        textView.text = "Step 1 "
        GlobalScope.launch(Dispatchers.Main + job) {
            textView.text = textView.text.toString() + "\nStep 2"
            val result = loadData(mBinding.txtLaunch)
            Log.d(TAG, "Step 5 :- result - $result")
            textView.text = textView.text.toString() + "\nStep 5 :- result - $result"
        }
        textView.text = textView.text.toString() + "\nOut of launch "
    }

    private fun launchSequentially(textView: TextView) {
        textView.text = "Step 1 "
        GlobalScope.launch(Dispatchers.Main + job) {
            textView.text = textView.text.toString() + "\nStep 2"
            val result1 = loadData(mBinding.txtSequentially)
            val result2 = loadData(mBinding.txtSequentially)
            textView.text = textView.text.toString() + "\nStep 5 :- result1 - $result1 \n result2 - $result2"
        }
        textView.text = textView.text.toString() + "\nOut of launch "
    }

    private fun launchParallel(textView: TextView) {
        textView.text = "Step 1 "
        GlobalScope.launch(Dispatchers.Main + job) {
            textView.text = textView.text.toString() + "\nStep 2"
            val result1 = async { loadData(mBinding.txtParallel) }.await()
            val result2 = async { loadData(mBinding.txtParallel) }.await()
            textView.text = textView.text.toString() + "\nStep 5 :- result1 - $result1 \n result2 - $result2"
        }
        textView.text = textView.text.toString() + "\nOut of launch "
    }

    private fun launchLaunchTimeout(textView: TextView) {
        textView.text = "Step 1 "
        GlobalScope.launch(Dispatchers.Main + job) {
            textView.text = mBinding.txtLaunchTimeout.text.toString() + "\nStep 2"
            val result =
                withTimeoutOrNull(TimeUnit.SECONDS.toMillis(1)) { loadData(mBinding.txtLaunchTimeout) }
            textView.text = mBinding.txtLaunchTimeout.text.toString() + "\nStep 5 :- time out :- $result"
        }
        textView.text = textView.text.toString() + "\nOut of launch "
    }

    private fun launchExceptionHandler(textView: TextView) {
        textView.text = "Step 1 "
        GlobalScope.launch(Dispatchers.Main + job) {
            textView.text = mBinding.txtExceptionHandler.text.toString() + "\nStep 2"
            try {
                val result = loadData(mBinding.txtLaunch)
                textView.text = mBinding.txtExceptionHandler.text.toString() + "\nStep 5 :- try :- $result"

            } catch (e: IllegalArgumentException) {
                textView.text = mBinding.txtExceptionHandler.text.toString() + "\nStep 5 :- catch :- ${e.message}"
            }
            textView.text = mBinding.txtExceptionHandler.text.toString() + "\nStep 6 :- out function"
        }
        textView.text = "${textView.text} \nOut of launch "
    }

    private fun launchLifecycleAware() {
        navigateScreen(CoroutineLifecycleAwareFrm.TAG)
    }

    private fun launchAndroidScope() {
        navigateScreen(CoroutineScopeFrm.TAG)
    }

    suspend fun loadData(txtLaunch: TextView): String {
        txtLaunch.text = txtLaunch.text.toString() + "\nStep 3"
        delay(TimeUnit.SECONDS.toMillis(3)) // imitate long running operation
        txtLaunch.text = txtLaunch.text.toString() + "\nStep 4"
        return "Data is available: ${Random().nextInt()}"
    }

    fun navigateScreen(tag: String?) {
        var frm: Fragment? = null
        when (tag) {
            CoroutineLifecycleAwareFrm.TAG -> frm = CoroutineLifecycleAwareFrm.getInstance(Bundle())
            CoroutineScopeFrm.TAG -> frm = CoroutineScopeFrm.getInstance(Bundle())
        }
        frm?.let { navigateAddFragment(R.id.container, it, true) }
    }
}

