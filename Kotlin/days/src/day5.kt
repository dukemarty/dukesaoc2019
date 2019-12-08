import dukemarty.aoc2019.common.IntcodeInterpreter
import dukemarty.aoc2019.common.IntcodeProgram
import java.io.BufferedReader
import java.io.FileReader

fun main(args: Array<String>) {
    println("--- Day 5: Sunny with a Chance of Asteroids ---");

    val br = BufferedReader(FileReader("puzzle_input/day5-input1.txt"))
    val line = br.readLine()

    partOne(line)

    partTwo(line)
}

fun partOne(line: String) {
    println("\n--- Part One ---")

    val program = IntcodeProgram(line)

    val interpreter = IntcodeInterpreter(program)
    interpreter.inputBuffer = arrayListOf(1)

    interpreter.runProgram()

    println("Output of program: ${interpreter.outputBuffer}")
}

fun partTwo(line: String) {
    println("\n--- Part Two ---")

    val program = IntcodeProgram(line)

    val interpreter = IntcodeInterpreter(program)
    interpreter.inputBuffer = arrayListOf(5)

    interpreter.runProgram()

    println("Output of program: ${interpreter.outputBuffer}")
}
