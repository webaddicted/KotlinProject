package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityCommonBinding
import com.webaddicted.kotlinproject.view.base.BaseActivity
import com.webaddicted.kotlinproject.view.fragment.ExoPlayerFrm
import com.webaddicted.kotlinproject.view.fragment.ExoPlayerRecyclerFrm

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class ExoPlayerActivity : BaseActivity(R.layout.activity_common) {
    private lateinit var mBinding: ActivityCommonBinding

    companion object {
        val TAG: String = ExoPlayerActivity::class.java.simpleName
        const val OPEN_FRM = "openFrm"
        fun newIntent(activity: Activity, frmName: String?) {
            val intent = Intent(activity, ExoPlayerActivity::class.java)
            intent.putExtra(OPEN_FRM, frmName)
            activity.startActivity(intent)
        }
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as ActivityCommonBinding
        init()
    }

    private fun init() {
        val frm = intent.getStringExtra(OPEN_FRM)
        if (frm.equals(ExoPlayerFrm.TAG)) navigateScreen(ExoPlayerFrm.TAG)
        else navigateScreen(ExoPlayerRecyclerFrm.TAG)
    }

    /**
     * navigate on fragment
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String?) {
        var frm: Fragment? = null
        when (tag) {
            ExoPlayerRecyclerFrm.TAG -> frm = ExoPlayerRecyclerFrm.getInstance(Bundle())
            ExoPlayerFrm.TAG -> frm = ExoPlayerFrm.getInstance(Bundle())
        }
        if (frm != null) navigateFragment(R.id.container, frm, false)
    }

}