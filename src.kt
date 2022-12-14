package connectfour

class ConnectFour {

    private var playerOne: String = ""
    private var playerTwo: String = ""
    private var dimensionsInput: String = ""
    private var row: Int = 6
    private var column: Int = 7
    private var grid = MutableList(row){ MutableList(column) { " " } }
    private var playerTurn: Int = 1
    private var numOfGames: Int = 1
    private var currGame: Int = 1
    private var scoreOne: Int = 0
    private var scoreTwo: Int = 0

    private fun printPlayerScore() {
        println("Score")
        println("$playerOne: $scoreOne $playerTwo: $scoreTwo")
    }

    private fun printProgramMessage() {
        println("$playerOne VS $playerTwo")
        println("$row X $column board")
        println(if (numOfGames == 1) "Single game" else "Total $numOfGames games")
    }

    private fun printCurrentGame() {
        if (numOfGames != 1) println("Game #$currGame")
    }

    private fun printBoard() {
        for (i in 1..column) {
            print(" $i")
        }
        println("")
        for (i in 0 until row) {
            println("║${grid[i].joinToString("║")}║")
        }
        println("╚${"═╩".repeat(column - 1)}═╝")
    }

    private fun printPlayerTurn() = println(if (playerTurn % 2 == 1) "$playerOne's turn:" else "$playerTwo's turn:")

    private fun printPlayerWin() = println(if (playerTurn % 2 == 1) "Player $playerOne won" else "Player $playerTwo won")

    private fun printPlayerDraw() = println("It is a draw")

    private fun printGameOver() = if (numOfGames == 1) {
        println("Game over!")
    } else {
        handlePlayerScore()
        printPlayerScore()
        println("Game over!")
    }

    private fun isColumnFull(playerInput: String): Boolean {
        for (i in 0 until row) {
            if (grid[i][playerInput.toInt() - 1] == " ") {
                return false
            }
        }
        return true
    }

    fun handlePlayerOne() {
        println("First player's name:")
        playerOne = readln()
    }

    fun handlePlayerTwo() {
        println("Second player's name:")
        playerTwo = readln()
    }

    fun handleDimensionsInput() {
        while (true) {
            println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")
            dimensionsInput = readln().replace("\\s".toRegex(), "")
            if (Regex("^(\\d+[x|X]\\d+)?$").matches(dimensionsInput)) {
                if (dimensionsInput.isNotEmpty()) {
                    row = dimensionsInput.substring(0, 1).toInt()
                    column = dimensionsInput.substring(dimensionsInput.length - 1).toInt()
                    grid = MutableList(row){ MutableList(column) { " " } }
                }
                when {
                    row < 5 || row > 9 -> println("Board rows should be from 5 to 9")
                    column < 5 || column > 9 -> println("Board columns should be from 5 to 9")
                    else -> break
                }
            } else {
                println("Invalid input")
            }
        }
    }

    fun handleNumberOfGames() {
        while (true) {
            println("Do you want to play single or multiple games?")
            println("For a single game, input 1 or press Enter")
            println("Input a number of games:")
            try {
                val num: String = readln()
                if (num.isEmpty() || num.toInt() > 0) {
                    when {
                        num.isEmpty() -> numOfGames = 1
                        num.toInt() > 0 -> numOfGames = num.toInt()
                    }
                    printProgramMessage()
                    printCurrentGame()
                    printBoard()
                    break
                } else {
                    println("Invalid input")
                }
            } catch (e: NumberFormatException) {
                println("Invalid input")
            }
        }
    }

    private fun handlePlayerScore() {
        when (checkGameStatus()) {
            "win" -> if (playerTurn % 2 == 1) scoreOne += 2 else scoreTwo += 2
            "draw" -> {
                scoreOne += 1
                scoreTwo += 1
            }
        }
    }

    private fun checkHorizontal(): Boolean {
        for (i in 0 until row) {
            val resultString = grid[i].joinToString("")
            if (Regex(".*(o{4}|\\*{4}).*").matches(resultString)) {
                return true
            }
        }
        return false
    }

    private fun checkVertical(): Boolean {
        var string = ""
        for (j in 0 until column) {
            for (i in 0 until row) {
                string += grid[i][j]
            }
            if (Regex(".*(o{4}|\\*{4}).*").matches(string)) {
                return true
            } else {
                string = ""
            }
        }
        return false
    }

    private fun checkDiagonal(): Boolean {
        for (i in 0 until row) {
            for (j in 0 until column) {
                if (grid[i][j] != " ") {
                    try {
                        when {
                            grid[i][j] == grid[i + 1][j + 1] && grid[i][j] == grid[i + 2][j + 2] && grid[i][j] == grid[i + 3][j + 3] -> return true
                        }
                    } catch (e: IndexOutOfBoundsException) {}
                    try {
                        when {
                            grid[i][j] == grid[i + 1][j - 1] && grid[i][j] == grid[i + 2][j - 2] && grid[i][j] == grid[i + 3][j - 3] -> return true
                        }
                    } catch (e: IndexOutOfBoundsException) {}
                }
            }
        }
        return false
    }

    private fun checkDraw(): Boolean {
        for (i in 0 until row) {
            if (grid[i].contains(" ")) {
                return false
            }
        }
        return true
    }

    private fun checkGameStatus(): String = when {
        checkHorizontal() || checkVertical() || checkDiagonal() -> "win"
        checkDraw() -> "draw"
        else -> "continue"
    }

    private fun nextGame() {
        handlePlayerScore()
        printPlayerScore()
        currGame++
        printCurrentGame()
        grid = MutableList(row){ MutableList(column) { " " } }
        printBoard()
    }

    fun handleGameInput() {
        main@while (true) {
            printPlayerTurn()
            val playerInput = readln()
            if (playerInput == "end") {
                println("Game over!")
                break
            } else if (!Regex("\\d+").matches(playerInput)){
                println("Incorrect column number")
            } else if (playerInput.toInt() < 1 || playerInput.toInt() > column) {
                println("The column number is out of range (1 - $column)")
            } else if (isColumnFull(playerInput)) {
                println("Column $playerInput is full")
            } else {
                for (i in (row - 1) downTo 0) {
                    if (grid[i][playerInput.toInt() - 1] == " ") {
                        if (playerTurn % 2 == 1) {
                            grid[i][playerInput.toInt() - 1] = "o"
                        } else {
                            grid[i][playerInput.toInt() - 1] = "*"
                        }
                        printBoard()
                        if (checkGameStatus() == "win") {
                            printPlayerWin()
                            if (currGame == numOfGames) {
                                printGameOver()
                                break@main
                            } else {
                                nextGame()
                            }
                        } else if (checkGameStatus() == "draw"){
                            printPlayerDraw()
                            if (currGame == numOfGames) {
                                printGameOver()
                                break@main
                            } else {
                                nextGame()
                            }
                        }
                        break
                    }
                }
                playerTurn++
            }
        }
    }

}

fun main() {
    val program = ConnectFour()
    println("Connect Four")
    program.handlePlayerOne()
    program.handlePlayerTwo()
    program.handleDimensionsInput()
    program.handleNumberOfGames()
    program.handleGameInput()
}
