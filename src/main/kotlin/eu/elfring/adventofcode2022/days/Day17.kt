package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle
import kotlin.math.min

class Day17 : AoCPuzzle(17)
{
    private val horizontalLine = listOf(Coord(0, 0), Coord(1, 0), Coord(2, 0), Coord(3, 0))
    private val cross = listOf(Coord(1, 0), Coord(0, 1), Coord(1, 1), Coord(2, 1), Coord(1, 2))
    private val lshape = listOf(Coord(0, 0), Coord(1, 0), Coord(2, 0), Coord(2, 1), Coord(2, 2))
    private val verticalLine = listOf(Coord(0, 0), Coord(0, 1), Coord(0, 2), Coord(0, 3))
    private val block = listOf(Coord(0, 0), Coord(1, 0), Coord(0, 1), Coord(1, 1))
    private val shapes = listOf(horizontalLine, cross, lshape, verticalLine, block)

    override fun part1(input: List<String>): Any
    {
        return runSimulation(input, 2022, false)
    }

    override fun part2(input: List<String>): Any
    {
        return runSimulation(input, 1_000_000_000_000L, true)
    }

    private fun runSimulation(input: List<String>, count: Long, cycleDetection: Boolean): Long
    {
        val instructions = input.first()
        var highestRock = -1 // First row of rocks on the floor is 0
        var instructionPointer = 0
        val blockedPositions = mutableListOf<Coord>()

        var cycleStart: Int? = null
        val seenCycles = mutableSetOf<Int>()
        var cycleStartLastRockCoord: Coord? = null
        var cycleStartRockCount = -1
        val partialCycleHeights = mutableListOf<Int>()


        // 10K must be enough to find a pattern
        repeat(min(count, 10_000).toInt()) { rockCount ->

            // getRockShape and position it 2 from the left & 4 positions higher than the last rock (3 spaces between them)
            var fallingRock = shapes[rockCount%shapes.size].map { it + Coord(2, highestRock + 4) }

            while (true)
            {
                // Move according to instructions, if possible
                val instruction = instructions[instructionPointer%instructions.length]
                instructionPointer++
                if (instruction == '<')
                {
                    val newPossibleFallingRock = fallingRock.map { it.move(Direction.LEFT) }
                    if (newPossibleFallingRock.none { it in blockedPositions || it.x !in 0..6 })
                    {
                        fallingRock = newPossibleFallingRock
                    }
                    // Else keep current rock
                }
                else
                {
                    val newPossibleFallingRock = fallingRock.map { it.move(Direction.RIGHT) }
                    if (newPossibleFallingRock.none { it in blockedPositions || it.x !in 0..6 })
                    {
                        fallingRock = newPossibleFallingRock
                    }
                    // Else keep current rock
                }

                // Move down
                val newPossibleFallingRock = fallingRock.map { it.move(Direction.DOWN) }
                if (newPossibleFallingRock.none { it in blockedPositions || it.y < 0 })
                {
                    fallingRock = newPossibleFallingRock
                }
                else
                {
                    // Hit rock bottom
                    break
                }
            }

            blockedPositions.addAll(fallingRock)
            highestRock = blockedPositions.maxOf { it.y }

            // Cycle detection:
            if (cycleDetection)
            {
                //Only after one complete set of rocks
                if (rockCount % shapes.size == shapes.size - 1)
                {
                    if (cycleStart != null && instructionPointer % instructions.length == cycleStart)
                    {
                        val cycleHeight = fallingRock.first().y - cycleStartLastRockCoord!!.y
                        val cycleLength = rockCount - cycleStartRockCount
                        val rocksToJumpOver = count - cycleStartRockCount
                        val cyclesToJump = rocksToJumpOver / cycleLength
                        val extraRocks = (rocksToJumpOver % cycleLength).toInt()
                        return (cyclesToJump * cycleHeight) + partialCycleHeights[extraRocks]
                    }
                    else if (cycleStart == null)
                    {
                        val instruction = instructionPointer % instructions.length
                        if (!seenCycles.add(instruction)) // Add and if seen before we've got a cycle: remember the start of this cycle
                        {
                            cycleStart = instruction
                            cycleStartLastRockCoord = fallingRock.first()
                            cycleStartRockCount = rockCount
                        }
                    }
                }
                // Remember heights for each step
                if (cycleStartLastRockCoord != null)
                {
                    partialCycleHeights.add(highestRock)
                }
            }
        }

        // No cycle detected before reaching count
        return highestRock.toLong() + 1
    }

    data class Coord(val x: Int, val y: Int)
    {
        fun move(direction: Direction) = this + direction.coord
        operator fun plus(other: Coord) = Coord(this.x + other.x, this.y + other.y)
    }

    enum class Direction(
        val coord: Coord
    )
    {
        DOWN(Coord(0, -1)),
        LEFT(Coord(-1, 0)),
        RIGHT(Coord(1, 0)),
    }
}
