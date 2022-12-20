package eu.elfring.adventofcode2022

import eu.elfring.adventofcode2022.days.Day20

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Year 2022 - Day 20")
internal class Day20Test
{
    private val input = """
        1
        2
        -3
        3
        -2
        0
        4
    """.trimIndent().split("\n")

    @Test
    @DisplayName("Part 1")
    fun part1()
    {
        Assertions.assertEquals(3L, Day20().part1(input))
    }

    @Test
    @DisplayName("Part 2")
    fun part2()
    {
        Assertions.assertEquals(1623178306L, Day20().part2(input))
    }
}
