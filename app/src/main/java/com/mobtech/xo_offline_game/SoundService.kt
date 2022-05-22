package com.mobtech.xo_offline_game

import android.media.AudioManager
import android.media.ToneGenerator

class SoundService {

    fun tapSound () {
        ToneGenerator(AudioManager.STREAM_MUSIC, 100).startTone(ToneGenerator.TONE_PROP_BEEP, 200)
    }
}