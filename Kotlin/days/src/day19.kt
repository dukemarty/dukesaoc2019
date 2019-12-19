package dukemarty.aoc2019.day19

import dukemarty.aoc2019.common.IntcodeInterpreter
import dukemarty.aoc2019.common.IntcodeProgram
import java.io.BufferedReader
import java.io.FileReader


fun main(args: Array<String>) {
    println("--- Day 19: Tractor Beam ---");

    val br = BufferedReader(FileReader("puzzle_input/day19-input1.txt"))
    var line = br.readLine()

    partOne(line)

    partTwo(line)
}

fun partOne(line: String) {
    println("\n--- Part One ---")

    val program = IntcodeProgram(line)
    val interpreter = IntcodeInterpreter(program)
    val scanfield = Array(50) { CharArray(50) { '.' } }
    var beamSize = 0

    for (r in 0 until scanfield.size) {
        for (c in 0 until scanfield[0].size) {
            interpreter.reset()
            interpreter.appendInput(c.toLong())
            interpreter.appendInput(r.toLong())

            interpreter.runProgram()

            if (interpreter.outputBuffer[0].value == 1L) {
                scanfield[r][c] = '#'
                ++beamSize
            }

            interpreter.clearOutput()
        }
    }

    println("Scanfield:\n${scanfield.joinToString(separator = "\n"    ) { itRow -> itRow.joinToString(separator = "") { itCol -> itCol.toString() } }}")
    println("\nSize of beam: $beamSize")
}


fun partTwo(line: String) {
    println("\n--- Part Two ---")

}

