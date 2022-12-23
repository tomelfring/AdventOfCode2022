package eu.elfring.adventofcode2022

import eu.elfring.adventofcode2022.days.Day22

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Year 2022 - Day 22")
internal class Day22Test
{
    private val input = """
                ...#
                .#..
                #...
                ....
        ...#.......#
        ........#...
        ..#....#....
        ..........#.
                ...#....
                .....#..
                .#......
                ......#.
        
        10R5L5R10L4R5L5
    """.trimIndent().split("\n")

    @Test
    @DisplayName("Part 1")
    fun part1()
    {
        Assertions.assertEquals(6032, Day22().part1(input))
    }

    @Disabled("Code only works on real input, not test inut")
    @Test
    @DisplayName("Part 2")
    fun part2()
    {
        Assertions.assertEquals(5031, Day22().part2(input))
    }
}
