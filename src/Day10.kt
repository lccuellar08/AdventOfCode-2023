import java.io.File

enum class PipeType(val char: Char) {
    VERTICAL('┃'),
    HORIZONTAL('━'),
    BEND_NORTH_EAST('┗'),
    BEND_NORTH_WEST('┛'),
    BEND_SOUTH_WEST('┓'),
    BEND_SOUTH_EAST('┏'),
    START('S'),
}

fun main() {
    val matchingLeftPipes = mapOf(
        PipeType.VERTICAL to listOf(PipeType.VERTICAL, PipeType.BEND_SOUTH_EAST, PipeType.BEND_SOUTH_WEST), // To its North
        PipeType.HORIZONTAL to listOf(PipeType.HORIZONTAL, PipeType.BEND_NORTH_EAST, PipeType.BEND_SOUTH_EAST), // To its West
        PipeType.BEND_NORTH_EAST to listOf(PipeType.VERTICAL, PipeType.BEND_SOUTH_EAST, PipeType.BEND_SOUTH_WEST), // To its North
        PipeType.BEND_NORTH_WEST to listOf(PipeType.VERTICAL, PipeType.BEND_SOUTH_EAST, PipeType.BEND_SOUTH_WEST), // To its North
        PipeType.BEND_SOUTH_WEST to listOf(PipeType.HORIZONTAL, PipeType.BEND_NORTH_EAST, PipeType.BEND_SOUTH_EAST), // To its West
        PipeType.BEND_SOUTH_EAST to listOf(PipeType.VERTICAL, PipeType.BEND_NORTH_WEST, PipeType.BEND_NORTH_EAST), // To its South
    )
    
    val matchingRightPipes = mapOf(
        PipeType.VERTICAL to listOf(PipeType.VERTICAL, PipeType.BEND_NORTH_EAST, PipeType.BEND_NORTH_WEST), // To its South
        PipeType.HORIZONTAL to listOf(PipeType.HORIZONTAL, PipeType.BEND_NORTH_WEST, PipeType.BEND_SOUTH_WEST), // To its East
        PipeType.BEND_NORTH_EAST to listOf(PipeType.HORIZONTAL, PipeType.BEND_NORTH_WEST, PipeType.BEND_SOUTH_WEST), // To its East
        PipeType.BEND_NORTH_WEST to listOf(PipeType.HORIZONTAL, PipeType.BEND_SOUTH_EAST, PipeType.BEND_NORTH_EAST), // To its West
        PipeType.BEND_SOUTH_WEST to listOf(PipeType.VERTICAL, PipeType.BEND_NORTH_EAST, PipeType.BEND_NORTH_WEST), // To its South
        PipeType.BEND_SOUTH_EAST to listOf(PipeType.HORIZONTAL, PipeType.BEND_NORTH_WEST, PipeType.BEND_SOUTH_WEST), // To its East
        )
    
    data class PipeNode(val pipeType: PipeType, val row: Int, val col: Int, var left: PipeNode? = null, var right: PipeNode? = null, var lengthFromStart: Int = 0)
    
    fun getAllPipes(input: List<List<String>>): List<PipeNode> {
        var allPipes = input.mapIndexed {row, line ->
            line.mapIndexed{col, it ->
                when(it) {
                    "|" -> PipeNode(PipeType.VERTICAL, row, col)
                    "-" -> PipeNode(PipeType.HORIZONTAL, row, col)
                    "L" -> PipeNode(PipeType.BEND_NORTH_EAST, row, col)
                    "J" -> PipeNode(PipeType.BEND_NORTH_WEST, row, col)
                    "7" -> PipeNode(PipeType.BEND_SOUTH_WEST, row, col)
                    "F" -> PipeNode(PipeType.BEND_SOUTH_EAST, row, col)
                    else -> PipeNode(PipeType.START, row, col)
                }
            }
        }
        
        allPipes.forEachIndexed{row, line ->
            line.forEachIndexed { col, pipeNode ->
                try {
                    if(pipeNode.pipeType != PipeType.START) {
                        var dRowLeft = 0
                        var dColLeft = 0
                        var dRowRight = 0
                        var dColRight = 0
                        when(pipeNode.pipeType) {
                            PipeType.VERTICAL -> {
                                dRowLeft = -1
                                dRowRight = 1
                            }
                            PipeType.HORIZONTAL -> {
                                dColLeft = -1
                                dColRight = 1
                            }
                            PipeType.BEND_NORTH_WEST -> {
                                dRowLeft = -1
                                dColRight = -1
                            }
                            PipeType.BEND_NORTH_EAST -> {
                                dRowLeft = -1
                                dColRight = 1
                            }
                            PipeType.BEND_SOUTH_WEST -> {
                                dColLeft = -1
                                dRowRight = 1
                            }
                            PipeType.BEND_SOUTH_EAST -> {
                                dRowLeft = 1
                                dColRight = 1
                            }
                            PipeType.START -> {
                            // Do nothing for now
                            }
                        }
                        var leftPipeNode = allPipes[row+dRowLeft][col+dColLeft]
                        var rightPipeNode = allPipes[row+dRowRight][col+dColRight]

                        var leftMatching = matchingLeftPipes[pipeNode.pipeType]
                        var rightMatching = matchingRightPipes[pipeNode.pipeType]

                        if(leftMatching != null && leftMatching.contains(leftPipeNode.pipeType)) {
                            pipeNode.left = leftPipeNode
                        }

                        if(rightMatching != null && rightMatching.contains(rightPipeNode.pipeType)) {
                            pipeNode.right = rightPipeNode
                        }
                    } else {
//                        val neighborNodes = listOf(
//                            allPipes[row-1][col],
//                            allPipes[row+1][col],
//                            allPipes[row][col-1],
//                            allPipes[row][col+1]
//                        )
//                        
//                        println("Starter neighbor nodes:")
//                        neighborNodes.forEach{println(it)}
//                        
//                        PipeType.values().filter{it != PipeType.START}.forEach{pipeType ->
//                            println("Checking ${pipeType}")
//                            var leftMatching = matchingLeftPipes[pipeType]
//                            var rightMatching = matchingRightPipes[pipeType]
//                            
//                            println("Left Matching: ${leftMatching}")
//                            println("Right Matching: ${rightMatching}")
//                            
//                            if(leftMatching != null && rightMatching != null) {
//                                var leftPossibilities = leftMatching.intersect(neighborNodes.map{it.pipeType})
//                                var rightPossibilities = rightMatching.intersect(neighborNodes.map{it.pipeType})
//                                
//                                println("Left Possibilities: ${leftPossibilities}")
//                                println("Right Possibilities: ${rightPossibilities}")
//                                
//                                if(leftPossibilities.size == 1 && rightPossibilities.size == 1) {
//                                    var leftNode = neighborNodes.find { it.pipeType == leftPossibilities.first() }
//                                    var rightNode = neighborNodes.find { it.pipeType == rightPossibilities.first() }
//                                    pipeNode.left = leftNode
//                                    pipeNode.right = rightNode
//                                    leftNode?.right = pipeNode
//                                    leftNode?.left = pipeNode
//                                }
//                            }
//                        }
                        
                    }
                } catch (e: IndexOutOfBoundsException) {
                    // Ignore
                }
            }
        }
        
        return allPipes.flatten()
    }
    
    fun getNodesInLoop(starterNode: PipeNode): List<PipeNode> {
        var leftCurrentNode = starterNode
        var leftPrevNode = starterNode.right
        var rightCurrentNode = starterNode
        var rightPrevNode = starterNode.left

        var cycleLength = 0

        var visitedNodes: MutableList<PipeNode> = mutableListOf()
        visitedNodes.add(starterNode)
        do {
            var leftNextNode = leftCurrentNode.left
            if(leftPrevNode?.row == leftNextNode?.row &&  leftPrevNode?.col == leftNextNode?.col) {
                leftNextNode = leftCurrentNode.right
            }

            if(visitedNodes.contains(leftNextNode)) {
                break
            }

            if(leftNextNode != null) {
                visitedNodes.add(leftNextNode)
                leftPrevNode = leftCurrentNode
                leftCurrentNode = leftNextNode
            } else {
                println("Hit Null")
                println("PipeNode(pipeType=${leftCurrentNode.pipeType.char}, row=${leftCurrentNode.row}, col=${leftCurrentNode.col}, left=${leftCurrentNode.left?.pipeType?.char}, right=${leftCurrentNode.right?.pipeType?.char})")
            }

            var rightNextNode = rightCurrentNode.right
            if(rightPrevNode?.row == rightNextNode?.row && rightPrevNode?.col == rightNextNode?.col) {
                rightNextNode = rightCurrentNode.left
            }
            if(visitedNodes.contains(rightNextNode)) {
                break
            }

            if(rightNextNode != null) {
                visitedNodes.add(rightNextNode)
                rightPrevNode = rightCurrentNode
                rightCurrentNode = rightNextNode
            } else {
                println("Hit Null")
                println("PipeNode(pipeType=${rightCurrentNode.pipeType.char}, row=${rightCurrentNode.row}, col=${rightCurrentNode.col}, left=${rightCurrentNode.left?.pipeType?.char}, right=${rightCurrentNode.right?.pipeType?.char})")
            }
            cycleLength++

            if(leftNextNode == null && rightNextNode == null) {
                break
            }
        }while(!(leftCurrentNode.row == rightCurrentNode.row && leftCurrentNode.col == rightCurrentNode.col))
        return visitedNodes
    }
    
    fun calculateMaxLength(starterNode: PipeNode): Int {
        var leftCurrentNode: PipeNode? = starterNode
        var leftPrevNode = starterNode.right
        var rightCurrentNode: PipeNode? = starterNode
        var rightPrevNode = starterNode.left
        
        var cycleLength = 0
        
        var visitedNodes: MutableList<String> = mutableListOf()
        visitedNodes.add("${starterNode.row}+${starterNode.col}")
        do {
            var leftNextNode = leftCurrentNode?.left
            if(leftPrevNode?.row == leftNextNode?.row &&  leftPrevNode?.col == leftNextNode?.col) {
                leftNextNode = leftCurrentNode?.right
            }
            
            if(visitedNodes.contains("${leftNextNode?.row}${leftNextNode?.col}")) {
                break
            }
            
            if(leftNextNode != null) {
                visitedNodes.add("${leftNextNode?.row}+${leftNextNode?.col}")
                leftPrevNode = leftCurrentNode
                leftCurrentNode = leftNextNode
            } else {
                println("Hit Null")
                println("PipeNode(pipeType=${leftCurrentNode?.pipeType?.char}, row=${leftCurrentNode?.row}, col=${leftCurrentNode?.col}, left=${leftCurrentNode?.left?.pipeType?.char}, right=${leftCurrentNode?.right?.pipeType?.char})")
            }
            
            var rightNextNode = rightCurrentNode?.right
            if(rightPrevNode?.row == rightNextNode?.row && rightPrevNode?.col == rightNextNode?.col) {
                rightNextNode = rightCurrentNode?.left
            }
            if(visitedNodes.contains("${rightNextNode?.row}${rightNextNode?.col}")) {
                break
            }

            if(rightNextNode != null) {
                visitedNodes.add("${rightNextNode?.row}+${rightNextNode?.col}")
                rightPrevNode = rightCurrentNode
                rightCurrentNode = rightNextNode
            } else {
                println("Hit Null")
                println("PipeNode(pipeType=${rightCurrentNode?.pipeType?.char}, row=${rightCurrentNode?.row}, col=${rightCurrentNode?.col}, left=${rightCurrentNode?.left?.pipeType?.char}, right=${rightCurrentNode?.right?.pipeType?.char})")
            }
            cycleLength++
            
            if(leftNextNode == null && rightNextNode == null) {
                break
            }
        }while(!(leftCurrentNode?.row == rightCurrentNode?.row && leftCurrentNode?.col == rightCurrentNode?.col))
        return cycleLength
    }
    
    fun getPipesEncountered(startX: Int, startY: Int, dX: Int, dY: Int, maxX: Int, maxY: Int, listOfNodes: List<PipeNode>): Int {
        var pipesEncountered = 0
        try {
            var x = startX
            var y = startY
            while(x >= 0 && y >= 0 && x < maxX && y < maxY) {
                x += dX
                y += dY
                var node = listOfNodes.filter { node -> node.row == y && node.col == x }.firstOrNull()
                if(node != null) {
                    pipesEncountered++
                }
            }
        } catch(e: java.lang.IndexOutOfBoundsException) {
            // We're good cuh
        }
        
        return pipesEncountered
    }
    
    fun getMatchingPipe(pipeType: PipeType): List<PipeType> {
        var matchingPipes: List<PipeType>
            
        matchingPipes = when(pipeType) {
            PipeType.VERTICAL -> {
                listOf(PipeType.VERTICAL)
            }
            PipeType.BEND_NORTH_EAST -> {
                listOf(PipeType.VERTICAL, PipeType.BEND_NORTH_EAST, PipeType.BEND_NORTH_WEST)
            }
            PipeType.BEND_NORTH_WEST -> {
                listOf(PipeType.VERTICAL, PipeType.BEND_NORTH_EAST, PipeType.BEND_NORTH_WEST)
            }
            PipeType.BEND_SOUTH_EAST -> {
                listOf(PipeType.VERTICAL, PipeType.BEND_SOUTH_EAST, PipeType.BEND_SOUTH_WEST)
            }
            PipeType.BEND_SOUTH_WEST -> {
                listOf(PipeType.VERTICAL, PipeType.BEND_SOUTH_EAST, PipeType.BEND_SOUTH_WEST)
            }
            else -> {
                listOf()
            }
        }
        
        return matchingPipes
    }
    
    fun getEnclosedTiles(listOfNodes: List<PipeNode>, sizeX: Int, sizeY: Int): List<Pair<Int,Int>> {
        var enclosedTiles: MutableList<Pair<Int,Int>> = mutableListOf()
        val upperBoundPipes = listOf(PipeType.VERTICAL, PipeType.BEND_NORTH_EAST, PipeType.BEND_NORTH_WEST)
        val lowerBoundPipes = listOf(PipeType.VERTICAL, PipeType.BEND_SOUTH_EAST, PipeType.BEND_SOUTH_WEST)
        
        for(row in 0 until sizeY) {
            var visitedPipeUpper: PipeType? = null
            var visiterPipeLower: PipeType? = null
            for(col in 0 until sizeX) {
                val node = listOfNodes.filter { it.row == row && it.col == col }.firstOrNull()
                
                if(node != null) {
                    if(visitedPipeUpper == null && upperBoundPipes.contains(node.pipeType)) {
                        visitedPipeUpper = node.pipeType
                    } else if(visitedPipeUpper != null && upperBoundPipes.contains(node.pipeType)) {
                        visitedPipeUpper = null
                    }
                    
                    if(visiterPipeLower == null && lowerBoundPipes.contains(node.pipeType)) {
                        visiterPipeLower = node.pipeType
                    } else if(visiterPipeLower != null && lowerBoundPipes.contains(node.pipeType)) {
                        visiterPipeLower = null
                    }
                } else {
                    if(visitedPipeUpper != null && visiterPipeLower != null) {
                        enclosedTiles.add(Pair(col,row))
                    }
                }
            }
        }
        
        return enclosedTiles.distinct()
    }
    
    
    fun part1(input: List<String>): Int {
        val inputParsed = input.map{it.map{itt->itt.toString()}}
        val allPipes = getAllPipes(inputParsed)
//        allPipes.map{println("PipeNode(pipeType=${it.pipeType.char}, row=${it.row}, col=${it.col}, left=${it.left?.pipeType?.char}, right=${it.right?.pipeType?.char})")}
        
        val starterNode = allPipes.find{it.row == 35 && it.col == 17}
//        val starterNode = allPipes.find{it.row == 0 && it.col == 0}
//        val starterNode = allPipes.find{it.row == 34 && it.col == 279}
        var maxSize = 0
        if(starterNode != null) {
            maxSize = calculateMaxLength(starterNode)
        }
        
        
        
        return maxSize
    }

    fun part2(input: List<String>): Int {
        val inputParsed = input.map{it.map{itt->itt.toString()}}
        val allPipes = getAllPipes(inputParsed)
        //        allPipes.map{println("PipeNode(pipeType=${it.pipeType.char}, row=${it.row}, col=${it.col}, left=${it.left?.pipeType?.char}, right=${it.right?.pipeType?.char})")}

        val starterNode = allPipes.find{it.row == 35 && it.col == 17}
//        val starterNode = allPipes.find{it.row == 34 && it.col == 279}
        println(starterNode?.pipeType)
        var enclosedTiles = 0
        if(starterNode != null) {
            val listOfNodes = getNodesInLoop(starterNode)
            var tiles = getEnclosedTiles(listOfNodes, inputParsed[0].size, inputParsed.size)
            File("Day10_Output_Test.txt").printWriter().use {out ->
                for(row in 0 until inputParsed.size) {
                    var lineToWrite = ""
                    for(col in 0 until inputParsed[0].size) {
                        var node = listOfNodes.filter { it.row == row && it.col == col }.firstOrNull()
                        if(node != null) {
//                            lineToWrite += node.pipeType.char
                            lineToWrite += " "
                        } else {
                            if(tiles.filter{it.first == col && it.second == row}.isNotEmpty()) {
                                lineToWrite += "I"
                            } else {
                                lineToWrite += "."
                            }
                        }
                    }
                    out.println(lineToWrite)
                }
            }
            
            enclosedTiles = tiles.size
        }

        return enclosedTiles
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
