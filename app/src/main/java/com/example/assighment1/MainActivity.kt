package com.example.assighment1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.assighment1.logic.GameManager
import com.example.assighment1.logic.GameTimer
import com.example.assighment1.utilities.GameConstants
import com.example.assighment1.utilities.SignalManager
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private val ROWS = 5
    private val COLS = 3

    private lateinit var hotdogs: Array<Array<ImageView>>
    private lateinit var cars: Array<ImageView>
    private lateinit var hearts: Array<ImageView>

    private lateinit var btnLeft: MaterialButton
    private lateinit var btnRight: MaterialButton

    private lateinit var gameManager: GameManager
    private lateinit var gameTimer: GameTimer
    private var pendingRestart: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SignalManager.init(applicationContext)
        gameManager = GameManager(ROWS, COLS, 3)

        initHotdogsMatrix()
        initCarsArray()
        initHeartsArray()
        initButtons()
        startNewGame()

        gameTimer = GameTimer(GameConstants.Timer.DELAY) {
            gameTick()
        }
    }

    override fun onStart() {
        super.onStart()
        gameTimer.start()
    }

    override fun onPause() {
        super.onPause()
        gameTimer.stop()
    }

    override fun onResume() {
        super.onResume()
        gameTimer.start()
    }

    private fun initHotdogsMatrix() {
        hotdogs = arrayOf(
            arrayOf(
                findViewById(R.id.imgHotdog00),
                findViewById(R.id.imgHotdog01),
                findViewById(R.id.imgHotdog02),
            ),
            arrayOf(
                findViewById(R.id.imgHotdog10),
                findViewById(R.id.imgHotdog11),
                findViewById(R.id.imgHotdog12),
            ),
            arrayOf(
                findViewById(R.id.imgHotdog20),
                findViewById(R.id.imgHotdog21),
                findViewById(R.id.imgHotdog22),
            ),
            arrayOf(
                findViewById(R.id.imgHotdog30),
                findViewById(R.id.imgHotdog31),
                findViewById(R.id.imgHotdog32),
            ),
            arrayOf(
                findViewById(R.id.imgHotdog40),
                findViewById(R.id.imgHotdog41),
                findViewById(R.id.imgHotdog42),
            ),
        )

        clearBoardUI()
    }

    private fun clearBoardUI() {
        for (r in 0 until ROWS) {
            for (c in 0 until COLS) {
                hotdogs[r][c].visibility = View.INVISIBLE
            }
        }
    }

    private fun initCarsArray() {
        cars = arrayOf(
            findViewById(R.id.main_IMG_car_left),
            findViewById(R.id.main_IMG_car),
            findViewById(R.id.main_IMG_car_right)
        )

        updateCarPosition()
    }

    private fun updateCarPosition() {
        val lane = gameManager.currentLane
        for (i in cars.indices) {
            cars[i].visibility = if (i == lane) View.VISIBLE else View.INVISIBLE
        }
    }

    // -------- hearts --------
    private fun initHeartsArray() {
        hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        updateHeartsUI()
    }

    private fun startNewGame() {
        gameManager.resetGame()
        clearBoardUI()
        updateHeartsUI()
        updateCarPosition()
    }

    private fun updateHeartsUI() {
        val lives = gameManager.lives
        println("updateHeartsUI: lives=$lives")
        for (i in hearts.indices) {
            hearts[i].visibility =
                if (i < lives) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun onCrashUI() {
        print("crash")

        SignalManager
            .getInstance()
            .toast(
                "Crush!🐶",
                SignalManager.ToastLength.SHORT
            )
        SignalManager.getInstance().vibrate()
    }


    // -------- buttons --------
    private fun initButtons() {
        btnLeft = findViewById(R.id.main_BTN_left)
        btnRight = findViewById(R.id.main_BTN_right)

        btnLeft.setOnClickListener {
            gameManager.moveLeft()
            updateCarPosition()
        }

        btnRight.setOnClickListener {
            gameManager.moveRight()
            updateCarPosition()
        }
    }

    private fun gameTick() {

        if (pendingRestart) {
            startNewGame()
            pendingRestart = false
            return
        }

        val result = gameManager.tick()
        drawBoard(result.boardSnapshot)

        if (result.crashed) {
            onCrashUI()
            updateHeartsUI()
        }

        if (result.gameOver) {
            gameTimer.stop()
            val intent = Intent(this, GameOverActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun drawBoard(board: Array<BooleanArray>) {
        println("BOARD SNAPSHOT:")
        for (r in 0 until ROWS) {
            val line = buildString {
                append("row $r: ")
                for (c in 0 until COLS) {
                    append(if (board[r][c]) "1 " else "0 ")
                }
            }
            println(line)
        }

        for (r in 0 until ROWS) {
            for (c in 0 until COLS) {
                hotdogs[r][c].visibility =
                    if (board[r][c]) View.VISIBLE else View.INVISIBLE
            }
        }
    }
}
