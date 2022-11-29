package com.mobtech.xo_offline_game.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.play.core.review.ReviewManagerFactory
import com.mobtech.xo_offline_game.R
import com.mobtech.xo_offline_game.Utils.CommonString.gameSoundLevel
import com.mobtech.xo_offline_game.Utils.CommonString.no
import com.mobtech.xo_offline_game.Utils.CommonString.no_browser_found
import com.mobtech.xo_offline_game.Utils.CommonString.ok
import com.mobtech.xo_offline_game.Utils.CommonString.policyURL
import com.mobtech.xo_offline_game.Utils.CommonString.privacy_description
import com.mobtech.xo_offline_game.Utils.CommonString.privacy_policy
import com.mobtech.xo_offline_game.Utils.CommonString.yes
import com.mobtech.xo_offline_game.Utils.CommonUtil.getBooleanSharedPref
import com.mobtech.xo_offline_game.Utils.CommonUtil.setBooleanSharedPref
import com.mobtech.xo_offline_game.Utils.CommonUtil.versionCode


@Suppress("UNUSED_PARAMETER")
class SettingsPage : AppCompatActivity() {

    // Properties
    private var isMusic: Boolean = true
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_page)
        initialView()
        getVersionCode()
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

    // Initialize the main page view
    private fun initialView() {
        isMusic = getBooleanSharedPref(this, gameSoundLevel)
        handleGameSoundSwitch()
    }

    // This method trigger to handle the game sound
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun handleGameSoundSwitch() {
        val gameSoundSwitch: Switch = findViewById(R.id.gameSoundSwitch)
        gameSoundSwitch.isChecked = isMusic
        handleMusic(isMusic)
        gameSoundSwitch.setOnClickListener {
            isMusic = !isMusic
            handleMusic(isMusic)
            setBooleanSharedPref(this, gameSoundLevel, isMusic)
        }
    }

    // This method trigger when click the game ratting
    fun ratting(view: View) {
        val reviewManager = ReviewManagerFactory.create(this)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result
                reviewManager.launchReviewFlow(this, reviewInfo)
            }
        }
        request.addOnFailureListener {
            Toast.makeText(this, "Something wrong. Please Try Again!", Toast.LENGTH_SHORT).show()
        }
    }

    // This method trigger when click the game privacy policy
    fun privacyPolicyClick(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(privacy_policy)
        builder.setMessage(privacy_description)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setPositiveButton(yes) { dialogInterface, _ ->
            dialogInterface.dismiss()
            try {
                val policyIntent = Intent(Intent.ACTION_VIEW, Uri.parse(policyURL))
                ContextCompat.startActivity(this, policyIntent, null)
            } catch (e: Exception) {
                showPrivacyPolicyURL()
            }
        }
        builder.setNegativeButton(no) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showPrivacyPolicyURL() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(privacy_policy)
        builder.setMessage("${no_browser_found}\n\nPrivacy Policy URL: $policyURL")
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setPositiveButton(ok) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    // This method is used to handle the music media player
    private fun handleMusic(isPlay: Boolean) {
        setIconToImageView(isMusic, R.drawable.ic_music_on, R.drawable.ic_music_off)
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

    // Set the game audio icon based on on/off
    private fun setIconToImageView(
        isTrue: Boolean,
        image1: Int,
        image2: Int,
    ) {
        val imageView: ImageView = findViewById(R.id.gameSoundImage)
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

    fun backBtnClick(view: View) {
        finish()
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}