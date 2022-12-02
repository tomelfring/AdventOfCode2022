package eu.elfring.adventofcode2022

import eu.elfring.adventofcode2022.days.Day02

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Year 2022 - Day 02")
internal class Day02Test
{
    private val input = """
        A Y
        B X
        C Z
        """.trimIndent().split("\n")

    @Test
    @DisplayName("Part 1")
    fun part1()
    {
        Assertions.assertEquals(15, Day02().part1(input))
    }

    @Test
    @DisplayName("Part 2")
    fun part2()
    {
        Assertions.assertEquals(12, Day02().part2(input))
    }
}
