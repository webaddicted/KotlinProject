package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils.loadLayoutAnimation
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmRecylcerViewBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.adapter.RecyclerListAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.base.ScrollListener
import java.util.*


class RecyclerViewFrm : BaseFragment(R.layout.frm_recylcer_view) {
    private var list: ArrayList<String>? = null
    private var mListAdapter: RecyclerListAdapter? = null
    private lateinit var mBinding: FrmRecylcerViewBinding
    private var animPos: Int = 0
    private val animArray: IntArray = intArrayOf(
        R.anim.rv_anim_down_to_up,
        R.anim.rv_anim_up_to_down,
        R.anim.rv_anim_left_to_right,
        R.anim.rv_anim_right_to_left

    )

    companion object {
        val TAG = RecyclerViewFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): RecyclerViewFrm {
            val fragment = RecyclerViewFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmRecylcerViewBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.recycler_view_title)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnGrid.setOnClickListener(this)
        mBinding.btnList.setOnClickListener(this)
        mBinding.btnHeader.setOnClickListener(this)

        mBinding.btnStaggered.setOnClickListener(this)
        mBinding.btnSwipeToDelete.setOnClickListener(this)
        mBinding.btnChangeAnim.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        super.onClick(v)
        list = getUrlBean()
        when (v.id) {
            R.id.img_back -> requireActivity().onBackPressedDispatcher.onBackPressed()
            R.id.btn_list -> setListAdapter(
                LinearLayoutManager(
                    activity,
                    LinearLayoutManager.VERTICAL,
                    false
                ), R.layout.row_recycler_list
            )
            R.id.btn_grid -> setListAdapter(GridLayoutManager(activity, 2), R.layout.row_grid)
            R.id.btn_header -> setListAdapter(
                LinearLayoutManager(
                    activity,
                    LinearLayoutManager.VERTICAL,
                    false
                ), R.layout.row_recycler_list
            )
            R.id.btn_staggered -> setListAdapter(
                StaggeredGridLayoutManager(
                    2,
                    LinearLayoutManager.VERTICAL
                ), R.layout.row_grid
            )
            R.id.btn_change_anim -> changeAnimation()
        }
    }

    private fun changeAnimation() {
        if (animPos >= 3) animPos = 0
        else animPos++
        val animation = loadLayoutAnimation(context, animArray[animPos])
        mBinding.recyclerView.layoutAnimation = animation
    }

    private fun setListAdapter(layoutMgr: RecyclerView.LayoutManager, layoutId: Int) {
        mListAdapter = RecyclerListAdapter(this, list, layoutId)
        mBinding.recyclerView.layoutManager = layoutMgr
        mBinding.recyclerView.addOnScrollListener(object : ScrollListener(layoutMgr) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                getUrlBean().let { list?.addAll(it) }
                mListAdapter?.notifyAdapter(list)
            }
        })
        changeAnimation()
        mBinding.recyclerView.adapter = mListAdapter
    }

    private fun getUrlBean(): ArrayList<String> {
        val mList = ArrayList<String>()
        mList.add("https://ya-webdesign.com/images/goofy-clipart-7.png")
        mList.add("https://upload.wikimedia.org/wikipedia/en/thumb/b/b4/Donald_Duck.svg/220px-Donald_Duck.svg.png")
        mList.add("https://banner2.kisspng.com/20171127/fdb/mickey-mouse-free-png-transparent-image-5a1c8ad550b294.6551180715118199893305.jpg")
        mList.add("https://c7.uihere.com/files/257/367/814/mickey-mouse-minnie-mouse-pluto-drawing-image-mickey-mouse.jpg")
        mList.add("https://stickeroid.com/uploads/pic/full-pngimg/182e1f18bc8883fd935234f79626cf303d3def38.png")
        mList.add("https://zorlu.com.mk/wp-content/uploads/2016/10/mickey-minnie-mouse-kiss.jpg")
        mList.add("https://wallimpex.com/data/out/596/imagenes-de-mickey-9561546.jpg")
        mList.add("http://www.pngall.com/wp-content/uploads/2017/03/Minnie-Mouse-PNG-Clipart.png")
        mList.add("http://ku.90sjimg.com/element_pic/17/09/29/c194786fd4036b233ebe6d441ec1d5d1.jpg")
        mList.add("https://telegram-stickers.github.io/public/stickers/unofficial/5.png")
        mList.add("https://steemitimages.com/DQmP64LEzof8MpyowfWaAdetv7YNtGrfvvkJqPfS4f5GRTa/WhatTheHell.png")
        mList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMuORxKRlJ-Md1C7KxBGmg56Hwkm0UAMEKxLAcZQ31foHDXAeO")
        mList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLqOB6WXgmKlKb_hpmzwqVo1Jf5uZ9_Oiy88lWAGAcTN_huX7s")
        mList.add("https://i.pinimg.com/originals/fd/2e/73/fd2e73df313e9e8d6e8b54c554d9ddcf.pnghttps://i.pinimg.com/originals/fd/2e/73/fd2e73df313e9e8d6e8b54c554d9ddcf.png")
        mList.add("https://i.pinimg.com/originals/4b/80/f7/4b80f7ddd0738a65a160d6aa1c84b24d.png")
        mList.add("https://stickershop.line-scdn.net/stickershop/v1/product/1235089/LINEStorePC/main.png")
        mList.add("https://i.pinimg.com/originals/53/04/d0/5304d0f506a8bec37120ab68ff5d3961.png")
        mList.add("https://stickershop.line-scdn.net/stickershop/v1/product/925/LINEStorePC/main.png")
        mList.add("https://clipground.com/images/aladdin-clipart-13.jpg")
        mList.add("https://www.disneyclips.com/imagesnewb/images/aladdin-jasmine-carpet3.png")
        mList.add("https://banner2.kisspng.com/20180509/lsq/kisspng-princess-jasmine-aladdin-genie-clip-art-5af39dbd2ce0c6.4068873515259150691838.jpg")
        mList.add("https://banner2.kisspng.com/20180514/ugw/kisspng-princess-jasmine-aladdin-prince-ali-clip-art-5af9f41e1cdf34.8730821715263303981183.jpg")
        mList.add("https://banner2.kisspng.com/20180509/efe/kisspng-genie-princess-jasmine-the-sultan-iago-clip-art-5af32a7391cd37.2063657615258855555972.jpg")
        mList.add("https://upload.wikimedia.org/wikipedia/en/thumb/7/71/Princess_Jasmine_disney.png/220px-Princess_Jasmine_disney.png")
        mList.add("https://i.pinimg.com/originals/4f/e1/44/4fe144ee29e8e95b00f708cb8b0685ba.png")
        mList.add("https://library.kissclipart.com/20180906/ocw/kissclipart-baby-disney-png-clipart-pluto-donald-duck-minnie-m-1e57853e9139a610.png")
        return mList
    }
}

