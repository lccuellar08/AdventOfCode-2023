import kotlin.math.*

fun main() {
    
    fun getRootsAtLine(a: Float, b: Float, c: Float): Pair<Float,Float> {
        // x = -b +- sqrt(b^2 - 4ac)
        // -------------------------
        //           2a

        val sqrtVal = sqrt((b.pow(2f)) - (4f*a*c) )
        val posRoot = ((-1f * b) + sqrtVal) /(2f * a)
        val negRoot = ((-1f * b) - sqrtVal) /(2f * a)

        return Pair(ceil(posRoot), floor(negRoot))
    }

    fun getRootsAtLine(a: Double, b: Double, c: Double): Pair<Double,Double> {
        // x = -b +- sqrt(b^2 - 4ac)
        // -------------------------
        //           2a

        val sqrtVal = sqrt((b.pow(2.0)) - (4.0*a*c) )
        val posRoot = ((-1.0 * b) + sqrtVal) /(2.0 * a)
        val negRoot = ((-1.0 * b) - sqrtVal) /(2.0 * a)

        return Pair(ceil(posRoot), floor(negRoot))
    }
    
    /**
     * Time:      7  15   30
     * Distance:  9  40  200
     */
    fun part1(input: List<String>): Int {
        val timeLists = input[0].split(":")[1].trim().split(" ").filter{it.isNotBlank() && it.isNotEmpty()}.map{it.toFloat()}
        val distanceLists = input[1].split(":")[1].trim().split(" ").filter{it.isNotBlank() && it.isNotEmpty()}.map{it.toFloat()}
        
        val totalNum = timeLists.zip(distanceLists) {time, distance ->
            val roots = getRootsAtLine(1f, -1f * time, distance)
            return@zip roots.second - roots.first + 1f
        }

        
        return totalNum.reduce {accum, element -> accum * element}.toInt()
    }

    fun part2(input: List<String>): Int {
        val time = input[0].split(":")[1].trim().split(" ").filter{it.isNotBlank() && it.isNotEmpty()}.joinToString("").toDouble()
        val distance = input[1].split(":")[1].trim().split(" ").filter{it.isNotBlank() && it.isNotEmpty()}.joinToString("").toDouble()

        
        val roots = getRootsAtLine(1.0, -1.0 * time, distance)
        val totalNum = max(roots.second, roots.first) - min(roots.first, roots.second) + 1.0

        return totalNum.toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
