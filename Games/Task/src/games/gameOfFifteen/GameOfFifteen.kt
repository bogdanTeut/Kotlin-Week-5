package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game
import games.game2048.addNewValue
import games.game2048.moveAndMergeEqual
import games.game2048.moveValues
import games.game2048.moveValuesInRowOrColumn

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {

        for (i in 1..board.width){
            for (j in 1..board.width) {
                board.addNewValue(i, j, initializer)
            }
        }
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean {
        val values = board.getAllCells().map { board[it] }
        return values.take(values.size-1) == (1 until values.size).toList()
    }

    override fun processMove(direction: Direction) {
        board.moveValuesFifteen(direction)
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

}

fun GameBoard<Int?>.addNewValue(i:Int, j: Int, initializer: GameOfFifteenInitializer) {
    val cell = this.getCell(i, j)
    val index = this.width * (i - 1) + (j - 1)
    this[cell] = if (index < 15) initializer.initialPermutation[index] else null
}

fun GameBoard<Int?>.moveValuesFifteen(direction: Direction): Boolean {
    val currentValues = this.getAllCells().map { this[it] }

    when (direction) {
        Direction.LEFT -> {
            (1..width).forEach {
                moveValuesInRowOrColumnForFifteen(this.getRow(it, 1..4))
            }
        }
        Direction.RIGHT -> {
            (1..width).forEach {
                moveValuesInRowOrColumnForFifteen(this.getRow(it, 4 downTo 1))
            }
        }
        Direction.UP -> {
            (1..width).forEach {
                moveValuesInRowOrColumnForFifteen(this.getColumn(1..4, it))
            }
        }
        Direction.DOWN -> {
            (1..width).forEach {
                moveValuesInRowOrColumnForFifteen(this.getColumn(4 downTo 1, it))
            }
        }
    }

    val mergedValues = this.getAllCells().map { this[it] }

    return currentValues != mergedValues
}

fun GameBoard<Int?>.moveValuesInRowOrColumnForFifteen(rowOrColumn: List<Cell>): Boolean {
    val currentValues = rowOrColumn.map { this[it] }
    val mergedValues = currentValues.move()

    rowOrColumn.forEachIndexed { index, cell ->
        this[cell] = if (index < mergedValues.size) mergedValues[index] else null
    }

    return mergedValues != currentValues
}