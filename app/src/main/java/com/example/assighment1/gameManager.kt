package com.example.assighment1

import kotlin.random.Random

class GameManager(
    private val rows: Int = 5,
    private val cols: Int = 3,
    private val maxLives: Int = 3
) {

    var currentLane: Int = 1
        private set

    var lives: Int = maxLives
        private set

    private val board: Array<BooleanArray> =
        Array(rows) { BooleanArray(cols) { false } }

    fun resetGame() {
        currentLane = 1
        lives = maxLives
        clearBoard()
    }

    private fun clearBoard() {
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                board[r][c] = false
            }
        }
    }

    fun moveLeft() {
        if (currentLane > 0) {
            currentLane--
        }
    }

    fun moveRight() {
        if (currentLane < cols - 1) {
            currentLane++
        }
    }


    fun tick(): TickResult {
        for (r in rows - 1 downTo 1) {
            for (c in 0 until cols) {
                board[r][c] = board[r - 1][c]
            }
        }

        for (c in 0 until cols) {
            board[0][c] = false
        }

        val spawnRoll = Random.nextInt(0, 4) // 0..3
        if (spawnRoll != 0) {
            val randomCol = Random.nextInt(0, cols)
            board[0][randomCol] = true
        }


        val bottomRow = rows - 1
        val crashedNow = board[bottomRow][currentLane]

        if (crashedNow) {
            lives--
        }

        val gameOver = lives <= 0

        val snapshotForUI = copyBoard()

        if (crashedNow) {
            board[bottomRow][currentLane] = false
        }

        println("bottom=$bottomRow, lane=$currentLane, valueInSnapshot=${snapshotForUI[bottomRow][currentLane]}, lives=$lives")

        return TickResult(
            crashed = crashedNow,
            gameOver = gameOver,
            boardSnapshot = snapshotForUI
        )
    }


    private fun copyBoard(): Array<BooleanArray> {
        return Array(rows) { r ->
            board[r].clone()
        }
    }

    data class TickResult(
        val crashed: Boolean,
        val gameOver: Boolean,
        val boardSnapshot: Array<BooleanArray>
    )
}