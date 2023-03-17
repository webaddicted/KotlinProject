package com.webaddicted.kotlinproject.view.fragment

//import com.google.android.exoplayer2.source.ExtractorMediaSource
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.util.Util
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmExoPlayerRecyclerBinding
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.base.BaseFragment

class ExoPlayerFrm : BaseFragment(R.layout.frm_exo_player_recycler) {
    private lateinit var mBinding: FrmExoPlayerRecyclerBinding
    private val BANDWIDTH_METER = DefaultBandwidthMeter()
    private var player: SimpleExoPlayer? = null
    private var playbackPosition: Long = 0
    private var currentWindow = 0
    private var playWhenReady = true
    companion object {
        val TAG = ExoPlayerFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): ExoPlayerFrm {
            val fragment = ExoPlayerFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmExoPlayerRecyclerBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.exo_player_title)
//        mBinding.rvExoPlayer.gone()
//        mBinding.exoPlayer.visible()
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> backDispatcher.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        if (player == null) {
//            player = ExoPlayerFactory.newSimpleInstance(
//                DefaultRenderersFactory(context),
//                DefaultTrackSelector(),
//                DefaultLoadControl()
//            )
//            mBinding.exoPlayer.player = player
            player?.playWhenReady = playWhenReady
            player?.seekTo(currentWindow, playbackPosition)
        }
        val mediaSource =
            buildMediaSource(Uri.parse(getString(R.string.media_url_mp4)))
        player?.prepare(mediaSource!!, true, false)
//        player?.addListener(object : Player.DefaultEventListener() {
//            override fun onPlayerStateChanged(playWhenReady: Boolean,playbackState: Int) {
//                when (playbackState) {
//                    Player.STATE_IDLE -> {}
//                    Player.STATE_BUFFERING -> {}
//                    Player.STATE_READY -> {}
//                    Player.STATE_ENDED -> {}
//                }
//            }
//        })
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player?.currentPosition!!
            currentWindow = player?.currentWindowIndex!!
            playWhenReady = player?.playWhenReady!!
            player?.release()
            player = null
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
//        val userAgent = "exoplayer-codelab"
//        return if (uri.lastPathSegment!!.contains("mp3") || uri.lastPathSegment!!.contains("mp4")) {
//            ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
//                .createMediaSource(uri)
//        } else if (uri.lastPathSegment!!.contains("m3u8")) {
//            HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
//                .createMediaSource(uri)
//        } else {
//            val dashChunkSourceFactory: DashChunkSource.Factory = DefaultDashChunkSource.Factory(
//                DefaultHttpDataSourceFactory("ua",BANDWIDTH_METER))
//            val manifestDataSourceFactory: DataSource.Factory =
//                DefaultHttpDataSourceFactory(userAgent)
//            DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
//                .createMediaSource(uri)
//        }
    return  null
    }

    @SuppressLint("InlinedApi")
    fun hideSystemUiFullScreen() {
//        mBinding.exoPlayer.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
//                or View.SYSTEM_UI_FLAG_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    @SuppressLint("InlinedApi")
    fun hideSystemUi() {
//        mBinding.exoPlayer.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
//                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val currentOrientation = resources.configuration.orientation
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                hideSystemUiFullScreen()
            } else {
                hideSystemUi()
            }
        }

}

