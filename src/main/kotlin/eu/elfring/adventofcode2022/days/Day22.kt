package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day22 : AoCPuzzle(22)
{
    override fun part1(input: List<String>): Any
    {
        val (board, instructions) = parseInput(input)

        return solvePart1(board, instructions)
    }

    override fun part2(input: List<String>): Any
    {
        val (board, instructions) = parseInput(input)

        return solvePart2(board, instructions)
    }

    fun solvePart1(board: Board, instructions: List<Instruction>): Int
    {
        var position = board.start
        var direction = Direction.RIGHT

        instructions.forEach { instruction ->
            when (instruction)
            {
                is TurnInstruction -> {
                    direction = direction.turn(instruction.direction)
                    println("Turn to ${direction.name}")
                }
                is StepInstruction ->
                {
                    println("Move ${instruction.steps} to ${direction.name}")
                    repeat(instruction.steps)
                    {
                        if (board.board.containsKey(position.move(direction)))
                        {
                            if (board.board[position.move(direction)] == BoardObject.PATH)
                            {
                                position = position.move(direction)
                                println("Move to $position")
                            }
                            // Else wall, don't move
                            else
                            {
                                println("Walked in a wall")
                            }
                        }
                        // Void wrapp around
                        else
                        {
                            when(direction)
                            {
                                Direction.RIGHT -> {
                                    val nextCoordinate = board.board.keys.filter { it.y == position.y }.minBy { it.x }
                                    if (board.board[nextCoordinate] == BoardObject.PATH)
                                    {
                                        position = nextCoordinate
                                        println("Move to $position")
                                    }
                                    else
                                    {
                                        println("No path at $nextCoordinate")
                                    }
                                }
                                Direction.DOWN -> {
                                    val nextCoordinate = board.board.keys.filter { it.x == position.x }.minBy { it.y }
                                    if (board.board[nextCoordinate] == BoardObject.PATH)
                                    {
                                        position = nextCoordinate
                                        println("Move to $position")
                                    }
                                    else
                                    {
                                        println("No path at $nextCoordinate")
                                    }
                                }
                                Direction.LEFT -> {
                                    val nextCoordinate = board.board.keys.filter { it.y == position.y }.maxBy { it.x }
                                    if (board.board[nextCoordinate] == BoardObject.PATH)
                                    {
                                        position = nextCoordinate
                                        println("Move to $position")
                                    }
                                    else
                                    {
                                        println("No path at $nextCoordinate")
                                    }

                                }
                                Direction.UP -> {
                                    val nextCoordinate = board.board.keys.filter { it.x == position.x }.maxBy { it.y }
                                    if (board.board[nextCoordinate] == BoardObject.PATH)
                                    {
                                        position = nextCoordinate
                                        println("Move to $position")
                                    }
                                    else
                                    {
                                        println("No path at $nextCoordinate")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return (position.y+1)*1_000+(position.x+1)*4+direction.score
    }

    fun solvePart2(board: Board, instructions: List<Instruction>): Int
    {
        var position = board.start
        var direction = Direction.RIGHT

        instructions.forEach { instruction ->
            when (instruction)
            {
                is TurnInstruction -> {
                    direction = direction.turn(instruction.direction)
                    println("Turn to ${direction.name}")
                }
                is StepInstruction ->
                {
                    println("Move ${instruction.steps} to ${direction.name}")
                    repeat(instruction.steps)
                    {
                        if (board.board.containsKey(position.move(direction)))
                        {
                            if (board.board[position.move(direction)] == BoardObject.PATH)
                            {
                                position = position.move(direction)
                                println("Move to $position")
                            }
                            // Else wall, don't move
                            else
                            {
                                println("Walked in a wall")
                            }
                        }
                        // Void wrapp around
                        else
                        {
                            val (nextCoordinate, nextDirection) = translatePart2(position, direction)

                            if (board.board[nextCoordinate] == BoardObject.PATH)
                            {
                                position = nextCoordinate
                                direction = nextDirection
                                println("Move to $position facing ${direction.name}")
                            }
                            else
                            {
                                println("No path at $nextCoordinate")
                            }
                        }
                    }
                }
            }
        }

        return (position.y+1)*1_000+(position.x+1)*4+direction.score
    }

    fun parseInput(input: List<String>): Pair<Board, List<Instruction>>
    {
        // Parse board
        val boardMap = mutableMapOf<Coord, BoardObject>()
        var start: Coord? = null
        input.dropLast(2).forEachIndexed { y, row ->
            row.forEachIndexed { x, boardChar ->
                if (start == null && (boardChar == '.' || boardChar == '#'))
                {
                    start = Coord(x, y)
                }
                when (boardChar)
                {
                    '.' -> boardMap[Coord(x, y)] = BoardObject.PATH
                    '#' -> boardMap[Coord(x, y)] = BoardObject.BLOCK
                }
            }
        }

        // Parse instructions
        var instructionString = input.last()
        val instructions = mutableListOf<Instruction>()
        while (instructionString.isNotEmpty())
        {
            if (instructionString.first().isDigit())
            {
                val digits = instructionString.takeWhile { it.isDigit() }
                instructionString = instructionString.drop(digits.length)
                instructions.add(StepInstruction(digits.toInt()))
            }
            else
            {
                val turn = instructionString.take(1)
                instructionString = instructionString.drop(1)
                instructions.add(TurnInstruction(if (turn == "L") TurnDirection.LEFT else TurnDirection.RIGHT))
            }
        }

        return Board(boardMap, start!!) to instructions
    }

    data class Board(
        val board: Map<Coord, BoardObject>,
        val start: Coord
    )

    enum class BoardObject
    {
        VOID,
        PATH,
        BLOCK
    }

    sealed interface Instruction

    data class StepInstruction(
        val steps: Int
    ): Instruction

    data class TurnInstruction(
        val direction: TurnDirection
    ): Instruction

    enum class TurnDirection
    {
        LEFT,
        RIGHT
    }

    data class Coord(
        val x: Int,
        val y: Int
    )
    {
        fun move(direction: Direction) = this + direction.coord
        operator fun plus(other: Coord) = Coord(this.x + other.x, this.y + other.y)
    }

    enum class Direction(val coord: Coord, val score: Int)
    {
        RIGHT(Coord(1, 0), 0),
        DOWN(Coord(0, 1), 1),
        LEFT(Coord(-1, 0), 2),
        UP(Coord(0, -1), 3);

        fun turn(turnDirection: TurnDirection): Direction
        {
            val wantedIndex = (values().indexOf(this) + values().size + (if (turnDirection == TurnDirection.RIGHT) 1 else -1)) % values().size
            return values()[wantedIndex]
        }
    }

    fun translatePart2(coord: Coord, direction: Direction): Pair<Coord, Direction>
    {
        /*
        A, B
        149,0   -> 99,149
        149,49  -> 99,100

        C, D
        100,49  -> 99, 50
        149,49  -> 99, 99

        E, F
        50, 50  -> 0, 100
        50, 99  -> 49, 100

        G, H
        50, 0   -> 0, 149
        50, 49  -> 0, 100

        I, J
        50, 0   -> 0, 150
        99, 0   -> 0, 199

        K, L
        100, 0  -> 0, 199
        149, 0  -> 49, 199

        M, N
        49, 150 -> 50, 149
        49, 199 -> 99, 149
         */
        return when(direction)
        {
            Direction.RIGHT -> {
                // A
                if (coord.x == 149 && coord.y in 0..49)
                {
                    Coord(99,149-coord.y) to Direction.LEFT
                }
                // D
                else if (coord.x == 99 && coord.y in 50..99)
                {
                    Coord(coord.y+50,49) to Direction.UP
                }
                // B
                else if (coord.x == 99 && coord.y in 100..149)
                {
                    Coord(149, 149-coord.y) to Direction.LEFT
                }
                // M
                else if (coord.x == 49 && coord.y in 150..199)
                {
                    Coord(coord.y-100,149) to Direction.UP
                }
                else
                {
                    throw IllegalArgumentException("Unknown position")
                }

            }
            Direction.DOWN -> {
                // C
                if (coord.y == 49 && coord.x in 100..149)
                {
                    Coord(99,coord.x-50) to Direction.LEFT
                }
                // N
                else if (coord.y == 149 && coord.x in 50..99)
                {
                    Coord(49,coord.x+100) to Direction.LEFT
                }
                // L
                else if (coord.y == 199 && coord.x in 0..49)
                {
                    Coord(coord.x+100,0) to Direction.DOWN
                }
                else
                {
                    throw IllegalArgumentException("Unknown position")
                }
            }
            Direction.LEFT -> {
                // G
                if (coord.x == 50 && coord.y in 0..49)
                {
                    Coord(0,149-coord.y) to Direction.RIGHT
                }
                // E
                else if (coord.x == 50 && coord.y in 50..99)
                {
                    Coord(coord.y-50,100) to Direction.DOWN
                }
                // H
                else if (coord.x == 0 && coord.y in 100..149)
                {
                    Coord(50,149-coord.y) to Direction.RIGHT
                }
                //J
                else if (coord.x == 0 && coord.y in 150..199)
                {
                    Coord(coord.y-100,0) to Direction.DOWN
                }
                else
                {
                    throw IllegalArgumentException("Unknown position")
                }
            }
            Direction.UP ->
            {
                // I
                if (coord.y == 0 && coord.x in 50..99)
                {
                    Coord(0,coord.x+100) to Direction.RIGHT
                }
                // K
                else if (coord.y == 0 && coord.x in 100..149)
                {
                    Coord(coord.x-100,199) to Direction.UP
                }
                // F
                else if (coord.y == 100 && coord.x in 0..49)
                {
                    Coord(50, coord.x+50) to Direction.RIGHT
                }
                else
                {
                    throw IllegalArgumentException("Unknown position")
                }
            }
        }
    }
}
