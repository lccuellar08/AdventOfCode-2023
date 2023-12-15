fun main() {
    
    data class Lens(val label: String, val focalLength: Int)
    
    fun getHash(string: String): Int {
        var currentValue = 0
        
        string.forEach{
            currentValue += it.code
            currentValue *= 17
            currentValue %= 256
            
        }
        
        return currentValue
    }
    
    fun processInstruction(hashMap: MutableMap<Int, MutableList<Lens>>, label: String, operation: Char, focalLength: Int) {
        val hash = getHash(label)
        val boxList = hashMap[hash]
        if(operation == '=') {
            if(boxList != null) {
                val newLens = Lens(label, focalLength)
                val oldLens = boxList.find {it.label == label}
                if(oldLens != null) {
                    val oldLensIndex = boxList.indexOf(oldLens)
                    boxList[oldLensIndex] = newLens
                } else {
                    boxList.add(newLens)
                }
            }
        } else {
            if(boxList != null) {
                val oldLens = boxList.find {it.label == label}
                if(oldLens != null) {
                    val oldLensIndex = boxList.indexOf(oldLens)
                    boxList.removeAt(oldLensIndex)
                }
            }
        }
    } 
    
    fun part1(input: List<String>): Int {
        val instructions = input[0].split(",")
        val hashes = instructions.map{getHash(it)}
        
        return hashes.sum()
    }
    
    fun getFocusingPower(boxes: List<Lens>, boxNum: Int): Int {
        val sum= boxes.mapIndexed { index, lens ->
//            println("${boxNum + 1} (box ${boxNum}) * ${index + 1} (slot)  * ${lens.focalLength} (focal length)")
            return@mapIndexed (boxNum + 1) * (index + 1) * lens.focalLength
        }.sum()
        
        return sum
    }

    fun part2(input: List<String>): Int {
        val instructions = input[0].split(",")
        
        val hashMap: MutableMap<Int, MutableList<Lens>> = mutableMapOf()
        for(i in 0 until 256) {
            hashMap[i] = mutableListOf()
        }
        
        instructions.forEach{instruction ->
            if(instruction.contains('=')) {
                val tokens = instruction.split("=")
                processInstruction(hashMap, tokens[0], '=', tokens[1].toInt())
            } else {
                val tokens = instruction.split("-")
                processInstruction(hashMap, tokens[0], '-', -1)
            }
            
        }
        
        hashMap.filter{it.value.isNotEmpty()}.forEach{boxNum, boxList ->
            println("${boxNum}: ${boxList.map{"[${it.label} ${it.focalLength}]"}.joinToString(" ")}")
        }
        
        val lensPowers = hashMap.keys.map {boxNum ->
            val boxList = hashMap[boxNum]
            getFocusingPower(boxList!!, boxNum)
        }
        
        return lensPowers.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    
    getHash("cm").println()
    
    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
