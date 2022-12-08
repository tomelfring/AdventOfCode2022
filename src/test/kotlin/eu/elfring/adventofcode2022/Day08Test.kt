package eu.elfring.adventofcode2022

import eu.elfring.adventofcode2022.days.Day08

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Year 2022 - Day 08")
internal class Day08Test
{
    private val input = """
        30373
        25512
        65332
        33549
        35390
    """.trimIndent().split("\n")

    @Test
    @DisplayName("Part 1")
    fun part1()
    {
        Assertions.assertEquals(21, Day08().part1(input))
    }

    @Test
    @DisplayName("Part 2")
    fun part2()
    {
        Assertions.assertEquals(8, Day08().part2(input))
    }
}
