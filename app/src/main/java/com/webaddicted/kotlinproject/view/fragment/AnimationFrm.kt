package com.webaddicted.kotlinproject.view.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmAnimationBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment

class AnimationFrm : BaseFragment() {
    private lateinit var mBinding: FrmAnimationBinding
    private var animBlink: Animation? = null
    private var animFade: Animation? = null
    private var animZoom: Animation? = null
    private var animMove: Animation? = null
    private var animRotate: Animation? = null
    private var animFadeIn: Animation? = null
    private var animFadeOut: Animation? = null
    private var animZoomIn: Animation? = null
    private var animZoomOut: Animation? = null
    private var animSlideUp: Animation? = null
    private var animSlideDown: Animation? = null
    private var animBounce: Animation? = null
    private var animSequential: Animation? = null
    private var animTogether: Animation? = null

    companion object {
        val TAG = AnimationFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): AnimationFrm {
            val fragment = AnimationFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_animation
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmAnimationBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.animation_title)
        animBlink = AnimationUtils.loadAnimation(activity, R.anim.blink)
        animFade = AnimationUtils.loadAnimation(activity, R.anim.fade)
        animZoom = AnimationUtils.loadAnimation(activity, R.anim.zoom)
        animMove = AnimationUtils.loadAnimation(activity, R.anim.move)
        animRotate = AnimationUtils.loadAnimation(activity, R.anim.rotate)
        animFadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        animFadeOut = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
        animZoomIn = AnimationUtils.loadAnimation(activity, R.anim.zoom_in)
        animZoomOut = AnimationUtils.loadAnimation(activity, R.anim.zoom_out)
        animSlideUp = AnimationUtils.loadAnimation(activity, R.anim.slide_up)
        animSlideDown = AnimationUtils.loadAnimation(activity, R.anim.slide_down)
        animBounce = AnimationUtils.loadAnimation(activity, R.anim.bounce)
        animSequential = AnimationUtils.loadAnimation(activity, R.anim.sequential)
        animTogether = AnimationUtils.loadAnimation(activity, R.anim.together)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnBlink.setOnClickListener(this)
        mBinding.btnFade.setOnClickListener(this)
        mBinding.btnZoom.setOnClickListener(this)
        mBinding.btnMove.setOnClickListener(this)
        mBinding.btnRotate.setOnClickListener(this)
        mBinding.btnFadeIn.setOnClickListener(this)
        mBinding.btnFadeOut.setOnClickListener(this)
        mBinding.btnCrossFadeIn.setOnClickListener(this)
        mBinding.btnCrossFadeOut.setOnClickListener(this)
        mBinding.btnZoomIn.setOnClickListener(this)
        mBinding.btnZoomOut.setOnClickListener(this)
        mBinding.btnSlideUp.setOnClickListener(this)
        mBinding.btnSlideDown.setOnClickListener(this)
        mBinding.btnBounce.setOnClickListener(this)
        mBinding.btnSequence.setOnClickListener(this)
        mBinding.btnTogether.setOnClickListener(this)
        mBinding.btnLeftRight.setOnClickListener(this)
        mBinding.btnTopTobBottom.setOnClickListener(this)
        mBinding.btnBottomToTop.setOnClickListener(this)
        mBinding.btnRightToLeft.setOnClickListener(this)
        mBinding.btnLeftToRight.setOnClickListener(this)
        mBinding.btnExpend.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_blink -> startAnimation(animBlink, mBinding.btnBlink, mBinding.txtBlink)
            R.id.btn_fade -> startAnimation(animFade, mBinding.btnFade, mBinding.txtFade)
            R.id.btn_zoom -> startAnimation(animZoom, mBinding.btnZoom, mBinding.txtZoom)
            R.id.btn_move -> startAnimation(animMove, mBinding.btnMove, mBinding.txtMove)
            R.id.btn_rotate -> startAnimation(animRotate, mBinding.btnRotate, mBinding.txtRotate)
            R.id.btn_cross_fade_in -> startAnimation(
                animFadeIn,
                mBinding.btnCrossFadeIn,
                mBinding.txtCrossFadeIn
            )
            R.id.btn_cross_fade_out -> startAnimation(
                animFadeOut,
                mBinding.btnCrossFadeOut,
                mBinding.txtCrossFadeOut
            )
            R.id.btn_zoom_in -> startAnimation(animZoomIn, mBinding.btnZoomIn, mBinding.txtZoomIn)
            R.id.btn_zoom_out -> startAnimation(
                animZoomOut,
                mBinding.btnZoomOut,
                mBinding.txtZoomOut
            )
            R.id.btn_slide_up -> startAnimation(
                animSlideUp,
                mBinding.btnSlideUp,
                mBinding.txtSlideUp
            )
            R.id.btn_slide_down -> startAnimation(
                animSlideDown,
                mBinding.btnSlideDown,
                mBinding.txtSlideDown
            )
            R.id.btn_bounce -> startAnimation(animBounce, mBinding.btnBounce, mBinding.txtBounce)
            R.id.btn_sequence -> startAnimation(
                animSequential,
                mBinding.btnSequence,
                mBinding.txtSequence
            )
            R.id.btn_together -> startAnimation(
                animTogether,
                mBinding.btnTogether,
                mBinding.txtTogether
            )
            R.id.btn_left_right -> leftRigtAnimation()
            R.id.btn_top_tob_bottom -> leftRigtAnimation()
            R.id.btn_bottom_to_top -> leftRigtAnimation()
            R.id.btn_right_to_left -> leftRigtAnimation()
            R.id.btn_left_to_right -> leftRigtAnimation()
            R.id.btn_expend -> {
                if (mBinding.expandableLayout.isExpanded) mBinding.expandableLayout.collapse()
                else mBinding.expandableLayout.expand()
            }
        }
    }

    fun startAnimation(animation: Animation?, btnView: View, txtView: View) {
        btnView.startAnimation(animation)
        txtView.startAnimation(animation)
        mBinding.imageAnimation.startAnimation(animation)
    }

    fun leftRigtAnimation() {
        // top to bottom animation............................
        val trans1 = TranslateAnimation(0f, 0f, -100f, 0f)
        trans1.duration = 1000
        mBinding.btnTopTobBottom.animation = trans1

        //botom to top animation...........................
        val trans2 = TranslateAnimation(0f, 0f, 100f, 0f)
        trans2.duration = 1000
        mBinding.btnBottomToTop.animation = trans2

        //left to right animation.............................
        val trans3 = TranslateAnimation(100f, 0f, 0f, 0f)
        trans3.duration = 1000
        mBinding.btnLeftToRight.animation = trans3

        // right to left animation..............................
        val trans4 = TranslateAnimation(-100f, 0f, 0f, 0f)
        trans4.duration = 1000
        mBinding.btnRightToLeft.animation = trans4
    }
}

