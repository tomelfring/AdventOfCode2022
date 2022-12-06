package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day06 : AoCPuzzle(6)
{
    override fun part1(input: List<String>): Any
    {
        return findMarker(input.first(), 4)
    }

    override fun part2(input: List<String>): Any
    {
        return findMarker(input.first(), 14)
    }

    private fun findMarker(line: String, distinctCharacters: Int) = line.windowed(distinctCharacters).withIndex()
        .first { it.value.toSet().size == distinctCharacters }
        .index.plus(distinctCharacters)
}
