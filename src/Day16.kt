fun main() {
    fun getContraption(input: List<String>): Array<CharArray> {
        val contraption: Array<CharArray> = Array(input.size) {CharArray(input[0].length) {'.'}}

        input.forEachIndexed{rowIndex, row ->
            row.forEachIndexed { colIndex, c ->
                contraption[rowIndex][colIndex] = c
            }
        }

        return contraption
    }
    
    /**
     * dx 1 / Goes to dx 0, dy -1
     * dx -1 / Goes to dx 0, dy 1
     * dy 1 / Goes to dx -1, dy 0
     * dy -1 / Goes to dx 1, dy 0
     *

     */
    
    data class VisitedTile(val row: Int, val col: Int, val dx: Int, val dy: Int)
    
    fun getEnergizedTiles(entryRow: Int, entryCol: Int, entryDX: Int, entryDY: Int,  contraption: Array<CharArray>, visitedTiles: MutableList<VisitedTile>): List<Pair<Int,Int>> {
        val energizedTiles: MutableList<Pair<Int,Int>> = mutableListOf()
        var row = entryRow
        var col = entryCol
        var tile: Char
        try {
            tile = contraption[row][col]
        } catch(e: IndexOutOfBoundsException) {
            return energizedTiles
        }
        var dx = entryDX
        var dy = entryDY
        
        
        var stopTile = if(dx != 0) '|' else '-'
        
        while(tile != stopTile) {
            if(visitedTiles.contains(VisitedTile(row, col, dx, dy))) {
                return energizedTiles
            } else {
                visitedTiles.add(VisitedTile(row, col, dx, dy))
            }
            energizedTiles.add(Pair(row, col))
            if(tile == '.') {
                // 
            } else if(tile == '/') {
                if(dx == 0) {
                    dx = dy * -1
                    dy = 0
                } else {
                    dy = dx * -1
                    dx = 0
                }
            } else if(tile == '\\') {
                if(dx == 0) {
                    dx = dy
                    dy = 0
                } else {
                    dy = dx
                    dx = 0
                }
            }
            try {
                stopTile = if(dx != 0) '|' else '-'
                row = row + dy
                col = col + dx
                tile = contraption[row][col]
            } catch(e: IndexOutOfBoundsException) {
                break
            }
        }
        
        if(tile == stopTile) {
            energizedTiles.add(Pair(row, col))  
            if(tile == '-') {
                // Westwards
                energizedTiles.addAll(getEnergizedTiles(
                    row,
                    col - 1,
                    -1,
                    0,
                    contraption,
                    visitedTiles
                ))
                // Eastwards
                energizedTiles.addAll(getEnergizedTiles(
                    row,
                    col + 1,
                    1,
                    0,
                    contraption,
                    visitedTiles
                ))
            } else {
                // Northwards
                energizedTiles.addAll(getEnergizedTiles(
                    row - 1,
                    col,
                    0,
                    -1,
                    contraption,
                    visitedTiles
                ))
                // Southwards
                energizedTiles.addAll(getEnergizedTiles(
                    row + 1,
                    col,
                    0,
                    1,
                    contraption,
                    visitedTiles
                ))
            }
        }
        
        return energizedTiles
    }
    
    fun part1(input: List<String>): Int {
        val contraption = getContraption(input)
        val energizedTiles = getEnergizedTiles(
            0,
            0,
            1,
            0,
            contraption,
            mutableListOf())
        
//        contraption.forEachIndexed { rowIndex, chars ->
//            chars.forEachIndexed { colIndex, c ->
//                if(energizedTiles.contains(Pair(rowIndex, colIndex))) {
//                        print("#")
//                    } else {
//                        print(".")
//                    }
//            }
//            println()
//        }
        
        return energizedTiles.distinct().count()
    }

    fun part2(input: List<String>): Int {
        val contraption = getContraption(input)
        val edgeTiles: MutableMap<Pair<Int,Int>, Pair<Int,Int>> = mutableMapOf()
        
        // Top Row && bottom row
        for( col in 0 until contraption[0].size) {
            edgeTiles[Pair(0,col)] = Pair(0, 1) // Top Southbound
            edgeTiles[Pair(contraption.size -1,col)] = Pair(0, -1) // Bottom Southbound
        }
        // Left and Right rows
        for( row in 0 until contraption.size) {
            edgeTiles[Pair(row, 0)] = Pair(1, 0) // Left eastbound
            edgeTiles[Pair(row, contraption[0].size - 1)] = Pair(-1, 0) // Right Eastbound
        }
        
        val energizedTilesList = edgeTiles.keys.map { (row, col) ->
            val (dx, dy) = edgeTiles.getOrDefault(Pair(row,col), Pair(0,0))
            getEnergizedTiles(
                row,
                col,
                dx,
                dy,
                contraption,
                mutableListOf()
            ).distinct().count()
        }
        
        val energizedTiles = energizedTilesList.max()

//        contraption.forEachIndexed { rowIndex, chars ->
//            chars.forEachIndexed { colIndex, c ->
//                if(energizedTiles.contains(Pair(rowIndex, colIndex))) {
//                    print("#")
//                } else {
//                    print(".")
//                }
//            }
//            println()
//        }

        return energizedTiles
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
