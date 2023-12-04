import kotlin.math.*

fun main() {
        
    fun getCardNumbers(line: String): Pair<List<Int>, List<Int>> {
        val tokens = line.split("|")
        val winningNumbers = tokens[0].trim().split(" ").filter{it.isNotEmpty() && it.isNotBlank()}.map {it.toInt()}
        val cardNumbers = tokens[1].trim().split(" ").filter{it.isNotEmpty() && it.isNotBlank()}.map {it.toInt()}
        return Pair(winningNumbers, cardNumbers)
    }
    
    fun getPoints(winningNumbers: List<Int>, cardNumbers: List<Int>): Int  {
        val matches = cardNumbers.filter { num -> winningNumbers.contains(num) }.size
        return (2.0.pow(matches - 1)).toInt()
    }
    
    fun getNumMatches(winningNumbers: List<Int>, cardNumbers: List<Int>): Int  {
        val matches = cardNumbers.filter { num -> winningNumbers.contains(num) }.size
        return matches
    }
    
    fun part1(input: List<String>): Int {
        val points = input.map {
            val tokens = it.split(":")
            val (winningNumbers, cardNumbers) = getCardNumbers(tokens[1].trim())
            return@map getPoints(winningNumbers, cardNumbers)
        }
        return points.sum()
    }

    fun part2(input: List<String>): Int {
        val numGamesList = (1 .. input.size).toList()
        val numGamesMap = numGamesList.associateWith { 1 }.toMutableMap()
        
        input.forEachIndexed {index, line ->
//            println(numGamesMap)
            val tokens = line.split(":")
            val (winningNumbers, cardNumbers) = getCardNumbers(tokens[1].trim())
            val numMatches = getNumMatches(winningNumbers, cardNumbers)
            val currentCards = numGamesMap[index + 1]
//            println("Card ${index + 1} has ${currentCards} values and ${numMatches} matches")
            
            if(currentCards != null) {
                for(i in index + 2 until numMatches + index + 2) {
//                    println("Adding ${currentCards} to card ${i}")
                    if(numGamesMap[i] != null) {
                        
                        numGamesMap[i] = numGamesMap.getOrDefault(i, 0) + currentCards  
                    }
                }
            }
            
        }
        
//        println(numGamesMap)
        
        return numGamesMap.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
