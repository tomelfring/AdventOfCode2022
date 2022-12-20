package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day20 : AoCPuzzle(20)
{
    override fun part1(input: List<String>): Any
    {
        val inputWithIndex = input.withIndex().map { it.index to it.value.toLong() }.toMutableList()

        mixCoordinates(inputWithIndex)

        return getScore(inputWithIndex)
    }

    override fun part2(input: List<String>): Any
    {
        val inputWithIndex = input.withIndex().map { it.index to it.value.toLong()*811589153L }.toMutableList()

        repeat(10)
        {
            mixCoordinates(inputWithIndex)
        }

        return getScore(inputWithIndex)
    }

    private fun mixCoordinates(mutableInputWithIndex: MutableList<Pair<Int, Long>>)
    {
        for (i in mutableInputWithIndex.indices)
        {
            val (index, toMove) = mutableInputWithIndex.withIndex().find { it.value.first == i } ?: throw IllegalStateException("Couldn't find resource with original index $i")


            if (toMove.second > 0)
            {
                mutableInputWithIndex.removeAt(index)
                mutableInputWithIndex.add(((index+toMove.second)%mutableInputWithIndex.size).toInt(), toMove)
            }
            else if (toMove.second < 0)
            {
                mutableInputWithIndex.removeAt(index)
                mutableInputWithIndex.add((((index+toMove.second)%mutableInputWithIndex.size+mutableInputWithIndex.size)%mutableInputWithIndex.size).toInt(), toMove)
            }
            // 0 stays
        }
    }

    private fun getScore(mutableInputWithIndex: MutableList<Pair<Int, Long>>): Long
    {
        val finalOrder = mutableInputWithIndex.map { it.second }
        val zeroIndex = finalOrder.withIndex().find { it.value == 0L }!!.index

        return finalOrder[(zeroIndex+1000)%finalOrder.size] + finalOrder[(zeroIndex+2000)%finalOrder.size] + finalOrder[(zeroIndex+3000)%finalOrder.size]
    }
}
