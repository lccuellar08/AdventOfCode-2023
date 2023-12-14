import java.lang.IndexOutOfBoundsException

fun main() {
    
    fun getHashGroups(input: String): List<String> {
        val regex = """#+""".toRegex()
        return regex.findAll(input).map { it.value }.toList()
    }
    
    fun getIsolatedHashGroups(input: String, endIndex: Int): List<String> {
        // "??#.??#.?#."
        // "0123456789a"
        
        val substring = (input+".").windowed(2,1).mapIndexed{index, it ->
            if(index == input.length && it[1] == '#' && index < endIndex)
                return@mapIndexed Pair(it, index)
            if(it == "#." && index < endIndex) Pair(it, index) else null
         }.filterNotNull()
        
        val output = substring.map{
            var c = input[it.second]
            var index = it.second
            while(c == '#' && index > 0) {
                index--
                c = input[index]
            }
            return@map input.substring(index + 1, it.second + 1)
        }
        
        return output
    }
    
    fun isConsistent(cleanInput: String, damagedGroups: List<Int>): Boolean {
        val tokens = cleanInput.split(".")
        val damaged = tokens.filter{it.isNotBlank() || it.isNotEmpty()}
        if(damaged.size != damagedGroups.size)
            return false

        damagedGroups.forEachIndexed { index, groupSize ->
            if(groupSize != damaged[index].length)
                return false
        }

        return true
    }
    
    fun isFirstConsistent(cleanInput: String, damagedGroup: Int): Boolean {
        val tokens = cleanInput.split(".")
        val damaged = tokens.filter{it.isNotBlank() || it.isNotEmpty()}
        
        return damagedGroup == damaged[0].length
    }
    
    fun numOfConsistent(input: String, damagedGroups: List<Int>, startIndex: Int, parent: String, memo: MutableMap<String, Long>, originalParent: String): Long {
        val wildIndex = input.indexOf('?')
//        println("\tInput ${input}; damagedGroups: ${damagedGroups}; startIndex: ${startIndex}; wildIndex: ${wildIndex}; parent: ${parent}")
        val memoParameter = "${input};${damagedGroups}"
        
        if(memo.containsKey(memoParameter)) {
            return memo.getOrDefault(memoParameter, 0L)
        }
        
        if(damagedGroups.isEmpty()) {
            val returnValue = if(input.contains('#')) 0L else 1L
            memo[memoParameter] = returnValue
            return returnValue
        }
        
        if(wildIndex == -1) {
            val isConsistent = isConsistent(input, damagedGroups)
            val returnValue = if(isConsistent) 1L else 0L
            memo[memoParameter] = returnValue
            if(isConsistent) {
//                println("------------------------${input} is consistent: ${isConsistent}; damagedGroups: ${damagedGroups}------------------------")
            } else {
//                println("${input} is consistent: ${isConsistent}; damagedGroups: ${damagedGroups}")
            }
            return returnValue
        }
        
        val isolatedHashGroups = getIsolatedHashGroups(input, startIndex)
        if(isolatedHashGroups.size > 0) {
            val firstDamagedIndex = input.indexOf('#')
            if(firstDamagedIndex > wildIndex) {
                val returnValue = numOfConsistent(input,
                                                  damagedGroups,
                                                  startIndex + 1,
                                                  input,
                                                  memo,
                                                  originalParent)
                memo[memoParameter] = returnValue
                return returnValue
            }

            var firstIsConsistent = false
            try {
                firstIsConsistent = isFirstConsistent(input, damagedGroups[0])
            } catch(e: IndexOutOfBoundsException) {
//                println("\tInput ${input}; damagedGroups: ${damagedGroups}; startIndex: ${startIndex}; wildIndex: ${wildIndex}; parent: ${parent}; original parent: ${originalParent}")
                throw e
            }
//            val firstIsConsistent = isFirstConsistent(input, damagedGroups[0])
//            println("${input} first is consistent: ${firstIsConsistent}; damagedGroups: ${damagedGroups}")
            if(firstIsConsistent) {
                val returnValue = numOfConsistent(input.substring(startIndex, input.length),
                                                  damagedGroups.subList(1, damagedGroups.size),
                                                  0,
                                                  input,
                                                  memo,
                                                  originalParent)
                memo[memoParameter] = returnValue
                return returnValue
            } else {
                return 0L
            }
        }
        
        if(wildIndex != startIndex) {
            val returnValue = numOfConsistent(input,
                                              damagedGroups,
                                              startIndex + 1,
                                              input,
                                              memo,
                                              originalParent)
            memo[memoParameter] = returnValue
            return returnValue
        }
        
        
        val operationalList = input.toMutableList()
        operationalList[wildIndex] = '.'
        val operationalInput = operationalList.joinToString("")
        val numOperationalConsistent = numOfConsistent(operationalInput,
                                                       damagedGroups,
                                                       startIndex + 1,
                                                       input,
                                                       memo,
                                                       originalParent)
        
        
        val damagedList = input.toMutableList()
        damagedList[wildIndex] = '#'
        val damagedInput = damagedList.joinToString("")
        val numDamagedConsistent = numOfConsistent(damagedInput,
                                                   damagedGroups,
                                                   startIndex + 1,
                                                   input,
                                                   memo,
                                                   originalParent)
        
        memo[memoParameter] = numOperationalConsistent + numDamagedConsistent
//        println("Returning ${numOperationalConsistent} from Operational ${operationalInput}")
//        println("Returning ${numDamagedConsistent} from Damaged ${damagedInput}")
//        println("____________Returning ${memo[memoParameter]} from ${input} __________________")
        return numOperationalConsistent + numDamagedConsistent
    }
    
    fun part1(input: List<String>): Long {
        var inputAndArrangements = input.map{
            val tokens = it.split(" ")
            val damagedInput = tokens[0]
            val damagedGroups = tokens[1].split(",").map{it.toInt()}
            return@map Pair(damagedInput, damagedGroups)
        }
        
        var consistentArrangements = inputAndArrangements.map{(damagedInput, damagedGroups) ->
            numOfConsistent(damagedInput, damagedGroups, 0, "", mutableMapOf(), damagedInput)
        }
        println(consistentArrangements)

        return consistentArrangements.sum()
    }
    
    fun part2(input: List<String>, numRepetitions: Int): Long {
        var sum = 0L
        var inputAndArrangements = input.map{
            val tokens = it.split(" ")
            val damagedInput = tokens[0]

            val newDamagedInput = List(numRepetitions) {damagedInput}.joinToString("?")
            val newDamagedGroups = List(numRepetitions) {tokens[1]}.joinToString(",").split(",").map{it.toInt()}

            return@map Pair(newDamagedInput, newDamagedGroups)
        }
        
        var consistentArrangements = inputAndArrangements.mapIndexed{index, (damagedInput, damagedGroups) ->
            val res =numOfConsistent(damagedInput, damagedGroups, 0, "", mutableMapOf(), damagedInput)
            sum += res
            if(index % 50 == 0)
                println("${index} / ${inputAndArrangements.size}; ${sum}")
            return@mapIndexed res
        }
    
//        println(consistentArrangements)
        
        return consistentArrangements.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    
    // Fold 1: [1, 4, 1, 1, 4, 10]
    // Fold 2: [1, 32, 1, 2, 20, 150]
    // Fold 3: 
    
    val input = readInput("Day12")
    
//    getIsolatedHashGroups(".#?.###????.###".substring(0, 2)).println()
//    numOfConsistent("???.###", listOf(1,1,3), 0).println()
    
//    part1(testInput).println()
    part2(input, 5).println()
}
