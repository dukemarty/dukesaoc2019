
package dukemarty.aoc2019.days

import java.io.*
import dukemarty.aoc2019.common.*


fun main(args: Array<String>) {

    println("--- Day 2: 1202 Program Alarm ---")

    val br = BufferedReader(FileReader("puzzle_input/day2-input1.txt"))
    val line = br.readLine()

    partOne(line)

    partTwo(line)
}

fun partOne(line: String) {
    println("\n--- Part One ---")

//    val t1 = "1,9,10,3,2,3,11,0,99,30,40,50"

    val program = IntcodeProgram(line)

    program.set(1, 12)
    program.set(2, 2)

    val interpreter = IntcodeInterpreter(program)

    interpreter.runDay1Style()

    println("Final program state at pos[0]: ${program.get(0)}")
}

fun partTwo(line: String) {
    println("\n--- Part Two ---")

    for (noun in 0..99) {
        for (verb in 0..99) {
            val program = IntcodeProgram(line)

            program.set(1, noun)
            program.set(2, verb)

            val interpreter = IntcodeInterpreter(program)
            interpreter.runDay1Style()

            if (program.get(0) == 19690720){
                println("Found result noun/verb = $noun / $verb => result: ${100 * noun + verb}")
            }
        }
    }

}

