package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day07 : AoCPuzzle(7)
{
    override fun part1(input: List<String>): Any
    {
        val directorySizes = getDirectorySizes(input)
        return directorySizes.values.filter { it < 100_000 }.sum()
    }

    override fun part2(input: List<String>): Any
    {
        val directorySizes = getDirectorySizes(input)

        val spaceToClear = NEEDED_DISK_SPACE - (TOTAL_DISK_SPACE - directorySizes["/"]!!)
        return directorySizes.values.filter { it > spaceToClear }.min()
    }

    private fun getDirectorySizes(instructions: List<String>): Map<String, Int>
    {
        val directorySizes = mutableMapOf<String, Int>()
        val stack = mutableListOf<String>()

        instructions.forEach {
            val splitted = it.split(" ")
            if (splitted[1] == "cd")
            {
                if (splitted[2] == "..")
                {
                    stack.removeLast()
                }
                else
                {
                    val dir = if (stack.isEmpty()) splitted[2] else stack.last()+"/"+splitted[2]

                    if (directorySizes[dir] != null)
                    {
                        throw AssertionError("Expected directory names to be unique")
                    }

                    stack.add(dir)
                    directorySizes[dir] = 0
                }
            }
            else if (splitted[0].toIntOrNull() != null)
            {
                val filesize = splitted[0].toInt()
                stack.forEach { stackItem ->
                    directorySizes[stackItem] = directorySizes[stackItem]!!+filesize
                }
            }
        }

        return directorySizes
    }

    companion object
    {
        private const val TOTAL_DISK_SPACE = 70_000_000
        private const val NEEDED_DISK_SPACE = 30_000_000
    }
}
