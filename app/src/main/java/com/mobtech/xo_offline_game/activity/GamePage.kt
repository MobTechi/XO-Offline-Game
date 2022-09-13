package com.mobtech.xo_offline_game.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobtech.xo_offline_game.R
import com.mobtech.xo_offline_game.Utils.CommonUtil.tapSoundUtil
import java.util.*

class GamePage : AppCompatActivity() {
    // Properties
    private var isMusic: Boolean = true
    private lateinit var mediaPlayer: MediaPlayer

    private var flag = 0
    private var isX = 10
    private var isO = 1
    private var win = 0
    private var i = 0
    private var game = 1
    private var sumValue = 0
    private var ctrflag = 0
    private var resetchecker = 1
    private var currentgamedonechecker = 0
    private var score1 = 0
    private var score2 = 0
    private var drawChecker = 0
    private var sum = IntArray(8)
    private var selectedSinglePlayer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_page)
        selectedSinglePlayer = intent.getBooleanExtra("singlePlayer", false)
        isMusic = intent.getBooleanExtra("isMusic", false)
        handleMusic(isMusic)
        val gameLevelText = findViewById<TextView>(R.id.gameLevel)
        Toast.makeText(this, getString(R.string.x_turn), Toast.LENGTH_SHORT).show()
        gameLevelText.text = if (selectedSinglePlayer) getString(R.string.single_player) else getString(R.string.multi_player)
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

    fun playerTap(view: View) {
        val img = view as ImageView
        tapSoundUtil(this)
        when (img.tag.toString().toInt()) {
            0 -> pos0()
            1 -> pos1()
            2 -> pos2()
            3 -> pos3()
            4 -> pos4()
            5 -> pos5()
            6 -> pos6()
            7 -> pos7()
            8 -> pos8()
        }
    }

    private fun pos0() {
        if (win == 0 && buttonpressed[0][0] == 0) {
            if (flag % 2 == 0) tracker[0][0] = isX else tracker[0][0] = isO
            printBoard()
            winChecker()
            cpuPlay()
            flag++
            buttonpressed[0][0]++
        }
    }

    private fun pos1() {
        if (win == 0 && buttonpressed[0][1] == 0) {
            if (flag % 2 == 0) tracker[0][1] = isX else tracker[0][1] = isO
            printBoard()
            winChecker()
            cpuPlay()
            buttonpressed[0][1]++
            flag++
        }
    }

    private fun pos2() {
        if (win == 0 && buttonpressed[0][2] == 0) {
            if (flag % 2 == 0) tracker[0][2] = isX else tracker[0][2] = isO
            printBoard()
            winChecker()
            cpuPlay()
            buttonpressed[0][2]++
            flag++
        }
    }

    private fun pos3() {
        if (win == 0 && buttonpressed[1][0] == 0) {
            if (flag % 2 == 0) tracker[1][0] = isX else tracker[1][0] = isO
            printBoard()
            winChecker()
            cpuPlay()
            ++buttonpressed[1][0]
            flag++
        }
    }

    private fun pos4() {
        if (win == 0 && buttonpressed[1][1] == 0) {
            if (flag % 2 == 0) tracker[1][1] = isX else tracker[1][1] = isO
            printBoard()
            winChecker()
            cpuPlay()
            ++buttonpressed[1][1]
            flag++
        }
    }

    private fun pos5() {
        if (win == 0 && buttonpressed[1][2] == 0) {
            if (flag % 2 == 0) tracker[1][2] = isX else tracker[1][2] = isO
            printBoard()
            winChecker()
            cpuPlay()
            ++buttonpressed[1][2]
            flag++
        }
    }

    private fun pos6() {
        if (win == 0 && buttonpressed[2][0] == 0) {
            if (flag % 2 == 0) tracker[2][0] = isX else tracker[2][0] = isO
            printBoard()
            winChecker()
            cpuPlay()
            ++buttonpressed[2][0]
            flag++
        }
    }

    private fun pos7() {
        if (win == 0 && buttonpressed[2][1] == 0) {
            if (flag % 2 == 0) tracker[2][1] = isX else tracker[2][1] = isO
            printBoard()
            winChecker()
            cpuPlay()
            ++buttonpressed[2][1]
            flag++
        }
    }

    private fun pos8() {
        if (win == 0 && buttonpressed[2][2] == 0) {
            if (flag % 2 == 0) tracker[2][2] = isX else tracker[2][2] = isO
            printBoard()
            winChecker()
            cpuPlay()
            ++buttonpressed[2][2]
            flag++
        }
    }

    @Suppress("ControlFlowWithEmptyBody")
    private fun cpuPlay() {
        if (selectedSinglePlayer && win == 0) {
            val handler = Handler(Looper.getMainLooper())
            val t = Timer()
            t.schedule(object : TimerTask() {
                override fun run() {
                    handler.post { //add com to be executed after a pause
                        if (ifCpuWin()) ; else if (ifOppWin()) ; else if (emptyCentre()) ; else if (emptyCorner()) ; else emptyAny()
                        printBoard()
                        winChecker()
                        flag++
                    }
                }
            }, 110)
            return
        }
    }

    fun ifCpuWin(): Boolean {
        i = 0
        while (i < 8) {
            if (sum[i] == 2 * isO) {
                if (i == 0) {
                    for (x in 0..2) if (tracker[0][x] == 0) tracker[0][x] = isO
                }
                if (i == 1) {
                    for (x in 0..2) if (tracker[1][x] == 0) tracker[1][x] = isO
                }
                if (i == 2) {
                    for (x in 0..2) if (tracker[2][x] == 0) tracker[2][x] = isO
                }
                if (i == 3) {
                    for (x in 0..2) if (tracker[x][0] == 0) tracker[x][0] = isO
                }
                if (i == 4) {
                    for (x in 0..2) if (tracker[x][1] == 0) tracker[x][1] = isO
                }
                if (i == 5) {
                    for (x in 0..2) if (tracker[x][2] == 0) tracker[x][2] = isO
                }
                if (i == 6) {
                    for (y in 0..2) for (x in 0..2) if (x == y) if (tracker[x][y] == 0) tracker[x][y] = isO
                }
                if (i == 7) {
                    if (tracker[0][2] == 0) tracker[0][2] = isO else if (tracker[1][1] == 0) tracker[1][1] = isO else tracker[2][0] =
                        isO
                }
                return true
            }
            i++
        }
        return false
    }

    fun ifOppWin(): Boolean {
        i = 0
        while (i < 8) {
            if (sum[i] == 2 * isX) {
                if (i == 0) {
                    for (x in 0..2) if (tracker[0][x] == 0) {
                        tracker[0][x] = isO
                        buttonpressed[0][x]++
                    }
                }
                if (i == 1) {
                    for (x in 0..2) if (tracker[1][x] == 0) {
                        tracker[1][x] = isO
                        buttonpressed[1][x]++
                    }
                }
                if (i == 2) {
                    for (x in 0..2) if (tracker[2][x] == 0) {
                        tracker[2][x] = isO
                        buttonpressed[2][x]++
                    }
                }
                if (i == 3) {
                    for (x in 0..2) if (tracker[x][0] == 0) {
                        tracker[x][0] = isO
                        buttonpressed[x][0]++
                    }
                }
                if (i == 4) {
                    for (x in 0..2) if (tracker[x][1] == 0) {
                        tracker[x][1] = isO
                        buttonpressed[x][1]++
                    }
                }
                if (i == 5) {
                    for (x in 0..2) if (tracker[x][2] == 0) {
                        tracker[x][2] = isO
                        buttonpressed[x][2]++
                    }
                }
                if (i == 6) {
                    for (y in 0..2) for (x in 0..2) if (x == y) if (tracker[x][y] == 0) {
                        tracker[x][y] = isO
                        buttonpressed[x][y]++
                    }
                }
                if (i == 7) {
                    if (tracker[0][2] == 0) {
                        tracker[0][2] = isO
                        buttonpressed[0][2]++
                    } else if (tracker[1][1] == 0) {
                        tracker[1][1] = isO
                        buttonpressed[1][1]++
                    } else {
                        tracker[2][0] = isO
                        buttonpressed[2][0]++
                    }
                }
                return true
            }
            i++
        }
        return false
    }

    fun emptyCentre(): Boolean {
        if (tracker[1][1] == 0) {
            tracker[1][1] = isO
            buttonpressed[1][1]++
            return true
        }
        return false
    }

    fun emptyCorner(): Boolean {
        if (tracker[0][0] + tracker[2][2] == 2 * isX || tracker[0][2] + tracker[2][0] == 2 * isX) {
            for (k in 0..2) for (j in 0..2) if ((k + j) % 2 == 1) {
                if (tracker[k][j] == 0) tracker[k][j] = isO
                buttonpressed[k][j]++
                return true
            }
        }
        if (sum[6] == isO || sum[7] == isO) {
            if (sum[6] == isO) {
                if (sum[0] + sum[3] > sum[2] + sum[5]) {
                    tracker[0][0] = isO
                    buttonpressed[0][0]++
                } else {
                    tracker[2][2] = isO
                    buttonpressed[2][2]++
                }
                return true
            }
            if (sum[7] == isO) {
                if (sum[0] + sum[5] > sum[3] + sum[2]) {
                    tracker[0][2] = isO
                    buttonpressed[0][2]++
                } else {
                    tracker[2][0] = isO
                    buttonpressed[2][0]++
                }
                return true
            }
        }
        for (i in 0..2) {
            if (tracker[0][i] == isX) {
                if (tracker[0][0] == 0) {
                    tracker[0][0] = isO
                    buttonpressed[0][0]++
                    return true
                }
                if (tracker[0][2] == 0) {
                    tracker[0][2] = isO
                    buttonpressed[0][2]++
                    return true
                }
            }
        }
        for (i in 0..2) {
            if (tracker[2][i] == isX) {
                if (tracker[2][0] == 0) {
                    tracker[2][0] = isO
                    buttonpressed[2][0]++
                    return true
                }
                if (tracker[2][2] == 0) {
                    tracker[2][2] = isO
                    buttonpressed[2][2]++
                    return true
                }
            }
        }
        for (i in 0..2) {
            if (tracker[i][0] == isX) {
                if (tracker[0][0] == 0) {
                    tracker[0][0] = isO
                    buttonpressed[0][0]++
                    return true
                }
                if (tracker[2][0] == 0) {
                    tracker[2][0] = isO
                    buttonpressed[2][0]++
                    return true
                }
            }
        }
        for (i in 0..2) {
            if (tracker[i][2] == isX) {
                if (tracker[0][2] == 0) {
                    tracker[0][2] = isO
                    buttonpressed[0][2]++
                    return true
                }
                if (tracker[2][2] == 0) {
                    tracker[2][2] = isO
                    buttonpressed[2][2]++
                    return true
                }
            }
        }
        return false
    }

    fun emptyAny() {
        if (ctrflag == 0) while (true) {
            val x = rand()
            val y = rand()
            if (tracker[x][y] == 0) {
                tracker[x][y] = isO
                buttonpressed[x][y]++
                return
            }
        }
        for (x in 0..2) for (y in 0..2) if (tracker[x][y] == 0) {
            tracker[x][y] = isO
            buttonpressed[x][y]++
            return
        }
    }

    private fun rand(): Int {
        return Random().nextInt(3)
    }

    fun printBoard() {
        val q1: ImageView = findViewById<View>(R.id.imageView0) as ImageView
        val q2: ImageView = findViewById<View>(R.id.imageView1) as ImageView
        val q3: ImageView = findViewById<View>(R.id.imageView2) as ImageView
        val q4: ImageView = findViewById<View>(R.id.imageView3) as ImageView
        val q5: ImageView = findViewById<View>(R.id.imageView4) as ImageView
        val q6: ImageView = findViewById<View>(R.id.imageView5) as ImageView
        val q7: ImageView = findViewById<View>(R.id.imageView6) as ImageView
        val q8: ImageView = findViewById<View>(R.id.imageView7) as ImageView
        val q9: ImageView = findViewById<View>(R.id.imageView8) as ImageView
        val xIcon = R.drawable.x
        val oIcon = R.drawable.o
        if (tracker[0][0] == 1) q1.setImageResource(oIcon)
        if (tracker[0][0] == 10) q1.setImageResource(xIcon)
        if (tracker[0][1] == 1) q2.setImageResource(oIcon)
        if (tracker[0][1] == 10) q2.setImageResource(xIcon)
        if (tracker[0][2] == 1) q3.setImageResource(oIcon)
        if (tracker[0][2] == 10) q3.setImageResource(xIcon)
        if (tracker[1][0] == 1) q4.setImageResource(oIcon)
        if (tracker[1][0] == 10) q4.setImageResource(xIcon)
        if (tracker[1][1] == 1) q5.setImageResource(oIcon)
        if (tracker[1][1] == 10) q5.setImageResource(xIcon)
        if (tracker[1][2] == 1) q6.setImageResource(oIcon)
        if (tracker[1][2] == 10) q6.setImageResource(xIcon)
        if (tracker[2][0] == 1) q7.setImageResource(oIcon)
        if (tracker[2][0] == 10) q7.setImageResource(xIcon)
        if (tracker[2][1] == 1) q8.setImageResource(oIcon)
        if (tracker[2][1] == 10) q8.setImageResource(xIcon)
        if (tracker[2][2] == 1) q9.setImageResource(oIcon)
        if (tracker[2][2] == 10) q9.setImageResource(xIcon)
        resetchecker++
    }

    @SuppressLint("SetTextI18n")
    fun winChecker() {
        ctrflag++
        sum[0] = tracker[0][0] + tracker[0][1] + tracker[0][2]
        sum[1] = tracker[1][0] + tracker[1][1] + tracker[1][2]
        sum[2] = tracker[2][0] + tracker[2][1] + tracker[2][2]
        sum[3] = tracker[0][0] + tracker[1][0] + tracker[2][0]
        sum[4] = tracker[0][1] + tracker[1][1] + tracker[2][1]
        sum[5] = tracker[0][2] + tracker[1][2] + tracker[2][2]
        sum[6] = tracker[0][0] + tracker[1][1] + tracker[2][2]
        sum[7] = tracker[0][2] + tracker[1][1] + tracker[2][0]
        val wins = BooleanArray(1)
        val returns = BooleanArray(1)
        currentgamedonechecker++
        resetchecker++
        for (i in 0..7) if (sum[i] == 3 || sum[i] == 30) {
            win++
            if (sum[i] == 3 && isX == 1) {
                score1++
                val q1 = findViewById<View>(R.id.x_score) as TextView
                wins[0] = true
                returns[0] = false
                if (wins[0]) {
                    wins[0] = false
                    returns[0] = true
                }
                val finalReturns = returns[0]
                Handler(Looper.getMainLooper()).postDelayed({
                    if (finalReturns) {
                        wins[0] = false
                        returns[0] = true
                    }
                }, 300)
                q1.text = "" + score1
                Handler(Looper.getMainLooper()).postDelayed({ playMore() }, 500)
            }
            if (sum[i] == 3 && isO == 1) {
                score2++
                val q1 = findViewById<View>(R.id.o_score) as TextView
                wins[0] = true
                returns[0] = false
                if (wins[0]) {
                    wins[0] = false
                    returns[0] = true
                }
                val finalReturns = returns[0]
                Handler(Looper.getMainLooper()).postDelayed({
                    if (finalReturns) {
                        wins[0] = false
                        returns[0] = true
                    }
                }, 500)
                q1.text = "" + score2
                Handler(Looper.getMainLooper()).postDelayed({ playMore() }, 300)
            }
            if (sum[i] == 30 && isX == 10) {
                score1++
                val q1 = findViewById<View>(R.id.x_score) as TextView
                wins[0] = true
                returns[0] = false
                if (wins[0]) {
                    wins[0] = false
                    returns[0] = true
                }
                val finalReturns = returns[0]
                Handler(Looper.getMainLooper()).postDelayed({
                    if (finalReturns) {
                        wins[0] = false
                        returns[0] = true
                    }
                }, 300)
                q1.text = "" + score1
                Handler(Looper.getMainLooper()).postDelayed({ playMore() }, 500)
            }
            if (sum[i] == 30 && isO == 10) {
                score2++
                val q1 = findViewById<View>(R.id.o_score) as TextView
                wins[0] = true
                returns[0] = false
                if (wins[0]) {
                    wins[0] = false
                    returns[0] = true
                }
                val finalReturns = returns[0]
                Handler(Looper.getMainLooper()).postDelayed({
                    if (finalReturns) {
                        wins[0] = false
                        returns[0] = true
                    }
                }, 300)
                q1.text = "" + score2
                // to asi netreba
                Handler(Looper.getMainLooper()).postDelayed({ playMore() }, 500)
            }
        }
        if (ctrflag == 9 && win == 0) {
            //  showDialog("This is a draw !", "" + score1, "" + "AI", "" + score2);
            Handler(Looper.getMainLooper()).postDelayed({ playMore() }, 900)
            Toast.makeText(this, "Match Draw", Toast.LENGTH_SHORT).show()
            drawChecker++
        }
    } //end winchecker()

    private fun playMore() {
        if (drawChecker > 0 || win > 0) {
            game++
            for (i in 0..7) sum[i] = 0
            drawChecker = 0
            val q1: ImageView = findViewById<View>(R.id.imageView0) as ImageView
            val q2: ImageView = findViewById<View>(R.id.imageView1) as ImageView
            val q3: ImageView = findViewById<View>(R.id.imageView2) as ImageView
            val q4: ImageView = findViewById<View>(R.id.imageView3) as ImageView
            val q5: ImageView = findViewById<View>(R.id.imageView4) as ImageView
            val q6: ImageView = findViewById<View>(R.id.imageView5) as ImageView
            val q7: ImageView = findViewById<View>(R.id.imageView6) as ImageView
            val q8: ImageView = findViewById<View>(R.id.imageView7) as ImageView
            val q9: ImageView = findViewById<View>(R.id.imageView8) as ImageView
            q1.setImageDrawable(null)
            q2.setImageDrawable(null)
            q3.setImageDrawable(null)
            q4.setImageDrawable(null)
            q5.setImageDrawable(null)
            q6.setImageDrawable(null)
            q7.setImageDrawable(null)
            q8.setImageDrawable(null)
            q9.setImageDrawable(null)
            for (i in 0..2) for (j in 0..2) buttonpressed[i][j] = 0
            for (i in 0..2) for (j in 0..2) tracker[i][j] = 0
            if (!selectedSinglePlayer) {
                if ((game + 1) % 2 == 0) {
                    Toast.makeText(this, getString(R.string.x_turn), Toast.LENGTH_SHORT).show()
                } else Toast.makeText(this, getString(R.string.o_turn), Toast.LENGTH_SHORT).show()
            }
            win = 0
            sumValue = 0
            ctrflag = 0
            flag = (game + 1) % 2
            currentgamedonechecker = 0
            if (selectedSinglePlayer && game % 2 == 0) cpuPlay()
        }
    }

    private fun gameReset() {
        for (i in 0..2) for (j in 0..2) tracker[i][j] = 0
        for (i in 0..2) for (j in 0..2) buttonpressed[i][j] = 0
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
        win = 0
        drawChecker = 0
        sumValue = 0
        resetchecker = 0
        ctrflag = 0
        score1 = 0
        score2 = 0
        game = 1
        flag = 0
        currentgamedonechecker = 0
        Toast.makeText(this, getString(R.string.x_turn), Toast.LENGTH_SHORT).show()
    }

    private fun showExitDialog() {
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
            this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        dismiss.setOnClickListener { dialog.dismiss() }
    }

    override fun onBackPressed() {
        showExitDialog()
    }

    companion object {
        private var tracker = Array(3) { IntArray(3) }
        private var buttonpressed = Array(3) { IntArray(3) }
    }
}