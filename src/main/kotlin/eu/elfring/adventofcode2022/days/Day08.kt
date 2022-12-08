package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day08 : AoCPuzzle(8)
{
    override fun part1(input: List<String>): Any
    {
        val trees = input.map { row ->
            row.toCharArray().map { treeChar -> treeChar.digitToInt() }
        }

        val visibleTrees = mutableSetOf<Pair<Int, Int>>()

        // Horizontal
        trees.forEachIndexed { yPos, row ->
            var highestTree: Int? = null

            // Left to right
            row.forEachIndexed { xPos, treeInt ->
                if (highestTree == null || treeInt > highestTree!!)
                {
                    highestTree = treeInt
                    visibleTrees.add(xPos to yPos)
                }
            }

            // Right to left
            highestTree = null
            row.reversed().forEachIndexed { xPosInvert, treeInt ->
                if (highestTree == null || treeInt > highestTree!! )
                {
                    highestTree = treeInt
                    visibleTrees.add((row.size-1-xPosInvert) to yPos)
                }
            }
        }

        // Vertical
        for (x in 0.until(trees.first().size))
        {
            var highestTree: Int? = null
            // Top to bottom
            for (y in trees.indices)
            {
                val treeInt = trees[y][x]
                if (highestTree == null || treeInt > highestTree!! )
                {
                    highestTree = treeInt
                    visibleTrees.add(x to y)
                }
            }

            // Bottom to top
            highestTree = null
            for (y in (trees.size-1) downTo 0)
            {
                val treeInt = trees[y][x]
                if (highestTree == null || treeInt > highestTree!! )
                {
                    highestTree = treeInt
                    visibleTrees.add(x to y)
                }
            }
        }

        return visibleTrees.size
    }

    override fun part2(input: List<String>): Any
    {
        val trees = input.map { row ->
            row.toCharArray().map { treeChar -> treeChar.digitToInt() }
        }

        var highestScenicScore = 0

        trees.forEachIndexed { yPos, row ->
            row.forEachIndexed { xPos, treeInt ->
                var scenicScore = 0
                var treeCount = 0
                // Left to right
                for (lookingXPos in xPos+1 until row.size)
                {
                    treeCount++
                    if (trees[yPos][lookingXPos]>=treeInt)
                    {
                        break
                    }
                }
                scenicScore = treeCount

                // Right to left
                treeCount = 0
                for (lookingXPos in xPos-1 downTo 0)
                {
                    treeCount++
                    if (trees[yPos][lookingXPos]>=treeInt)
                    {
                        break
                    }
                }
                scenicScore *= treeCount

                // Top to bottom
                treeCount = 0
                for (lookingYPos in yPos+1 until trees.size)
                {
                    treeCount++
                    if (trees[lookingYPos][xPos]>=treeInt)
                    {
                        break
                    }
                }
                scenicScore *= treeCount

                // Bottom to top
                treeCount = 0
                for (lookingYPos in yPos-1 downTo 0)
                {
                    treeCount++
                    if (trees[lookingYPos][xPos]>=treeInt)
                    {
                        break
                    }
                }
                scenicScore *= treeCount

                if (scenicScore > highestScenicScore)
                {
                    highestScenicScore = scenicScore
                }
            }
        }

        return highestScenicScore
    }
}
