package com.webaddicted.kotlinproject.view.fragment

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmLandingPageBinding
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.customview.UIAlphaFrame
import com.webaddicted.kotlinproject.view.base.BaseFragment


class LandingPageFrm : BaseFragment() {
    private lateinit var mBinding: FrmLandingPageBinding
    private val urls = arrayOf(
        "https://image.tmdb.org/t/p/original/q2BrsPEztd0L1cueuFIZakHObl7.jpg",
//        "https://image.tmdb.org/t/p/original/1oUVoqtAft3yU8PPHDRhsm0yR5T.jpg",
        "https://i.pinimg.com/originals/6f/24/a9/6f24a9a04bb643c18156a19565f085b8.jpg",
        "https://mfiles.alphacoders.com/770/770826.jpg",
        "https://image.tmdb.org/t/p/original/9xDD53hohCMp5VoFtqNPfzTsnLJ.jpg",
        "https://mfiles.alphacoders.com/442/44293.jpg",
        "https://image.tmdb.org/t/p/original/yeRW8JhHqQznEdOUe97dLWnyeEq.jpg",
        "https://movieposterhd.com/wp-content/uploads/2019/03/Captain-Marvel-Wallpaper-Phone.jpg",
        "https://image.tmdb.org/t/p/original/oCBq1lqg9Q1PwvVediz8Lq7k6jj.jpg",
        "https://image.tmdb.org/t/p/original/v1QQKq8M0fWxMgSdGOX1aCv8qMB.jpg"
    )
    private var image = true
    private var index = 0
    private var frame: UIAlphaFrame? = null

    companion object {
        val TAG = LandingPageFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): LandingPageFrm {
            val fragment = LandingPageFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_landing_page
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmLandingPageBinding
        init()
    }

    private fun init() {
        frame = UIAlphaFrame(mBinding.root)
        prepareGrid(urls[0])
    }
    private fun prepareGrid(url: String) {
        Glide.with(this)
            .load(url)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource is BitmapDrawable) {
                        val palette: Palette =
                            Palette.from(resource.bitmap).generate()
                        val vibrant: Int = palette.getVibrantColor(Color.WHITE)
                        frame!!.changeAlphaTo(vibrant)
//                        setStatusBarColor(vibrant)
                        if (image) {
                            fade(mBinding.titleImage, mBinding.titleImageTrue)
                        } else {
                            fade(mBinding.titleImageTrue, mBinding.titleImage)
                        }
                        image = !image
                        startRunner(++index)
                    } else {
                        frame!!.changeAlphaTo(Color.BLACK)
//                        setStatusBarColor(Color.BLACK)
                        Lg.d(TAG,"Not an bitmap drawable")
                        if (image) {
                            fade(mBinding.titleImage, mBinding.titleImageTrue)
                        } else {
                            fade(mBinding.titleImageTrue, mBinding.titleImage)
                        }
                        image = !image
                        startRunner(++index)
                    }
                    return false
                }
            })
            .into(DrawableImageViewTarget(if (image) mBinding.titleImageTrue else mBinding.titleImage))
    }

//    private fun setStatusBarColor(color: Int) {
//        (mActivity as BaseActivity).setStatusBarColor(color)
//    }

    private fun startRunner(index: Int) {
        Handler(Looper.getMainLooper())
            .postDelayed({
                val real = index % urls.size
                if (lifecycle.currentState
                        .isAtLeast(Lifecycle.State.STARTED)
                ) prepareGrid(urls[real]) else Lg.d(TAG,"Delayed finished")
            }, 5000)
    }

    private fun fade(v1: ImageView, v2: ImageView) {
        v1.animate().alpha(0f).duration = 1000
        v2.animate().alpha(1f).duration = 1000
    }
}

