package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day02 : AoCPuzzle(2)
{
    override fun part1(input: List<String>): Any
    {
        val scores = mapOf(
            "%s %s".format(OPPONENT_ROCK,    PLAYED_ROCK)    to SCORE_ROCK    + SCORE_DRAW,
            "%s %s".format(OPPONENT_ROCK,    PLAYED_PAPER)   to SCORE_PAPER   + SCORE_WIN,
            "%s %s".format(OPPONENT_ROCK,    PLAYED_SCISSOR) to SCORE_SCISSOR + SCORE_LOSS,
            "%s %s".format(OPPONENT_PAPER,   PLAYED_ROCK)    to SCORE_ROCK    + SCORE_LOSS,
            "%s %s".format(OPPONENT_PAPER,   PLAYED_PAPER)   to SCORE_PAPER   + SCORE_DRAW,
            "%s %s".format(OPPONENT_PAPER,   PLAYED_SCISSOR) to SCORE_SCISSOR + SCORE_WIN,
            "%s %s".format(OPPONENT_SCISSOR, PLAYED_ROCK)    to SCORE_ROCK    + SCORE_WIN,
            "%s %s".format(OPPONENT_SCISSOR, PLAYED_PAPER)   to SCORE_PAPER   + SCORE_LOSS,
            "%s %s".format(OPPONENT_SCISSOR, PLAYED_SCISSOR) to SCORE_SCISSOR + SCORE_DRAW,
        )

        return input.sumOf { scores[it]!! }
    }

    override fun part2(input: List<String>): Any
    {
        val scores = mapOf(
            "%s %s".format(OPPONENT_ROCK,    PLAYED_LOSS) to SCORE_SCISSOR + SCORE_LOSS,
            "%s %s".format(OPPONENT_ROCK,    PLAYED_DRAW) to SCORE_ROCK    + SCORE_DRAW,
            "%s %s".format(OPPONENT_ROCK,    PLAYED_WIN)  to SCORE_PAPER   + SCORE_WIN,
            "%s %s".format(OPPONENT_PAPER,   PLAYED_LOSS) to SCORE_ROCK    + SCORE_LOSS,
            "%s %s".format(OPPONENT_PAPER,   PLAYED_DRAW) to SCORE_PAPER   + SCORE_DRAW,
            "%s %s".format(OPPONENT_PAPER,   PLAYED_WIN)  to SCORE_SCISSOR + SCORE_WIN,
            "%s %s".format(OPPONENT_SCISSOR, PLAYED_LOSS) to SCORE_PAPER   + SCORE_LOSS,
            "%s %s".format(OPPONENT_SCISSOR, PLAYED_DRAW) to SCORE_SCISSOR + SCORE_DRAW,
            "%s %s".format(OPPONENT_SCISSOR, PLAYED_WIN)  to SCORE_ROCK    + SCORE_WIN,
        )

        return input.sumOf { scores[it]!! }
    }

    companion object
    {
        const val OPPONENT_ROCK = "A"
        const val OPPONENT_PAPER = "B"
        const val OPPONENT_SCISSOR = "C"
        const val PLAYED_ROCK = "X"
        const val PLAYED_PAPER = "Y"
        const val PLAYED_SCISSOR = "Z"
        const val PLAYED_LOSS = "X"
        const val PLAYED_DRAW = "Y"
        const val PLAYED_WIN = "Z"

        const val SCORE_LOSS = 0
        const val SCORE_DRAW = 3
        const val SCORE_WIN = 6
        const val SCORE_ROCK = 1
        const val SCORE_PAPER = 2
        const val SCORE_SCISSOR = 3
    }
}
