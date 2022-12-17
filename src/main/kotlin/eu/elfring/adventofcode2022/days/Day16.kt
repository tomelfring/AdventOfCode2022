package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day16 : AoCPuzzle(16)
{
    override fun part1(input: List<String>): Any
    {
        val valves = parseInput(input)
        val usefulValveCount = valves.filterValues { it.flowRate > 0 }.count()

        val start = valves["AA"]!!
        val startPath = Part1State(listOf(start), HashMap())
        var allPaths = listOf(startPath)
        var bestPath = startPath
        var time = 1

        while (time < PART_ONE_MINUTES)
        {
            // Calculate all possible new steps
            val newPaths = allPaths.flatMap { currentPath ->
                val previousValve = currentPath.myPreviousValve()
                val currentValves = currentPath.myVisitedValves

                buildList {
                    // If path has already opened all valves, there's no need to continue this path
                    if (currentPath.opendValves.size != usefulValveCount)
                    {
                        if (previousValve.flowRate > 0 && !currentPath.opendValves.contains(previousValve))
                        {
                            val openedValves = currentPath.opendValves.toMutableMap()
                            openedValves[previousValve] = time
                            add(Part1State(currentValves + previousValve, openedValves))
                        }
                        previousValve.neighbours.forEach { neighbour ->
                            val possibleValve = valves[neighbour]!!
                            add(Part1State(currentValves + possibleValve, currentPath.opendValves))
                        }
                    }
                }
            }

            // Limit paths searched by just using top $PATH_FINDING_LIMIT ones
            allPaths = newPaths.sortedByDescending { it.total(PART_ONE_MINUTES) }.take(PATH_FINDING_LIMIT)

            // Record best path
            if (allPaths.first().total(PART_ONE_MINUTES) > bestPath.total(PART_ONE_MINUTES)) bestPath = allPaths.first()
            time++
        }

        return bestPath.total(PART_ONE_MINUTES)
    }

    override fun part2(input: List<String>): Any
    {
        val valves = parseInput(input)
        val usefulValveCount = valves.filterValues { it.flowRate > 0 }.count()

        val start = valves["AA"]!!
        val startPath = Part2State(listOf(start), listOf(start), HashMap())
        var allPaths = listOf(startPath)
        var bestPath = startPath
        var time = 1

        while (time < PART_TWO_MINUTES)
        {
            val newPaths = allPaths.flatMap { currentPath ->
                val myPreviousValve = currentPath.myPreviousValve()
                val elephantPreviousValve = currentPath.elephantPreviousValve()
                val currentValvesMe = currentPath.myVisitedValves
                val currentValvesElephant = currentPath.elephantVisitedValves

                buildList {
                    if (currentPath.opendValves.size != usefulValveCount)
                    {
                        val iCanOpen = myPreviousValve.flowRate > 0 && !currentPath.opendValves.containsKey(myPreviousValve)
                        val elephantCanOpen = elephantPreviousValve.flowRate > 0 && !currentPath.opendValves.containsKey(elephantPreviousValve)

                        // open both, mine or elephant's valve
                        if (iCanOpen || elephantCanOpen)
                        {
                            val opendValves = currentPath.opendValves.toMutableMap()

                            val myPossibleValves: List<List<Valve>> = if (iCanOpen)
                            {
                                opendValves[myPreviousValve] = time
                                listOf(currentValvesMe + myPreviousValve)
                            }
                            else
                            {
                                myPreviousValve.neighbours.map { neighbour ->
                                    // add possible path and move on
                                    val possibleValve = valves[neighbour]!!
                                    val possibleValves = currentValvesMe + possibleValve
                                    possibleValves
                                }
                            }

                            val elephantPossibleValves: List<List<Valve>> = if (elephantCanOpen)
                            {
                                opendValves[elephantPreviousValve] = time
                                listOf(currentValvesElephant + elephantPreviousValve)
                            }
                            else
                            {
                                elephantPreviousValve.neighbours.map { neighbour ->
                                    // add possible path and move on
                                    val possibleValve = valves[neighbour]!!
                                    val possibleValves = currentValvesElephant + possibleValve
                                    possibleValves
                                }
                            }

                            for (possibleValvesMe in myPossibleValves)
                            {
                                for (possibleValvesElephant in elephantPossibleValves)
                                {
                                    val possibleOpenedPath = Part2State(possibleValvesMe, possibleValvesElephant, opendValves)
                                    add(possibleOpenedPath)
                                }
                            }
                        }

                        // move to valves
                        val combinedLeads = myPreviousValve.neighbours.flatMap { myNeighbours ->
                            elephantPreviousValve.neighbours.map { elephantNeighbours ->
                                myNeighbours to elephantNeighbours
                            }
                        }.filter { (a, b) -> a != b }

                        val possiblePaths = combinedLeads.map { (leadMe, leadElephant) ->
                            val possibleValveMe = valves[leadMe]!!
                            val possibleValvesMe = currentValvesMe + possibleValveMe
                            val possibleValveElephant = valves[leadElephant]!!
                            val possibleValvesElephant = currentValvesElephant + possibleValveElephant
                            val possiblePath = Part2State(possibleValvesMe, possibleValvesElephant, currentPath.opendValves)
                            possiblePath
                        }

                        addAll(possiblePaths)
                    }
                }
            }

            // Limit paths searched by just using top $PATH_FINDING_LIMIT ones
            allPaths = newPaths.sortedByDescending { it.total(PART_TWO_MINUTES) }.take(PATH_FINDING_LIMIT)

            // Record best path
            if (allPaths.first().total(PART_TWO_MINUTES) > bestPath.total(PART_TWO_MINUTES)) bestPath = allPaths.first()
            time++
        }

        return bestPath.total(PART_TWO_MINUTES)
    }

    data class Part1State(val myVisitedValves: List<Valve>, val opendValves: Map<Valve, Int> /* Which valve opened at which minute */)
    {
        fun myPreviousValve() = myVisitedValves.last()
        fun total(totalTime: Int) = opendValves.map { (valve, time) -> (totalTime - time) * valve.flowRate }.sum()
    }

    data class Part2State(val myVisitedValves: List<Valve>, val elephantVisitedValves: List<Valve>, val opendValves: Map<Valve, Int> /* Which valve opened at which minute */)
    {
        fun myPreviousValve() = myVisitedValves.last()
        fun elephantPreviousValve() = elephantVisitedValves.last()

        fun total(totalTime: Int) = opendValves.map { (valve, time) -> (totalTime - time) * valve.flowRate }.sum()
    }

    private fun parseInput(input: List<String>): Map<String, Valve>
    {
        return input.associate {
            val (name, rate, neighbours) = inputRegex.matchEntire(it)!!.destructured
            name to Valve(rate.toInt(), neighbours.split(", "))
        }
    }

    data class Valve(
        val flowRate: Int,
        val neighbours: List<String>
    )

    companion object
    {
        // Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        private val inputRegex = """^Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? (.+)$""".toRegex()

        private const val PATH_FINDING_LIMIT = 10_000
        private const val PART_ONE_MINUTES = 30
        private const val PART_TWO_MINUTES = 26
    }
}
