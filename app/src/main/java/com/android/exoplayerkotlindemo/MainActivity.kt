package com.android.exoplayerkotlindemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, VideoActivity::class.java).apply {
            putExtra(AD_URL, "https://vodcache.worldwidetv.club/trailers/barcelona.mp4/playlist.m3u8")
            putExtra(VIDEO_URL, "https://vodcache.worldwidetv.club/live/179_480p/playlist.m3u8")
            startActivity(this)
        }
        finish()

        // Notes:
        // To change loading animation,
        //     Goto https://lottiefiles.com/ (search for "loading")
        //     Download json and place it in raw folder
        //     Change the "app:lottie_rawRes="@raw/loading_hourglass"" in activity_main.xml to "app:lottie_rawRes="@raw/<your_json>""
    }
}
