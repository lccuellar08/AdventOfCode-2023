import java.lang.IndexOutOfBoundsException

fun main() {
    
    fun getPlatform(input: List<String>): Array<CharArray> {
        val platform: Array<CharArray> = Array(input.size) {CharArray(input[0].length) {'.'}}
        
        input.forEachIndexed{rowIndex, row ->
            row.forEachIndexed { colIndex, c ->
                platform[rowIndex][colIndex] = c
            }
        }
        
        return platform
    }
    
    fun rollNorth(platform: Array<CharArray>, column: Int) {
        var movements = 0
        platform.forEachIndexed { rowIndex, _ ->
            if(rowIndex > 0) {
                val prevSpot = platform[rowIndex -1][column]
                val thisSpot = platform[rowIndex][column]
                if(prevSpot == '.' && thisSpot == 'O'){
                    platform[rowIndex -1][column] = thisSpot
                    platform[rowIndex][column] = '.'
                    movements++
                } 
            }
        }
        if(movements > 0)
            rollNorth(platform, column)
    }
    
    fun rollNorthSouth(platform: Array<CharArray>, colIndex: Int, dy: Int) {
        var movements = 0
        
        var indices = platform.indices.toList()
        if(dy > 0)
            indices = indices.reversed()
        
        indices.forEach { rowIndex ->
            try {
                val prevSpot = platform[rowIndex + dy][colIndex]
                val thisSpot = platform[rowIndex][colIndex]
                if(prevSpot == '.' && thisSpot == 'O'){
                    platform[rowIndex + dy][colIndex] = thisSpot
                    platform[rowIndex][colIndex] = '.'
                    movements++
                } 
            } catch(e: IndexOutOfBoundsException) {
            // All good
            }
        }
        
        if(movements > 0)
            rollNorthSouth(platform, colIndex, dy)
    }
    
    fun rollEastWest(platform: Array<CharArray>, rowIndex: Int, dx: Int) {
        var movements = 0

        var indices = platform[0].indices.toList()
        if(dx > 0)
            indices = indices.reversed()

        indices.forEach { colIndex->
            try {
                val prevSpot = platform[rowIndex][colIndex + dx]
                val thisSpot = platform[rowIndex][colIndex]
                if(prevSpot == '.' && thisSpot == 'O'){
                    platform[rowIndex][colIndex + dx] = thisSpot
                    platform[rowIndex][colIndex] = '.'
                    movements++
                } 
            } catch(e: IndexOutOfBoundsException) {
            // All good
            }
        }

        if(movements > 0)
            rollEastWest(platform, rowIndex, dx)
    }
    
    fun rollDirection(platform: Array<CharArray>, charIndex: Int, dx: Int, dy: Int) {
        if(dy != 0) {
            rollNorthSouth(platform, charIndex, dy)
        }
        else {
            rollEastWest(platform, charIndex, dx)
        }
    }
    
    fun getTotalLoad(platform: Array<CharArray>): Int {
        var totalLoad = 0
        
        platform.forEachIndexed { index, row ->
            val totalRocks = row.filter { it == 'O' }.count()
            totalLoad += totalRocks * (platform.size - index)
        }
        
        return totalLoad
    }
    
    fun spinCycle(platform: Array<CharArray>) {
        for(c in platform[0].indices) {
            rollDirection(platform, c, 0, -1) // North
        }
        for(r in platform.indices) {
            rollDirection(platform, r, -1, 0) // West
        }
        for(c in platform[0].indices) {
            rollDirection(platform, c, 0, 1) // South
        }
        for(r in platform.indices) {
            rollDirection(platform, r, 1, 0) // East
        }
    }

    fun part1(input: List<String>): Int {
        val platform = getPlatform(input)
        
        println("Before:")
        platform.forEach{
            println(it.joinToString(" "))
        }
        
//        for(c in platform[0].indices) {
//            rollDirection(platform, c, 0, 1)
//        }
        
        for(r in platform.indices) {
            rollDirection(platform, r, 1, 0)
        }
        
        println("After:")
        platform.forEach{
            println(it.joinToString(" "))
        }
        
        return getTotalLoad(platform)
    }

    fun getHash(platform: Array<CharArray>): String {
        return platform.joinToString("") {it.joinToString("")}
    }
    
    fun part2(input: List<String>): Int {
        val platform = getPlatform(input)

        println("Before:")
        platform.forEach{
            println(it.joinToString(" "))
        }

        val visitedMap: MutableMap<String, Int> = mutableMapOf() 
        visitedMap[getHash(platform)] = 0
        
        var i = 0
        var cycleFirst = -1
        var cycleSecond = 0
        while(true) {
            spinCycle(platform)
            i++
            var hash = getHash(platform)
            if(visitedMap.containsKey(hash)) {
                val visitTimes = visitedMap[hash]
                if(visitTimes == 2) {
                    cycleSecond = i
                    visitedMap[hash] = 3
                    println("2nd Cycle found at ${i} iterations")
                    break
                } else {
                    visitedMap[hash] = 2
                    if(cycleFirst == -1) {
                        cycleFirst = i
                        println("Cycle found at ${i} iterations")
                    }
                }
            } else {
                visitedMap[hash] = 1
            }
        }
        var cycleLength = cycleSecond - cycleFirst
        val totalCycles = 1000000000L
        var totalRuntime = ((totalCycles - cycleFirst) % cycleLength) + cycleFirst
        val newPlatform = getPlatform(input)
        println("Cycle length: ${cycleLength}; Cycle first: ${cycleFirst}; Cycle second: ${cycleSecond}; total runtime: ${totalRuntime}")
        for(newI in 0 until totalRuntime) {
            spinCycle(newPlatform)
//            val totalLoad = getTotalLoad(newPlatform)
//            println("${newI}: $totalLoad")
        }
        
        

        println("After:")
        newPlatform.forEach{
            println(it.joinToString(" "))
        }

        return getTotalLoad(newPlatform)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")

    val input = readInput("Day14")

//    part1(testInput).println()
    part2(input).println()
}
