package com.android.exoplayerkotlindemo

import android.os.Build
import android.os.Looper

const val AD_URL = "ad_url"
const val VIDEO_URL = "video_url"

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()
fun isJellyBean1Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
