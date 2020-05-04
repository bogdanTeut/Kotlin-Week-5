package board

import board.Direction.*
import java.lang.IllegalArgumentException

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(createSquareBoard(width))

class SquareBoardImpl(override val width: Int) : SquareBoard {

    private val matrix = run {
        var matrix = arrayOf<Array<Cell>>()
        for (i in 1..width) {
            var row = arrayOf<Cell>()
            for (j in 1..width) {
                row += Cell(i, j)
            }
            matrix += row
        }
        matrix
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? = matrix.getOrNull(i-1)?.getOrNull(j-1)

    override fun getCell(i: Int, j: Int): Cell = matrix.getOrNull(i-1)?.getOrNull(j-1) ?: throw IllegalArgumentException("No such element")

    override fun getAllCells(): Collection<Cell> {
        return matrix.toList().flatMap { it.toList() }
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        var result = mutableListOf<Cell>()
        var row = matrix.getOrNull(i-1)?.toList() ?: emptyList()

        for (j in jRange) {
            row.getOrNull(j-1)?.let { result.add(it)}
        }
        return result
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        var result = mutableListOf<Cell>()

        for (i in iRange) {
            matrix.getOrNull(i-1)?.let {
                row -> row.getOrNull(j-1)?.let { result.add(it) }
            }
        }
        return result
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {

        val neighbourI = when(direction) {
            UP -> this.i-2
            DOWN -> this.i
            else -> this.i-1
        }

        val neighbourJ = when(direction) {
            LEFT -> this.j-2
            RIGHT -> this.j
            else -> this.j-1
        }

        return matrix.getOrNull(neighbourI)?.getOrNull(neighbourJ)
    }

}

class GameBoardImpl<T>(private val squareBoard: SquareBoard) : GameBoard<T>, SquareBoard by squareBoard {
    private var valuesMap: MutableMap<Cell, T?> = run {
        valuesMap = mutableMapOf()
        squareBoard.getAllCells().forEach { valuesMap[it] = null }
        valuesMap
    }


    override fun get(cell: Cell): T? = valuesMap[cell]

    override fun set(cell: Cell, value: T?) {
        valuesMap[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = valuesMap.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell? = filter(predicate).first()

    override fun any(predicate: (T?) -> Boolean): Boolean = valuesMap.values.any(predicate)

    override fun all(predicate: (T?) -> Boolean): Boolean = valuesMap.values.all(predicate)
}
