package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day24 : AoCPuzzle(24)
{
    override fun part1(input: List<String>): Any
    {
        val valley = getValley(input)

        val start = Coord(valley.minX, valley.minY-1)
        val end = Coord(valley.maxX, valley.maxY+1)

        return simulate(valley, start, end).second
    }

    override fun part2(input: List<String>): Any
    {
        val valley = getValley(input)

        val start = Coord(valley.minX, valley.minY-1)
        val end = Coord(valley.maxX, valley.maxY+1)

        val (valleyA, timeA) = simulate(valley, start, end)
        val (valleyB, timeB) = simulate(valleyA, end, start)
        val (_, timeC) = simulate(valleyB, start, end)

        return timeA + timeB + timeC
    }

    private fun simulate(valley: Valley, start: Coord, end: Coord): Pair<Valley, Int>
    {
        // BFS
        val toVisit = ArrayDeque(listOf(SimulationState(0, start)))
        val valleys = mutableMapOf(0 to valley)

        while(toVisit.isNotEmpty())
        {
            val currentGameState = toVisit.removeFirst()

            var nextValley = valleys[currentGameState.minute+1]
            if (nextValley == null)
            {
                nextValley = getNextValley(valleys[currentGameState.minute]!!)
                valleys[currentGameState.minute+1] = nextValley
            }
            val blizzardPositions = nextValley.blizzards.map { it.coord }

            // Found the end
            if (currentGameState.position + Direction.DOWN.movement == end || currentGameState.position + Direction.UP.movement == end)
            {
                return nextValley to currentGameState.minute+1
            }

            // Down
            Direction.values().forEach { direction ->
                val newPos = currentGameState.position + direction.movement
                if (newPos !in blizzardPositions
                        && newPos.x in nextValley.minX..nextValley.maxX
                        && newPos.y in nextValley.minY..nextValley.maxY)
                {
                    val newState = SimulationState(currentGameState.minute+1, newPos)
                    if (!toVisit.contains(newState))
                    {
                        toVisit.add(newState)
                    }
                }
            }

            // Can I Stay
            if (currentGameState.position !in blizzardPositions)
            {
                toVisit.add(SimulationState(currentGameState.minute+1, currentGameState.position))
            }
        }

        throw IllegalStateException("Couldn't find exit")
    }

    private data class SimulationState(
        val minute: Int,
        val position: Coord
    )

    private fun getNextValley(valley: Valley): Valley
    {
        val newBlizzards = valley.blizzards.map {
            val newPos = it.coord + it.direction.movement
            if (newPos.x in valley.minX..valley.maxX && newPos.y in valley.minY..valley.maxY)
            {
                Blizzard(newPos, it.direction)
            }
            else
            {
                if (newPos.x < valley.minX)
                {
                    Blizzard(Coord(valley.maxX, newPos.y), it.direction)
                }
                else if (newPos.x > valley.maxX)
                {
                    Blizzard(Coord(valley.minX, newPos.y), it.direction)
                }
                else if (newPos.y < valley.minY)
                {
                    Blizzard(Coord(newPos.x, valley.maxY), it.direction)
                }
                else if (newPos.y > valley.maxY)
                {
                    Blizzard(Coord(newPos.x, valley.minY), it.direction)
                }
                else
                {
                    throw IllegalStateException("Shoudn't happen")
                }
            }
        }
        return valley.copy(blizzards = newBlizzards)
    }

    private fun getValley(input: List<String>): Valley
    {
        val blizzards = buildList{
            input.forEachIndexed { y, row ->
                row.forEachIndexed { x, char ->
                    when (char)
                    {
                        '<' -> add(Blizzard(Coord(x, y), Direction.LEFT))
                        '>' -> add(Blizzard(Coord(x, y), Direction.RIGHT))
                        'v' -> add(Blizzard(Coord(x, y), Direction.DOWN))
                        '^' -> add(Blizzard(Coord(x, y), Direction.UP))
                    }
                }
            }
        }
        return Valley(
            blizzards,
            1, input.first().length-2,
            1, input.size-2

        )
    }

    private data class Valley(
        val blizzards: List<Blizzard>,
        val minX: Int,
        val maxX: Int,
        val minY: Int,
        val maxY: Int
    )

    private data class Blizzard(
        val coord: Coord,
        val direction: Direction
    )

    private data class Coord(
        val x: Int,
        val y: Int
    )
    {
        operator fun plus(other: Coord) = Coord(this.x + other.x, this.y + other.y)
    }

    private enum class Direction(val movement: Coord)
    {
        LEFT(Coord(-1, 0)),
        RIGHT(Coord(1, 0)),
        UP(Coord(0, -1)),
        DOWN(Coord(0, 1)),
    }
}
