import kotlin.math.*

fun main() {
    fun getF(x: Float, n: Float): Float {
        return (-1.0f * (x.pow(2f))) + (n * x)
    }
    
    fun getNumOfWays(time: Float, distance: Float): Int {
        var successful = 0
        
        for(i in 0 .. time.toInt()) {
            if(getF(i.toFloat(), time) > distance) {
                successful += 1
            }
        }
        
        return successful
    }
    
    fun getF(x: Double, n: Double): Double {
        return (-1.0 * (x.pow(2.0))) + (n * x)
    }

    fun getNumOfWays(time: Double, distance: Double): Int {
        var successful = 0

        for(i in 0 .. time.toInt()) {
            if(getF(i.toDouble(), time) > distance) {
                successful += 1
            }
        }

        return successful
    }
    
    /**
     * Time:      7  15   30
     * Distance:  9  40  200
     */
    fun part1(input: List<String>): Int {
        val timeLists = input[0].split(":")[1].trim().split(" ").filter{it.isNotBlank() && it.isNotEmpty()}.map{it.toFloat()}
        val distanceLists = input[1].split(":")[1].trim().split(" ").filter{it.isNotBlank() && it.isNotEmpty()}.map{it.toFloat()}
        
        val totalNum = timeLists.zip(distanceLists) {time, distance -> getNumOfWays(time, distance)}
        println(totalNum)
        
        return totalNum.reduce {accum, element -> accum * element}
    }

    fun part2(input: List<String>): Int {
        val time = input[0].split(":")[1].trim().split(" ").filter{it.isNotBlank() && it.isNotEmpty()}.joinToString("").toDouble()
        val distance = input[1].split(":")[1].trim().split(" ").filter{it.isNotBlank() && it.isNotEmpty()}.joinToString("").toDouble()

        println(time)
        println(distance)
        
        val totalNum = getNumOfWays(time, distance)

        return totalNum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
