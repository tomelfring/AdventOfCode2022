package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day01 : AoCPuzzle(1)
{
    override fun part1(input: List<String>): Any
    {
        val caloriesPerElf = input.joinToString("\n").split("\n\n")
            .map { elfInventory -> elfInventory.split("\n").sumOf { inventoryItem -> inventoryItem.toLong() } }

        return caloriesPerElf.max()
    }

    override fun part2(input: List<String>): Any
    {
        val caloriesPerElf = input.joinToString("\n").split("\n\n")
            .map { elfInventory -> elfInventory.split("\n").sumOf { inventoryItem -> inventoryItem.toLong() } }

        return caloriesPerElf.sortedDescending().take(3).sum()
    }
}
