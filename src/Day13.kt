import java.lang.IndexOutOfBoundsException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@SinceKotlin("1.1") 
public inline infix fun Byte.and(other: Byte): Byte = (this.toInt() and other.toInt()).toByte()

fun main() {
//    data class ByteBias(val string: String, val bias: Int)
    
    fun convertToByte(byteList: List<Byte>): Int {
        var result = 0
        for (bit in byteList) {
            result = (result shl 1) or (bit.toInt() and 1)
        }
        return result
    }
    
    fun numLeftTrailingZeros(bitList: List<Byte>): Int {
        var leadingZeros = 0

        for (bit in bitList) {
            if (bit == 0.toByte() && leadingZeros < bitList.size) {
                leadingZeros += 1
            } else {
                break
            }
        }

        return leadingZeros
    }
    
    fun findMatchingLineIndex(byte: String, lines: List<String>, byteIndex: Int): Int? {
        val matchingLines = lines.mapIndexed{index, it ->
            return@mapIndexed if (it == byte && index != byteIndex) index else null
        }.filterNotNull().map{Pair(it, abs(byteIndex - it))}
        
        
        if(matchingLines.isEmpty()) {
            return null
        }
        
        val matchingIndex =  matchingLines.minBy{it.second}
        
        return matchingIndex.first
    }
    
    fun getVerticalLines(input: List<List<String>>): List<String> {
        val verticalLines: MutableList<MutableList<String>> = MutableList(input[0].size) {MutableList(input.size) {""}}
        
        for(i in verticalLines.indices) {
            for(ii in verticalLines[0].indices) {
                verticalLines[i][ii] = input[ii][i]
            }
        }
        val v = verticalLines.map{
            return@map it.joinToString("")
        }
        
        return v
    }
    
    fun getHorizontalLines( input: List<List<String>>): List<String> {
        val horizontalLines: MutableList<MutableList<String>> = MutableList(input.size) {MutableList(input[0].size) {""}}

        for(i in horizontalLines.indices) {
            for(ii in horizontalLines[0].indices) {
                horizontalLines[i][ii] = input[i][ii]
            }
        }

        return horizontalLines.map{
            return@map it.joinToString("")
        }
    }
    
    fun isReflectionLine(reflexLine: Int, lines: List<String>): Boolean {
//        println("Reflex line: ${reflexLine}")
        for(i in 0 .. reflexLine) {
//            println("Comparing ${reflexLine - i} to ${reflexLine + i + 1}")
            try {
//                println("${lines[reflexLine - i]} == ${lines[reflexLine + i + 1]}")
                if(lines[reflexLine - i] != lines[reflexLine + i + 1]) {
//                    println("Returning false")
                    return false
                }
            } catch(e: IndexOutOfBoundsException) {
                // All good
            }
        }
        
//        println("Returning true")
        return true
    }
    
    fun getDifferences(string1: String, string2: String): Pair<Int, List<Int>> {
        var list1 = string1.toCharArray()
        var list2 = string2.toCharArray()

        var differences = 0
        var differentIndexes: MutableList<Int> = mutableListOf()
        list1.zip(list2).forEachIndexed{index, pair ->
            if(pair.first != pair.second) {
                differences++
                differentIndexes.add(index)
            }
        }
        return Pair(differences, differentIndexes)
    }
    
    data class Smudge(val index1: Int, val index2: Int, val subIndex: Int)

    fun fixSmudge(lines: List<String>): List<List<String>> {
        var listOfSmudges: MutableList<Smudge> = mutableListOf()
        lines.forEachIndexed{thisIndex, thisLine ->
            lines.forEachIndexed{otherIndex, otherLine ->
                val differences = getDifferences(thisLine, otherLine)
                if(differences.first == 1) {
                    listOfSmudges.add(Smudge(thisIndex, otherIndex, differences.second.first()))
                }
            }
        }
        
        val distinctListOfSmudges = listOfSmudges.distinctBy { Pair(max(it.index1, it.index2), min(it.index2, it.index1)) }

        distinctListOfSmudges.forEach{
//            println("Line ${it.index1}: ${lines[it.index1]} matches to Line ${it.index2}: ${lines[it.index2]}, charIndex: ${it.subIndex}: ${lines[it.index1][it.subIndex]}")
        }
        
        var newListOfLines: MutableList<List<String>> = mutableListOf()
        
        distinctListOfSmudges.forEach{smudge ->
            val newList = lines.toMutableList()
            val newChar = if(lines[smudge.index1][smudge.subIndex] == '#') '.' else '#'
            val mutableString = newList[smudge.index1].toMutableList()
            mutableString[smudge.subIndex] = newChar
            newList[smudge.index1] = mutableString.joinToString("")
            
            newListOfLines.add(newList)
        }

        return newListOfLines
    }
    
    fun getReflectionLine(input: List<String>, oldLine: Int? = null): Int {
//        input.forEach{
//            println(it)
//        }
        
        val verticalLines = getVerticalLines(input.map{it.split("").filter { itt -> itt.isNotBlank() || itt.isNotEmpty() }})
        val horizontalLines = getHorizontalLines(input.map{it.split("").filter { itt -> itt.isNotBlank() || itt.isNotEmpty() }})
        
//        verticalLines.forEachIndexed{index, it ->
//            println(it)
//        }
//        
//        
//        horizontalLines.forEachIndexed{index, it ->
//            println(it)
//        }
        var isOldHorizontal = false
        if(oldLine != null)
            isOldHorizontal = oldLine % 100 == 0

        val matchingVerticalLines = verticalLines.mapIndexed{index, it ->
            return@mapIndexed Pair(index, findMatchingLineIndex(it, verticalLines, index))
        }
        val matchingHorizontalLines = horizontalLines.mapIndexed{index, it ->
            return@mapIndexed  Pair(index, findMatchingLineIndex(it, horizontalLines, index))
        }
        
//        println("Matching Vertical Lines")
//        matchingVerticalLines.forEach{println(it)}
//        
//        println("Matching Horizontal Lines")
//        matchingHorizontalLines.forEach{println(it)}
        
//        val verticalNulls = matchingVerticalLines.filter{it.second == null}.count()
//        val horizontalNulls = matchingHorizontalLines.filter{it.second == null}.count()
        
        var isVertical = false
        var verticalReflexLines: List<Int?>
        var verticalReflexLine: Int? = 0
        
        var verticalDeltaLine = matchingVerticalLines.filter{it.second != null}.map{Pair(it.first, it.second!! - it.first)}
        if(oldLine != null && !isOldHorizontal) {
//            println("Filtering Vertical lines")
//            println("Old: ${verticalDeltaLine}")
            verticalDeltaLine = verticalDeltaLine.filter{it.first != oldLine - 1}
//            println("New: ${verticalDeltaLine}")
        }
//        println("Vertical Delta Line: ${verticalDeltaLine}")
        val verticalDeltas = verticalDeltaLine.filter{it.second == 1}
//        println("Vertical Deltas: ${verticalDeltas}")
        if(verticalDeltas.isNotEmpty()) {
//            println("Vertical Deltas: ${verticalDeltas}")
//            println("VerticalReflexLines: ${verticalDeltas.map{it.first}}")
            verticalReflexLines = verticalDeltas.map{it.first}
            
//            println("Map: ${verticalReflexLines.map{Pair(it, isReflectionLine(it, verticalLines))}}")
            verticalReflexLine = verticalReflexLines.map{Pair(it, isReflectionLine(it, verticalLines))}.filter{it.second}.firstOrNull()?.first
            isVertical = verticalReflexLine != null
        }
//        println()
        
        
//        println("Old Line: ${oldLine}; isVertical: ${isVertical}")
        if(isVertical && verticalReflexLine != null) {
//            println("oldLine != null ${ oldLine != null} && !isOldHorizontal ${!isOldHorizontal}")
            if(oldLine != null && !isOldHorizontal) {
                if(verticalReflexLine + 1 != oldLine) {
                    return verticalReflexLine + 1
                } else {
                    // Do nothing
                }
            } else {
//                println("Returning verticalReflexLine: ${verticalReflexLine + 1}")
                return verticalReflexLine + 1
            }
        }
        
        var isHorizontal = false
        var horizontalReflexLines: List<Int?>
        var horizontalReflexLine: Int? = 0
        
        var horizontalDeltaLine = matchingHorizontalLines.filter{it.second != null}.map{Pair(it.first, it.second!! - it.first)}
        if(oldLine != null && isOldHorizontal) {
//            println("Filtering Horizontal lines")
//            println("Old: ${horizontalDeltaLine}")
            horizontalDeltaLine = horizontalDeltaLine.filter{100 * (it.first + 1) != oldLine}
//            println("New: ${horizontalDeltaLine}")
        }
        
        val horizontalDeltas = horizontalDeltaLine.filter{it.second == 1}
        if(horizontalDeltas.isNotEmpty()) {
//            println("Horizontal Deltas: ${horizontalDeltas}")
            horizontalReflexLines = horizontalDeltas.map{it.first}
            
//            println("${horizontalReflexLines.map{Pair(it, isReflectionLine(it, horizontalLines))}}")
//            println(horizontalReflexLines.map{Pair(it, isReflectionLine(it, horizontalLines))}.filter{it.second})
            horizontalReflexLine = horizontalReflexLines.map{Pair(it, isReflectionLine(it, horizontalLines))}.filter{it.second}.firstOrNull()?.first
            isHorizontal = horizontalReflexLine != null
        }
        
        
        if(isHorizontal && horizontalReflexLine != null)
            if(oldLine != null) {
                if((horizontalReflexLine + 1) * 100 != oldLine) {
                    return (horizontalReflexLine + 1) * 100
                } else {
                // Do nothing
                }
            } else {
                return (horizontalReflexLine + 1) * 100
            }
        
        return -1
    }
    
    fun printReflection(reflectionLine: Int, input: List<String>) {
        var isHorizontal = reflectionLine % 100 == 0
        var reflectionIndex = reflectionLine
        if(isHorizontal)
            reflectionIndex = reflectionLine / 100

        if(isHorizontal) {
            input.forEachIndexed {index, line ->
                if(index == reflectionIndex)
                    println(List(line.length) {"-"}.joinToString(""))
                println(line)
            }
        } else {
            input.forEachIndexed{index, line ->
                line.forEachIndexed { index, c ->
                    if(index == reflectionIndex)
                        print("|")
                    print(c)
                }
                println()
            }
        }
    }
    
    fun getReflectionLine(input: List<String>, findSmudges: Boolean, oldLine: Int): Int {
        val verticalLines = getVerticalLines(input.map{it.split("").filter { itt -> itt.isNotBlank() || itt.isNotEmpty() }})
        val horizontalLines = getHorizontalLines(input.map{it.split("").filter { itt -> itt.isNotBlank() || itt.isNotEmpty() }})

        val newVerticalLines = fixSmudge(verticalLines)
        var reformattedVerticalLines = newVerticalLines.map{
            getVerticalLines(it.map{itt->itt.split("").filter{ittt-> ittt.isNotBlank() || ittt.isNotEmpty()}})
        }
//        reformattedVerticalLines.forEach{lines ->
//            lines.forEach{
//                println(it)
//            }
//            println()
//        }

        val newHorizontalLines = fixSmudge(horizontalLines)
////        println("New Horizontal Lines")
//        newHorizontalLines.forEach{lines ->
//            lines.forEach{
//                println(it)
//            }
//            println()
//        }
        
        var reflectionLines: MutableList<Int> = mutableListOf()
        
        val newHorizontalReflectionlines = newHorizontalLines.map{
            val reflectionLine = getReflectionLine(it, oldLine)
            printReflection(reflectionLine, it)
            println()
            return@map reflectionLine
        }

        val newVerticalReflectionLines = reformattedVerticalLines.map{
            val reflectionLine = getReflectionLine(it, oldLine)
            printReflection(reflectionLine, it)
            println()
            return@map reflectionLine
        }
        
        reflectionLines.addAll(newHorizontalReflectionlines.filter{it != -1})
        reflectionLines.addAll(newVerticalReflectionLines.filter{it != -1})
        
        reflectionLines.println()
        return reflectionLines.first()
    }
    
    
    fun part1(input: List<String>): Int {
        var inputs: MutableList<List<String>> = mutableListOf()
        var temp: MutableList<String> = mutableListOf()
        input.forEach{
            if(it.isBlank() || it.isEmpty() || it.trim() == "") {
                inputs.add(temp.toList())
                temp.clear()
            } else {
                temp.add(it)
            }
        }
        inputs.add(temp)
        
        val reflectionLines = inputs.map{
            val reflectionLine = getReflectionLine(it)
            printReflection(reflectionLine, it)
            println()
            return@map reflectionLine
        }
        
        reflectionLines.println()
        
        return reflectionLines.sum()
    }

    fun part2(input: List<String>): Int {

        var inputs: MutableList<List<String>> = mutableListOf()
        var temp: MutableList<String> = mutableListOf()
        input.forEach{
            if(it.isBlank() || it.isEmpty() || it.trim() == "") {
                inputs.add(temp.toList())
                temp.clear()
            } else {
                temp.add(it)
            }
        }
        inputs.add(temp)

        val oldReflectionLines = inputs.map{getReflectionLine(it)}

        val reflectionLines = inputs.mapIndexed{index, it ->
            val reflectionLine = getReflectionLine(it, true, oldReflectionLines[index])
            printReflection(reflectionLine, it)
            println()
            return@mapIndexed reflectionLine
        }
        
        return reflectionLines.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")

    val input = readInput("Day13")
//    part1(testInput).println()
    // Old: Horizontal 0: (0 + 1) * 100
    part2(input).println()
}
