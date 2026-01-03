package com.example.assighment1.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.assighment1.interfaces.TiltCallback

class TiltDetector(context: Context, private val tiltCallback: TiltCallback) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var timestampX: Long = 0L
    private var timestampY: Long = 0L

    private lateinit var sensorEventListener: SensorEventListener

    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                calculateTilt(x, y)
            }
        }
    }

    private fun calculateTilt(x: Float, y: Float) {
        val currentTime = System.currentTimeMillis()

        if (currentTime - timestampX >= 250) {
            if (x >= 2.5) {
                timestampX = currentTime
                tiltCallback.onTiltLeft()
            } else if (x <= -2.5) {
                timestampX = currentTime
                tiltCallback.onTiltRight()
            }
        }

        if (currentTime - timestampY >= 500) {
            if (y <= 1.0) {
                timestampY = currentTime
                tiltCallback.onTiltForward()
            } else if (y >= 6.0) {
                timestampY = currentTime
                tiltCallback.onTiltBackward()
            }
        }
    }

    fun start() {
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    fun stop() {
        sensorManager.unregisterListener(sensorEventListener, sensor)
    }
}