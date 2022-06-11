package com.mobtech.xo_offline_game

import android.media.AudioManager
import android.media.ToneGenerator

class SoundService {
    var isTone: Boolean = false

    fun tapSound() {
        // Check toneGenerator is already started or not
        if (isTone) {
            return
        } else {
            isTone = true
            val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
            toneGenerator.release();
        }
        isTone = false
    }
}