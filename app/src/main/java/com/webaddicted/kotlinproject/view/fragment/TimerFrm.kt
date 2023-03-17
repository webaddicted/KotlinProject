package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmTimerBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment


class TimerFrm : BaseFragment(R.layout.frm_timer) {
    private var isStopHandler: Boolean = true
    private lateinit var mBinding: FrmTimerBinding
    private var countDownTimerMilisec: Long = 330000
    private lateinit var updateTimerThread: Runnable
    private lateinit var countDownTimerHandler: Handler
    private lateinit var countDownTime: CountDownTimer

    companion object {
        val TAG = TimerFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): TimerFrm {
            val fragment = TimerFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmTimerBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.timer_title)

        countDownTimerHandler = Handler(Looper.getMainLooper())
        updateTimerThread = Runnable { updateUi() }
        initCountDown()
        updateUi()
        mBinding.txtCdTimer.text = "05:30"
    }

    private fun initCountDown() {
        countDownTime = object : CountDownTimer(330000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                mBinding.txtCdTimer.text = String.format(
                    "%02d",
                    minutes
                ) + ":" + String.format("%02d", seconds)
            }

            override fun onFinish() {
                mBinding.txtCdTimer.text = "00:00"
            }
        }
    }

    private fun updateUi() {
        val minutes = countDownTimerMilisec / 1000 / 60
        val seconds = countDownTimerMilisec / 1000 % 60
        mBinding.txtHandlerTimer.text = String.format(
            "%02d",
            minutes
        ) + ":" + String.format("%02d", seconds)
        countDownTimerMilisec -= 1000
        if (!isStopHandler) countDownTimerHandler.postDelayed(updateTimerThread, 1000)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnHandlerStart.setOnClickListener(this)
        mBinding.btnHandlerReset.setOnClickListener(this)
        mBinding.btnHandlerStop.setOnClickListener(this)
        mBinding.btnCdStart.setOnClickListener(this)
        mBinding.btnCdReset.setOnClickListener(this)
        mBinding.btnCdStop.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> backDispatcher.onBackPressed()
            R.id.btn_handler_start -> {
                stopHandler()
                isStopHandler = false
                countDownTimerHandler.postDelayed(updateTimerThread, 1000)
            }
            R.id.btn_handler_reset -> {
                stopHandler()
                isStopHandler = true
                countDownTimerMilisec = 330000
                updateUi()
            }
            R.id.btn_handler_stop -> {
                isStopHandler = true
                stopHandler()
            }
            R.id.btn_cd_start -> countDownTime.start()
            R.id.btn_cd_reset -> {
                initCountDown()
                countDownTime.cancel()
                mBinding.txtCdTimer.text = "05:30"
            }
            R.id.btn_cd_stop -> countDownTime.cancel()
        }
    }

    private fun stopHandler() {
        countDownTimerHandler.removeCallbacks(updateTimerThread)
        countDownTimerHandler.removeMessages(0)
    }
}

