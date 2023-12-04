fun main() {
    fun part1(input: List<String>): Int {
        val numLists: List<Int> = input.map { line ->
            var firstNumber = -1
            var lastNumber = -1

            for (i in line.indices) {
                val char = line[i].digitToIntOrNull()
                if(char != null) {
                    firstNumber = if(firstNumber == -1) char else firstNumber
                    lastNumber = char
                }
            }

            return@map "${firstNumber}${lastNumber}".toInt()
        }
        return numLists.sum()
    }

    fun part2(input: List<String>): Int {
        val numbers = mapOf("one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9",
            "1" to "1",
            "2" to "2",
            "3" to "3",
            "4" to "4",
            "5" to "5",
            "6" to "6",
            "7" to "7",
            "8" to "8",
            "9" to "9")
        val numLists: List<Int> = input.map {line ->
            var firstNumber = -1
            var lastNumber = -1

            val mappings = numbers.keys.mapNotNull{
                val regex = Regex(it)
                val appearances =regex.findAll(line).count()
                if(appearances == 1) {
                    return@mapNotNull listOf(Pair(it, line.indexOf(it)))
                } else if (appearances > 1){
                    // If key appears more than once in the string, we must find all occurrences and decide which one we want to return
                    val indices = mutableListOf<Int>()
                    var currentIndex = line.indexOf(it)

                    // Loop through the string until no more occurrences are found
                    while (currentIndex != -1) {
                        indices.add(currentIndex)
                        currentIndex = line.indexOf(it, currentIndex + 1)
                    }

                    return@mapNotNull listOf(Pair(it, indices.first()), Pair(it, indices.last()))
                } else {
                    return@mapNotNull listOf(Pair(null, -1))
                }
            }.flatten().filter { it -> it.first != null }

            val firstNumberString = numbers[mappings.sortedBy { it.second }.first().first]
            val lastNumberString = numbers[mappings.sortedBy { it.second }.last().first]
            if(firstNumberString != null && lastNumberString != null) {
                firstNumber = firstNumberString.toInt()
                lastNumber = lastNumberString.toInt()
            }

            return@map "${firstNumber}${lastNumber}".toInt()
        }

        return numLists.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
