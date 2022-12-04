package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day04 : AoCPuzzle(4)
{
    override fun part1(input: List<String>): Any
    {
        return input.count {
            val (a1, a2, b1, b2) = it.split('-', ',').map(String::toInt)
            val rangeA = a1..a2
            val rangeB = b1..b2

            val overlapCount = rangeA.intersect(rangeB).count()
            overlapCount == rangeA.count() || overlapCount == rangeB.count()
        }
    }

    override fun part2(input: List<String>): Any
    {
        return input.count {
            val (a1, a2, b1, b2) = it.split('-', ',').map(String::toInt)
            val rangeA = a1..a2
            val rangeB = b1..b2

            (rangeA intersect rangeB).isNotEmpty()
        }
    }
}
