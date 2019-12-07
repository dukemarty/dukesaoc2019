import dukemarty.aoc2019.common.IntcodeInterpreter
import dukemarty.aoc2019.common.IntcodeProgram
import java.io.BufferedReader
import java.io.FileReader

fun main(args: Array<String>) {
    println("--- Day 7: Amplification Circuit ---");

    val br = BufferedReader(FileReader("puzzle_input/day7-input1.txt"))
    val line = br.readLine()

    day7partOne(line)

//    day7partTwo(line)
}

fun day7partOne(line: String) {
    println("\n--- Part One ---")

    var maxRes = Int.MIN_VALUE
    var combination = IntArray(5)
    var used = HashSet<Int>()
    for (i1 in IntRange(0, 4)) {
        used.clear()
        used.add(i1)
        for (i2 in IntRange(0, 4)) {
            if (used.contains(i2)) continue
            used.add(i2)
            for (i3 in IntRange(0, 4)) {
                if (used.contains(i3)) continue
                used.add(i3)
                for (i4 in IntRange(0, 4)) {
                    if (used.contains(i4)) continue
                    used.add(i4)
                    for (i5 in IntRange(0, 4)) {
                        if (used.contains(i5)) continue

                        val progs = arrayOf(IntcodeProgram(line), IntcodeProgram(line), IntcodeProgram(line), IntcodeProgram(line), IntcodeProgram(line))
                        val amps = arrayOf(IntcodeInterpreter(progs[0]), IntcodeInterpreter(progs[1]), IntcodeInterpreter(progs[2]), IntcodeInterpreter(progs[3]), IntcodeInterpreter(progs[4]))

                        amps[0].inputBuffer = intArrayOf(i1, 0)
                        amps[0].runProgram()
                        println("   Output Amp1 for input $i1: ${amps[0].outputBuffer.joinToString { "$it" }}")

                        amps[1].inputBuffer = intArrayOf(i2, amps[0].outputBuffer[0].value)
                        amps[1].runProgram()
                        println("   Output Amp2 for input $i2: ${amps[1].outputBuffer[0].value}")

                        amps[2].inputBuffer = intArrayOf(i3, amps[1].outputBuffer[0].value)
                        amps[2].runProgram()
                        println("   Output Amp3 for input $i3: ${amps[2].outputBuffer[0].value}")

                        amps[3].inputBuffer = intArrayOf(i4, amps[2].outputBuffer[0].value)
                        amps[3].runProgram()
                        println("   Output Amp4 for input $i4: ${amps[3].outputBuffer[0].value}")

                        amps[4].inputBuffer = intArrayOf(i5, amps[3].outputBuffer[0].value)
                        amps[4].runProgram()
                        println("   Output Amp5 for input $i5: ${amps[4].outputBuffer[0].value}")

                        val result = amps[4].outputBuffer[0].value
                        if (result > maxRes){
                            maxRes = result
                            combination = intArrayOf(i1, i2, i3, i4, i5)
                        }
                    }
                    used.remove(i4)
                }
                used.remove(i3)
            }
            used.remove(i2)
        }
        used.remove(i1)
    }

    println("Optimal amplifier output: $maxRes for combination [${combination.joinToString { "$it" }}]")
}

fun day7partTwo(line: String) {
    println("\n--- Part Two ---")

    val program = IntcodeProgram(line)

    val interpreter = IntcodeInterpreter(program)
    interpreter.inputBuffer = intArrayOf(5)

    interpreter.runProgram()

    println("Output of program: ${interpreter.outputBuffer}")
}
