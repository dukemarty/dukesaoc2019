import dukemarty.aoc2019.common.IntcodeInterpreter
import dukemarty.aoc2019.common.IntcodeProgram
import java.io.BufferedReader
import java.io.FileReader

fun main(args: Array<String>) {
    println("--- Day 15: Oxygen System ---");

    val br = BufferedReader(FileReader("puzzle_input/day15-input1.txt"))
    var line = br.readLine()

//    line = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
//    line = "1102,34915192,34915192,7,4,7,99,0"
//    line = "104,1125899906842624,99"

    day15PartOne(line)

//    day7partTwo(line)
}


fun day15PartOne(line: String) {
    println("\n--- Part One ---")

    val program = IntcodeProgram(line)

    val interpreter = IntcodeInterpreter(program)

    interpreter.runProgram()

    println("Output of program: ${interpreter.outputBuffer}")
    println("Simpler: ${interpreter.outputBuffer.joinToString { it.value.toString() }}")
}

