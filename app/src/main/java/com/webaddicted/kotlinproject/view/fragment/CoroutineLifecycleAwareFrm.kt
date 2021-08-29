package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmCoroutineBinding
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.showToast
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class MainScope : CoroutineScope, LifecycleObserver {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onCreate() {
        job = Job()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun destroy() = job.cancel()
}

class CoroutineLifecycleAwareFrm : BaseFragment() {
    private lateinit var mBinding: FrmCoroutineBinding
    private val mainScope = MainScope()

    companion object {
        val TAG = CoroutineLifecycleAwareFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): CoroutineLifecycleAwareFrm {
            val fragment = CoroutineLifecycleAwareFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(mainScope)
    }

    override fun getLayout(): Int {
        return R.layout.frm_coroutine
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmCoroutineBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text =
            resources.getString(R.string.lifecycle_aware_coroutine_title)
    }

    private fun clickListener() {
        mBinding.btnLaunch.gone()
        mBinding.btnSequentially.gone()
        mBinding.btnParallel.gone()
        mBinding.btnLaunchTimeout.gone()
        mBinding.btnExceptionHandler.gone()
        mBinding.btnLifecycleAware.visible()
        mBinding.btnAndroidScoped.gone()
        mBinding.btnCancel.visible()
        mBinding.toolbar.imgBack.visible()
        mBinding.btnLifecycleAware.setOnClickListener(this)
        mBinding.btnCancel.setOnClickListener(this)
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_lifecycle_aware -> launchLifecycleAware(mBinding.txtLifecycleAware)
            R.id.btn_cancel -> {
                activity?.showToast(getString(R.string.job_cancel))
            }
        }
    }


    private fun launchLifecycleAware(textView: TextView) {
        textView.text = "Step 1 "
        mainScope.launch {
            textView.text = textView.text.toString() + "\nStep 2"
            var result = loadData(mBinding.txtLifecycleAware)
            textView.text = textView.text.toString() + "\nStep 5 :- result  :- $result"
        }
        textView.text = textView.text.toString() + "\nOut of launch "
    }

    suspend fun loadData(txtLaunch: TextView): String {
        txtLaunch.text = txtLaunch.text.toString() + "\nStep 3"
        delay(TimeUnit.SECONDS.toMillis(3)) // imitate long running operation
        txtLaunch.text = txtLaunch.text.toString() + "\nStep 4"
        return "Data is available: ${Random().nextInt()}"
    }

}

