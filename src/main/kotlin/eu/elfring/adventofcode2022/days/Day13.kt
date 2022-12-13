package eu.elfring.adventofcode2022.days

import eu.elfring.adventofcode2022.AoCPuzzle

class Day13 : AoCPuzzle(13)
{
    override fun part1(input: List<String>): Any
    {
        val validPairs = input.chunked(3).withIndex().filter {
            val (left, right) = it.value.take(2).map(::parse)
           isValid(left, right) == true
        }

        return validPairs.sumOf { it.index+1 }
    }

    override fun part2(input: List<String>): Any
    {
        val newList = input.filter { it.isNotEmpty() }.toMutableList()
        newList.addAll(listOf("[[2]]", "[[6]]"))

        val orderedList = newList.map(::parse).toList().filterNotNull().sorted().reversed()

        val packetOne = PacketList(listOf(PacketList(listOf(PacketValue(2)))))
        val packetTwo = PacketList(listOf(PacketList(listOf(PacketValue(6)))))

        return (orderedList.indexOf(packetOne)+1) * (orderedList.indexOf(packetTwo)+1)
    }

    fun parse(line: String): Packet?
    {
        if (line.isEmpty())
        {
            return null
        }

        if (line[0].isDigit())
        {
            return PacketValue(line.toInt())
        }

        var bracketCount = 0
        var previousCommaIndex = 0

        val packets = mutableListOf<Packet?>()

        for ((index, character) in line.withIndex()) {
            if (character == '[') {
                bracketCount++
            } else if (character == ']') {
                bracketCount--
                if (bracketCount == 0) {
                    packets += parse(line.take(index).drop(previousCommaIndex + 1))
                }
            } else if (character == ',') {
                if (bracketCount == 1) {
                    packets += parse(line.take(index).drop(previousCommaIndex + 1))
                    previousCommaIndex = index
                }
            }
        }

        return PacketList(packets.filterNotNull())
    }

    companion object
    {
        fun isValid(left: Packet?, right: Packet?): Boolean?
        {
            // Both values
            if (left is PacketValue && right is PacketValue)
            {
                return if (left.value < right.value)
                {
                    // If the left integer is lower than the right integer, the inputs are in the right order.
                    true
                }
                else if (left.value > right.value)
                {
                    // If the left integer is higher than the right integer, the inputs are not in the right order.
                    false
                }
                else
                {
                    // Otherwise, the inputs are the same integer; continue checking the next part of the input.
                    null
                }
            }
            // Both lists
            else if (left is PacketList && right is PacketList)
            {
                for (i in 0 until left.packets.size)
                {
                    // compare the first value of each list, then the second value, and so on.
                    val rightValue = right.packets.getOrNull(i)
                    if (rightValue != null)
                    {
                        val isValid = isValid(left.packets[i], rightValue)
                        if (isValid != null)
                        {
                            return isValid
                        }
                    }
                    else
                    {
                        // If the right list runs out of items first, the inputs are not in the right order.
                        return false
                    }
                }
                return if (left.packets.size == right.packets.size)
                {
                    // If the lists are the same length and no comparison makes a decision about the order, continue checking the next part of the input.
                    null
                }
                else
                {
                    // If the left list runs out of items first, the inputs are in the right order.
                    true
                }
            }
            else
            {
                // If exactly one value is an integer, convert the integer to a list which contains that integer as its only value, then retry the comparison.
                val leftList = if (left is PacketList) left else PacketList(listOfNotNull(left))
                val rightList = if (right is PacketList) right else PacketList(listOfNotNull(right))

                return isValid(leftList, rightList)
            }
        }
    }

    sealed interface Packet: Comparable<Packet>
    data class PacketList(val packets: List<Packet>): Packet
    {
        override fun compareTo(other: Packet): Int
        {
            val valid = Day13.isValid(this, other)

            return if (valid == true) 1 else -1
        }
    }

    data class PacketValue(val value: Int): Packet
    {
        override fun compareTo(other: Packet): Int
        {
            TODO("Not yet implemented")
        }
    }
}
