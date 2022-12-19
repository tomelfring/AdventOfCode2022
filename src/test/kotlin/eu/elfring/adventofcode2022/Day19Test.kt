package eu.elfring.adventofcode2022

import eu.elfring.adventofcode2022.days.Day19

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Year 2022 - Day 19")
internal class Day19Test
{
    private val input = """
        Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
        Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
    """.trimIndent().split("\n")

    @Test
    @DisplayName("Part 1")
    fun part1()
    {
        Assertions.assertEquals(33, Day19().part1(input))
    }

    @Disabled("Not enough memory available in CI environment")
    @Test
    @DisplayName("Part 2")
    fun part2()
    {
        Assertions.assertEquals(3472, Day19().part2(input))
    }
}
