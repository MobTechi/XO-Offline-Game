package com.mobtech.xo_offline_game.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.mobtech.xo_offline_game.R
import com.mobtech.xo_offline_game.Utils.CommonString
import com.mobtech.xo_offline_game.Utils.CommonUtil.getBooleanSharedPref


class MusicService : Service(), MediaPlayer.OnErrorListener {
    private val mBinder: IBinder = LocalBinder()

    private var mPlayer: MediaPlayer? = null
    private var length = 0
    private var isMusic = false

    inner class LocalBinder : Binder() {
        val service: MusicService
            get() = this@MusicService
    }

    override fun onBind(arg0: Intent?): IBinder {
        isMusic = getBooleanSharedPref(this, CommonString.gameSoundLevel)
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        mPlayer = MediaPlayer.create(this, R.raw.bg_music)
        mPlayer!!.setOnErrorListener(this)
        mPlayer!!.isLooping = true
        mPlayer!!.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                onError(mPlayer, what, extra)
                return true
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isMusic) {
            mPlayer!!.start()
        }
        return START_STICKY
    }

    fun pauseMusic() {
        if (isMusic && mPlayer != null && mPlayer!!.isPlaying) {
            mPlayer!!.pause()
            length = mPlayer!!.currentPosition
        }
    }

    fun resumeMusic() {
        if (isMusic && mPlayer != null && !mPlayer!!.isPlaying) {
            mPlayer!!.seekTo(length)
            mPlayer!!.start()
        }
    }

    private fun stopMusic() {
        mPlayer!!.stop()
        mPlayer!!.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            stopMusic()
        } finally {
        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        try {
            mPlayer!!.stop()
            mPlayer!!.release()
        } finally {
        }
        return false
    }
}