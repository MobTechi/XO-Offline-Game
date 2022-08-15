package com.mobtech.xo_offline_game.Utils

import android.content.Context
import android.preference.PreferenceManager

@Suppress("DEPRECATION")
class Utils {
    fun setPref(c: Context, pref: String, value: Boolean) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putBoolean(pref, value)
        e.apply()
    }

    fun getPref(c: Context, pref: String): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(pref, true)
    }
}