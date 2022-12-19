package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day19 : AoCPuzzle(19)
{
    override fun part1(input: List<String>): Any
    {
        val bluePrints = input.map(::parseBlueprint)

        val highestGeodeCount = bluePrints.map { simulateBlueprint(it, 24) }

        return highestGeodeCount.withIndex().sumOf { (it.index+1) * it.value }
    }

    override fun part2(input: List<String>): Any
    {
        val bluePrints = input.map(::parseBlueprint)

        val highestGeodeCount = bluePrints.take(3).map { simulateBlueprint(it, 32) }
        return highestGeodeCount.reduce { acc, i -> acc*i }
    }

    private fun simulateBlueprint(bluePrint: BluePrint, timeLimit: Int): Int
    {
        // BFS
        val start = GameState(0, 0, 0, 0, 0, 1, 0, 0, 0)
        val toVisit = ArrayDeque(listOf(start))

        var highestGeodeCount = 0

        while (toVisit.isNotEmpty())
        {
            val toCheck = toVisit.removeFirst()

            val newToVisit = mutableListOf<GameState>()
            if (toCheck.minute < timeLimit)
            {
                // Calculate new resource count
                val oreCount = toCheck.oreCount + toCheck.oreRobots
                val clayCount = toCheck.clayCount + toCheck.clayRobots
                val obsidianCount = toCheck.obsidianCount + toCheck.obsidianRobots
                val geodeCount = toCheck.geodeCount + toCheck.geodeRobots

                // As you can only spend x resources per minute, don't bother to create more robots than max needed resources, use old resource count, not the new one
                // create geode robot, if we can create it, do it, skip all other possibilities
                if (toCheck.oreCount >= bluePrint.geodeRobotOreCosts && toCheck.obsidianCount >= bluePrint.geodeRobotObsidianCosts)
                {
                    newToVisit.add(GameState(
                        minute = toCheck.minute+1,
                        oreCount = oreCount-bluePrint.geodeRobotOreCosts,
                        clayCount = clayCount,
                        obsidianCount = obsidianCount-bluePrint.geodeRobotObsidianCosts,
                        geodeCount = geodeCount,
                        oreRobots = toCheck.oreRobots,
                        clayRobots = toCheck.clayRobots,
                        obsidianRobots = toCheck.obsidianRobots,
                        geodeRobots = toCheck.geodeRobots+1,
                    ))
                }
                else
                {
                    // create ore robot
                    if (toCheck.oreCount >= bluePrint.oreRobotOreCosts && toCheck.oreRobots < bluePrint.maxOreCosts)
                    {
                        newToVisit.add(GameState(
                            minute = toCheck.minute+1,
                            oreCount = oreCount-bluePrint.oreRobotOreCosts,
                            clayCount = clayCount,
                            obsidianCount = obsidianCount,
                            geodeCount = geodeCount,
                            oreRobots = toCheck.oreRobots+1,
                            clayRobots = toCheck.clayRobots,
                            obsidianRobots = toCheck.obsidianRobots,
                            geodeRobots = toCheck.geodeRobots,
                        ))
                    }

                    // create clay robot
                    if (toCheck.oreCount >= bluePrint.clayRobotOreCosts && toCheck.clayRobots < bluePrint.obsidianRobotClayCosts)
                    {
                        newToVisit.add(GameState(
                            minute = toCheck.minute+1,
                            oreCount = oreCount-bluePrint.clayRobotOreCosts,
                            clayCount = clayCount,
                            obsidianCount = obsidianCount,
                            geodeCount = geodeCount,
                            oreRobots = toCheck.oreRobots,
                            clayRobots = toCheck.clayRobots+1,
                            obsidianRobots = toCheck.obsidianRobots,
                            geodeRobots = toCheck.geodeRobots,
                        ))

                    }

                    // create obsidian robot
                    if (toCheck.oreCount >= bluePrint.obsidianRobotOreCosts && toCheck.clayCount >= bluePrint.obsidianRobotClayCosts && toCheck.obsidianRobots < bluePrint.geodeRobotObsidianCosts)
                    {
                        newToVisit.add(GameState(
                            minute = toCheck.minute+1,
                            oreCount = oreCount-bluePrint.obsidianRobotOreCosts,
                            clayCount = clayCount-bluePrint.obsidianRobotClayCosts,
                            obsidianCount = obsidianCount,
                            geodeCount = geodeCount,
                            oreRobots = toCheck.oreRobots,
                            clayRobots = toCheck.clayRobots,
                            obsidianRobots = toCheck.obsidianRobots+1,
                            geodeRobots = toCheck.geodeRobots,
                        ))

                    }

                    // Always wait to gather resources if there isn't enough to buy all the things
                    if ((toCheck.oreCount < bluePrint.maxOreCosts
                            && toCheck.clayCount < bluePrint.obsidianRobotClayCosts
                            && toCheck.obsidianCount < bluePrint.geodeRobotObsidianCosts)
                            || newToVisit.isEmpty())
                    {
                        newToVisit.add(GameState(
                            minute = toCheck.minute+1,
                            oreCount = oreCount,
                            clayCount = clayCount,
                            obsidianCount = obsidianCount,
                            geodeCount = geodeCount,
                            oreRobots = toCheck.oreRobots,
                            clayRobots = toCheck.clayRobots,
                            obsidianRobots = toCheck.obsidianRobots,
                            geodeRobots = toCheck.geodeRobots,
                        ))
                    }
                }
            }
            // Reached time limit, record highest value of geodes
            else
            {
                if (toCheck.geodeCount >= highestGeodeCount)
                {
                    highestGeodeCount = toCheck.geodeCount
                }
            }

            toVisit.addAll(newToVisit)
        }

        return highestGeodeCount
    }

    private fun parseBlueprint(input: String): BluePrint
    {
        val (id, oreOre, clayOre, obsOre, obsClay, geodeOre, geodeObs) = INPUT_REGEX.matchEntire(input)!!.destructured
        return BluePrint(id.toInt(), oreOre.toInt(), clayOre.toInt(), obsOre.toInt(), obsClay.toInt(), geodeOre.toInt(), geodeObs.toInt())
    }

    data class GameState(
        val minute: Int,

        val oreCount: Int,
        val clayCount: Int,
        val obsidianCount: Int,
        val geodeCount: Int,

        val oreRobots: Int,
        val clayRobots: Int,
        val obsidianRobots: Int,
        val geodeRobots: Int
    )
    {
        init
        {
            require(oreCount>=0)
            require(clayCount>=0)
            require(obsidianCount>=0)
            require(geodeCount>=0)
        }
    }

    data class BluePrint(
        val id: Int,

        val oreRobotOreCosts: Int,

        val clayRobotOreCosts: Int,

        val obsidianRobotOreCosts: Int,
        val obsidianRobotClayCosts: Int,

        val geodeRobotOreCosts: Int,
        val geodeRobotObsidianCosts: Int,
    )
    {
        val maxOreCosts: Int by lazy {
            listOf(oreRobotOreCosts, clayRobotOreCosts, obsidianRobotOreCosts, geodeRobotOreCosts).max()
        }
    }

    companion object
    {
        private val INPUT_REGEX = """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()
    }
}
