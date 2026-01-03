package com.example.assighment1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.assighment1.logic.GameManager
import com.example.assighment1.logic.GameTimer
import com.example.assighment1.utilities.GameConstants
import com.example.assighment1.utilities.SignalManager
import com.example.assighment1.utilities.TiltDetector
import com.example.assighment1.interfaces.TiltCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.example.assighment1.utilities.SingleSoundPlayer
import com.example.assighment1.logic.RecordListManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private val ROWS = 5
    private val COLS = 5

    private lateinit var hotdogs: Array<Array<ImageView>>
    private lateinit var cars: Array<ImageView>
    private lateinit var hearts: Array<ImageView>

    private lateinit var btnLeft: MaterialButton
    private lateinit var btnRight: MaterialButton

    private lateinit var gameManager: GameManager
    private lateinit var gameTimer: GameTimer

    private var useSensors: Boolean = false
    private var gameDelay: Long = GameConstants.Timer.SLOW_DELAY

    private var tiltDetector: TiltDetector? = null
    private lateinit var main_LBL_odometer: MaterialTextView
    private lateinit var singleSoundPlayer: SingleSoundPlayer
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        bundle?.let {
            useSensors = it.getBoolean("USE_SENSORS", false)
            gameDelay = it.getLong("DELAY", GameConstants.Timer.SLOW_DELAY)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        main_LBL_odometer = findViewById(R.id.main_LBL_odometer)
        SignalManager.init(applicationContext)
        gameManager = GameManager(ROWS, COLS, 3)
        singleSoundPlayer = SingleSoundPlayer(this)

        initHotdogsMatrix()
        initCarsArray()
        initHeartsArray()
        initButtons()

        if (useSensors) {
            initTiltDetector()
            btnLeft.visibility = View.GONE
            btnRight.visibility = View.GONE
        } else {
            btnLeft.visibility = View.VISIBLE
            btnRight.visibility = View.VISIBLE
        }

        startNewGame()

        gameTimer = GameTimer(gameDelay) {
            gameTick()
        }
    }

    private fun gameTick() {
        val result = gameManager.tick()
        main_LBL_odometer.text = String.format("%05d", gameManager.distance)
        drawBoard(result.boardSnapshot)

        if (result.crashed) {
            singleSoundPlayer.playSound(R.raw.oops)
            onCrashUI()
            updateHeartsUI()
        }

        if (result.collectedCoin) {
            singleSoundPlayer.playSound(R.raw.yummy)
            SignalManager.getInstance().toast("Yummy! 🦴", SignalManager.ToastLength.SHORT)
        }

        if (result.gameOver) {
            stopGame()
            val finalScore = gameManager.distance

            getCurrentLocation { lat, lon ->
                RecordListManager.getInstance().addRecord(score = finalScore, lat = lat, lon = lon)
                val intent = Intent(this, MenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        }
    }

    private fun initTiltDetector() {
        if (!useSensors) return

        tiltDetector = TiltDetector(this, object : TiltCallback {
            override fun onTiltLeft() {
                gameManager.moveLeft()
                updateCarPosition()
            }

            override fun onTiltRight() {
                gameManager.moveRight()
                updateCarPosition()
            }

            override fun onTiltForward() {
                updateSpeed(GameConstants.Timer.FAST_DELAY)
            }

            override fun onTiltBackward() {
                updateSpeed(GameConstants.Timer.SLOW_DELAY)
            }
        })
    }

    private fun updateSpeed(newDelay: Long) {
        if (!useSensors) return

        if (gameDelay != newDelay) {
            gameDelay = newDelay
            gameTimer.updateDelay(newDelay)

            val status = if (newDelay == GameConstants.Timer.FAST_DELAY) "FAST ⚡" else "SLOW 🐢"
            SignalManager.getInstance().toast(status, SignalManager.ToastLength.SHORT)
        }
    }

    private fun stopGame() {
        gameTimer.stop()
        if (useSensors) {
            tiltDetector?.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        gameTimer.start()
        if (useSensors) {
            tiltDetector?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        gameTimer.stop()
        if (useSensors) {
            tiltDetector?.stop()
        }
    }

    private fun getCurrentLocation(onResult: (Double, Double) -> Unit) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            onResult(0.0, 0.0)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) onResult(location.latitude, location.longitude)
            else onResult(0.0, 0.0)
        }.addOnFailureListener { onResult(0.0, 0.0) }
    }

    private fun initHotdogsMatrix() {
        hotdogs = arrayOf(
            arrayOf(findViewById(R.id.imgHotdog00), findViewById(R.id.imgHotdog01), findViewById(R.id.imgHotdog02), findViewById(R.id.imgHotdog03), findViewById(R.id.imgHotdog04)),
            arrayOf(findViewById(R.id.imgHotdog10), findViewById(R.id.imgHotdog11), findViewById(R.id.imgHotdog12), findViewById(R.id.imgHotdog13), findViewById(R.id.imgHotdog14)),
            arrayOf(findViewById(R.id.imgHotdog20), findViewById(R.id.imgHotdog21), findViewById(R.id.imgHotdog22), findViewById(R.id.imgHotdog23), findViewById(R.id.imgHotdog24)),
            arrayOf(findViewById(R.id.imgHotdog30), findViewById(R.id.imgHotdog31), findViewById(R.id.imgHotdog32), findViewById(R.id.imgHotdog33), findViewById(R.id.imgHotdog34)),
            arrayOf(findViewById(R.id.imgHotdog40), findViewById(R.id.imgHotdog41), findViewById(R.id.imgHotdog42), findViewById(R.id.imgHotdog43), findViewById(R.id.imgHotdog44))
        )
        clearBoardUI()
    }

    private fun clearBoardUI() {
        for (r in 0 until ROWS) {
            for (c in 0 until COLS) hotdogs[r][c].visibility = View.INVISIBLE
        }
    }

    private fun initCarsArray() {
        cars = arrayOf(
            findViewById(R.id.main_IMG_car_0), findViewById(R.id.main_IMG_car_1),
            findViewById(R.id.main_IMG_car_2), findViewById(R.id.main_IMG_car_3),
            findViewById(R.id.main_IMG_car_4)
        )
        updateCarPosition()
    }

    private fun updateCarPosition() {
        val lane = gameManager.currentLane
        for (i in cars.indices) cars[i].visibility = if (i == lane) View.VISIBLE else View.INVISIBLE
    }

    private fun initHeartsArray() {
        hearts = arrayOf(findViewById(R.id.main_IMG_heart0), findViewById(R.id.main_IMG_heart1), findViewById(R.id.main_IMG_heart2))
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
        for (i in hearts.indices) hearts[i].visibility = if (i < lives) View.VISIBLE else View.INVISIBLE
    }

    private fun onCrashUI() {
        SignalManager.getInstance().toast("Crush!🐶", SignalManager.ToastLength.SHORT)
        SignalManager.getInstance().vibrate()
    }

    private fun initButtons() {
        btnLeft = findViewById(R.id.main_BTN_left)
        btnRight = findViewById(R.id.main_BTN_right)
        btnLeft.setOnClickListener { gameManager.moveLeft(); updateCarPosition() }
        btnRight.setOnClickListener { gameManager.moveRight(); updateCarPosition() }
    }

    private fun drawBoard(board: Array<IntArray>) {
        for (r in 0 until ROWS) {
            for (c in 0 until COLS) {
                val cellValue = board[r][c]
                val currentImageView = hotdogs[r][c]
                when (cellValue) {
                    1 -> { currentImageView.setImageResource(R.drawable.hotdog); currentImageView.visibility = View.VISIBLE }
                    2 -> { currentImageView.setImageResource(R.drawable.dog_food); currentImageView.visibility = View.VISIBLE }
                    else -> currentImageView.visibility = View.INVISIBLE
                }
            }
        }
    }
}