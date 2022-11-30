package eu.elfring.adventofcode2022

import org.reflections.Reflections
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main()
{
    Main().run()
}

class Main
{
    private val puzzles: HashMap<Int, AoCPuzzle> = HashMap()

    @ExperimentalTime
    fun run()
    {
        registerPuzzles()

        println("registered ${puzzles.count()} puzzles")

        while (true)
        {
            if (!loop())
            {
                return
            }
        }
    }

    @ExperimentalTime
    fun loop(): Boolean
    {
        println("Please enter the puzzle numbers to execute:")
        val input = readLine()

        if (input == null || input.equals("exit", true))
        {
            return false
        }
        val range = parseInput(input)
        val puzzlesToRun = puzzles.filterKeys { it in range }
        val results: HashMap<Int, DayAnswers> = HashMap()
        puzzlesToRun.forEach { (day, puzzle) ->
            println("Executing day: $day")
            results[day] = puzzle.solve()
        }

        results.forEach { (day, answers) ->
            println("Day $day\tpart 1: ${answers.part1.value} in ${answers.part1.duration.inWholeMilliseconds}ms")
            println("Day $day\tpart 2: ${answers.part2.value} in ${answers.part2.duration.inWholeMilliseconds}ms")
        }
        return true
    }

    private fun parseInput(input: String): IntRange
    {
        return if(input.contains(".."))
        {
            //Range
            val splitted = input.split("..")
            splitted[0].toInt()..splitted[1].toInt()
        }
        else
        {
            //Just 1 number
            val int = Integer.parseInt(input)
            int..int
        }
    }

    private fun registerPuzzles()
    {
        val reflection = Reflections("eu.elfring.adventofcode2022.days")
        val toRegister2022 = reflection.getSubTypesOf(AoCPuzzle::class.java)
            .map { it.constructors.first().newInstance() as AoCPuzzle }.toList()

        toRegister2022.forEach { puzzles[it.day] = it }
    }
}
