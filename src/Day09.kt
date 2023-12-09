fun main() {
    
    fun getNextValue(history: List<Int>): Int {
        var listOfHistories: MutableList<MutableList<Int>> = mutableListOf()
        
        var nextHistory = history
        while(nextHistory.filter{it != 0}.isNotEmpty()) {
            listOfHistories.add(nextHistory.toMutableList())
            var newHistory = nextHistory.windowed(2, 1).map{ints ->  ints.last() - ints.first()}
            nextHistory = newHistory
        }
        listOfHistories.add(nextHistory.toMutableList())
        listOfHistories = listOfHistories.reversed().toMutableList()
        
        listOfHistories.forEachIndexed {index, it ->
            if(it.last() == 0) {
                it.add(0)
            } else {
                if(index == 0) {
                    println("Will get error")
                    println(it)
                }
                it.add(it.last() + listOfHistories[index - 1].last())
            }
        }
        
        listOfHistories.forEach{println(it)}
        
        return listOfHistories.last().last()
    }
    
    fun getPrevValue(history: List<Int>): Int {
        var listOfHistories: MutableList<MutableList<Int>> = mutableListOf()

        var nextHistory = history
        while(nextHistory.filter{it != 0}.isNotEmpty()) {
            listOfHistories.add(nextHistory.toMutableList())
            var newHistory = nextHistory.windowed(2, 1).map{ints ->  ints.last() - ints.first()}
            nextHistory = newHistory
        }
        listOfHistories.add(nextHistory.toMutableList())
        listOfHistories = listOfHistories.reversed().toMutableList()

        listOfHistories.forEachIndexed {index, it ->
            if(it.last() == 0) {
                it.add(0, 0)
            } else {
                if(index == 0) {
                    println("Will get error")
                    println(it)
                }
                it.add(0, it.first() - listOfHistories[index - 1].first())
            }
        }

        listOfHistories.forEach{println(it)}

        return listOfHistories.last().first()
    }
    
    fun part1(input: List<String>): Int {
        var listOfHistories = input.map{it.split(" ").map{itt -> itt.toInt()}}
        var listOfNewValues = listOfHistories.map{getNextValue(it)}
        
        println(listOfNewValues)
        
        return listOfNewValues.sum()
    }

    fun part2(input: List<String>): Int {
        var listOfHistories = input.map{it.split(" ").map{itt -> itt.toInt()}}
        var listOfNewValues = listOfHistories.map{getPrevValue(it)}

        println(listOfNewValues)

        return listOfNewValues.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
