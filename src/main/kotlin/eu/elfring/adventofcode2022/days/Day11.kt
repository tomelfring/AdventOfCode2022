package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day11 : AoCPuzzle(11)
{
    override fun part1(input: List<String>): Any
    {
        val monkeys = input.joinToString("\n").split("\n\n").map(::parseMonkey)
        val monkeyInspects = playGame(monkeys, 20, true)
        return monkeyInspects.sortedDescending().take(2).reduce { acc, i -> acc*i }
    }

    override fun part2(input: List<String>): Any
    {
        val monkeys = input.joinToString("\n").split("\n\n").map(::parseMonkey)
        val monkeyInspects = playGame(monkeys, 10_000, false)
        return monkeyInspects.sortedDescending().take(2).reduce { acc, i -> acc*i }
    }

    private fun parseMonkey(monkeyString: String): Monkey
    {
        val id = monkeyIdRegex.find(monkeyString)!!.groupValues.last().toInt()
        val items = monkeyItemsRegex.find(monkeyString)!!.groupValues.last().split(", ").map { it.toLong() }.toMutableList()

        val (operator, numberOrOld) = monkeyOperationRegex.find(monkeyString)!!.groupValues.toList().drop(1)
        val testValue = monkeyTestRegex.find(monkeyString)!!.groupValues.last().toLong()
        val ifTrue = monkeyTrueRegex.find(monkeyString)!!.groupValues.last().toInt()
        val ifFalse = monkeyFalseRegex.find(monkeyString)!!.groupValues.last().toInt()

        val operation = when (operator)
        {
            "*" -> { i: Long -> i * (if (numberOrOld == "old") i else numberOrOld.toLong()) }
            "+" -> { i: Long -> i + (if (numberOrOld == "old") i else numberOrOld.toLong()) }
            else -> throw NotImplementedError("Operation not implemented")
        }
        val test = { i: Long ->  if (i % testValue == 0L) ifTrue else ifFalse }

        return Monkey(id, items, operation, testValue, test)
    }

    /**
     * Returns list of inspects per Monkey
     */
    private fun playGame(monkeys: List<Monkey>, turns: Int, relieve: Boolean): List<Long>
    {
        val monkeyInspects = monkeys.map { 0L }.toMutableList()

        val kindaLcm = monkeys.map { it.testValue }.reduce { acc, l -> acc*l }

        repeat(turns)
        {
            monkeys.forEach { monkey ->
                while (monkey.items.any())
                {
                    // Count inspections
                    monkeyInspects[monkey.id] = monkeyInspects[monkey.id]+1

                    val item = monkey.items.removeFirst()
                    val worryLevel = if (relieve) monkey.operation(item) / 3 % kindaLcm else monkey.operation(item) % kindaLcm
                    val monkeyToTrow = monkey.test(worryLevel)
                    monkeys[monkeyToTrow].items.add(worryLevel)
                }
            }
        }

        return monkeyInspects
    }

    data class Monkey(
        val id: Int,
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val testValue: Long,
        val test: (Long) -> Int
    )

    companion object
    {
        private val monkeyIdRegex = """Monkey (\d):""".toRegex()
        private val monkeyItemsRegex = """Starting items: ([\d, ]*)""".toRegex()
        private val monkeyOperationRegex = """Operation: new = old ([*+]) (\d+|old)""".toRegex()
        private val monkeyTestRegex = """Test: divisible by (\d+)""".toRegex()
        private val monkeyTrueRegex = """If true: throw to monkey (\d+)""".toRegex()
        private val monkeyFalseRegex = """If false: throw to monkey (\d+)""".toRegex()
    }
}
