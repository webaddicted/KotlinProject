package com.webaddicted.kotlinproject.viewModel.main

import com.webaddicted.kotlinproject.model.bean.language.LanguageBean
import com.webaddicted.kotlinproject.model.repository.news.NewsRepository
import com.webaddicted.kotlinproject.viewModel.base.BaseViewModel

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class MainViewModel(private val projectRepository: NewsRepository) :BaseViewModel() {
    fun setLanguage(get: LanguageBean) {
        preferenceMgr.setLanguage(get)
    }
}