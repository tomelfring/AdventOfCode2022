package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day15 : AoCPuzzle(15)
{
    override fun part1(input: List<String>): Any
    {
        val toCheck = if (input.size == 14) 10 else 2_000_000
        val sensors = parseInput(input)
        val covered = sensors.mapNotNull { it.getCoveredColumnsForRow(toCheck) }.flatten().toSet()

        val beaconsInToCheck = sensors.filter { it.beaconY == toCheck }.map { it.beaconY }

        return covered.filter { it !in beaconsInToCheck }.size
    }

    override fun part2(input: List<String>): Any
    {
        val toCheck = if (input.size == 14) 0..20 else 0..4_000_000
        val sensors = parseInput(input)

        for (i in toCheck)
        {
            val coveredRanges = sensors.mapNotNull { it.getCoveredColumnsForRow(i) }
            val mergedRanges = mergeRanges(coveredRanges)

            if (mergedRanges.count() > 1)
            {
                return (mergedRanges.first().last+1)*4_000_000L + i
            }
        }

        return -1
    }

    private fun parseInput(input: List<String>): List<Sensor>
    {
        return input.map {
            val (x, y, beaconX, beaconY) = inputRegex.matchEntire(it)!!.groupValues.drop(1).map {
                it.toInt()
            }
            Sensor(x, y, beaconX, beaconY)
        }
    }

    private fun mergeRanges(ranges: List<IntRange>): List<IntRange>
    {
        val sortedRanges = ranges.sortedBy { it.first }

        var start = sortedRanges.first().first
        var end = sortedRanges.first().last
        val mergedRanges = mutableListOf<IntRange>()
        sortedRanges.forEach {
            // It is in range (or direct next to) && extends our range
            if (it.first <= end+1 && it.last > end)
            {
                end = it.last
            }
            // It is a new range
            else if (it.first > end)
            {
                mergedRanges.add(start..end)
                start = it.first
                end = it.last
            }
        }
        mergedRanges.add(start..end)

        return mergedRanges
    }

    data class Sensor(
        val x: Int,
        val y: Int,
        val beaconX: Int,
        val beaconY: Int
    )
    {
        private val manhattenDistance by lazy {
            abs(x-beaconX)+abs(y - beaconY)
        }

        fun getCoveredColumnsForRow(targetY: Int): IntRange?
        {
            val rangeTaken = if (targetY >= y)
            {
                targetY-y
            }
            else
            {
                y-targetY
            }

            val rangeRemaining = manhattenDistance-rangeTaken
            if (rangeRemaining < 0)
            {
                return null
            }

            return (x-rangeRemaining)..(x+rangeRemaining)
        }
    }

    companion object
    {
        val inputRegex = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()
    }
}
