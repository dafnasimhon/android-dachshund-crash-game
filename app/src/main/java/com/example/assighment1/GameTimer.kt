package com.example.assighment1

import android.os.Handler
import android.os.Looper

class GameTimer(
    private val delayMillis: Long,
    private val onTick: () -> Unit
) {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var isRunning: Boolean = false

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (!isRunning) return


            onTick()


            handler.postDelayed(this, delayMillis)
        }
    }

    fun start() {
        if (isRunning) return
        isRunning = true
        handler.postDelayed(runnable, delayMillis)
    }

    fun stop() {
        isRunning = false
        handler.removeCallbacks(runnable)
    }
}