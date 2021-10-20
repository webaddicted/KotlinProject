package com.webaddicted.kotlinproject.view.adapter

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowCircleBinding
import com.webaddicted.kotlinproject.global.common.AppApplication
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.showImage
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.circle.CircleGameBean
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.fcmkit.FcmHomeFrm
import com.webaddicted.kotlinproject.view.fragment.CircleFrm

/**
 * Today's Top game list & animation handle in same adapter
 */
class CircleGameAdapter(
    private var frm: BaseFragment,
    private var mFilterBean: ArrayList<CircleGameBean>?
) : BaseAdapter() {

    private var slideAnmimations: Animation
    private var slideAnmimation: Animation

    init {
        slideAnmimation = AnimationUtils.loadAnimation(frm.context, R.anim.bounce_game)
        slideAnmimations = AnimationUtils.loadAnimation(frm.context, R.anim.bounce_game)
    }

    override fun getListSize(): Int {
        var size = 0
        if (mFilterBean != null && mFilterBean!!.size > 0) {
            size = mFilterBean!!.size / 4
            if (reminder != 0) size++
        }
        return size
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_circle
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowCircleBinding) {
            slideAnmimation = AnimationUtils.loadAnimation(mContext, R.anim.bounce_game)
            slideAnmimations = AnimationUtils.loadAnimation(mContext, R.anim.bounce_game)
            rowBinding.imgFirst.animation = slideAnmimation
            //            mBinding.imgSecond.startAnimation(slideAnmimation);
            rowBinding.imgThird.animation = slideAnmimation
            //            mBinding.imgFourth.startAnimation(slideAnmimation);
            Handler(Looper.getMainLooper()).postDelayed({
                rowBinding.imgSecond.animation = slideAnmimations
                rowBinding.imgFourth.animation = slideAnmimations
            }, 1000)
            checkView(rowBinding, position)
            clickEvent(rowBinding, position)
            setImage(rowBinding, position)
        }
    }

    /**
     * manage circle image view as per position
     */
    private fun checkView(mBinding: RowCircleBinding, position: Int) {
        when (getCurrentReminder(position)) {
            0 -> {
                mBinding.imgFirst.visible()
                mBinding.txtFirst.visible()
                mBinding.imgSecond.visible()
                mBinding.txtSecond.visible()
                mBinding.imgThird.visible()
                mBinding.txtThird.visible()
                mBinding.imgFourth.visible()
                mBinding.txtFourth.visible()
            }
            1 -> {
                mBinding.imgFirst.visible()
                mBinding.txtFirst.visible()
                mBinding.imgSecond.gone()
                mBinding.txtSecond.gone()
                mBinding.imgThird.gone()
                mBinding.txtThird.gone()
                mBinding.imgFourth.gone()
                mBinding.txtFourth.gone()
            }
            2 -> {
                mBinding.imgFirst.visible()
                mBinding.txtFirst.visible()
                mBinding.imgSecond.visible()
                mBinding.txtSecond.visible()
                mBinding.imgThird.gone()
                mBinding.txtThird.gone()
                mBinding.imgFourth.gone()
                mBinding.txtFourth.gone()
            }
            3 -> {
                mBinding.imgFirst.visible()
                mBinding.txtFirst.visible()
                mBinding.imgSecond.visible()
                mBinding.txtSecond.visible()
                mBinding.imgThird.visible()
                mBinding.txtThird.visible()
                mBinding.imgFourth.gone()
                mBinding.txtFourth.gone()
            }
            4 -> {
                mBinding.imgFirst.visible()
                mBinding.txtFirst.visible()
                mBinding.imgSecond.visible()
                mBinding.txtSecond.visible()
                mBinding.imgThird.visible()
                mBinding.txtThird.visible()
                mBinding.imgFourth.visible()
                mBinding.txtFourth.visible()
            }
        }
    }

    /**
     * circle image row item click
     */
    private fun clickEvent(mBinding: RowCircleBinding, position: Int) {
        onClickListener(mBinding, mBinding.imgFirst, position)
        onClickListener(mBinding, mBinding.txtFirst, position)
        onClickListener(mBinding, mBinding.imgSecond, position)
        onClickListener(mBinding, mBinding.txtSecond, position)
        onClickListener(mBinding, mBinding.imgThird, position)
        onClickListener(mBinding, mBinding.txtThird, position)
        onClickListener(mBinding, mBinding.imgFourth, position)
        onClickListener(mBinding, mBinding.txtFourth, position)
    }


    override fun getClickEvent(mRowBinding: ViewDataBinding, view: View?, position: Int) {
        super.getClickEvent(mRowBinding, view, position)
        val currentPosi = position + 1
        var categoriesBean: CircleGameBean? = null
        when (view?.id) {
            R.id.img_first -> categoriesBean = getGameInfo(currentPosi, 0)
            R.id.img_second -> categoriesBean = getGameInfo(currentPosi, 1)
            R.id.img_third -> categoriesBean = getGameInfo(currentPosi, 2)
            R.id.img_fourth -> categoriesBean = getGameInfo(currentPosi, 3)
        }
        if (frm is FcmHomeFrm)
            (frm as FcmHomeFrm).openImg(categoriesBean?.Image)
        else if (frm is CircleFrm)
            (frm as CircleFrm).openImg(categoriesBean?.Image)
    }

    /**
     * get url & name of game from bean list
     *
     * @param currentPos of row position
     * @param remPos     is circle image of one row  image
     * @return current position game bean
     */
    private fun getGameInfo(currentPos: Int, remPos: Int): CircleGameBean {
        val staggeredModel = CircleGameBean()
        staggeredModel.Name = (mFilterBean!![currentPos * 4 - 4 + remPos].Name)
        staggeredModel.Image = (mFilterBean!![currentPos * 4 - 4 + remPos].Image)
        staggeredModel.id = (mFilterBean!![currentPos * 4 - 4 + remPos].id)
        return staggeredModel
    }

    /**
     * set image & text parameter on current position view
     */
    private fun setImage(mBinding: RowCircleBinding, position: Int) {
        val currentPos = position + 1
        when (getCurrentReminder(position)) {
            0 -> {
                mBinding.txtFirst.text = getGameInfo(currentPos, 0).Name
                mBinding.txtSecond.text = getGameInfo(currentPos, 1).Name
                mBinding.txtThird.text = getGameInfo(currentPos, 2).Name
                mBinding.txtFourth.text = getGameInfo(currentPos, 3).Name
                mBinding.imgFirst.showImage(
                    getGameInfo(currentPos, 0).Image,
                    getPlaceHolder(3)
                )
                mBinding.imgSecond.showImage(
                    getGameInfo(currentPos, 1).Image,
                    getPlaceHolder(3)
                )
                mBinding.imgThird.showImage(
                    getGameInfo(currentPos, 2).Image,
                    getPlaceHolder(3)
                )
                mBinding.imgFourth.showImage(
                    getGameInfo(currentPos, 3).Image,
                    getPlaceHolder(3)
                )
            }
            1 -> {
                mBinding.txtFirst.text = getGameInfo(currentPos, 0).Name
                mBinding.imgFirst.showImage(
                    getGameInfo(currentPos, 0).Image,
                    getPlaceHolder(3)
                )
            }
            2 -> {
                mBinding.txtFirst.text = getGameInfo(currentPos, 0).Name
                mBinding.txtSecond.text = getGameInfo(currentPos, 1).Name
                mBinding.imgFirst.showImage(
                    getGameInfo(currentPos, 0).Image,
                    getPlaceHolder(3)
                )
                mBinding.imgSecond.showImage(
                    getGameInfo(currentPos, 1).Image,
                    getPlaceHolder(3)
                )
            }
            3 -> {
                mBinding.txtFirst.text = getGameInfo(currentPos, 0).Name
                mBinding.txtSecond.text = getGameInfo(currentPos, 1).Name
                mBinding.txtThird.text = getGameInfo(currentPos, 2).Name
                mBinding.imgFirst.showImage(
                    getGameInfo(currentPos, 0).Image,
                    getPlaceHolder(3)
                )
                mBinding.imgSecond.showImage(
                    getGameInfo(currentPos, 1).Image,
                    getPlaceHolder(3)
                )
                mBinding.imgThird.showImage(
                    getGameInfo(currentPos, 2).Image,
                    getPlaceHolder(3)
                )
            }
        }
    }

    /**
     * get image item position
     *
     * @param position        row position
     * @param adapterPosition item position on one row
     * @return
     */
    fun getClickPosition(position: Int, adapterPosition: Int): Int {
        return position * 4 + adapterPosition
    }

    private val reminder: Int
        get() = mFilterBean!!.size % 4

    /**
     * get current position of game
     *
     * @param position one row position
     * @return current position of game list
     */
    private fun getCurrentReminder(position: Int): Int {
        val row = mFilterBean!!.size / 4
        return if (position == row || row == 0) {
            reminder
        } else
            0
    }

    fun notifyAdapter(prodList: ArrayList<CircleGameBean>) {
        mFilterBean = prodList
        notifyDataSetChanged()
    }
}