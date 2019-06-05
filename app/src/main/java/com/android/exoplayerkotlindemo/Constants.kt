package com.android.exoplayerkotlindemo

import android.os.Build
import android.os.Looper

const val AD_URL = "ad_url"
const val VIDEO_URL = "video_url"
const val PROGRESS_UPDATE_INTERVAL = 1000
const val STATE_PLAYING_AD = 1
const val STATE_PLAYING_AD_SKIPABLE = 2
const val STATE_PLAYING_VIDEO = 3
const val STATE_VIDEO_FINISHED = 3

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()
fun isJellyBean1Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
