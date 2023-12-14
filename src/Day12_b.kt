fun main() {
    
    fun extractHashGroups(input: String): List<String> {
        val regex = """#+""".toRegex()
        return regex.findAll(input).map { it.value }.toList()
    }
    
    fun splitString(input: String): List<Pair<String, Int>> {
        val regex = """([?#]+)""".toRegex()
        return regex.findAll(input).map { matchResult ->
            Pair(matchResult.value, matchResult.range.first)
        }.toList()
    }
    
    fun getWildCards(input: String): List<Pair<String, Int>> {
        val regex = """([?]+)""".toRegex()
        return regex.findAll(input).map { matchResult ->
            Pair(matchResult.value, matchResult.range.first)
        }.toList()
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
    
    fun atLeastFirstNIsConsistent(cleanInput: String, damagedGroups: List<Int>, nGroups: Int): Boolean {
        val tokens = splitString(cleanInput)
        val damaged = tokens.filter{it.first.isNotBlank() || it.first.isNotEmpty()}
        if(damaged.isEmpty() || damaged.size < nGroups || damagedGroups.size < nGroups)
            return false

        for(i in 0 until nGroups) {
            if(damaged[i].first.contains('?')) {
                if(damagedGroups[i] <= damaged[i].first.length)
                    return true
            } else {
                if(damagedGroups[i] != damaged[i].first.length) {
                    return false
                }
            }
        }

        return true
    }
    
    val memo: MutableMap<String, List<String>> = mutableMapOf()
    
    fun generateArrangements(input: String): List<String> {
        if (input !in memo) {
            val damagedIndex = input.indexOf('?')
            if (damagedIndex == -1)
                memo[input] = listOf(input)
            else {
                val operationalList = input.toMutableList()
                operationalList[damagedIndex] = '.'
                val operationalInput = operationalList.joinToString("")
                val operationalDownstream = generateArrangements(operationalInput)
    
                val damagedList = input.toMutableList()
                damagedList[damagedIndex] = '#'
                val damagedInput = damagedList.joinToString("")
                val damagedDownstream = generateArrangements(damagedInput)
    
                val arrangementsList: MutableList<String> = mutableListOf()
    
                if (operationalDownstream.isNotEmpty()) {
                    arrangementsList.addAll(operationalDownstream)
                } else {
                    arrangementsList.add(operationalInput)
                }
    
                if (damagedDownstream.isNotEmpty()) {
                    arrangementsList.addAll(damagedDownstream)
                } else {
                    arrangementsList.add(damagedInput)
                }
    
                memo[input] = arrangementsList
            }
        }
        
        return memo[input] ?: emptyList()
    }
    
//    fun generateArrangements(input: String): List<String> {
//        return generateArrangements(input, mutableMapOf())
//    }
    
    fun getConsistentArrangements(damagedInput: String, damagedGroups: List<Int>): List<String> {
        val arrangements = generateArrangements(damagedInput)
        return arrangements.filter{isConsistent(it, damagedGroups)}
    }
    
    fun part1(input: List<String>): Int {
        var inputAndArrangements = input.map{
            val tokens = it.split(" ")
            val damagedInput = tokens[0]
            val damagedGroups = tokens[1].split(",").map{it.toInt()}
            return@map Pair(damagedInput, damagedGroups)
        }
        
        var consistentArrangements = inputAndArrangements.map{(damagedInput, damagedGroups) ->
            getConsistentArrangements(damagedInput, damagedGroups)
        }
        
        var numOfConsistentArrangements = consistentArrangements.map{it.size}
        println(numOfConsistentArrangements)
        
        return numOfConsistentArrangements.sum()
    }
    
    data class ConsistencyParams(val input: String, val hashGroup: String, val hashGroupStartIndex: Int, val hashGroupEndIndex: Int, val groupIndex: Int)
    
    fun generateConsistentInputs(
        input: String,
        hashGroup: String,
        hashGroupStartIndex: Int,
        hashGroupEndIndex: Int,
        damagedGroups: List<Int>,
        groupIndex: Int,
        startIndex: Int,
        consistencyMemo: MutableMap<String, List<String>>
    ): List<String> {
        println("Generating for input: $input, startIndex: ${startIndex}")
        
        val newInput = input.substring(0, startIndex)
        val consistencyParams = "${newInput};${groupIndex}"
        val damagedIndex = input.substring(0, hashGroupEndIndex).indexOf('?')
        
        val tokens = extractHashGroups(newInput)
        println("New input: ${newInput}")
        
        if(damagedIndex == -1 || tokens.size > groupIndex) {
            val isConsistent = atLeastFirstNIsConsistent(input, damagedGroups, groupIndex + 1)
            println("${input} is consistent: ${isConsistent}, groupIndex: ${groupIndex}")
            if(isConsistent) {
                consistencyMemo[consistencyParams] = listOf(input)
                println("Return value ${input}")
                return listOf(input)
            }
            else {
                consistencyMemo[consistencyParams] = listOf()
                println("Return value []")
                return listOf()
            }
        }
        if(consistencyMemo.containsKey(consistencyParams)) {
            println("------------------Getting from Generator Cache-${consistencyParams}----------------")
            val returnValue = consistencyMemo.getOrDefault(consistencyParams, listOf()).println()
            println("Return value: ${returnValue}")
            return consistencyMemo.getOrDefault(consistencyParams, listOf())
        }
        
        val operationalList = input.toMutableList()
        operationalList[damagedIndex] = '.'
        val operationalInput = operationalList.joinToString("")
        println("Calling . Downstream with starting index of ${damagedIndex + 1}")
        val operationalDownstream = generateConsistentInputs(operationalInput, hashGroup, hashGroupStartIndex, hashGroupEndIndex, damagedGroups, groupIndex, damagedIndex + 1, consistencyMemo)
        println("Returning to input ${input} from # Downstream")
        
        val damagedList = input.toMutableList()
        damagedList[damagedIndex] = '#'
        val damagedInput = damagedList.joinToString("")
        println("Calling # Downstream with starting index of ${damagedIndex + 1}")
        val damagedDownstream = generateConsistentInputs(damagedInput, hashGroup, hashGroupStartIndex, hashGroupEndIndex, damagedGroups, groupIndex, damagedIndex + 1, consistencyMemo)
        println("Returning to input ${input} from # Downstream")
        
        val arrangementsList: MutableList<String> = mutableListOf()
        
        if(operationalDownstream.isNotEmpty()) {
            arrangementsList.addAll(operationalDownstream)
        } else {
            val subInput = operationalInput// operationalInput.replaceRange(hashGroupStartIndex, hashGroupEndIndex, hashGroup)
            val isConsistent = atLeastFirstNIsConsistent(subInput, damagedGroups, groupIndex + 1)
            println("${subInput} is consistent: ${isConsistent}, groupIndex: ${groupIndex}")
            if(isConsistent)
                arrangementsList.add(operationalInput)
        }
        
        if(damagedDownstream.isNotEmpty()) {
            arrangementsList.addAll(damagedDownstream)
        } else {
            val subInput = damagedInput //.replaceRange(hashGroupStartIndex, hashGroupEndIndex, hashGroup)
            val isConsistent = atLeastFirstNIsConsistent(subInput, damagedGroups, groupIndex + 1)
            println("${subInput} is consistent: ${isConsistent}, arrangement: ${hashGroup}, hashGroup: ${hashGroup}, hashGroupEndIndex: ${hashGroupEndIndex}")
            if(isConsistent)
                arrangementsList.add(damagedInput)
        }
        consistencyMemo[consistencyParams] = arrangementsList
        println("Return value: ${arrangementsList}")
        return arrangementsList
    }
    
//    fun getNumberOfConsistentArrangements(damagedInput: String, damagedGroups: List<Int>): Int {
//        val 
//    }
    
    data class Parameters(val input: String, val startIndex: Int, val groupIndex: Int)
    
    fun getParameters(input: String, startIndex: Int, groupIndex: Int, prevStartIndex: Int): Parameters {
        val newInput = input.substring(prevStartIndex, input.length)
        return Parameters(newInput, startIndex, groupIndex)
    }
    
    fun memoizedNumConsistent(input: String, damagedGroups: List<Int>, cache: MutableMap<Parameters, Int>, startIndex: Int, groupIndex: Int, prevStartIndex: Int, consistencyCache: MutableMap<String, List<String>>): Int {
        println("------------------------Input: ${input}, damagedGroups: ${damagedGroups}, startIndex: ${startIndex}, groupIndex: ${groupIndex}")
        
        val parameters = getParameters(input, startIndex, groupIndex, prevStartIndex)
        if(cache.containsKey(parameters)) {
            println("------------------Getting from cache------------------")
            return cache.getOrDefault(parameters, 0)
        }

        val wildIndex = input.indexOf('?')
        if(wildIndex == -1 || damagedGroups.size == 0) {
            cache[parameters] = if(isConsistent(input, damagedGroups)) 1 else 0
            println("Return value for ${input}: ${cache[parameters]}")
            return cache.getOrDefault(parameters, 0)
        }

//        val hashGroups = splitString(input)
        val hashGroups = getWildCards(input)
//        if(hashGroups.size <= groupIndex) {
//            cache[parameters] = if(isConsistent(input, damagedGroups)) 1 else 0
//            println("Return value for ${input}: ${cache[parameters]}")
//            return cache.getOrDefault(parameters, 0)
//        } 
//        val subArrangements = generateArrangements(hashGroups[groupIndex].first)
        val hashGroupEndIndex = hashGroups[0].second + hashGroups[0].first.length
        val consistentInputs = generateConsistentInputs(
            input,
            hashGroups[0].first,
            hashGroups[0].second,
            hashGroupEndIndex,
            damagedGroups,
            groupIndex,
            0,
            mutableMapOf()
        ) 
//        val consistentInputs = subArrangements.map{
//            val hashGroupStartIndex = hashGroups[groupIndex].second
//            val hashGroupEndIndex = hashGroups[groupIndex].second + hashGroups[groupIndex].first.length
//            val subInput = input.replaceRange(hashGroupStartIndex, hashGroupEndIndex, it)
//            val isConsistent = atLeastFirstNIsConsistent(subInput, damagedGroups, groupIndex + 1)
//            println("${subInput} is consistent: ${isConsistent}, arrangement: ${it}, hashGroups: ${hashGroups}, hashGroupEndIndex: ${hashGroupEndIndex}")
//            return@map if(isConsistent) Pair(subInput, hashGroupEndIndex) else null 
//        }.filterNotNull()
        
        if(consistentInputs.size == 0) {
            cache[parameters] = 0
            return cache.getOrDefault(parameters, 0)
        }
        if(damagedGroups.size != 0) {
            val subReturnValues = consistentInputs.map{subInput ->
                val subInputSubstring = subInput.substring(startIndex, hashGroupEndIndex)
                val isEmpty = subInputSubstring.filter{c -> c != '.'}.isEmpty()
                val newHashGroups = extractHashGroups(subInputSubstring)
                memoizedNumConsistent(
                    subInput,
                    damagedGroups,
                    cache,
                    hashGroupEndIndex,
                    if(isEmpty) groupIndex else groupIndex + newHashGroups.size,
                    startIndex,
                    consistencyCache
                )
            }
            val returnValue = subReturnValues.sum()
            cache[parameters] = returnValue
//            println("Return value for ${input}: ${returnValue}")
            return returnValue
        } else {
            val returnValue = consistentInputs.size
            cache[parameters] = returnValue
            return returnValue
        }
    }

    fun part2(input: List<String>, numRepetitions: Int): Int {
        var inputAndArrangements = input.map{
            val tokens = it.split(" ")
            val damagedInput = tokens[0]
            
            val newDamagedInput = List(numRepetitions) {damagedInput}.joinToString("?")
            val newDamagedGroups = List(numRepetitions) {tokens[1]}.joinToString(",").split(",").map{it.toInt()}
            
            return@map Pair(newDamagedInput, newDamagedGroups)
        }

        var numOfConsistentArrangements = inputAndArrangements.mapIndexed{index, (damagedInput, damagedGroups) ->
//            val hashGroups = splitString(damagedInput)
            val memoizedConsistent = memoizedNumConsistent(damagedInput, damagedGroups, mutableMapOf(), 0, 0, 0, mutableMapOf())
            println("${index} / ${inputAndArrangements.size}")
            return@mapIndexed memoizedConsistent
        }
        println(numOfConsistentArrangements)

        return numOfConsistentArrangements.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    
    // Fold 1: [1, 4, 1, 1, 4, 10]
    // Fold 2: [1, 32, 1, 2, 20, 150]
    // Fold 3: 
    
    val input = readInput("Day12")
    part1(testInput).println()
//    part2(testInput, 1).println()
}
