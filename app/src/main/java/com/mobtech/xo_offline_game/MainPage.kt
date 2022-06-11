package com.mobtech.xo_offline_game

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        getVersionCode()
    }

    private fun getVersionCode() {
        val versionTextView = findViewById<TextView>(R.id.version)
        try {
            val version = packageManager.getPackageInfo(packageName, 0).versionName
            versionTextView.text = version
        } catch (e: PackageManager.NameNotFoundException) {
            versionTextView.text = resources.getString(R.string.company_name)
        }
    }

    fun playSinglePlayer(@Suppress("UNUSED_PARAMETER") view: View) {
        tapSound()
        val playWithComputerIntent = Intent(this, GamePage::class.java)
        playWithComputerIntent.putExtra("type", resources.getString(R.string.single_player))
        this.startActivity(playWithComputerIntent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun playMultiPlayer(@Suppress("UNUSED_PARAMETER") view: View) {
        tapSound()
        val playWithFriendIntent = Intent(this, GamePage::class.java)
        playWithFriendIntent.putExtra("type", resources.getString(R.string.multi_player))
        this.startActivity(playWithFriendIntent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun tapSound() {
        val soundService = SoundService()
        soundService.tapSound()
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.exit)
        builder.setMessage(R.string.exit_confirmation)
        builder.setIcon(R.mipmap.ic_launcher)

        builder.setPositiveButton(R.string.yes) { dialogInterface, _ ->
            dialogInterface.dismiss()
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