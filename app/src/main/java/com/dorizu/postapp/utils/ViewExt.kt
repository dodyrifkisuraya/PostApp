package com.dorizu.postapp.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View.applySystemBarPadding() {
    val initialLeft = paddingLeft
    val initialTop = paddingTop
    val initialRight = paddingRight
    val initialBottom = paddingBottom

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
        val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

        v.setPadding(
            initialLeft,
            initialTop + statusBar.top,
            initialRight,
            initialBottom + navBar.bottom
        )
        insets
    }
}