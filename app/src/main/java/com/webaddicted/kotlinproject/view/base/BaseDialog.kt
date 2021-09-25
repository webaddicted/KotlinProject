package com.webaddicted.kotlinproject.view.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.webaddicted.kotlinproject.R

/**
 * this class help in show working process of create game &
 * select player/fielder rule
 */
@SuppressLint("ValidFragment")
abstract class BaseDialog(private val layoutId: Int) : DialogFragment(), View.OnClickListener {
    private lateinit var mBinding: ViewDataBinding
    protected val mActivity by lazy { requireActivity() }
    protected abstract fun onBindTo(binding: ViewDataBinding?)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBindTo(mBinding)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(v: View) {
    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        dialog?.window!!
            .attributes.windowAnimations = R.style.DialogFadeAnimation
    }
}
