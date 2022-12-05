package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day05 : AoCPuzzle(5)
{
    override fun part1(input: List<String>): Any
    {
        val cargo = parseCargo(input.takeWhile { it.isNotEmpty() })

        val instructions = input.dropWhile { it.isNotEmpty() }.drop(1)

        instructions.forEach {
            val (count, from, to) = instructionRegex.matchEntire(it)!!.destructured
            repeat(count.toInt())
            {
                cargo[to.toInt()-1].add(cargo[from.toInt()-1].removeLast())
            }
        }

        return cargo.map { it.removeLast() }.joinToString("")
    }

    override fun part2(input: List<String>): Any
    {
        val cargo = parseCargo(input.takeWhile { it.isNotEmpty() })

        val instructions = input.dropWhile { it.isNotEmpty() }.drop(1)

        instructions.forEach {
            val (count, from, to) = instructionRegex.matchEntire(it)!!.destructured
            val removed = buildList {
                repeat(count.toInt())
                {
                    add(cargo[from.toInt()-1].removeLast())
                }
            }.reversed()
            cargo[to.toInt()-1].addAll(removed)
        }

        return cargo.map { it.removeLast() }.joinToString("")
    }

    private fun parseCargo(input: List<String>): List<ArrayDeque<Char>>
    {
        val reversed = input.dropLast(1).reversed()
        val stackCount = (reversed.first().length+1)/4

        val stacks = buildList<ArrayDeque<Char>> {
            repeat(stackCount)
            {
                add(ArrayDeque())
            }
        }

        reversed.forEach {
            for (i in 0 until ((it.length+1)/4))
            {
                val char = it[i*4+1]
                if (char != ' ')
                {
                    stacks[i].add(char)
                }
            }
        }

        return stacks
    }

    companion object
    {
        private val instructionRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()
    }
}
