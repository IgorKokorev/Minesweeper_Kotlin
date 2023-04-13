package minesweeper

import kotlin.random.Random

var field: Array<Array<Boolean>> = arrayOf()
val rows = 9
val columns = 9
const val WR_INP = "Wrong input!"


fun main() {

    print("How many mines do you want on the field? ")
    val numMines = try {
        readln().toInt()
    } catch (e: Exception) {
        println(WR_INP)
        return
    }

    if (numMines < 0 || numMines > rows * columns) {
        println(WR_INP)
        return
    }

    field = Array(rows) { Array(columns) { false } }

    // filling
    for (i in 0 until numMines) {
        var rngPosition: Int
        do {
            rngPosition = Random.nextInt(rows * columns)
        } while (field[rngPosition / columns][rngPosition % columns])
        field[rngPosition / columns][rngPosition % columns] = true
    }

    // printing
    for (r in 0 until rows) {
        for (c in 0 until columns) {
            if (field[r][c]) print('X')
            else print('.')
        }
        println("")
    }
}
