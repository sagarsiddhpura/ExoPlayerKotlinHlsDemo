package com.android.exoplayerkotlindemo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.activity_main.*

class VideoActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private var adUrl : String? = null
    private var videoUrl : String? = null
    private var mProgressHandler: Handler? = null
    private var state = STATE_PLAYING_AD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intent.apply {
            adUrl = getStringExtra(AD_URL)
            videoUrl = getStringExtra(VIDEO_URL)
            if(adUrl == null || videoUrl == null) {
                toast("Error! VideoActivity started without intent extras AD_URL and VIDEO_URL")
                finish()
                return
            }
        }
        val adMediaSource = HlsMediaSource.Factory(DefaultHttpDataSourceFactory("ExoPlayerHLSKotlinDemo"))
            .createMediaSource(Uri.parse(adUrl))
        mProgressHandler = Handler()
        state = STATE_PLAYING_AD
        refreshState()
        startPlayback(adMediaSource)
    }

    private fun refreshState() {
        when (state) {
            // We are playing Ad. Need to show message textview and start listener on exoplayer to check 10 seconds progress
            STATE_PLAYING_AD -> {
                video_message_view.beVisible()
                video_message_view.text = getString(R.string.playing_ad)
                addProgressHandler()
            }
            // We have crossed 10 second mark. User can now skip ad and remove listener on exoplayer
            STATE_PLAYING_AD_SKIPABLE -> {
                video_message_view.beVisible()
                removeProgressHandler()
                video_message_view.text = getString(R.string.skip_ad)
                // Set textview on click to skip ad
                video_message_view.setOnClickListener {
                    state = STATE_PLAYING_VIDEO
                    refreshState()
                }
            }
            // Playing main video. Hide textview
            STATE_PLAYING_VIDEO -> {
                playVideo()
                video_message_view.beGone()
            }
            // Main video finished. What should we do?
            STATE_PLAYING_VIDEO -> {
                releasePlayer()
                // finish()
            }
        }
    }

    private fun startPlayback(source: HlsMediaSource) {
        if (player == null) {
            val adaptiveTrackSelection = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
            player = ExoPlayerFactory.newSimpleInstance( this,
                DefaultRenderersFactory(this),
                DefaultTrackSelector(adaptiveTrackSelection),
                DefaultLoadControl()
            )
            video_view?.player = player
            video_view.useController = false
            player!!.playWhenReady = true
            video_view!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            loading_view.beVisible()
        }
        player!!.prepare(source, true, false)
        player!!.addListener(object : Player.DefaultEventListener() {
            override fun onPlayerStateChanged(
                playWhenReady: Boolean,playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        loading_view.beGone()
                    }
                    Player.STATE_BUFFERING -> {
                        loading_view.beVisible()
                    }
                    Player.STATE_READY -> {
                        loading_view.beGone()
                    }
                    Player.STATE_ENDED -> {
                        loading_view.beGone()
                        playVideo()
                        if(state == STATE_PLAYING_AD || state == STATE_PLAYING_AD_SKIPABLE) {
                            state = STATE_PLAYING_VIDEO
                            refreshState()
                        } else if(state == STATE_PLAYING_VIDEO) {
                            state = STATE_VIDEO_FINISHED
                            refreshState()
                        }
                    }
                }
            }
        })
    }

    private fun playVideo() {
        val defaultBandwidthMeter = DefaultBandwidthMeter()
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(
            this,
            "ExoPlayerHLSKotlinDemo", defaultBandwidthMeter
        )
        val videoMediaSource = HlsMediaSource.Factory(dataSourceFactory).setAllowChunklessPreparation(true)
            .createMediaSource(Uri.parse(videoUrl))
        startPlayback(videoMediaSource)
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun addProgressHandler() {
        mProgressHandler?.post(object : Runnable {
            override fun run() {
                if(player != null) {
                    val secs = player!!.currentPosition / 1000
                    if(secs > 10) {
                        state = STATE_PLAYING_AD_SKIPABLE
                        refreshState()
                    } else {
                        video_message_view.text = getString(R.string.playing_video, (10-secs).toString())
                        mProgressHandler!!.removeCallbacksAndMessages(null)
                        mProgressHandler!!.postDelayed(this, PROGRESS_UPDATE_INTERVAL.toLong())
                    }
                }
            }
        })
    }

    private fun removeProgressHandler() {
        mProgressHandler?.removeCallbacksAndMessages(null)
    }
}
