package eu.elfring.adventofcode2022

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

abstract class AoCPuzzle(val day: Int)
{
    @ExperimentalTime
    fun solve(): DayAnswers
    {
        val input = getInput()
        val part1 = measureTimedValue { part1(input) }
        val part2 = measureTimedValue { part2(input) }

        return DayAnswers(part1, part2)
    }

    private fun getInput(): List<String>
    {
        val file = File("src/main/resources/input$day.txt")

        if (!file.isFile)
        {
            throw IllegalStateException("File input$day.txt doesn't exist")
        }

        return file.useLines { it.toList() }
    }

    abstract fun part1(input: List<String>): Any
    abstract fun part2(input: List<String>): Any
}
