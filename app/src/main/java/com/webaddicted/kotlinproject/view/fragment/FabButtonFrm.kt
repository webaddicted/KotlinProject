package com.webaddicted.kotlinproject.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ContextThemeWrapper
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmFabBtnBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.util.*


class FabButtonFrm : BaseFragment(R.layout.frm_fab_btn) {
    private lateinit var mBinding: FrmFabBtnBinding
    private val menus: ArrayList<FloatingActionMenu> = ArrayList()
    private val mUiHandler = Handler(Looper.getMainLooper())
    private val mMaxProgress = 100
    private var mProgressTypes: LinkedList<ProgressType>? = null

    companion object {
        val TAG = FabButtonFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): FabButtonFrm {
            val fragment = FabButtonFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmFabBtnBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.fab_btn)
        fabBtnProgress()
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.fab1.setOnClickListener(this)
        mBinding.fab2.setOnClickListener(this)
        mBinding.fab3.setOnClickListener(this)
        mBinding.fabProgress.setOnClickListener(this)
        mBinding.menuRed.setOnMenuButtonClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> requireActivity().onBackPressedDispatcher.onBackPressed()
            R.id.fab2 -> mBinding.fab2.gone()
            R.id.fab3 -> mBinding.fab2.visible()
            R.id.fab_Progress -> fabBtnProgress()
            R.id.menu_red -> {
                if (mBinding.menuRed.isOpened) GlobalUtility.showToast(mBinding.menuRed.menuButtonLabelText)
                mBinding.menuRed.toggle(true)
            }
        }
    }

    private fun fabBtnProgress() {
        mProgressTypes = LinkedList()
        mBinding.fabProgress.max = mMaxProgress
        for (type in ProgressType.values()) {
            mProgressTypes!!.offer(type)
        }
        mBinding.fabProgress.setOnClickListener {
            when (mProgressTypes?.poll()) {
                ProgressType.INDETERMINATE -> {
                    mBinding.fabProgress.setShowProgressBackground(true)
                    mBinding.fabProgress.setIndeterminate(true)
                    mProgressTypes!!.offer(ProgressType.INDETERMINATE)
                }
                ProgressType.PROGRESS_POSITIVE -> {
                    mBinding.fabProgress.setIndeterminate(false)
                    mBinding.fabProgress.setProgress(70, true)
                    mProgressTypes!!.offer(ProgressType.PROGRESS_POSITIVE)
                }
                ProgressType.PROGRESS_NEGATIVE -> {
                    mBinding.fabProgress.setProgress(30, true)
                    mProgressTypes!!.offer(ProgressType.PROGRESS_NEGATIVE)
                }
                ProgressType.HIDDEN -> {
                    mBinding.fabProgress.hideProgress()
                    mProgressTypes!!.offer(ProgressType.HIDDEN)
                }
                ProgressType.PROGRESS_NO_ANIMATION -> increaseProgress(mBinding.fabProgress, 0)
                ProgressType.PROGRESS_NO_BACKGROUND -> {
                    mBinding.fabProgress.setShowProgressBackground(false)
                    mBinding.fabProgress.setIndeterminate(true)
                    mProgressTypes!!.offer(ProgressType.PROGRESS_NO_BACKGROUND)
                }
                else -> { hideApiLoader()}
            }
        }


        val programFab1 =
            FloatingActionButton(activity)
        programFab1.buttonSize = FloatingActionButton.SIZE_MINI
        programFab1.labelText = getString(R.string.dummyText)
        programFab1.setImageResource(R.drawable.ic_edit)
        mBinding.menuRed.addMenuButton(programFab1)
        programFab1.setOnClickListener {
            programFab1.setLabelColors(
                ContextCompat.getColor(mActivity, R.color.grey),
                ContextCompat.getColor(mActivity, R.color.light_grey),
                ContextCompat.getColor(
                    mActivity,
                    R.color.white_transparent
                )
            )
            programFab1.setLabelTextColor(
                ContextCompat.getColor(
                    mActivity,
                    R.color.black
                )
            )
        }
        menuData()
        val context =
            ContextThemeWrapper(activity, R.style.MenuButtonsStyle)
        val programFab2 =
            FloatingActionButton(context)
        programFab2.labelText = "Programmatically added button"
        programFab2.setImageResource(R.drawable.ic_edit)
        mBinding.menuYellow.addMenuButton(programFab2)

        mBinding.fab1.isEnabled = false
        mBinding.menuRed.setClosedOnTouchOutside(true)
        mBinding.menuBlue.isIconAnimated = false

        mBinding.menuDown.hideMenuButton(false)
        mBinding.menuRed.hideMenuButton(false)
        mBinding.menuYellow.hideMenuButton(false)
        mBinding.menuGreen.hideMenuButton(false)
        mBinding.menuBlue.hideMenuButton(false)
        mBinding.menuLabelsRight.hideMenuButton(false)

//        fabEdit.setShowAnimation(AnimationUtils.loadAnimation( FloatingButton.this, R.anim.scale_up));
//        fabEdit.setHideAnimation(AnimationUtils.loadAnimation( FloatingButton.this, R.anim.scale_down));
    }

    //    @Override
    //    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    //        super.onActivityCreated(savedInstanceState);
    //
    private fun menuData() {
        menus.add(mBinding.menuDown)
        menus.add(mBinding.menuRed)
        menus.add(mBinding.menuYellow)
        menus.add(mBinding.menuGreen)
        menus.add(mBinding.menuBlue)
        menus.add(mBinding.menuLabelsRight)
        mBinding.menuYellow.setOnMenuToggleListener { opened ->
            val text: String = if (opened) {
                "Menu opened"
            } else {
                "Menu closed"
            }
            Toast.makeText(context, text, Toast.LENGTH_SHORT)
                .show()
        }
        mBinding.fab1.setOnClickListener(clickListener)
        mBinding.fab2.setOnClickListener(clickListener)
        mBinding.fab3.setOnClickListener(clickListener)
        var delay = 400
        for (menu in menus) {
            mUiHandler.postDelayed({ menu.showMenuButton(true) }, delay.toLong())
            delay += 150
        }
        Handler(Looper.getMainLooper()).postDelayed(
            { mBinding.fabEdit.show(true) },
            delay + 150.toLong()
        )
        mBinding.menuRed.setOnMenuButtonClickListener {
            if (mBinding.menuRed.isOpened) {
                Toast.makeText(
                    activity,
                    mBinding.menuRed.menuButtonLabelText,
                    Toast.LENGTH_SHORT
                ).show()
            }
            mBinding.menuRed.toggle(true)
        }
        createCustomAnimation()
    }

    private fun createCustomAnimation() {
        val set = AnimatorSet()
        val scaleOutX = ObjectAnimator.ofFloat(
            mBinding.menuGreen.menuIconView,
            "scaleX",
            1.0f,
            0.2f
        )
        val scaleOutY = ObjectAnimator.ofFloat(
            mBinding.menuGreen.menuIconView,
            "scaleY",
            1.0f,
            0.2f
        )
        val scaleInX = ObjectAnimator.ofFloat(
            mBinding.menuGreen.menuIconView,
            "scaleX",
            0.2f,
            1.0f
        )
        val scaleInY = ObjectAnimator.ofFloat(
            mBinding.menuGreen.menuIconView,
            "scaleY",
            0.2f,
            1.0f
        )
        scaleOutX.duration = 50
        scaleOutY.duration = 50
        scaleInX.duration = 150
        scaleInY.duration = 150
        scaleInX.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                mBinding.menuGreen.menuIconView
                    .setImageResource(if (mBinding.menuGreen.isOpened) R.drawable.ic_close else R.drawable.fill_star)
            }
        })
        set.play(scaleOutX).with(scaleOutY)
        set.play(scaleInX).with(scaleInY).after(scaleOutX)
        set.interpolator = OvershootInterpolator(2F)
        mBinding.menuGreen.iconToggleAnimatorSet = set
    }

    private val clickListener =
        View.OnClickListener { v ->
            when (v.id) {
                R.id.fab1 -> {
                }
                R.id.fab2 -> mBinding.fab2.visibility = View.GONE
                R.id.fab3 -> mBinding.fab2.visibility = View.VISIBLE
            }
        }

    private enum class ProgressType {
        INDETERMINATE, PROGRESS_POSITIVE, PROGRESS_NEGATIVE, HIDDEN, PROGRESS_NO_ANIMATION, PROGRESS_NO_BACKGROUND
    }

    private fun increaseProgress(fab: FloatingActionButton, i: Int) {
        var iValue = i
        if (iValue <= mMaxProgress) {
            fab.setProgress(iValue, false)
            val progress = ++iValue
            mUiHandler.postDelayed({ increaseProgress(fab, progress) }, 30)
        } else {
            mUiHandler.postDelayed({ fab.hideProgress() }, 200)
            //            mProgressTypes.offer(ProgressType.PROGRESS_NO_ANIMATION);
        }
    }

}

