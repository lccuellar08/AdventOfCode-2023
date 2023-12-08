fun main() {
    data class Direction(val left: String, val right: String)
    
    fun transverse(directionsMap: Map<String, Direction>, directions: String, start: String, end: String): Int {
        var steps = 0
        var currentStep = start
        
        while(currentStep != end) {
            val stepDirection = directions[steps % directions.length]
            when(stepDirection) {
                'L' -> currentStep = directionsMap[currentStep]?.left + ""
                'R' -> currentStep = directionsMap[currentStep]?.right + ""
            }
            steps++
        }
        
        return steps
    }
    
    
    fun part1(input: List<String>): Int {
        val directionsMap: MutableMap<String, Direction> = mutableMapOf()
        var directions = ""
        
        input.forEach{
            val tokens = it.split("=")
            if(tokens.size > 1) {
                val element = tokens[0].trim()
                val tokenDirections = tokens[1].trim().split(",")
                val direction = Direction(tokenDirections[0].trim().replace('(',' ').trim(), tokenDirections[1].trim().replace(')',' ').trim())
                directionsMap[element] = direction
            } else {
                directions = it
            }
        }
        
        
        return transverse(directionsMap, directions, "AAA", "ZZZ")
    }
    
    data class DirectionNode(val value: String, var left: DirectionNode?, var right: DirectionNode?) 
    
    fun getDirectionTree(directionsMap: Map<String, Direction>): List<DirectionNode> {
        var nodesList = directionsMap.keys.map{DirectionNode(it, null, null)}.toMutableList()
        directionsMap.forEach {value, direction ->
            val node = nodesList.filter {it.value == value}.first()
            val leftNode = nodesList.filter {it.value == direction.left}.first()
            val rightNode = nodesList.filter {it.value == direction.right}.first()
            if(node !== leftNode) node.left = leftNode
            if(node !== rightNode) node.right = rightNode
        }
        
        return nodesList.filter{it.value[2] == 'A'}
    }
    
    fun getShortestPath(start: DirectionNode, end: Char): List<String>? {
        val nodeQueue: MutableList<Pair<DirectionNode, List<String>>> = mutableListOf()
        val explored: MutableSet<String> = mutableSetOf()
        nodeQueue.add(Pair(start, listOf(start.value)))

        while (nodeQueue.isNotEmpty()) {
            val (node, path) = nodeQueue.removeAt(0)
//            println("Exploring node: ${node.value} path: ${path.joinToString(" -> ")}")

            if (node.value[2] == end) {
                return path
            }

            val leftNode = node.left
            if (leftNode != null && !explored.contains(leftNode.value)) {
                explored.add(leftNode.value)
                nodeQueue.add(Pair(leftNode, path + leftNode.value))
            }

            val rightNode = node.right
            if (rightNode != null && !explored.contains(rightNode.value)) {
                explored.add(rightNode.value)
                nodeQueue.add(Pair(rightNode, path + rightNode.value))
            }
        }

        return null // If no path is found
    }
    
    
    fun ghostTransverse(directionsMap: Map<String, Direction>, nodesList: List<DirectionNode>, directions: String, startChar: Char, endChar: Char): Int {
        var startingSteps = directionsMap.keys.filter{it[2] == startChar}
        val lengths: MutableList<Int> = MutableList(startingSteps.size) { 0 }
        
        startingSteps.forEachIndexed {index, start ->
            val node = nodesList.find{it.value == start}
            if(node != null) {
                val shortestPath = getShortestPath(node, 'Z')
                if(shortestPath != null) {
//                    println(shortestPath)
                    lengths[index] = shortestPath.size - 1
                }
            }
        }

        lengths.println()

        return 0
    }

    fun part2(input: List<String>): Int {
        val directionsMap: MutableMap<String, Direction> = mutableMapOf()
        var directions = ""

        input.forEach{
            val tokens = it.split("=")
            if(tokens.size > 1) {
                val element = tokens[0].trim()
                val tokenDirections = tokens[1].trim().split(",")
                val direction = Direction(tokenDirections[0].trim().replace('(',' ').trim(), tokenDirections[1].trim().replace(')',' ').trim())
                directionsMap[element] = direction
            } else {
                directions = it
            }
        }

        var nodesList = getDirectionTree(directionsMap)

        return ghostTransverse(directionsMap, nodesList, directions, 'A', 'Z')
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
