package com.mobtech.xo_offline_game.service

import android.media.AudioManager
import android.media.ToneGenerator
import java.lang.reflect.InvocationTargetException

class SoundService {
    fun tapSound() {
        // Play tap sound using try/catch block
        try {
            val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
            toneGenerator.release()
        } catch (exception: InvocationTargetException) {
        }
    }
}