package com.example.brainmon.ui

import android.content.Context
import android.media.MediaPlayer
import com.example.brainmon.R

class SoundManager(private val context: Context) {

    // 1. Standard Catch (The "Pop" or "Ding")
    fun playCatch() {
        playSound(R.raw.caught) // Make sure this matches your filename
    }

    // 2. Biome Unlock (The "Fanfare" or "Big Success")
    fun playUnlock() {
        playSound(R.raw.success)
    }

    // 3. Wrong Answer (The "Buzz")
    fun playFail() {
        playSound(R.raw.fail)
    }

    private fun playSound(resId: Int) {
        try {
            val mp = MediaPlayer.create(context, resId)
            mp.setOnCompletionListener { it.release() }
            mp.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}