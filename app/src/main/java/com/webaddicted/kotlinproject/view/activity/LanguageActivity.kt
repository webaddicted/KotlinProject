package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Intent

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityLanguageBinding
import com.webaddicted.kotlinproject.global.common.AppApplication.Companion.context
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.model.bean.language.LanguageBean
import com.webaddicted.kotlinproject.view.adapter.LanguageAdapter
import com.webaddicted.kotlinproject.view.base.BaseActivity
import com.webaddicted.kotlinproject.viewModel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class LanguageActivity : BaseActivity(R.layout.activity_language) {

    private lateinit var mBinding: ActivityLanguageBinding
    private lateinit var mLanguageList: ArrayList<LanguageBean>
    private lateinit var languageAdapter: LanguageAdapter
    private val mainViewModel: MainViewModel by viewModel()

    companion object {
        val TAG: String = LanguageActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, LanguageActivity::class.java))
        }
    }

    override fun onBindTo(binding: ViewDataBinding) {
        mBinding = binding as ActivityLanguageBinding
        init()
        clickListener()
        languageObserver(0)
        setAdapter()
    }

    private fun init() {
        setNavigationColor(ContextCompat.getColor(context, R.color.app_color))
        mBinding.toolbar.imgProfile.visibility = View.GONE
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.select_language)
        mLanguageList = setLanguageBean()
    }

    private fun clickListener() {
        mBinding.btnNext.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_next -> {
                navigateToNext()
            }
        }
    }

    private fun setAdapter() {
        languageAdapter = LanguageAdapter(this, mLanguageList)
        mBinding.rvLanguage.layoutManager = LinearLayoutManager(this)
        mBinding.rvLanguage.adapter = languageAdapter
    }

    /**
     * navigate to welcome activity after Splash timer Delay
     */
    private fun navigateToNext() {
        val position = languageAdapter.selectedPos
        if (position < 0) {
            GlobalUtility.showToast(resources.getString(R.string.please_select_language))
            return
        } else GlobalUtility.changeLanguage(baseContext, mLanguageList[position].languageCode)
        mainViewModel.setLanguage(mLanguageList[position])
        OnBoardActivity.newIntent(this)
        finish()
    }

    fun languageObserver(position: Int) {
        GlobalUtility.changeLanguage(baseContext, mLanguageList[position].languageCode)
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.select_language)
        mBinding.btnNext.text = resources.getString(R.string.submit)
    }

    private fun setLanguageBean(): ArrayList<LanguageBean> {
        val languageBeanList = ArrayList<LanguageBean>()
        languageBeanList.add(LanguageBean().apply {
            id = "0"
            languageCode = Locale.getDefault().language
            languageName = "Default (" + Locale.getDefault().displayName.lowercase() + ")"
            languageFlag =
                "https://upload.wikimedia.org/wikipedia/en/thumb/a/aa/English_Language_Flag.png/640px-English_Language_Flag.png"
        })
        languageBeanList.add(LanguageBean().also {
            it.id = "1"
            it.languageCode = "ar"
            it.languageName = "Argentina"
            it.languageFlag =
                "https://mirrorspectator.com/wp-content/uploads/2019/03/31WNPn82f2L._SX425_.jpg"
        })
        languageBeanList.add(LanguageBean().apply {
            id = "2"
            languageCode = "en"
            languageName = "English"
            languageFlag =
                "https://upload.wikimedia.org/wikipedia/en/thumb/a/aa/English_Language_Flag.png/640px-English_Language_Flag.png"
        })
        languageBeanList.add(LanguageBean().apply {
            id = "3"
            languageCode = "hi"
            languageName = "Hindi"
            languageFlag =
                "https://www.imediaethics.org/wp-content/uploads/archive/B_Image_4450.jpg"
        })
        return languageBeanList
    }
}