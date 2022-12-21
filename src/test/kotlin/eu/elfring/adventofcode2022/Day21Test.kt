package eu.elfring.adventofcode2022

import eu.elfring.adventofcode2022.days.Day21

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Year 2022 - Day 21")
internal class Day21Test
{
    private val input = """
        root: pppw + sjmn
        dbpl: 5
        cczh: sllz + lgvd
        zczc: 2
        ptdq: humn - dvpt
        dvpt: 3
        lfqf: 4
        humn: 5
        ljgn: 2
        sjmn: drzm * dbpl
        sllz: 4
        pppw: cczh / lfqf
        lgvd: ljgn * ptdq
        drzm: hmdt - zczc
        hmdt: 32
    """.trimIndent().split("\n")

    @Test
    @DisplayName("Part 1")
    fun part1()
    {
        Assertions.assertEquals(152L, Day21().part1(input))
    }

    @Test
    @DisplayName("Part 2")
    fun part2()
    {
        Assertions.assertEquals(301L, Day21().part2(input))
    }
}
