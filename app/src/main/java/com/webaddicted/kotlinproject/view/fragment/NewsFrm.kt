package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.apiutils.ApiResponse
import com.webaddicted.kotlinproject.databinding.FrmNewsBinding
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.global.constant.AppConstant
import com.webaddicted.kotlinproject.model.bean.newschannel.NewsChanelRespo
import com.webaddicted.kotlinproject.view.adapter.NewsAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.base.ScrollListener
import com.webaddicted.kotlinproject.viewModel.list.NewsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFrm : BaseFragment() {
    private var mLanguageCode: String = ""
    private var newsList: ArrayList<NewsChanelRespo.Source>? = null
    private lateinit var mBinding: FrmNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private val mViewModel: NewsViewModel by viewModel()
    private var mPageCount: Int = 1

    companion object {
        val TAG = NewsFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): NewsFrm {
            val fragment = NewsFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_news
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmNewsBinding
        init()
        clickListener()
        setAdapter()
    }

    private fun init() {
        mBinding.toolbar.imgProfile.visible()
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.news_channel)
        mLanguageCode = preferenceMgr.getLanguageInfo().languageCode
        mBinding.parent.setBackgroundColor(ContextCompat.getColor(context!!,R.color.grey_light))
        callApi()
    }

    private fun clickListener() {
        mBinding.toolbar.imgProfile.setOnClickListener(this)
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_profile -> navigateScreen(ProfileFrm.TAG)
            R.id.img_back -> activity?.onBackPressed()
        }
    }

    private fun setAdapter() {
        newsAdapter = NewsAdapter(newsList)
        mBinding.rvNewsChannel.layoutManager = LinearLayoutManager(activity)
        mBinding.rvNewsChannel.addOnScrollListener(object :
            ScrollListener(mBinding.rvNewsChannel.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                mPageCount++
                callApi()
            }
        })
        mBinding.rvNewsChannel.adapter = newsAdapter
        mBinding.swipeView.setColorSchemeColors(ContextCompat.getColor(requireActivity(),R.color.white))
        mBinding.swipeView.setWaveColor(ContextCompat.getColor(requireActivity(),R.color.app_color))
        mBinding.swipeView.setOnRefreshListener {
            Handler().postDelayed({
                mBinding.swipeView.isRefreshing = false
            }, 1000)
        }
    }

    private fun callApi() {
        mViewModel.getNewsChannelLiveData().observe(this, channelObserver)
        mViewModel.newsChannelApi(
            "https://newsapi.org/v2/sources?language=" + mLanguageCode + "&page=" + mPageCount + "&pageSize=" + AppConstant.PAGINATION_SIZE + "&apiKey=" + getString(
                R.string.news_api_key
            )
        )
    }

    private val channelObserver: Observer<ApiResponse<NewsChanelRespo>> by lazy {
        Observer { response: ApiResponse<NewsChanelRespo> -> handleLoginResponse(response) }
    }

    private fun handleLoginResponse(response: ApiResponse<NewsChanelRespo>) {
        apiResponseHandler(mBinding.parent, response)
        when (response.status) {
            ApiResponse.Status.SUCCESS -> {
                hideApiLoader()
                if (newsList == null || newsList?.size == 0) newsList = response.data!!.sources
                else newsList?.addAll(response.data!!.sources)
                newsAdapter.notifyAdapter(newsList!!)
                if (newsList == null || newsList?.size == 0)
                    mBinding.txtNoDataFound.visible()
                else mBinding.txtNoDataFound.gone()
            }
        }
    }

    /**
     * navigate on fragment
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            ProfileFrm.TAG -> frm = ProfileFrm.getInstance(Bundle())
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }
}


