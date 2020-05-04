package games.game2048

/*
 * This function moves all the non-null elements to the beginning of the list
 * (by removing nulls) and merges equal elements.
 * The parameter 'merge' specifies the way how to merge equal elements:
 * it returns a new element that should be present in the resulting list
 * instead of two merged elements.
 *
 * If the function 'merge("a")' returns "aa",
 * then the function 'moveAndMergeEqual' transforms the input in the following way:
 *   a, a, b -> aa, b
 *   a, null -> a
 *   b, null, a, a -> b, aa
 *   a, a, null, a -> aa, a
 *   a, null, a, a -> aa, a
 *
 * You can find more examples in 'TestGame2048Helper'.
*/
fun <T : Any> List<T?>. moveAndMergeEqual(merge: (T) -> T): List<T> {
    val nonNullElements = this.filterNotNull().toMutableList()

    return resolveGroups(nonNullElements, merge)
}

private fun <T> resolveGroups(list: List<T>, merge: (T) -> T): List<T> {
    if (list.isEmpty()) return emptyList()

    val group = list.takeWhile { it == list[0] }
    val groupResolving = when (group.size) {
        2 -> mutableListOf(merge(group[0]))
        3 -> mutableListOf(merge(group[0]), group[2])
        else -> mutableListOf(group[0])
    }

    val tail = list.takeLast(list.size - group.size)

    return mutableListOf(groupResolving, resolveGroups(tail, merge)).flatten()
}