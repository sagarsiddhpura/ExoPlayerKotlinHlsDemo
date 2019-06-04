package com.android.exoplayerkotlindemo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.util.Util.getUserAgent
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory





class MainActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adMediaSource = HlsMediaSource.Factory(DefaultHttpDataSourceFactory("ExoPlayerKotlinDemo"))
            .createMediaSource(Uri.parse("https://vodcache.worldwidetv.club/trailers/barcelona.mp4/playlist.m3u8"))
       startPlayback(adMediaSource, true)
    }

    private fun startPlayback(source: HlsMediaSource, isAd: Boolean) {
        if (player == null) {
            val adaptiveTrackSelection = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
            player = ExoPlayerFactory.newSimpleInstance( this,
                DefaultRenderersFactory(this),
                DefaultTrackSelector(adaptiveTrackSelection),
                DefaultLoadControl()
            )
            video_view?.player = player
            player!!.playWhenReady = true
            video_view!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
        player!!.prepare(source, true, false)
        player!!.addListener(object : Player.DefaultEventListener() {
            override fun onPlayerStateChanged(
                playWhenReady: Boolean,playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {}
                    Player.STATE_BUFFERING -> {}
                    Player.STATE_READY -> {}
                    Player.STATE_ENDED -> {
                        if(isAd) {
                            playVideo()
                        } else {
                            // main video ended. finish()?
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
            "ExoPlayerKotlinDemo", defaultBandwidthMeter
        )
        val videoMediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse("https://vodcache.worldwidetv.club/live/179_480p/playlist.m3u8"))
        startPlayback(videoMediaSource, true)
    }
}
