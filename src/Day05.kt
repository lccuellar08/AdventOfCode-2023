fun main() {
    data class RangeMap(val destRange: Long, val srcRange: Long, val range: Long)
    
    fun getMap(input: List<String>): List<RangeMap> {
//        input.forEach {
//            println(input)
//        }
//        println()

        val mutableMap = mutableListOf<RangeMap>()
        
        input.forEach{line ->
            val tokens = line.split(" ")
            val destRange = tokens[0].toLong()
            val srcRange = tokens[1].toLong()
            val range = tokens[2].toLong()
            
            mutableMap.add(RangeMap(destRange, srcRange, range))
        }
        
        return mutableMap
    }
    
    
    fun getMapValue(map: List<RangeMap>, searchVal: Long): Long {
        val rangeMap = map.filter {rangeMap -> searchVal >= rangeMap.srcRange && searchVal <= rangeMap.srcRange + rangeMap.range}.firstOrNull()
        if(rangeMap != null) {
            val delta = searchVal - rangeMap.srcRange
            return rangeMap.destRange + delta
        } else {
            return searchVal
        }
    }
    
    fun part1(input: List<String>): Long {
        var seeds = listOf<Long>()
        var seedToSoilMap = listOf<RangeMap>()
        var soilToFertilzerMap = listOf<RangeMap>()
        var fertilizerToWaterMap = listOf<RangeMap>()
        var waterToLightMap = listOf<RangeMap>()
        var lightToTemperatureMap = listOf<RangeMap>()
        var temperatureToHumidityMap = listOf<RangeMap>()
        var humidityToLocationMap = listOf<RangeMap>()
        
        var mapState = ""
        
        val inputSubset = mutableListOf<String>()
        input.forEach {line ->
            if(mapState.isEmpty() || mapState.isBlank()) {
                val tokens = line.split(":")
                val title = tokens[0].trim()
                when(title) {
                    "seeds" -> {
                        seeds = tokens[1].trim().split(" ").map{it -> it.trim().toLong()}
                    }
                    "seed-to-soil map" -> {
                        mapState = "seed-to-soil"
                    }
                    "soil-to-fertilizer map" -> {
                        mapState = "soil-to-fertilizer"
                    }
                    "fertilizer-to-water map" -> {
                        mapState = "fertilizer-to-water"
                    }
                    "water-to-light map" -> {
                        mapState = "water-to-light"
                    }
                    "light-to-temperature map" -> {
                        mapState = "light-to-temperature"
                    }
                    "temperature-to-humidity map" -> {
                        mapState = "temperature-to-humidity"
                    }
                    "humidity-to-location map" -> {
                        mapState = "humidity-to-location"
                    }
                }
//                println("Starting ${mapState}")
            } else {
                if(line.isBlank() || line.isEmpty()) {
                    val newMap = getMap(inputSubset)
                    
                    when(mapState) {
                        "seed-to-soil" -> seedToSoilMap = newMap;
                        "soil-to-fertilizer" -> soilToFertilzerMap = newMap
                        "fertilizer-to-water" -> fertilizerToWaterMap = newMap
                        "water-to-light" -> waterToLightMap = newMap
                        "light-to-temperature" -> lightToTemperatureMap = newMap
                        "temperature-to-humidity" -> temperatureToHumidityMap = newMap
                        "humidity-to-location" -> humidityToLocationMap = newMap
                    }
                    inputSubset.clear()
                    mapState = ""
                } else {
                    inputSubset.add(line)
                }
            }
        }
        
        return seeds.map{seed ->
            val soil = getMapValue(seedToSoilMap, seed)
            val fertilizer = getMapValue(soilToFertilzerMap, soil)
            val water = getMapValue(fertilizerToWaterMap, fertilizer)
            val light = getMapValue(waterToLightMap, water)
            val temperature = getMapValue(lightToTemperatureMap, light)
            val humidty = getMapValue(temperatureToHumidityMap, temperature)
            val location = getMapValue(humidityToLocationMap, humidty)
            
            
//            println("Seed ${seed}, soil ${soil}, fertilizer ${fertilizer}, water ${water}, light ${light}, temperature ${temperature}, humidity ${humidty}, location ${location}.")
            return@map location
        }.min()
    }

    fun part2(input: List<String>): Long {
        var seeds = listOf<List<Long>>()
        var seedToSoilMap = listOf<RangeMap>()
        var soilToFertilzerMap = listOf<RangeMap>()
        var fertilizerToWaterMap = listOf<RangeMap>()
        var waterToLightMap = listOf<RangeMap>()
        var lightToTemperatureMap = listOf<RangeMap>()
        var temperatureToHumidityMap = listOf<RangeMap>()
        var humidityToLocationMap = listOf<RangeMap>()

        var mapState = ""

        val inputSubset = mutableListOf<String>()
        input.forEach {line ->
            if(mapState.isEmpty() || mapState.isBlank()) {
                val tokens = line.split(":")
                val title = tokens[0].trim()
                when(title) {
                    "seeds" -> {
                        seeds = tokens[1].trim().split(" ").map{it -> it.trim().toLong()}.windowed(2, step = 2)
                    }
                    "seed-to-soil map" -> {
                        mapState = "seed-to-soil"
                    }
                    "soil-to-fertilizer map" -> {
                        mapState = "soil-to-fertilizer"
                    }
                    "fertilizer-to-water map" -> {
                        mapState = "fertilizer-to-water"
                    }
                    "water-to-light map" -> {
                        mapState = "water-to-light"
                    }
                    "light-to-temperature map" -> {
                        mapState = "light-to-temperature"
                    }
                    "temperature-to-humidity map" -> {
                        mapState = "temperature-to-humidity"
                    }
                    "humidity-to-location map" -> {
                        mapState = "humidity-to-location"
                    }
                }
            //                println("Starting ${mapState}")
            } else {
                if(line.isBlank() || line.isEmpty()) {
                    val newMap = getMap(inputSubset)

                    when(mapState) {
                        "seed-to-soil" -> seedToSoilMap = newMap;
                        "soil-to-fertilizer" -> soilToFertilzerMap = newMap
                        "fertilizer-to-water" -> fertilizerToWaterMap = newMap
                        "water-to-light" -> waterToLightMap = newMap
                        "light-to-temperature" -> lightToTemperatureMap = newMap
                        "temperature-to-humidity" -> temperatureToHumidityMap = newMap
                        "humidity-to-location" -> humidityToLocationMap = newMap
                    }
                    inputSubset.clear()
                    mapState = ""
                } else {
                    inputSubset.add(line)
                }
            }
        }
        
        var minLocation = Long.MAX_VALUE
        
        seeds.forEach{it ->
            val seedStarter = it[0]
            val seedRange = it[1]
            for(seed in seedStarter until seedStarter + seedRange) {
                val soil = getMapValue(seedToSoilMap, seed)
                val fertilizer = getMapValue(soilToFertilzerMap, soil)
                val water = getMapValue(fertilizerToWaterMap, fertilizer)
                val light = getMapValue(waterToLightMap, water)
                val temperature = getMapValue(lightToTemperatureMap, light)
                val humidty = getMapValue(temperatureToHumidityMap, temperature)
                val location = getMapValue(humidityToLocationMap, humidty)


                minLocation = minOf(location, minLocation)
//                println("Seed ${seed}, soil ${soil}, fertilizer ${fertilizer}, water ${water}, light ${light}, temperature ${temperature}, humidity ${humidty}, location ${location}.")
//                locationsList.add(location)
            }
        }
        
        return minLocation
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
