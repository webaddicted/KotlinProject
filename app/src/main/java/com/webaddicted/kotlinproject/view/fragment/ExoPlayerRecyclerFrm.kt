package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmExoPlayerRecyclerBinding
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.bean.common.ExoPlayerBean
import com.webaddicted.kotlinproject.view.adapter.ExoPlayerAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.util.*

class ExoPlayerRecyclerFrm : BaseFragment() {
    private lateinit var mAdapter: ExoPlayerAdapter
    private lateinit var videoBean: ArrayList<ExoPlayerBean>
    private lateinit var mBinding: FrmExoPlayerRecyclerBinding
    private var firstTime = true
    companion object {
        val TAG = ExoPlayerRecyclerFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): ExoPlayerRecyclerFrm {
            val fragment = ExoPlayerRecyclerFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_exo_player_recycler
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmExoPlayerRecyclerBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.exo_player_title)
        mBinding.exoPlayer.gone()
        mBinding.rvExoPlayer.visible()
        videoBean = ArrayList()
        setAdapter()
    }

    private fun setAdapter() {
        createDataBean()

        //set data object
        mBinding.rvExoPlayer.setMediaObjects(videoBean)
        mAdapter = ExoPlayerAdapter(videoBean)
        mBinding.rvExoPlayer.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvExoPlayer.itemAnimator = DefaultItemAnimator()
        mBinding.rvExoPlayer.adapter = mAdapter
        mBinding.rvExoPlayer.setRepeatVideo(false)
        if (firstTime) {
            Handler(Looper.getMainLooper()).post { mBinding.rvExoPlayer.playVideo(false) }
            firstTime = false
        }
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
        }
    }

    private fun createDataBean() {
        videoBean.add(ExoPlayerBean().apply {
            uId = 1
            userHandle = "@h.pandya"
            title = "Do you think the concept of marriage will no longer exist in the future?"
            mediaCoverImgUrl =
                "https://terrigen-cdn-dev.marvel.com/content/prod/1x/002irm_ons_mas_mob_01_0.jpg"
            mediaUrl = "https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"
        })
        videoBean.add(ExoPlayerBean().apply {
            uId = 2
            userHandle = "@hardik.patel"
            title =
                "If my future husband doesn't cook food as good as my mother should I scold him?"
            mediaCoverImgUrl =
                "https://terrigen-cdn-dev.marvel.com/content/prod/1x/002irm_ons_mas_mob_01_0.jpg"
            mediaUrl = "https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"
        })
        videoBean.add(ExoPlayerBean().apply {
            uId = 3
            userHandle = "@arun.gandhi"
            title = "Give your opinion about the Ayodhya temple controversy."
            mediaCoverImgUrl =
                "https://terrigen-cdn-dev.marvel.com/content/prod/1x/002irm_ons_mas_mob_01_0.jpg"
            mediaUrl = "https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"
        })
        videoBean.add(ExoPlayerBean().apply {
            uId = 4
            userHandle = "@sachin.patel"
            title = "When did kama founders find sex offensive to Indian traditions"
            mediaCoverImgUrl =
                "https://terrigen-cdn-dev.marvel.com/content/prod/1x/002irm_ons_mas_mob_01_0.jpg"
            mediaUrl = "https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"
        })
        videoBean.add(ExoPlayerBean().apply {
            uId = 5
            userHandle = "@monika.sharma"
            title = "When did you last cry in front of someone?"
            mediaCoverImgUrl =
                "https://terrigen-cdn-dev.marvel.com/content/prod/1x/002irm_ons_mas_mob_01_0.jpg"
            mediaUrl = "https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"
        })
    }

    override fun onDestroy() {
        mBinding.rvExoPlayer.releasePlayer()
        super.onDestroy()
    }
}

