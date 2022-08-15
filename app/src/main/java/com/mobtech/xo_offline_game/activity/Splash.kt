package com.mobtech.xo_offline_game.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mobtech.xo_offline_game.R
import com.mobtech.xo_offline_game.Utils.Utils

class Splash : AppCompatActivity() {

    // Values
    private val splash: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val splash = Intent(this, MainPage::class.java)
            this.startActivity(splash)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

            // close this activity
            finish()
        }, splash)
    }
}