package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day23 : AoCPuzzle(23)
{
    override fun part1(input: List<String>): Any
    {
        val elves = getElves(input)

        val (resultElves, _) = simulate(elves, 10)

        val minX = resultElves.minOf { it.coord.x }
        val maxX = resultElves.maxOf { it.coord.x }
        val minY = resultElves.minOf { it.coord.y }
        val maxY = resultElves.maxOf { it.coord.y }

        return (maxX-minX+1)*(maxY-minY+1)-resultElves.count()
    }

    override fun part2(input: List<String>): Any
    {
        val elves = getElves(input)

        val (_, count) = simulate(elves, null)

        return count
    }

    fun getElves(input: List<String>): List<Elf>
    {
        return buildList {
            var elfIndex = 0
            input.forEachIndexed { y, row ->
                row.forEachIndexed { x, char ->
                    if (char == '#')
                    {
                        add(Elf(++elfIndex, Coordinate(x, y)))
                    }
                }
            }
        }

    }

    fun simulate(inputElves: List<Elf>, maxCount: Int? = null): Pair<List<Elf>, Int>
    {
        var elves = inputElves
        var count = 0
        while(true) {
            val consideredPositions = mutableMapOf<Coordinate, Elf>()
            // Determine positions
            val allCoords = elves.map { it.coord }
            var allStayedStill = true
            elves.forEach { elf ->
                if (around.none { elf.coord+it in allCoords })
                {
                    // Stay
                }
                else
                {

                    // First direction to check:
                    val consideredPosition = if (directionsToConsider[count%4].none { elf.coord+it in allCoords })
                    {
                        allStayedStill = false
                        directionsToConsider[count%4][1] + elf.coord
                    }
                    else if (directionsToConsider[(count+1)%4].none { elf.coord+it in allCoords })
                    {
                        allStayedStill = false
                        directionsToConsider[(count+1)%4][1] + elf.coord
                    }
                    else if (directionsToConsider[(count+2)%4].none { elf.coord+it in allCoords })
                    {
                        allStayedStill = false
                        directionsToConsider[(count+2)%4][1] + elf.coord
                    }
                    else if (directionsToConsider[(count+3)%4].none { elf.coord+it in allCoords })
                    {
                        allStayedStill = false
                        directionsToConsider[(count+3)%4][1] + elf.coord
                    }
                    else
                    {
                        // All posibilities are blocked
                        null
                    }

                    if (consideredPosition != null)
                    {
                        if (consideredPositions.containsKey(consideredPosition))
                        {
                            consideredPositions.remove(consideredPosition)
                        }
                        else
                        {
                            consideredPositions[consideredPosition] = elf
                        }
                    }
                }
            }

            count++
            if (allStayedStill)
            {
                break
            }

            // Move
            elves = buildList {
                // Moved elves
                consideredPositions.forEach { (coordinate, elf) ->
                    add(Elf(elf.number, coordinate))
                }
                // Not moved elves
                val movedelves = consideredPositions.values.map { it.number }
                addAll(elves.filter { it.number !in movedelves })
            }



            if (maxCount != null && maxCount == count)
            {
                break
            }
        }

        return elves to count
    }

    data class Elf(
        val number: Int,
        val coord: Coordinate,
    )

    data class Coordinate(
        val x: Int,
        val y: Int
    )
    {
        operator fun plus(other: Coordinate) = Coordinate(this.x + other.x, this.y + other.y)
    }

    companion object
    {
        val around = listOf(
            Coordinate(-1, -1),
            Coordinate( 0, -1),
            Coordinate(+1, -1),
            Coordinate(-1,  0),
            // Not itself
            Coordinate(+1,  0),
            Coordinate(-1, +1),
            Coordinate( 0, +1),
            Coordinate(+1, +1),
        )
        val directionsToConsider = listOf(
            //N
            listOf(Coordinate(-1, -1), Coordinate(0, -1), Coordinate(+1, -1)),
            //S
            listOf(Coordinate(-1, +1), Coordinate(0, +1), Coordinate(+1, +1)),
            //W
            listOf(Coordinate(-1, -1), Coordinate(-1, 0), Coordinate(-1, +1)),
            //E
            listOf(Coordinate(+1, -1), Coordinate(+1, 0), Coordinate(+1, +1)),
        )
    }
}
