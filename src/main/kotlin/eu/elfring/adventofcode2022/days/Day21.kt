package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle
import java.math.BigDecimal

class Day21 : AoCPuzzle(21)
{
    override fun part1(input: List<String>): Any
    {
        val numberMonkeys = mutableMapOf<String, BigDecimal>()
        val mathMonkeys = mutableListOf<MathMonkey>()

        input.forEach {
            val matchResult = inputRegex.matchEntire(it)!!.groupValues

            // Math monkey
            if (matchResult[2] == "")
            {
                mathMonkeys.add(MathMonkey(matchResult[1], matchResult[3], matchResult[4], matchResult[5]))
            }
            // Number monkey
            else
            {
                numberMonkeys[matchResult[1]] = matchResult[2].toBigDecimal()
            }
        }

        return solve(numberMonkeys, mathMonkeys)["root"]!!.toLong()
    }

    override fun part2(input: List<String>): Any
    {
        val numberMonkeys = mutableMapOf<String, BigDecimal>()
        val mathMonkeys = mutableListOf<MathMonkey>()

        input.forEach {
            val matchResult = inputRegex.matchEntire(it)!!.groupValues

            // Math monkey
            if (matchResult[2] == "")
            {
                mathMonkeys.add(MathMonkey(matchResult[1], matchResult[3], matchResult[4], matchResult[5]))
            }
            // Number monkey
            else
            {
                numberMonkeys[matchResult[1]] = matchResult[2].toBigDecimal()
            }
        }

        val rootMonkey = mathMonkeys.first { it.name == "root" }

        numberMonkeys["humn"] = BigDecimal.ZERO.setScale(50)
        val a = solve(numberMonkeys, mathMonkeys)
        numberMonkeys["humn"] = BigDecimal.ONE.setScale(50)
        val b = solve(numberMonkeys, mathMonkeys)

        // The result is a linear equation, solve for 0 & 1 and calculate result
        // Right hand site contains human
        return if (a[rootMonkey.left] == b[rootMonkey.left])
        {
            val step = (b[rootMonkey.right]!! - a[rootMonkey.right]!!)
            val diff = a[rootMonkey.left]!! - a[rootMonkey.right]!!
            (diff/step).toLong()
        }
        // Left hand site contians human
        else if (a[rootMonkey.right] == b[rootMonkey.right])
        {
            val step = (b[rootMonkey.left]!! - a[rootMonkey.left]!!)
            val diff = a[rootMonkey.right]!! - a[rootMonkey.left]!!
            (diff/step).toLong()
        }
        else
        {
            throw IllegalArgumentException("No side with human found")
        }
    }

    private fun solve(numberMonkeys: Map<String, BigDecimal>, mathMonkeys: List<MathMonkey>): Map<String, BigDecimal>
    {
        val numberMonkeysCopy = numberMonkeys.toMutableMap()
        val mathMonkeysCopy = mathMonkeys.toMutableList()

        while (mathMonkeysCopy.isNotEmpty())
        {
            val addedMonkeys = mutableListOf<MathMonkey>()
            mathMonkeysCopy.forEach {
                if (it.left in numberMonkeysCopy.keys && it.right in numberMonkeysCopy.keys)
                {
                    when (it.operator)
                    {
                        "+" -> numberMonkeysCopy[it.name] = numberMonkeysCopy[it.left]!! + numberMonkeysCopy[it.right]!!
                        "-" -> numberMonkeysCopy[it.name] = numberMonkeysCopy[it.left]!! - numberMonkeysCopy[it.right]!!
                        "*" -> numberMonkeysCopy[it.name] = numberMonkeysCopy[it.left]!! * numberMonkeysCopy[it.right]!!
                        "/" -> numberMonkeysCopy[it.name] = numberMonkeysCopy[it.left]!! / numberMonkeysCopy[it.right]!!
                    }
                    addedMonkeys.add(it)
                }
            }
            mathMonkeysCopy.removeAll(addedMonkeys)
        }

        return numberMonkeysCopy
    }

    data class MathMonkey(val name: String, val left: String, val operator: String, val right: String)

    companion object
    {
        val inputRegex = """([a-z]{4}): (?:(\d+)|([a-z]{4}) ([+\-*/]) ([a-z]{4}))""".toRegex()
    }
}
