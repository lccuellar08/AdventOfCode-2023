import java.lang.NumberFormatException

fun main() {
    val nonSymbols = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "a")
    
    
    fun getEngineSchematic(input: List<String>): Array<Array<String>> {
        val engineSchematic: Array<Array<String>> = Array(input.size) {Array(input[0].length) {""}}
        
        input.forEachIndexed{i, line ->
            line.forEachIndexed { ii, char ->
                engineSchematic[i][ii] = char.toString()
            }
        }
        
        return engineSchematic
    }
    
    fun getPart(schematicLine: Array<String>, index: Int): Int {
//        println("Index: ${index}")
//        println("Array: ${schematicLine.map{it}.joinToString(",")}")
        var partNumberString = ""
        
        schematicLine.forEachIndexed { i, it ->
            // Base case, we are searching
            if(partNumberString.isBlank()) {
                val characterNum = it.toIntOrNull()
                if(characterNum != null) {
                    // We found a number, begin writing the part number
                    partNumberString += it
                } else {
                    // We keep going
                }
            } else {
                // We are already in a number
                val characterNum = it.toIntOrNull()
                if(characterNum != null) {
                    // We found a number, continue wriitng it and continue seraching
                    partNumberString += it
                } else {
                    // We found the end of the number
                    if(index > i) {
                        // Not the number we're searching for, clear and continue searching
                        partNumberString = ""
                    } else {
                        // We found the number
                        return partNumberString.toInt()
                    }
                }
            }
        }
        
        return partNumberString.toInt()
    }
    
    fun part1(input: List<String>): Int {
        val engineSchematic = getEngineSchematic(input)
        val partsList: MutableList<Int> = mutableListOf()
        
        engineSchematic.forEachIndexed {i, line ->
            line.forEachIndexed {ii, it ->
                if(nonSymbols.contains(it)) {
                    // Do nothing
                } else {
                    // We have a symbol
                    val partsToAdd: MutableList<Int> = mutableListOf()
                    for(row in -1..1) {
                        for(col in -1 .. 1) {
                            try {
                                val charString = engineSchematic[i + row][ii + col]
                                val charNum = charString.toIntOrNull()
                                if(charNum != null) {
                                    // We have a number
                                    val partNumber = getPart(engineSchematic[i+row], ii + col)
                                    if(partNumber == -1) {
                                        println("Failed to find ${charNum} at index ${ii + col} in row ${i+row}")
                                    }
                                    partsToAdd.add(partNumber)
                                }
                            } catch (e: IndexOutOfBoundsException) {
                                // Ignore
                            }
                        }
                    }
                    
                    partsList.addAll(partsToAdd.distinct())
                }
            }
        }
        return partsList.sum()
    }

    fun part2(input: List<String>): Int {
        val engineSchematic = getEngineSchematic(input)
        val gearRatios: MutableList<Int> = mutableListOf()

        engineSchematic.forEachIndexed {i, line ->
            line.forEachIndexed {ii, it ->
                if(it == "*") {
                    val partsToAdd: MutableList<Int> = mutableListOf()
                    for(row in -1..1) {
                        for(col in -1 .. 1) {
                            try {
                                val charString = engineSchematic[i + row][ii + col]
                                val charNum = charString.toIntOrNull()
                                if(charNum != null) {
                                    // We have a number
                                    val partNumber = getPart(engineSchematic[i+row], ii + col)
                                    if(partNumber == -1) {
                                        println("Failed to find ${charNum} at index ${ii + col} in row ${i+row}")
                                    }
                                    partsToAdd.add(partNumber)
                                }
                            } catch (e: IndexOutOfBoundsException) {
                            // Ignore
                            }
                        }
                    }
                    val distinctParts = partsToAdd.distinct()
                    if(distinctParts.size == 2) {
                        gearRatios.add(distinctParts[0] * distinctParts[1])
                    }
                } 
            }
        }
//        println(gearRatios)
        return gearRatios.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
