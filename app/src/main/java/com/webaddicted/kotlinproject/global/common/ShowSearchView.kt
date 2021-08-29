package com.webaddicted.kotlinproject.global.common

import android.animation.Animator
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.cardview.widget.CardView

class ShowSearchView {

    //    public static void handleToolBar(final Context context, final CardView search, Toolbar toolbarMain,final EditText editText) {
    fun handleToolBar(context: Context?, search: CardView, editText: EditText) {
        val fade_in =
            AnimationUtils.loadAnimation(context?.applicationContext, android.R.anim.fade_in)
        val fade_out =
            AnimationUtils.loadAnimation(context?.applicationContext, android.R.anim.fade_out)
        editText.setText("")
        if (search.visibility == View.VISIBLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val animatorHide = ViewAnimationUtils.createCircularReveal(
                    search,
                    search.width - convertDpToPixel(56f, context).toInt(),
                    convertDpToPixel(23f, context).toInt(),
                    Math.hypot(search.width.toDouble(), search.height.toDouble()).toFloat(),
                    0f
                )
                animatorHide.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        editText.startAnimation(fade_out)
                        editText.visibility = View.INVISIBLE
                        search.visibility = View.GONE
                        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            editText.windowToken,
                            0
                        )
                    }

                    override fun onAnimationCancel(animation: Animator) {

                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })
                animatorHide.duration = 300
                animatorHide.start()
            } else {
                (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    editText.windowToken,
                    0
                )
                editText.startAnimation(fade_out)
                editText.visibility = View.INVISIBLE
                search.visibility = View.GONE
            }
            editText.setText("")
            //            toolbarMain.getMenu().clear();
            //            toolbarMain.inflateMenu(R.menu.menu_main);
            search.isEnabled = false
        } else {
            //            toolbarMain.getMenu().clear();
            //            toolbarMain.setNavigationIcon(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val animator = ViewAnimationUtils.createCircularReveal(
                    search,
                    search.width - convertDpToPixel(56f, context).toInt(),
                    convertDpToPixel(23f, context).toInt(),
                    0f,
                    Math.hypot(search.width.toDouble(), search.height.toDouble()).toFloat()
                )
                animator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        editText.visibility = View.VISIBLE
                        editText.startAnimation(fade_in)
                        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                            InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY
                        )
                    }

                    override fun onAnimationCancel(animation: Animator) {

                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })
                search.visibility = View.VISIBLE
                if (search.visibility == View.VISIBLE) {
                    animator.duration = 300
                    animator.start()
                    search.isEnabled = true
                }
                fade_in.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {

                    }

                    override fun onAnimationEnd(animation: Animation) {
                        editText.requestFocus()
                    }

                    override fun onAnimationRepeat(animation: Animation) {

                    }
                })
            } else {
                search.visibility = View.VISIBLE
                search.isEnabled = true
                editText.requestFocus()
                (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                    editText,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }
        }
    }

    fun convertDpToPixel(dp: Float, context: Context?): Float {
        val resources = context?.resources
        val metrics = resources?.displayMetrics
        return dp * (metrics?.densityDpi!! / 160f)
    }
}


