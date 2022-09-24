@file:Suppress("DEPRECATION")

package com.mobtech.xo_offline_game.Utils

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.preference.PreferenceManager
import java.lang.reflect.InvocationTargetException
import java.util.*

object CommonUtil {

    // Play tap sound using try/catch block
    fun tapSoundUtil(c: Context) {
        val isMusic = getBooleanSharedPref(c, CommonString.gameSoundLevel)
        if (isMusic) {
            try {
                val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
                toneGenerator.release()
            } catch (exception: InvocationTargetException) {
            }
        }
    }

    fun setBooleanSharedPref(c: Context, pref: String, value: Boolean) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putBoolean(pref, value)
        e.apply()
    }

    fun getBooleanSharedPref(c: Context, pref: String): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(pref, true)
    }

    fun versionCode(context: Context): String {
        val version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        return "V $version"
    }
}