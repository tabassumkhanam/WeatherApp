package com.toi.weathetapp.utils

import android.content.Context
import android.view.View
import android.widget.Toast

fun Context.showLongToast(msg: String) {
    Toast.makeText(
        this.applicationContext,
        msg,
        Toast.LENGTH_LONG
    ).show()
}

fun Context.showShortToast(msg: String) {
    Toast.makeText(
        this.applicationContext,
        msg,
        Toast.LENGTH_SHORT
    ).show()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}
