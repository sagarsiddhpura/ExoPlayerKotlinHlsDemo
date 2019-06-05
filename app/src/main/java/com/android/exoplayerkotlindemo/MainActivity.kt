package com.android.exoplayerkotlindemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer


class MainActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, VideoActivity::class.java).apply {
            putExtra(AD_URL, "https://vodcache.worldwidetv.club/trailers/barcelona.mp4/playlist.m3u8")
            putExtra(VIDEO_URL, "https://vodcache.worldwidetv.club/live/179_480p/playlist.m3u8")
            startActivity(this)
        }
        finish()
    }
}
