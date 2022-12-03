package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day03 : AoCPuzzle(3)
{
    override fun part1(input: List<String>): Any
    {
        return input.sumOf { line ->
            val (a, b) = line.chunked(line.length/2).map { it.toSet() }
            val common = a.intersect(b)
            if (common.size != 1)
            {
                throw AssertionError("More than 1 common")
            }

            val duplicate = common.first()
            if (duplicate.isLowerCase())
            {
                (duplicate.code-96) // a = 97, needs to be 1
            }
            else{
                (duplicate.code-38) // A = 65, needs to be 27
            }
        }
    }

    override fun part2(input: List<String>): Any
    {
        return input.chunked(3).sumOf { threeElves ->
            val (a, b, c) = threeElves.map { it.toSet() }
            val common = a.intersect(b).intersect(c)
            if (common.size != 1)
            {
                throw AssertionError("More than 1 common")
            }

            val duplicate = common.first()
            if (duplicate.isLowerCase())
            {
                (duplicate.code-96)
            }
            else{
                (duplicate.code-38)
            }
        }
    }
}
