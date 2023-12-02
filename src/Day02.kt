

public enum class Color {BLUE, GREEN, RED}

fun main() {
    
    data class CubeSubset(val color: Color, val number: Int)
    data class Configuration(val numBlue: Int, val numGreen: Int, val numRed: Int)
    
    fun getColor(color: String): Color {
        val color = when(color.trim().lowercase()) {
            "blue" -> Color.BLUE
            "green" -> Color.GREEN
            "red" -> Color.RED
            else -> throw IllegalArgumentException("Invalid color: $color")
        }
        return color
    }
    
    // Input will be something like: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    fun getCubesFromGameString(line: String): MutableList<MutableList<CubeSubset>> {
        val allSubsets: MutableList<MutableList<CubeSubset>> = mutableListOf()
        val games = line.split(";")
        games.forEach {draw -> // draw will be: 3 blue, 4 red
            val cubeSetList = mutableListOf<CubeSubset>()
            val drawString = draw.trim().split(",")
            drawString.forEach {subsetString -> //subsetString will be: 3 blue
                val tokens = subsetString.trim().split(" ")
                if(tokens.size == 2) {

                    val newCubeSubset = CubeSubset(getColor(tokens[1]), tokens[0].toInt())
                    cubeSetList.add(newCubeSubset)
                }
            }
            allSubsets.add(cubeSetList)
        }
        
        return allSubsets
    }
    
    fun part1(input: List<String>, configuration: Configuration): Int {
        val listOfGameIDs = mutableListOf<Int>()
        input.forEach {game ->
            val tokens = game.split(":")
            val gameID = tokens[0].trim().split(" ")[1].trim().toInt()
            val gameString = tokens[1].trim()
            
            val cubeSet = getCubesFromGameString(gameString)
            
            var possibleGame = true
            cubeSet.forEach { it ->
                it.forEach { cubeSubset ->
                    when(cubeSubset.color) {
                        Color.BLUE -> {
                            if(cubeSubset.number > configuration.numBlue) {
                                possibleGame = false
                            }
                        }
                        Color.RED -> {
                            if(cubeSubset.number > configuration.numRed) {
                                possibleGame = false
                            }
                        }
                        Color.GREEN -> {
                            if(cubeSubset.number > configuration.numGreen) {
                                possibleGame = false
                            }
                        }
                    }
                }
            }
            if(possibleGame) {
                listOfGameIDs.add(gameID)
            }
        }
        return listOfGameIDs.sum()
    }

    fun part2(input: List<String>): Int {
        val cubePowerList = mutableListOf<Int>()
        input.forEach {game ->
            val tokens = game.split(":")
            val gameString = tokens[1].trim()

            val cubeSet = getCubesFromGameString(gameString)
            
            var maxGreen = 0
            var maxBlue = 0
            var maxRed = 0

            cubeSet.forEach { it ->
                it.forEach { cubeSubset ->
                    when(cubeSubset.color) {
                        Color.BLUE -> {
                            if(cubeSubset.number > maxBlue) {
                                maxBlue = cubeSubset.number
                            }
                        }
                        Color.RED -> {
                            if(cubeSubset.number > maxRed) {
                                maxRed = cubeSubset.number
                            }
                        }
                        Color.GREEN -> {
                            if(cubeSubset.number > maxGreen) {
                                maxGreen = cubeSubset.number
                            }
                        }
                    }
                }
            }
            cubePowerList.add(maxGreen * maxBlue * maxRed)
        }
        return cubePowerList.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")

    val input = readInput("Day02")
    val configuration = Configuration(14, 13, 12)
    
    part1(input, configuration).println()
    part2(input).println()
}
