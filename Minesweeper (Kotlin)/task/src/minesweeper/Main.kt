package minesweeper

import kotlin.random.Random
import kotlin.math.*

const val rows = 9
const val columns = 9
const val WR_INP = "Wrong input!"

var mines = Array(rows) { Array(columns) { false } } // mines
var nums = Array(rows) { Array(columns) { 0 } } // numbers of mines nearby
var guess = Array(rows) { Array(columns) { false } } // user's guesses of mines
var explored = Array(rows) { Array(columns) { false } } // opened cells

var numMines = 0

fun main() {

    print("How many mines do you want on the field? ")
    numMines = try {
        readln().toInt()
    } catch (e: Exception) {
        println(WR_INP)
        return
    }

    if (numMines < 0 || numMines > rows * columns - 1) {
        println(WR_INP)
        return
    }

    // filling
    // fillField()

    // printing
    printField(false)
    play()
}

fun play() {
    var isFirstMove = true

    do {
        print("Set/unset mines marks or claim a cell as free: ")

        val inp = readln().split(" ")
        if(inp.size != 3) {
            println(WR_INP)
            continue
        }

        val col = try {
            inp[0].toInt() - 1
        } catch (e: NumberFormatException) {
            println(WR_INP)
            continue
        }

        val row = try {
            inp[1].toInt() - 1
        } catch (e: NumberFormatException) {
            println(WR_INP)
            continue
        }

        if (row !in 0..8 || col !in 0..8) {
            println(WR_INP)
            continue
        }

        if (explored[row][col]) {
            println("The cell is already explored.")
            continue
        }

        when (inp[2]) {
            "mine" -> mark(row, col)
            "free" -> {
                if (isFirstMove) fillField(row, col)
                if (explore(row, col)) {
                    printField(true)
                    println("You stepped on a mine and failed!")
                    break
                }
            }
            else -> {
                println(WR_INP)
                continue
            }
        }

        isFirstMove = false
        printField(false)

        if (isFinished()) {
            println("Congratulations! You found all the mines!")
            break
        }
    } while (true)
}

fun explore(row: Int, col: Int): Boolean {
    if (mines[row][col]) return true
    if (nums[row][col] > 0) explored[row][col] = true
    else {
        explored[row][col] = true
        for (r in max(0, row-1)..min(row+1, rows-1))
            for (c in max(0, col-1).. min(col+1, columns-1))
                if (!explored[r][c]) explore(r, c)
    }
    return false
}

fun mark(row: Int, col: Int) {
    guess[row][col] = !guess[row][col]
}

fun isFinished(): Boolean {

    // if all the mines marked correctly
    var areMarkedCorrectly = true
    for (r in 0 until rows) {
        for (c in 0 until columns) {
            if (guess[r][c].xor(mines[r][c])) {
                areMarkedCorrectly = false
                break
            }
        }
        if(!areMarkedCorrectly) break
    }
    if (areMarkedCorrectly) return true

    //if only unexplored cells with mines left
    var isNothingUnexploredLeft = true
    for (r in 0 until rows) {
        for (c in 0 until columns) {
            if (explored[r][c].xor(!mines[r][c])) {
                isNothingUnexploredLeft = false
                break
            }
        }
        if(!isNothingUnexploredLeft) break
    }

    return isNothingUnexploredLeft
}

private fun fillField(row: Int, col: Int) {
    val numbers = MutableList(rows * columns) { i -> i }
    numbers.removeAt(row * columns + col)

    // filling field with mines
    for (i in 0 until numMines) {
        val rng = Random.Default.nextInt(rows * columns - 1 - i)
        mines[numbers[rng] / columns][numbers[rng] % columns] = true
        numbers.removeAt(rng)
    }

    // calculating numbers of mines around
    for (r in 0 until rows) {
        for (c in 0 until columns) {
            if (mines[r][c]) continue
            else nums[r][c] = numberOfMinesAround(r, c)
        }
    }
}

private fun printField(showMines: Boolean) {
    println("\n |123456789|")
    println("—|—————————|")
    for (r in 0 until rows) {
        print("${r+1}|")
        for (c in 0 until columns) {
            if (mines[r][c] && showMines) {
                print('X')
                continue
            }
            if (!explored[r][c])
                if (guess[r][c]) print('*') else print('.')
            else if (nums[r][c] == 0) print('/')
            else print(nums[r][c])
        }
        println("|")
    }
    println("—|—————————|")
}

fun numberOfMinesAround(row: Int, col: Int): Int {

    var numOfMines = 0
    for (r in max(0, row-1)..min(row+1, rows-1))
        for (c in max(0, col-1).. min(col+1, columns-1))
            if (mines[r][c]) numOfMines++
    return numOfMines

}
