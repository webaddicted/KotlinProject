package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmNewsBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.circle.CircleGameBean
import com.webaddicted.kotlinproject.view.adapter.CircleGameAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.util.*
import kotlin.collections.ArrayList

class CircleFrm : BaseFragment(R.layout.frm_news) {
    private lateinit var mBinding: FrmNewsBinding
    private lateinit var newsAdapter: CircleGameAdapter

    companion object {
        val TAG = CircleFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): CircleFrm {
            val fragment = CircleFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmNewsBinding
        init()
        clickListener()
        setAdapter()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.circle_title)
//        mBinding.parent.setBackgroundColor(resources.getColor(R.color.app_color))
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setAdapter() {
        val circleBean = setCircleGameBean()
        Collections.sort(circleBean, CircleGameBean())
        newsAdapter = CircleGameAdapter(this, circleBean)
        mBinding.rvNewsChannel.layoutManager = LinearLayoutManager(activity)
        mBinding.rvNewsChannel.adapter = newsAdapter
    }

    private fun setCircleGameBean(): ArrayList<CircleGameBean> {
        val languageBeanList = ArrayList<CircleGameBean>()
        languageBeanList.add(CircleGameBean().apply {
            id = "0"
            Name = "top 3 game you have played"
            Image =
                "https://images.pexels.com/photos/257360/pexels-photo-257360.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        })
        languageBeanList.add(CircleGameBean().also {
            it.id = "1"
            it.Name = "your next 3 game"
            it.Image =
                "https://www.lovethisimages.com/wp-content/uploads/2018/04/sorry-images-download-1.jpg"
        })
        languageBeanList.add(CircleGameBean().apply {
            id = "2"
            Name = "top beach games"
            Image =
                "https://jacquet-autocars.com/wp-content/uploads/2016/12/Courchevel-transfert-gare-ski-DR-JuliaKuznetsova.jpg"
        })
        languageBeanList.add(CircleGameBean().apply {
            id = "3"
            Name = "your next 3 game"
            Image = "https://i.imgur.com/R1eeO.jpg"
        })
        languageBeanList.add(CircleGameBean().also {
            it.id = "1"
            it.Name = "top 3 game you have played"
            it.Image = "http://sfwallpaper.com/images/see-hd-wallpaper-26.jpg"
        })
        languageBeanList.add(CircleGameBean().apply {
            id = "2"
            Name = "your next 3 game"
            Image =
                "https://broadway.showtickets.com/cdn/img/articles/broadway/the-5-best-places-to-see-fall-foliage-in-new-york-city/st-autumn-central-park-600.jpg"
        })
        languageBeanList.add(CircleGameBean().apply {
            id = "3"
            Name = "top beach games"
            Image =
                "https://www.tucantravel.com/blog/wp-content/uploads/2019/05/Northern-lights-in-Iceland-750x400.jpg"
        })

        return languageBeanList
    }


    /**
     * navigate on fragment
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String?, bundle: Bundle) {
        var frm: Fragment? = null
        when (tag) {
            ProfileFrm.TAG -> frm = ProfileFrm.getInstance(Bundle())
            ZoomImageFrm.TAG -> frm = ZoomImageFrm.getInstance(bundle)
        }
        if (frm != null) navigateFragment(R.id.container, frm, false)
    }

    fun openImg(image: String?) {
        val bundle = Bundle()
        bundle.putString(ZoomImageFrm.IMAGE_PATH, image)
        bundle.putBoolean(ZoomImageFrm.IS_LOCAL_FILE, false)
        navigateScreen(ZoomImageFrm.TAG, bundle)
    }


}


