import kotlin.math.*
import java.util.PriorityQueue

fun main() {
    fun expandUniverse(input: List<List<Char>>): List<List<Char>> {
        var emptyRows: MutableList<Int> = mutableListOf()
        for(i in input.indices) {
            var row = input[i]
            if(row.filter{it != '.'}.isEmpty()) {
                emptyRows.add(i)
            }
        }
        
        var emptyCols: MutableList<Int> = mutableListOf() 
        for(i in input[0].indices) {
            var col = input.map{it[i]}
            if(col.filter{it != '.'}.isEmpty()) {
                emptyCols.add(i)
            }
        }
        
        var newUniverse = input.toMutableList()
        emptyRows.forEachIndexed{rowIndex, row ->
            val oldRow = newUniverse[row + rowIndex]
            newUniverse.add(row + rowIndex, oldRow)
        }
        
        emptyCols.forEachIndexed{colIndex, col ->
            val oldCol = newUniverse.map{it[col + colIndex]}
            newUniverse.forEachIndexed{rowIndex, row ->
                val newRow = row.toMutableList()
                newRow.add(col + colIndex, '.')
                newUniverse[rowIndex] = newRow
            }
        }
        
        return newUniverse
    }
    
    fun expandUniverse(input: List<List<Char>>, expansionRate: Int): List<List<Char>> {
        var emptyRows: MutableList<Int> = mutableListOf()
        for(i in input.indices) {
            var row = input[i]
            if(row.filter{it != '.'}.isEmpty()) {
                emptyRows.add(i)
            }
        }

        var emptyCols: MutableList<Int> = mutableListOf() 
        for(i in input[0].indices) {
            var col = input.map{it[i]}
            if(col.filter{it != '.'}.isEmpty()) {
                emptyCols.add(i)
            }
        }

        var newUniverse = input.toMutableList()
        var rowsAdded = 0
//        println(emptyRows)
        emptyRows.forEachIndexed{rowIndex, row ->
            val oldRow = newUniverse[row + rowsAdded]
            for(n in 1 until expansionRate) {
                newUniverse.add(row + rowsAdded, oldRow)
            }
            rowsAdded += expansionRate - 1
        }
        var colsAdded = 0
        emptyCols.forEachIndexed{colIndex, col ->
            val oldCol = newUniverse.map{it[col + colsAdded]}
            newUniverse.forEachIndexed{rowIndex, row ->
                val newRow = row.toMutableList()
                for(n in 1 until expansionRate) {
                    newRow.add(col + colsAdded, '.')
                    newUniverse[rowIndex] = newRow
                }
            }
            colsAdded += expansionRate - 1
        }

        return newUniverse
    }
    
    // Pair(x,y)
    fun getGalaxies(input: List<List<Char>>): List<Pair<Int,Int>> {
        var galaxies: MutableList<Pair<Int,Int>> = mutableListOf()
        for(row in input.indices) {
            for(col in input[row].indices) {
                if(input[row][col] == '#') {
                    galaxies.add(Pair(col, row))
                }
            }
        }
        return galaxies
    }
    
    fun getShortestPath(x1: Int, y1: Int, x2: Int, y2: Int): Float {
        return sqrt(
            (x2.toFloat() - x1.toFloat()).pow(2f) - (y2.toFloat() - y1.toFloat()).pow(2f)
        )
    }
    
    fun getHeuristic(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        return abs(y2 - y1) + abs(x2 - x1)
    }
    
    fun getNeighbors(point: Pair<Int,Int>, universe: List<List<Char>>): List<Pair<Int,Int>> {
        val listOfNeighbors: MutableList<Pair<Int,Int>> = mutableListOf()
        val x = point.first
        val y = point.second
        
        try {
            // North
            listOfNeighbors.add(Pair(x,y-1))
            // South
            listOfNeighbors.add(Pair(x,y+1))
            // West
            listOfNeighbors.add(Pair(x-1,y))
            // East
            listOfNeighbors.add(Pair(x+1,y))
        } catch(e: IndexOutOfBoundsException) {
            // Do nothing
        }
        
        return listOfNeighbors
    }
    
    fun getEdgeWeight(current: Pair<Int,Int>, neighbor: Pair<Int,Int>): Int {
        return 1
    }
    
    fun reconstructPath(cameFrom: Map<Pair<Int,Int>, Pair<Int,Int>>, current: Pair<Int,Int>): List<Pair<Int,Int>> {
        val totalPath: MutableList<Pair<Int,Int>> = mutableListOf()
        var temp: Pair<Int,Int>? = current
        totalPath.add(current)
        while(temp in cameFrom.keys) {
            temp = cameFrom[temp]
            if(temp != null)
                totalPath.add(0, temp)
        }
        return totalPath
    }
    
    fun getAStar(start: Pair<Int,Int>, goal: Pair<Int,Int>, universe: List<List<Char>>): List<Pair<Int,Int>> {
        val openSet: MutableSet<Pair<Int,Int>> = mutableSetOf()
        openSet.add(start)
        
        val cameFrom: MutableMap<Pair<Int,Int>, Pair<Int,Int>> = mutableMapOf()
        
        val gScore: MutableMap<Pair<Int,Int>, Int> = mutableMapOf()
        gScore[start] = 0
        
        val fScore: MutableMap<Pair<Int,Int>, Int> = mutableMapOf()
        fScore[start] = getHeuristic(start.first, start.second, goal.first, goal.second)
        
        while(openSet.isNotEmpty()) {
            val current = fScore
                .filter{it ->
                    openSet.contains(it.key)
                }.minBy {it.value}
            if(current.key == goal) {
                return reconstructPath(cameFrom, current.key)
            }
            
            openSet.remove(current.key)
            val neighbors = getNeighbors(current.key, universe)
            for(neighbor in neighbors) {
                val tentativeGScore = gScore.getOrDefault(neighbor, 0) + getEdgeWeight(current.key, neighbor)
                if(tentativeGScore < gScore.getOrDefault(neighbor, Int.MAX_VALUE)) {
                    cameFrom[neighbor] = current.key
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = tentativeGScore + getHeuristic(neighbor.first, neighbor.second, goal.first, goal.second)
                    if(!openSet.contains(neighbor)) {
                        openSet.add(neighbor)
                    }
                }
            }
        }
        return emptyList()
    }
    
    fun getAllShortestPaths(listOfGalaxies: List<Pair<Int,Int>>, universe: List<List<Char>>): List<Int> {
        var shortestPaths: MutableList<Int> = mutableListOf()
        for(index1 in 0 until listOfGalaxies.size) {
            for(index2 in index1 + 1 until listOfGalaxies.size) {
                val galaxy1 = listOfGalaxies[index1]
                val galaxy2 = listOfGalaxies[index2]
                if(galaxy1 != galaxy2) {
                    val path = getAStar(galaxy1, galaxy2, universe)
                    shortestPaths.add(path.size - 1)
//                    println("Path between ${index1 + 1} and ${index2 + 1}")
//                    println("${path.size -1 }")
//                    println("Path between ${index1 + 1} and ${index2 + 1}: ${path.size.toFloat()} \t\tGalaxy ${index1 + 1}: ${galaxy1}\t\tGalaxy ${index2 + 1}: ${galaxy2}")
                }
            }
        }
        return shortestPaths
    }
    
    fun part1(input: List<String>): Int {
        val newUniverse = expandUniverse(input.map{it.map{itt -> itt}})
        newUniverse.forEach{
            println(it.map{it.toString()}.joinToString(""))
        }
        val galaxies = getGalaxies(newUniverse)
        println("Num of galaxies: ${galaxies.size}")
        val allPaths = getAllShortestPaths(galaxies, newUniverse)
        
        return allPaths.sum()
    }
    
    fun printUniverse(universe: List<List<Char>>) {
        print("\t")
        universe[0].indices.forEach{it ->
            print("${it + 1} ")
        }
        println()
        universe.forEachIndexed{index, it ->
            print("${index + 1}:\t")
            println(it.map{it.toString()}.joinToString(" "))
        }
        
    }

    fun part2(input: List<String>, expansionRate: Int): Long {
        val oldUniverse = expandUniverse(input.map{it.map{itt -> itt}}, 1)
        val galaxies = getGalaxies(oldUniverse)
        println("Num of galaxies: ${galaxies.size}")
        val oldAllPaths = getAllShortestPaths(galaxies, oldUniverse)
        
        val oldPathSum = oldAllPaths.sum() // 8618476
        val newPathSum = part1(input)// 9329143
        
        println(oldPathSum)
        
        val diff = (newPathSum - oldPathSum).toLong() // 710667
        
        return oldPathSum.toLong() + (diff * (expansionRate.toLong() - 1L))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")

    val input = readInput("Day11")
    part1(testInput).println()
    part2(input, 1000000).println()
}
