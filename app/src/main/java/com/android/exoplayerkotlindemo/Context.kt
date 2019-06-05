package com.android.exoplayerkotlindemo

import android.content.Context
import android.widget.Toast

fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    try {
        Toast.makeText(applicationContext, msg, length).show()
    } catch (e: Exception) {
    }
}
