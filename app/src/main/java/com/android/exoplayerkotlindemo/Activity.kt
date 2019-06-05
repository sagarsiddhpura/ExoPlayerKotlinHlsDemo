package com.android.exoplayerkotlindemo

import android.app.Activity
import android.view.View
import android.widget.Toast

fun Activity.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    if (isOnMainThread()) {
        showToast(this, msg, length)
    } else {
        runOnUiThread {
            showToast(this, msg, length)
        }
    }
}

private fun showToast(activity: Activity, message: String, length: Int) {
    if (!activity.isActivityDestroyed()) {
        activity.applicationContext.toast(message, length)
    }
}

fun Activity.isActivityDestroyed() = isJellyBean1Plus() && isDestroyed

fun View.beVisible() {
    visibility = View.VISIBLE
}

fun View.beGone() {
    visibility = View.GONE
}
