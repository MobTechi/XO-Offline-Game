package com.mobtech.xo_offline_game.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobtech.xo_offline_game.R
import com.mobtech.xo_offline_game.Utils.CommonString.gameSoundLevel
import com.mobtech.xo_offline_game.Utils.CommonUtil.getBooleanSharedPref
import com.mobtech.xo_offline_game.Utils.CommonUtil.tapSoundUtil
import com.mobtech.xo_offline_game.Utils.CommonUtil.versionCode

@Suppress("DEPRECATION")
class MainPage : AppCompatActivity() {

    // Properties
    private var isMusic: Boolean = true
    private var isLoaded: Boolean = false
    // var serviceBound = false

    // private var musicService: MusicService? = null
    private lateinit var mediaPlayer: MediaPlayer
    private val gameIntentCode = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        initialView()
        getVersionCode()
    }

    // Initialize the main page view
    private fun initialView() {
        isMusic = getBooleanSharedPref(this, gameSoundLevel)
        handleMusic(isMusic)
        isLoaded = true
        findViewById<RelativeLayout>(R.id.playSinglePlayer).setOnClickListener {
            intentHandler(GamePage(), true)
        }
        findViewById<RelativeLayout>(R.id.playMultiPlayer).setOnClickListener {
            intentHandler(GamePage(), false)
        }
        findViewById<RelativeLayout>(R.id.settings).setOnClickListener {
            intentHandler(SettingsPage(), false)
        }
    }

    // Get the game version code and display in the main page
    @SuppressLint("SetTextI18n")
    private fun getVersionCode() {
        val versionTextView = findViewById<TextView>(R.id.version)
        try {
            versionTextView.text = versionCode(this)
        } catch (e: PackageManager.NameNotFoundException) {
            versionTextView.text = resources.getString(R.string.company_name)
        }
    }

    private fun intentHandler(activity: Activity, singlePlayer: Boolean) {
        if (isLoaded) {
            isLoaded = false
            handleMusic(false)
            tapSoundUtil(this)
            val intent = Intent(this, activity.javaClass)
            intent.putExtra("singlePlayer", singlePlayer)
            intent.putExtra("isMusic", isMusic)
            this.startActivityForResult(intent, gameIntentCode)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private fun handleMusic(isPlay: Boolean) {
        if (isPlay) {
            mediaPlayer = MediaPlayer.create(this, R.raw.bg_music)
            mediaPlayer.isLooping = true
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        } else {
            try {
                mediaPlayer.stop()
            } catch (_: Exception) {
            }
        }
    }
//    private fun handleMusic() {
//        if (!serviceBound) {
//            val playerIntent = Intent(this, MusicService::class.java)
//            startService(playerIntent)
//            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
//        } else {
//            //Service is active
//            //Send a broadcast to the service -> PLAY_NEW_AUDIO
//            val broadcastPlayNewAudio = "${this.packageName}.PlayNewAudio";
//            val broadcastIntent = Intent(broadcastPlayNewAudio)
//            sendBroadcast(broadcastIntent)
//        }
//    }

//    private val serviceConnection: ServiceConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName, service: IBinder) {
//            // We've bound to LocalService, cast the IBinder and get LocalService instance
//            val binder: MusicService.LocalBinder = service as MusicService.LocalBinder
//            musicService = binder.service
//            serviceBound = true
//        }
//
//        override fun onServiceDisconnected(name: ComponentName) {
//            serviceBound = false
//        }
//    }

    override fun onResume() {
        super.onResume()
        isMusic = getBooleanSharedPref(this, gameSoundLevel)
        if (isMusic && !mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isMusic && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == gameIntentCode) {
            isMusic = getBooleanSharedPref(this, gameSoundLevel)
            handleMusic(false)
            handleMusic(isMusic)
            val countDownTimer: Long = 1500
            Handler(Looper.getMainLooper()).postDelayed({
                isLoaded = true
            }, countDownTimer)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.exit_dialog_layout)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.show()
        val exit = dialog.findViewById<Button>(R.id.yes_button)
        val dismiss = dialog.findViewById<Button>(R.id.no_button)
        exit.setOnClickListener {
            finish()
        }
        dismiss.setOnClickListener { dialog.dismiss() }
    }
}