package eu.elfring.adventofcode2022

import eu.elfring.adventofcode2022.days.Day06

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Year 2022 - Day 06")
internal class Day06Test
{
    private val input = """
        mjqjpqmgbljsphdztnvjfqwrcgsmlb
    """.trimIndent().split("\n")

    @Test
    @DisplayName("Part 1")
    fun part1()
    {
        Assertions.assertEquals(7, Day06().part1(input))
    }

    @Test
    @DisplayName("Part 2")
    fun part2()
    {
        Assertions.assertEquals(19, Day06().part2(input))
    }
}
