package eu.elfring.adventofcode2022

import eu.elfring.adventofcode2022.days.Day17

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Year 2022 - Day 17")
internal class Day17Test
{
    private val input = """
        >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
    """.trimIndent().split("\n")

    @Test
    @DisplayName("Part 1")
    fun part1()
    {
        Assertions.assertEquals(3068L, Day17().part1(input))
    }

    @Test
    @DisplayName("Part 2")
    fun part2()
    {
        Assertions.assertEquals(1514285714288L, Day17().part2(input))
    }
}
