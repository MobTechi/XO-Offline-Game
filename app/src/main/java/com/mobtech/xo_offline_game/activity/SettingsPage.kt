package com.mobtech.xo_offline_game.activity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mobtech.xo_offline_game.R
import com.mobtech.xo_offline_game.Utils.CommonString.cancel
import com.mobtech.xo_offline_game.Utils.CommonString.confirm
import com.mobtech.xo_offline_game.Utils.CommonString.gameLevel
import com.mobtech.xo_offline_game.Utils.CommonString.gameSoundLevel
import com.mobtech.xo_offline_game.Utils.CommonString.no
import com.mobtech.xo_offline_game.Utils.CommonString.no_browser_found
import com.mobtech.xo_offline_game.Utils.CommonString.ok
import com.mobtech.xo_offline_game.Utils.CommonString.policyURL
import com.mobtech.xo_offline_game.Utils.CommonString.privacy_description
import com.mobtech.xo_offline_game.Utils.CommonString.privacy_policy
import com.mobtech.xo_offline_game.Utils.CommonString.yes
import com.mobtech.xo_offline_game.Utils.CommonUtil.getBooleanSharedPref
import com.mobtech.xo_offline_game.Utils.CommonUtil.getDiffLabel
import com.mobtech.xo_offline_game.Utils.CommonUtil.getIntSharedPref
import com.mobtech.xo_offline_game.Utils.CommonUtil.setBooleanSharedPref
import com.mobtech.xo_offline_game.Utils.CommonUtil.setIntSharedPref
import com.mobtech.xo_offline_game.Utils.CommonUtil.versionCode

@Suppress("UNUSED_PARAMETER")
class SettingsPage : AppCompatActivity() {

    // Properties
    private lateinit var dif: TextView
    private var gameDifLevel: Int = 0
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
        gameDifLevel = getIntSharedPref(this, gameLevel)
        isMusic = getBooleanSharedPref(this, gameSoundLevel)
        dif = findViewById(R.id.dif)
        dif.text = getDiffLabel(gameDifLevel)
        handleGameSoundSwitch()
    }

    // This method trigger when click the game ratting
    fun difficulty(view: View) {
        val choices = arrayOf(" Easy", " Medium", " Hard")
        val builder = android.app.AlertDialog.Builder(this, R.style.difficultyDialogTheme)
            .setTitle("Difficulty")
            .setPositiveButton(confirm) { dialog, _ ->
                gameDifLevel = getIntSharedPref(this, gameLevel)
                dif.text = getDiffLabel(gameDifLevel)
                dialog.dismiss()
            }
            .setNegativeButton(cancel) { dialog, _ -> dialog.dismiss() }
            .setSingleChoiceItems(choices, gameDifLevel) { _, itemPosition ->
                if (itemPosition > -1 && itemPosition < 3) {
                    setIntSharedPref(this, gameLevel, itemPosition)
                }
            }
        val alertDialog = builder.create()
        alertDialog.show()
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
        val uri = Uri.parse("market://details?id=" + applicationContext.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                )
            )
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