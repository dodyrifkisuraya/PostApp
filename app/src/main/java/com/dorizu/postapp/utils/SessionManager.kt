package com.dorizu.postapp.utils

import android.os.Handler
import android.os.Looper

object SessionManager {

    private const val TIMEOUT_DURATION_MS = 180_000L // 180 detik

    private val handler = Handler(Looper.getMainLooper())
    private var timeoutCallback: (() -> Unit)? = null

    private val timeoutRunnable = Runnable {
        timeoutCallback?.invoke()
    }

    fun startTimer(onTimeout: () -> Unit) {
        timeoutCallback = onTimeout
        resetTimer()
    }

    fun resetTimer() {
        handler.removeCallbacks(timeoutRunnable)
        handler.postDelayed(timeoutRunnable, TIMEOUT_DURATION_MS)
    }

    fun stopTimer() {
        handler.removeCallbacks(timeoutRunnable)
        timeoutCallback = null
    }
}
