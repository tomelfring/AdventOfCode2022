package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day09 : AoCPuzzle(9)
{
    override fun part1(input: List<String>): Any
    {
        var headPos = Point(0, 0)
        var tailPos = Point(0, 0)

        val visited = mutableSetOf(tailPos)

        input.forEach {
            val (dir, count) = it.split(" ")
            repeat(count.toInt())
            {
                when (dir)
                {
                    "L" -> headPos += Point(-1,  0)
                    "R" -> headPos += Point( 1,  0)
                    "U" -> headPos += Point( 0, -1)
                    "D" -> headPos += Point( 0 , 1)
                }
                tailPos = getTailPos(tailPos, headPos)
                visited.add(tailPos)
            }

        }

        return visited.size
    }

    override fun part2(input: List<String>): Any
    {
        var headPos = Point(0, 0)
        val allTailPos = mutableListOf(
            Point(0, 0),
            Point(0, 0),
            Point(0, 0),
            Point(0, 0),
            Point(0, 0),
            Point(0, 0),
            Point(0, 0),
            Point(0, 0),
            Point(0, 0))

        val visited = mutableSetOf(allTailPos[8])

        input.forEach {
            val (dir, count) = it.split(" ")
            repeat(count.toInt())
            {
                when (dir)
                {
                    "L" -> headPos += Point(-1,  0)
                    "R" -> headPos += Point( 1,  0)
                    "U" -> headPos += Point( 0, -1)
                    "D" -> headPos += Point( 0 , 1)
                }
                allTailPos[0] = getTailPos(allTailPos[0], headPos)
                for (i in 0 until 8)
                {
                    allTailPos[i+1] = getTailPos(allTailPos[i+1], allTailPos[i])
                }
                visited.add(allTailPos.last())
            }

        }

        return visited.size
    }

    private fun getTailPos(tail: Point, other: Point): Point
    {
        val diff = other-tail
        if (diff.x.absoluteValue == 2 || diff.y.absoluteValue == 2) // not touching
        {
            return tail + Point(diff.x.sign, diff.y.sign)
        }
        return tail
    }

    data class Point(val x: Int, val y: Int)
    {
        operator fun plus(other: Point) = Point(x+other.x, y+other.y)
        operator fun minus(other: Point) = Point(x-other.x, y-other.y)
    }
}
