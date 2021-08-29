package com.webaddicted.kotlinproject.viewModel.map

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.webaddicted.kotlinproject.model.repository.news.NewsRepository
import com.webaddicted.kotlinproject.viewModel.base.BaseViewModel

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class MapViewModel( private val projectRepository: NewsRepository) :BaseViewModel() {
//  gteUpdated location
    var locationUpdated = MutableLiveData<Location>()

}
