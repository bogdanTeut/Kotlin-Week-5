package games.gameOfFifteen

import board.Cell
import board.GameBoard

/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */
fun isEven(permutation: List<Int>): Boolean = createNotationGraph(permutation) % 2 == 0

private fun createNotationGraph(permutation: List<Int>): Int {
    val cycleNotationPairs = generateCycleNotationPairs(permutation)
    val visitedMap: MutableMap<Int, Boolean> = mutableMapOf()

    return calculateTotalLengthOfThePermutation(cycleNotationPairs, visitedMap)
}

private fun generateCycleNotationPairs(permutation: List<Int>): List<Pair<Int, Int>> {
    val firstElementPermutation = permutation.min()!!
    return (firstElementPermutation..permutation.size).zip(permutation)
}

private fun calculateTotalLengthOfThePermutation(cycleNotationPairs: List<Pair<Int, Int>>, visitedMap: MutableMap<Int, Boolean>): Int {
    var accumulator = 0
    for (cycleNotationPair in cycleNotationPairs) {
        accumulator += calculateCycleLength(visitedMap, cycleNotationPair, cycleNotationPairs)
    }
    return accumulator
}

private fun calculateCycleLength(visitedMap: MutableMap<Int, Boolean>, cycleNotationPair: Pair<Int, Int>, cycleNotationPairs: List<Pair<Int, Int>>) =
        if (visitedMap[cycleNotationPair.first] == true) 0
        else calculateGraphLengthRecursively(cycleNotationPair, cycleNotationPairs, visitedMap) - 1


private fun calculateGraphLengthRecursively(startingPair: Pair<Int, Int>,
                                    cycleNotationPairs: List<Pair<Int, Int>>,
                                    visitedMap: MutableMap<Int, Boolean>): Int {
    if (visitedMap[startingPair.first] == true) return 0

    val nextElement = startingPair.second
    visitedMap[startingPair.first] = true
    val nextCycleElement = cycleNotationPairs.find { it.first == nextElement }!!
    return 1 + calculateGraphLengthRecursively(nextCycleElement, cycleNotationPairs, visitedMap)
}

fun <T :Any> List<T?>.move(): MutableList<T?> {
    val indexOfNull = this.indexOf(null)

    val mutableRowOrColumn = this.toMutableList()
    if (indexOfNull != -1 && indexOfNull != mutableRowOrColumn.size-1) {
        mutableRowOrColumn[indexOfNull] = mutableRowOrColumn[indexOfNull+1]
        mutableRowOrColumn[indexOfNull+1] = null
    }

    return mutableRowOrColumn
}