@file:Suppress("DEPRECATION")

package com.mobtech.xo_offline_game.Utils

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.preference.PreferenceManager
import java.lang.reflect.InvocationTargetException

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

    fun setIntSharedPref(c: Context, pref: String, value: Int) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putInt(pref, value)
        e.apply()
    }

    fun getIntSharedPref(c: Context, pref: String): Int {
        return PreferenceManager.getDefaultSharedPreferences(c).getInt(pref, 0)
    }

    fun setStringSharedPref(c: Context, pref: String, value: String) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putString(pref, value)
        e.apply()
    }

    fun getStringSharedPref(c: Context, pref: String): String? {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(pref, "")
    }

    fun versionCode(context: Context): String {
        val version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        return "V $version"
    }

    fun getDiffLabel(level: Int): String {
        var label = ""
        when (level) {
            0 -> label = "Easy"
            1 -> label = "Medium"
            2 -> label = "Hard"
        }
        return label
    }
}