package com.mobtech.xo_offline_game.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.mobtech.xo_offline_game.R
import com.mobtech.xo_offline_game.service.SoundService
import kotlin.random.Random

class GamePage : AppCompatActivity() {
    // Properties
    private var isMusic: Boolean = true
    private lateinit var mediaPlayer: MediaPlayer

    private var status: TextView? = null
    private var isSinglePayerMatch = false
    private var isComputerPlayed = true
    private var gameActive = true

    // Player representation
    // 0 - X
    // 1 - O
    private var activePlayer = 0
    private var counter = 0
    private var xScore = 0
    private var oScore = 0
    private var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
    private var availablePosition: MutableList<Int> = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)


    // State meanings:
    //    0 - X
    //    1 - O
    //    2 - Null
    // put all win positions in a 2D array
    private var winPositions = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )

    // Injections
    private val soundService = SoundService(this)

    // this function will be called every time a
    // players tap in an empty box of the grid
    fun playerTap(view: View) {
        if ((activePlayer == 0) && isSinglePayerMatch) {
            isComputerPlayed = false
            val img = view as ImageView
            val tappedImage = img.tag.toString().toInt()

            // if the tapped image is empty
            if (gameState[tappedImage] == 2) {
                // increase the counter
                // after every tap
                counter++

                // check if its the last box
                if (counter == 9) {
                    // reset the game
                    gameActive = false
                }

                // mark this position
                gameState[tappedImage] = activePlayer
                activePlayer = 1

                // this will give a motion
                // effect to the image
                tapSound()
                img.translationY = -1000f
                img.setImageResource(R.drawable.x)
                status?.text = getString(R.string.o_turn)
                img.animate().translationYBy(1000f).duration = 300
                checkGameStatus()

                Handler(Looper.getMainLooper()).postDelayed({
                    if (gameActive) {
                        computerPlay(tappedImage)
                    }
                }, 500)
            } else {
                isComputerPlayed = true
            }
        } else {
            val img = view as ImageView
            val tappedImage = img.tag.toString().toInt()

            // if the tapped image is empty
            if (gameState[tappedImage] == 2) {
                // increase the counter
                // after every tap
                counter++

                // check if its the last box
                if (counter == 9) {
                    // reset the game
                    gameActive = false
                }

                // mark this position
                gameState[tappedImage] = activePlayer

                // this will give a motion
                // effect to the image
                img.translationY = -1000f

                // change the active player
                // from 0 to 1 or 1 to 0
                tapSound()
                if (activePlayer == 0) {
                    // set the image of x
                    img.setImageResource(R.drawable.x)
                    activePlayer = 1

                    // change the status
                    status?.text = getString(R.string.o_turn)
                } else {
                    // set the image of o
                    img.setImageResource(R.drawable.o)
                    activePlayer = 0
                    val status = findViewById<TextView>(R.id.status)

                    // change the status
                    status.text = getString(R.string.x_turn)
                }
                img.animate().translationYBy(1000f).duration = 300
            }
            checkGameStatus()
        }
    }

    private fun computerPlay(tappedImage: Int) {
        availablePosition.remove(tappedImage)
        val randomClick = Random.nextInt(availablePosition.size)
        val computerMove = availablePosition[randomClick]

        val img = findViewById<ImageView>(R.id.imageView + computerMove + 1)
        // if the tapped image is empty
        if (gameState[computerMove] == 2) {
            // increase the counter
            // after every tap
            counter++
            availablePosition.remove(computerMove)

            // check if its the last box
            if (counter == 9) {
                // reset the game
                gameActive = false
            }

            // mark this position
            gameState[computerMove] = activePlayer
            activePlayer = 0

            // this will give a motion
            // effect to the image
            tapSound()
            img.translationY = -1000f
            img.setImageResource(R.drawable.o)
            status?.text = getString(R.string.x_turn)
            img.animate().translationYBy(1000f).duration = 300
            checkGameStatus()
        }
        isComputerPlayed = true
    }

    private fun checkGameStatus() {
        var flag = 0
        // Check if any player has won
        for (winPosition in winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] && gameState[winPosition[1]] == gameState[winPosition[2]] && gameState[winPosition[0]] != 2) {
                flag = 1
                // game reset function be called
                gameActive = false
                val winnerStr: String = if (gameState[winPosition[0]] == 0)
                    getString(R.string.x_won) else
                    getString(R.string.o_won)

                // Update the status bar for winner announcement
                val status = findViewById<TextView>(R.id.status)
                status.text = winnerStr

                // game reset function will be called
                // if someone wins or the boxes are full
                if (!gameActive) {
                    gameRestartDialog(status.text.toString())
                }
            }
        }
        // set the status if the match draw
        if (counter == 9 && flag == 0) {
            val status = findViewById<TextView>(R.id.status)
            status.text = getString(R.string.match_draw)
            gameRestartDialog(getString(R.string.match_draw))
        }
    }

    // game reset dialog
    private fun gameRestartDialog(message: String) {
        val restartDialogView =
            LayoutInflater.from(this).inflate(R.layout.game_over_dialog, null)

        //AlertDialogBuilder
        val restartDialogBuilder =
            AlertDialog.Builder(this, R.style.CustomDialog).setView(restartDialogView)
        restartDialogBuilder.setCancelable(false)

        val playerIcon = restartDialogView.findViewById<ImageView>(R.id.player_icon)
        val homeButton = restartDialogView.findViewById<CardView>(R.id.home_btn)
        val playAgainButton = restartDialogView.findViewById<CardView>(R.id.play_again)
        val drawText = restartDialogView.findViewById<TextView>(R.id.match_draw)

        playerIcon.visibility = View.GONE
        drawText.text = message

        if (message != getString(R.string.match_draw)) {
            playerIcon.visibility = View.VISIBLE
            if (message == getString(R.string.x_won)) {
                xScore++
                (findViewById<View>(R.id.x_score) as TextView).text = xScore.toString()
                playerIcon.setImageResource(R.drawable.x)
            } else {
                oScore++
                (findViewById<View>(R.id.o_score) as TextView).text = oScore.toString()
                playerIcon.setImageResource(R.drawable.o)
            }
        }

        //show dialog
        val restartDialog = restartDialogBuilder.show()

        //set Listener
        homeButton.setOnClickListener {
            //close dialog
            restartDialog.dismiss()
            finish()
        }
        playAgainButton.setOnClickListener {
            //close dialog
            restartDialog.dismiss()
            gameReset()
        }
    }

    // reset the game
    private fun gameReset() {
        gameActive = true
        activePlayer = 0
        counter = 0
        for (i in gameState.indices) {
            gameState[i] = 2
        }
        isComputerPlayed = true
        availablePosition = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        // remove all the images from the boxes inside the grid
        (findViewById<View>(R.id.imageView0) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView1) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView2) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView3) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView4) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView5) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView6) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView7) as ImageView).setImageResource(0)
        (findViewById<View>(R.id.imageView8) as ImageView).setImageResource(0)
        val status = findViewById<TextView>(R.id.status)
        status.text = getString(R.string.x_turn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_page)

        val gameType = intent.getStringExtra("type").toString()
        isMusic = intent.getBooleanExtra("isMusic", false)
        handleMusic(isMusic)
        isSinglePayerMatch = gameType == resources.getString(R.string.single_player)

        status = findViewById(R.id.status)
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

    private fun tapSound() {
        soundService.tapSound()
    }

    override fun onBackPressed() {
        mainMenuDialog()
    }

    private fun mainMenuDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.main_menu)
        builder.setMessage(R.string.main_menu_confirmation)

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