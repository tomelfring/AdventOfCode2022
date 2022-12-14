package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day14 : AoCPuzzle(14)
{
    override fun part1(input: List<String>): Any
    {
        val map = parse(input)

        var dropPoint = findDropPosition(map, STARTING_POINT)
        while (dropPoint != null)
        {
            map[dropPoint] = SAND
            dropPoint = findDropPosition(map, STARTING_POINT)
        }

        return map.count { it.value == SAND }
    }

    /*
    Warning: slow on real input
     */
    override fun part2(input: List<String>): Any
    {
        val map = parse(input)
        val lowestPoint = map.maxBy { it.key.second }.key.second

        var dropPoint = findDropPosition(map, STARTING_POINT)!! // It can never fall off
        while (dropPoint != STARTING_POINT)
        {
            map[dropPoint] = SAND
            dropPoint = findDropPosition(map, STARTING_POINT, lowestPoint + 2)!! // It can never fall off
        }

//        printMap(map)

        return map.count { it.value == SAND } + 1
    }

    private fun printMap(map: Map<Pair<Int, Int>, Char>)
    {
        val xMin = map.keys.minOf { it.first }
        val xMax = map.keys.maxOf { it.first }
        val yMin = map.keys.minOf { it.second }
        val yMax = map.keys.maxOf { it.second }

        for (y in yMin..yMax)
        {
            for (x in xMin..xMax)
            {
                print(map[x to y] ?: '.')
            }
            println()
        }
    }

    private fun findDropPosition(map: Map<Pair<Int, Int>, Char>, below: Pair<Int, Int>, floorHeight: Int? = null): Pair<Int, Int>?
    {
        val highest = findHighest(map, below)

        // Never ending
        return if (highest == null)
        {
            if (floorHeight != null)
            {
                below.first to floorHeight-1
            }
            else
            {
                null
            }
        }
        // Down & left
        else if (!map.contains(highest.first-1 to highest.second))
        {
            findDropPosition(map, highest.first-1 to highest.second, floorHeight)
        }
        // Down right
        else if (!map.contains(highest.first+1 to highest.second))
        {
            findDropPosition(map, highest.first+1 to highest.second, floorHeight)
        }
        // Comes to rest
        else
        {
            highest.first to highest.second-1
        }
    }

    private fun findHighest(map: Map<Pair<Int, Int>, Char>, below: Pair<Int, Int>): Pair<Int, Int>?
    {
        val posiblePoints = map.keys.filter { it.first == below.first && it.second > below.second }

        return posiblePoints.minByOrNull { it.second }
    }

    private fun parse(input: List<String>): MutableMap<Pair<Int, Int>, Char>
    {
        val map = mutableMapOf<Pair<Int, Int>, Char>()
        input.forEach { line ->
            val pairs = line.split(" -> ").map { val (a, b) = it.split(","); a.toInt() to b.toInt() }

            var previous = pairs.first()
            map[previous] = STONE

            pairs.drop(1).forEach { current ->
                // Draw line from previous to current
                if (previous.first == current.first)
                {
                    val min = minOf(current.second, previous.second)
                    val max = maxOf(current.second, previous.second)
                    for (y in min..max)
                    {
                        map[previous.first to y] = STONE
                    }
                }
                else if (previous.second == current.second)
                {
                    val min = minOf(current.first, previous.first)
                    val max = maxOf(current.first, previous.first)
                    for (x in min..max)
                    {
                        map[x to previous.second] = STONE
                    }
                }
                else
                {
                    throw IllegalStateException("Cannot draw diagonals")
                }

                // Set previous to current
                previous = current
            }
        }

        return map
    }

    companion object
    {
        private const val STONE = '#'
        private const val SAND = 'o'
        private val STARTING_POINT = 500 to 0
    }
}
