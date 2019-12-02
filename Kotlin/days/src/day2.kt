
import java.io.*


fun main(args: Array<String>) {

    println("--- Day 2: 1202 Program Alarm ---")

    val br = BufferedReader(FileReader("puzzle_input/day2-input1.txt"))
    val line = br.readLine()

    partOne(line)

//    partTwo(puzzleInput1FromFile)
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


