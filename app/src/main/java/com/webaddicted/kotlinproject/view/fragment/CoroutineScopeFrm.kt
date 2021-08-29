package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
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

abstract class ScopedFragment : BaseFragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}

class CoroutineScopeFrm : ScopedFragment() {
    private lateinit var mBinding: FrmCoroutineBinding

    companion object {
        val TAG = CoroutineScopeFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): CoroutineScopeFrm {
            val fragment = CoroutineScopeFrm()
            fragment.arguments = bundle
            return fragment
        }
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
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.scope_coroutine_title)
    }

    private fun clickListener() {
        mBinding.btnLaunch.gone()
        mBinding.btnSequentially.gone()
        mBinding.btnParallel.gone()
        mBinding.btnLaunchTimeout.gone()
        mBinding.btnExceptionHandler.gone()
        mBinding.btnLifecycleAware.gone()
        mBinding.btnAndroidScoped.visible()
        mBinding.btnCancel.visible()
        mBinding.toolbar.imgBack.visible()
        mBinding.btnAndroidScoped.setOnClickListener(this)
        mBinding.btnCancel.setOnClickListener(this)
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_android_scoped -> launchAndroidScope(mBinding.txtAndroidScoped)
            R.id.btn_cancel -> {
                activity?.showToast(getString(R.string.job_cancel))
            }
        }
    }


    private fun launchAndroidScope(textView: TextView) {
        textView.text = "Step 1 "
        launch{
            textView.text = textView.text.toString() + "\nStep 2"
            var result = loadData(mBinding.txtAndroidScoped)
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

