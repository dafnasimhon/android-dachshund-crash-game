package com.example.assighment1.logic

import kotlin.random.Random

class GameManager(
    private val rows: Int = 5,
    private val cols: Int = 5,
    private val maxLives: Int = 3
) {

    var currentLane: Int = 2
        private set

    var lives: Int = maxLives
        private set

    var distance: Int = 0
        private set


    private val board: Array<IntArray> =
        Array(rows) { IntArray(cols) { 0 } }

    fun resetGame() {
        distance = 0
        currentLane = 2
        lives = maxLives
        clearBoard()
    }

    private fun clearBoard() {
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                board[r][c] = 0
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
        distance++

        for (r in rows - 1 downTo 1) {
            for (c in 0 until cols) {
                board[r][c] = board[r - 1][c]
            }
        }


        for (c in 0 until cols) {
            board[0][c] = 0
        }

        val spawnRoll = Random.nextInt(0, 100)
        val randomCol = Random.nextInt(0, cols)

        if (spawnRoll < 75) {

            board[0][randomCol] = 1
        } else {

            board[0][randomCol] = 2
        }


        val bottomRow = rows - 1
        val itemAtPlayerPos = board[bottomRow][currentLane]

        var crashedNow = false
        var collectedCoin = false

        if (itemAtPlayerPos == 1) {
            crashedNow = true
            lives--
        } else if (itemAtPlayerPos == 2) {
            collectedCoin = true
            distance += 10
        }

        val gameOver = lives <= 0
        val snapshotForUI = copyBoard()


        board[bottomRow][currentLane] = 0

        return TickResult(
            crashed = crashedNow,
            collectedCoin = collectedCoin,
            gameOver = gameOver,
            boardSnapshot = snapshotForUI
        )
    }

    private fun copyBoard(): Array<IntArray> {
        return Array(rows) { r ->
            board[r].clone()
        }
    }

    data class TickResult(
        val crashed: Boolean,
        val collectedCoin: Boolean,
        val gameOver: Boolean,
        val boardSnapshot: Array<IntArray>
    )
}