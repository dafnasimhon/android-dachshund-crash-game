package com.example.assighment1.logic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class GameTimer(
    private val delayMillis: Long,
    private val onTick: () -> Unit
) : CoroutineScope {


    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var timerJob: Job? = null
    private var startTime: Long = 0L
    private var timerOn: Boolean = false
    private var lastElapsedMillis: Long = 0L

    fun start(reset: Boolean = false) {
        if (timerOn) return

        if (reset) {
            lastElapsedMillis = 0L
        }

        timerOn = true
        startTime = System.currentTimeMillis() - lastElapsedMillis

        timerJob = launch {
            while (timerOn) {
                val currentTime = System.currentTimeMillis()
                lastElapsedMillis = currentTime - startTime

                onTick()

                delay(delayMillis)
            }
        }
    }

    fun stop() {
        if (!timerOn) return

        timerOn = false
        timerJob?.cancel()
        timerJob = null


        lastElapsedMillis = System.currentTimeMillis() - startTime
    }

    fun getElapsedMillis(): Long {
        return if (timerOn) {
            System.currentTimeMillis() - startTime
        } else {
            lastElapsedMillis
        }
    }

    fun release() {
        timerOn = false
        timerJob?.cancel()
        job.cancel()
    }
}