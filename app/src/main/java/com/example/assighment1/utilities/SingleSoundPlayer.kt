package com.example.assighment1.utilities

import android.content.Context
import android.media.MediaPlayer
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SingleSoundPlayer(context: Context) {
    private val context: Context = context.applicationContext
    private val executor: Executor = Executors.newSingleThreadExecutor()

    fun playSound(resourceId: Int) {
        executor.execute {
            try {
                val mediaPlayer = MediaPlayer.create(context, resourceId)
                if (mediaPlayer != null) {
                    mediaPlayer.isLooping = false
                    mediaPlayer.setVolume(1.0f, 1.0f)
                    mediaPlayer.start()

                    mediaPlayer.setOnCompletionListener { mp ->
                        mp.stop()
                        mp.release()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}