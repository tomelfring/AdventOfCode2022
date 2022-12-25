package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle
import kotlin.math.pow

class Day25 : AoCPuzzle(25)
{
    override fun part1(input: List<String>): Any
    {
        val digit = input.sumOf(::fromSnafu)
        return toSnafu(digit)
    }

    override fun part2(input: List<String>): Any
    {
        return "Merry Christmas!"
    }

    private fun fromSnafu(snafu: String): Long
    {
        var result = 0L
        snafu.reversed().forEachIndexed { index, c ->
            val value = when (c)
            {
                '='  -> -2
                '-'  -> -1
                '0'  -> 0
                '1'  -> 1
                '2'  -> 2
                else -> throw IllegalArgumentException("Character $c not recognized")
            }
            result += (5.0.pow(index).toLong() * value)
        }
        return result
    }

    private fun toSnafu(digit: Long): String
    {
        var tempDigit = digit
        var result = ""
        while (tempDigit > 0)
        {
            result += digits[((tempDigit + 2) % 5).toInt()]
            tempDigit = (tempDigit + 2) / 5
        }
        return result.reversed()
    }

    companion object
    {
        private val digits = listOf('=', '-', '0', '1', '2')
    }
}
