package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day10 : AoCPuzzle(10)
{
    override fun part1(input: List<String>): Any
    {
        val instructions = ArrayDeque(input)
        var waiting = false
        var registerX = 1
        var signalStrength = 0

        for (cycle in 1..220)
        {
            // Read before instruction
            if ((cycle-20)%40==0)
            {
                signalStrength += cycle*registerX
            }

            // *After* two cycles, the X register is increased by the value V.
            val instruction = instructions.first()

            if (instruction == "noop" || waiting)
            {
                instructions.removeFirst()
                if (waiting)
                {
                    waiting = false
                    registerX += instruction.split(" ").last().toInt()
                }
            }
            else
            {
                waiting = true
            }
        }

        return signalStrength
    }

    override fun part2(input: List<String>): Any
    {
        val instructions = ArrayDeque(input)
        var waiting = false
        var registerX = 1
        val crtScreen = mutableListOf<Char>()

        for (cycle in 1..240)
        {
            // Draw
            val drawLocation = (cycle-1)%40 // During cycle 1: CRT draws pixel in position 0
            if (drawLocation in registerX-1..registerX+1)
            {
                crtScreen.add('#')
            }
            else
            {
                crtScreen.add(' ')
            }

            // End of cycle, execute instruction
            val instruction = instructions.first()

            if (instruction == "noop" || waiting)
            {
                instructions.removeFirst()
                if (waiting)
                {
                    waiting = false
                    registerX += instruction.split(" ").last().toInt()
                }
            }
            else
            {
                waiting = true
            }
        }

        return "\n${crtScreen.joinToString("").chunked(40).joinToString("\n")}"
    }
}
