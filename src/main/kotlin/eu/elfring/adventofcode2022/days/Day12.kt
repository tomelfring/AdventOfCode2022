package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle
import java.util.PriorityQueue

class Day12 : AoCPuzzle(12)
{
    override fun part1(input: List<String>): Any
    {
        lateinit var start: Pair<Int, Int>
        lateinit var end: Pair<Int, Int>
        val grid = mutableMapOf<Pair<Int, Int>, GridItem>().apply {
            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, value ->
                    when (value)
                    {
                        'S' -> {
                            start = x to y
                            put(x to y, GridItem(x, y, 0))
                        }
                        'E' -> {
                            end = x to y
                            put(x to y, GridItem(x, y, 25))
                        }
                        else -> {
                            put(x to y, GridItem(x, y, value.code - 'a'.code))
                        }
                    }
                }
            }
        }

        val path = aStar(grid, start, end)
        return path!!.size-1
    }

    override fun part2(input: List<String>): Any
    {
        lateinit var end: Pair<Int, Int>
        val grid = mutableMapOf<Pair<Int, Int>, GridItem>().apply {
            input.forEachIndexed { y, line ->
                line.forEachIndexed { x, value ->
                    when (value)
                    {
                        'S' -> {
                            put(x to y, GridItem(x, y, 0))
                        }
                        'E' -> {
                            end = x to y
                            put(x to y, GridItem(x, y, 25))
                        }
                        else -> {
                            put(x to y, GridItem(x, y, value.code - 'a'.code))
                        }
                    }
                }
            }
        }

        val startingPositions = grid.filter { it.value.value == 0 }.map { it.key }
        return startingPositions.mapNotNull { aStar(grid, it, end)?.size }.min()-1
    }

    private fun aStar(grid: Map<Pair<Int, Int>, GridItem>, start: Pair<Int, Int>, end: Pair<Int, Int>): List<Pair<Int, Int>>?
    {
        //<Priority, <X, Y>>
        val comparator: Comparator<Pair<Int, Pair<Int, Int>>> = compareBy { it.first }
        val openSet = PriorityQueue(comparator)
        openSet.add(0 to start)

        val cameFrom = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()

        val gScore = mutableMapOf<Pair<Int, Int>, Int>()
        gScore[start] = 0

        val fScore = mutableMapOf<Pair<Int, Int>, Int>()
        fScore[start] = 0

        val maxSize = grid.maxOf { it.key.first }

        while (openSet.isNotEmpty())
        {
            val current = openSet.remove().second

            if (current == end)
            {
                return reconstructPath(cameFrom, current)
            }

            val validNeighbours = grid[current]!!.getNeighbours(grid).filter { (it.value - grid[current]!!.value) <= 1 }
            for (neighbour in validNeighbours)
            {
                val tentative_gScore = gScore[current]!! + 1
                if (tentative_gScore < gScore.getOrDefault(neighbour.getXY(), Int.MAX_VALUE))
                {
                    cameFrom[neighbour.getXY()] = current
                    gScore[neighbour.getXY()] = tentative_gScore
                    fScore[neighbour.getXY()] = tentative_gScore + (maxSize*2 - neighbour.x - neighbour.y) //Manhatten distance
                    if (openSet.none { it.second == neighbour.getXY() })
                    {
                        openSet.add(fScore[neighbour.getXY()]!! to neighbour.getXY())
                    }
                }
            }
        }

        return null
    }

    private fun reconstructPath(cameFrom: MutableMap<Pair<Int, Int>, Pair<Int, Int>>, current: Pair<Int, Int>): List<Pair<Int, Int>>
    {
        val result = mutableListOf<Pair<Int, Int>>()

        var toCheck = current
        result.add(toCheck)
        while(cameFrom.contains(toCheck))
        {
            toCheck = cameFrom[toCheck]!!
            result.add(toCheck)
        }
        return result.reversed()
    }

    data class GridItem(val x: Int, val y: Int, var value: Int)
    {
        fun getNeighbours(grid: Map<Pair<Int, Int>, GridItem>): List<GridItem>
        {
            return listOfNotNull(
                grid[x to y-1],
                grid[x-1 to y  ], grid[x+1 to y],
                grid[x to y+1])
        }

        fun getXY(): Pair<Int, Int>
        {
            return x to y
        }
    }
}
