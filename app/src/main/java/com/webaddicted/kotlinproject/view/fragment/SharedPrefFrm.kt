package com.webaddicted.kotlinproject.view.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmSharedPrefBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.global.constant.PreferenceConstant.Companion.PREF_USER_AGE
import com.webaddicted.kotlinproject.global.constant.PreferenceConstant.Companion.PREF_USER_IS_MARRIED
import com.webaddicted.kotlinproject.global.constant.PreferenceConstant.Companion.PREF_USER_NAME
import com.webaddicted.kotlinproject.model.bean.preference.PreferenceBean
import com.webaddicted.kotlinproject.view.base.BaseFragment

class SharedPrefFrm : BaseFragment(R.layout.frm_shared_pref) {
    private lateinit var mBinding: FrmSharedPrefBinding

    companion object {
        val TAG = SharedPrefFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): SharedPrefFrm {
            val fragment = SharedPrefFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmSharedPrefBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.shared_pref_title)
    }


    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnSetValue.setOnClickListener(this)
        mBinding.btnGetValueFromPreference.setOnClickListener(this)
        mBinding.btnRemoveKey.setOnClickListener(this)
        mBinding.btnClearPreference.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> backDispatcher.onBackPressed()
            R.id.btn_set_value -> setValuePref()
            R.id.btn_get_value_from_preference -> getValuePref()
            R.id.btn_remove_key -> removeKeyPref()
            R.id.btn_clear_preference -> clearPref()
        }
    }

    private fun setValuePref() {
        val prefUer = PreferenceBean().apply {
            name = "Deepak Sharma"
            gender = "M"
            age = 24
            weight = 75
            isMarried = false
        }
        preferenceMgr.setUserInfo(prefUer)
        mBinding.txtSavePreference.text = getString(R.string.user_info_saved)
    }

    private fun getValuePref() {
        val userInfo = preferenceMgr.getUserInfo()
        val userInfoString = "<br><font color='#000000'>Name : </font>" + userInfo.name + "<br>" +
                "<font color='" + ContextCompat.getColor(
            mActivity,
            R.color.black
        ) + "'>Gender : </font>" + userInfo.gender + "<br>" +
                "<font color='" + ContextCompat.getColor(
            mActivity,
            R.color.black
        ) + "'>Age : </font>" + userInfo.age + "<br>" +
                "<font color='" + ContextCompat.getColor(
            mActivity,
            R.color.black
        ) + "'>Weight : </font>" + userInfo.weight + "<br>" +
                "<font color='" + ContextCompat.getColor(
            mActivity,
            R.color.black
        ) + "'>Married : </font>" + userInfo.isMarried + "<br>"
        mBinding.txtGetPreference.text = Html.fromHtml(userInfoString, Html.FROM_HTML_MODE_LEGACY)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun removeKeyPref() {
        preferenceMgr.removeKey(PREF_USER_NAME)
        preferenceMgr.removeKey(PREF_USER_AGE)
        preferenceMgr.removeKey(PREF_USER_IS_MARRIED)
        val userInfo = preferenceMgr.getUserInfo()
        val userInfoString = "<br><font color='#000000'>Name : </font>" + userInfo.name + "<br>" +
                "<font color='" + ContextCompat.getColor(
            mActivity,
            R.color.black
        ) + "'>Gender : </font>" + userInfo.gender + "<br>" +
                "<font color='" + ContextCompat.getColor(
            mActivity,
            R.color.black
        ) + "'>Age : </font>" + userInfo.age + "<br>" +
                "<font color='" + ContextCompat.getColor(
            mActivity,
            R.color.black
        ) + "'>Weight : </font>" + userInfo.weight + "<br>" +
                "<font color='" + ContextCompat.getColor(
            mActivity,
            R.color.black
        ) + "'>Married : </font>" + userInfo.isMarried + "<br>"
        mBinding.txtRemoveKey.text = Html.fromHtml(userInfoString, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun clearPref() {
        preferenceMgr.clearPref()
        mBinding.txtClearPreference.text = getString(R.string.pref_cleared_successfully)
    }

}

