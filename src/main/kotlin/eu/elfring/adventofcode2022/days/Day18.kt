package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day18 : AoCPuzzle(18)
{
    override fun part1(input: List<String>): Any
    {
        val coordinates = input.map(::parseLine)

        return coordinates.sumOf { it.getDirectNeighbours().count { it !in coordinates } }
    }

    override fun part2(input: List<String>): Any
    {
        val coordinates = input.map(::parseLine)

        val minX = coordinates.minOf { it.x }-1
        val maxX = coordinates.maxOf { it.x }+1
        val rangeX = minX..maxX

        val minY = coordinates.minOf { it.y }-1
        val maxY = coordinates.maxOf { it.y }+1
        val rangeY = minY..maxY

        val minZ = coordinates.minOf { it.z }-1
        val maxZ = coordinates.maxOf { it.z }+1
        val rangeZ = minZ..maxZ

        // BFS
        val start = Coordinate(minX, minY, minZ)
        val toVisit = ArrayDeque<Coordinate>()
        val explored = mutableListOf<Coordinate>()
        toVisit.add(start)

        var outsideVisibleSides = 0

        while (toVisit.isNotEmpty())
        {
            val toCheck = toVisit.removeFirst()

            val neigbours = toCheck.getDirectNeighbours()
                .filter { it.x in rangeX && it.y in rangeY && it.z in rangeZ }
                .filter { it !in explored }

            outsideVisibleSides += neigbours.count { it in coordinates }

            neigbours.filter { it !in coordinates && it !in toVisit }.forEach {
                toVisit.add(it)
            }
            explored.add(toCheck)
        }

        return outsideVisibleSides
    }

    private fun parseLine(input: String): Coordinate
    {
        val (x, y, z) = input.split(",").map { it.toInt() }
        return Coordinate(x, y, z)
    }

    data class Coordinate(val x: Int, val y: Int, val z: Int)
    {
        fun getDirectNeighbours(): List<Coordinate>
        {
            return listOf(
                Coordinate(x-1, y, z),
                Coordinate(x+1, y, z),
                Coordinate(x, y-1, z),
                Coordinate(x, y+1, z),
                Coordinate(x, y, z-1),
                Coordinate(x, y, z+1),
            )
        }
    }
}
