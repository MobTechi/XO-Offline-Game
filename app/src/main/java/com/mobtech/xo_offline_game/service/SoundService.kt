package com.mobtech.xo_offline_game.service

import android.app.Activity
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import com.mobtech.xo_offline_game.R
import com.mobtech.xo_offline_game.Utils.Utils
import java.lang.reflect.InvocationTargetException

@Suppress("DEPRECATION")
class SoundService (private val activity: Activity) {

    fun tapSound() {
        // Play tap sound using try/catch block
        val utils = Utils()
        val prefString = "musicPref"
        val isMusic = utils.getPref(activity, prefString)
        if (isMusic) {
            try {
                val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
                toneGenerator.release()
            } catch (exception: InvocationTargetException) {
            }
        }
    }
}