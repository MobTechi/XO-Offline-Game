package com.mobtech.xo_offline_game.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mobtech.xo_offline_game.R
import com.mobtech.xo_offline_game.Utils.Utils
import com.mobtech.xo_offline_game.service.SoundService

@Suppress("DEPRECATION")
class MainPage : AppCompatActivity() {

    // Values
    private val musicPref = "musicPref"
    private val policyURL = "https://tamilandroo.web.app/xo-offline-game/privacy-policy"

    // Properties
    private var isMusic: Boolean = true
    private lateinit var musicToggle: ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private val gameIntentCode = 111

    // Injection
    private val utils = Utils()
    private val soundService = SoundService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        initialView()
        getVersionCode()
    }

    private fun initialView() {
        isMusic = utils.getPref(this, musicPref)
        handleMusic(isMusic)
        musicToggle = findViewById(R.id.musicToggle)
        setIconToImageView(isMusic, musicToggle, R.drawable.music_on_ic, R.drawable.music_off_ic)

        musicToggle.setOnClickListener {
            isMusic = !isMusic
            setIconToImageView(isMusic, musicToggle, R.drawable.music_on_ic, R.drawable.music_off_ic)
            handleMusic(isMusic)
            utils.setPref(this, musicPref, isMusic)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getVersionCode() {
        val versionTextView = findViewById<TextView>(R.id.version)
        try {
            val version = packageManager.getPackageInfo(packageName, 0).versionName
            versionTextView.text = "V $version"
        } catch (e: PackageManager.NameNotFoundException) {
            versionTextView.text = resources.getString(R.string.company_name)
        }
    }

    fun playSinglePlayer(@Suppress("UNUSED_PARAMETER") view: View) {
        tapSound()
        val playWithComputerIntent = Intent(this, GamePage::class.java)
        playWithComputerIntent.putExtra("type", resources.getString(R.string.single_player))
        playWithComputerIntent.putExtra("isMusic", isMusic)
        this.startActivityForResult(playWithComputerIntent, gameIntentCode)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun playMultiPlayer(@Suppress("UNUSED_PARAMETER") view: View) {
        tapSound()
        val playWithFriendIntent = Intent(this, GamePage::class.java)
        playWithFriendIntent.putExtra("type", resources.getString(R.string.multi_player))
        playWithFriendIntent.putExtra("isMusic", isMusic)
        this.startActivityForResult(playWithFriendIntent, gameIntentCode)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun privacyPolicy(@Suppress("UNUSED_PARAMETER") view: View) {
        tapSound()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.privacy_policy)
        builder.setMessage(R.string.privacy_description)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setPositiveButton(R.string.yes) { dialogInterface, _ ->
            dialogInterface.dismiss()
            try {
                val policyIntent = Intent(Intent.ACTION_VIEW, Uri.parse(policyURL))
                ContextCompat.startActivity(this, policyIntent, null)
            } catch (e: Exception) {
                showPrivacyPolicyURL()
            }
        }
        builder.setNegativeButton(R.string.no) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showPrivacyPolicyURL() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.privacy_policy)
        builder.setMessage("${getString(R.string.no_browser_found)}\n\nPrivacy Policy URL: $policyURL")
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setPositiveButton(R.string.ok) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun tapSound() {
        soundService.tapSound()
    }

    private fun handleMusic(isPlay: Boolean) {
        if (isPlay) {
            mediaPlayer = MediaPlayer.create(this, R.raw.bg_music)
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        } else {
            try {
                mediaPlayer.stop()
            } catch (i: Exception) {
            }
        }
    }

    private fun setIconToImageView(
        isTrue: Boolean,
        imageView: ImageView,
        image1: Int,
        image2: Int,
    ) {
        if (isTrue) {
            imageView.setImageResource(image1)
        } else {
            imageView.setImageResource(image2)
        }
    }

    override fun onResume() {
        super.onResume()
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
            handleMusic(false)
            handleMusic(isMusic)
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.exit)
        builder.setMessage(R.string.exit_confirmation)
        builder.setIcon(R.mipmap.ic_launcher)

        builder.setPositiveButton(R.string.yes) { dialogInterface, _ ->
            dialogInterface.dismiss()
            handleMusic(false)
            finish()
        }
        builder.setNegativeButton(R.string.no) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}