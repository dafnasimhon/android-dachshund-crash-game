package com.example.assighment1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.assighment1.utilities.GameConstants
import com.google.android.material.switchmaterial.SwitchMaterial

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val speedSwitch = findViewById<SwitchMaterial>(R.id.menu_SW_speed)
        val btnButtonsMode = findViewById<Button>(R.id.menu_BTN_start_buttons)
        val btnSensorsMode = findViewById<Button>(R.id.menu_BTN_sensors)
        val btnHighScores = findViewById<Button>(R.id.menu_BTN_scores)

        btnButtonsMode.setOnClickListener {
            val selectedDelay = if (speedSwitch.isChecked) {
                GameConstants.Timer.FAST_DELAY
            } else {
                GameConstants.Timer.SLOW_DELAY
            }
            startGame(useSensors = false, delay = selectedDelay)
        }

        btnSensorsMode.setOnClickListener {
            startGame(useSensors = true, delay = GameConstants.Timer.SLOW_DELAY)
        }

        btnHighScores.setOnClickListener {
            startActivity(Intent(this, ScoreActivity::class.java))
        }
    }

    private fun startGame(useSensors: Boolean, delay: Long) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("USE_SENSORS", useSensors)
        bundle.putLong("DELAY", delay)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}