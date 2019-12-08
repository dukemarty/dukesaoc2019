import dukemarty.aoc2019.common.IntcodeInterpreter
import dukemarty.aoc2019.common.IntcodeProgram
import java.io.BufferedReader
import java.io.FileReader

fun main(args: Array<String>) {
    println("--- Day 7: Amplification Circuit ---");

    val br = BufferedReader(FileReader("puzzle_input/day7-input1.txt"))
    val line = br.readLine()

    day7partOne(line)

    day7partTwo(line)
}

fun day7partOne(line: String) {
    println("\n--- Part One ---")

    var maxRes = Int.MIN_VALUE
    var combination = IntArray(5)

    for (perm in permute(listOf(0, 1, 2, 3, 4))) {
        val progs = arrayOf(IntcodeProgram(line), IntcodeProgram(line), IntcodeProgram(line), IntcodeProgram(line), IntcodeProgram(line))
        val amps = arrayOf(IntcodeInterpreter(progs[0]), IntcodeInterpreter(progs[1]), IntcodeInterpreter(progs[2]), IntcodeInterpreter(progs[3]), IntcodeInterpreter(progs[4]))

        amps[0].inputBuffer = arrayListOf(perm[0], 0)
        for (i in IntRange(0, 3)) {
            amps[i].addOutputInterpreter(amps[i + 1])

            amps[i + 1].inputBuffer = arrayListOf(perm[i + 1])
        }

        for (i in IntRange(0, 4)) {
            amps[i].runProgram()
        }

        val result = amps[4].outputBuffer[0].value
        if (result > maxRes) {
            maxRes = result
            combination = perm.toIntArray()
        }
    }

    println("Optimal amplifier output: $maxRes for combination [${combination.joinToString { "$it" }}]")
}

fun day7partTwo(line: String) {
    println("\n--- Part Two ---")

    var maxRes = Int.MIN_VALUE
    var combination = IntArray(5)

    for (perm in permute(listOf(5, 6, 7, 8, 9))) {
        val progs = arrayOf(IntcodeProgram(line), IntcodeProgram(line), IntcodeProgram(line), IntcodeProgram(line), IntcodeProgram(line))
        val amps = arrayOf(IntcodeInterpreter(progs[0]), IntcodeInterpreter(progs[1]), IntcodeInterpreter(progs[2]), IntcodeInterpreter(progs[3]), IntcodeInterpreter(progs[4]))

        amps[0].inputBuffer = arrayListOf(perm[0], 0)
        for (i in IntRange(0, 3)) {
            amps[i].addOutputInterpreter(amps[i + 1])

            amps[i + 1].inputBuffer = arrayListOf(perm[i + 1])
        }
        amps[4].addOutputInterpreter(amps[0])

        while (amps[4].state != amps[4].STATE_STOPPED) {
            for (i in IntRange(0, 4)) {
                amps[i].runProgram()
            }
        }

        val result = amps[4].outputBuffer[amps[4].outputBuffer.size - 1].value
        if (result > maxRes) {
            maxRes = result
            combination = perm.toIntArray()
        }
    }

    println("Optimal amplifier output: $maxRes for combination [${combination.joinToString { "$it" }}]")
}

private fun permute(list: List<Int>): List<List<Int>> {
    if (list.size == 1) return listOf(list)
    val perms = mutableListOf<List<Int>>()
    val sub = list[0]
    for (perm in permute(list.drop(1)))
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, sub)
            perms.add(newPerm)
        }
    return perms
}