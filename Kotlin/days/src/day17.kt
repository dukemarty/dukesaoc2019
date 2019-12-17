package dukemarty.aoc2019.day17

import dukemarty.aoc2019.common.IntcodeInterpreter
import dukemarty.aoc2019.common.IntcodeProgram
import java.io.BufferedReader
import java.io.FileReader
import java.lang.StringBuilder

const val SCAFFOLD = '#'
const val OPEN_SPACE = '.'
const val ROBOT__UP = '^'
const val ROBOT__RIGHT = '>'
const val ROBOT__DOWN = 'v'
const val ROBOT__LEFT = '<'
const val ROBOT_TUMBLING = 'X'

data class Coordinate(val x: Int, val y: Int)

data class ScaffoldMap(val rawMap: CharArray) {

    val map: List<CharArray>
    var crossings = ArrayList<Coordinate>()

    init {
        val stringMap = rawMap.joinToString(separator = "").split("\n").filter { it.isNotEmpty() }
        map = stringMap.map { it.toCharArray() }.toList()

        identifyCrossings()
    }

    private fun identifyCrossings() {
        crossings.clear()

        for (y in 1 until map.size - 1) {
            for (x in 1 until map[y].size - 1) {
                if (isCrossing(x, y)) {
                    crossings.add(Coordinate(x, y))
                }
            }
        }
    }

    private fun isCrossing(x: Int, y: Int): Boolean {
        println("Check $x / $y")
        if (map[y][x] == OPEN_SPACE || map[y][x] == ROBOT_TUMBLING) {
            return false
        }

        return map[y - 1][x] == SCAFFOLD && map[y + 1][x] == SCAFFOLD && map[y][x - 1] == SCAFFOLD && map[y][x + 1] == SCAFFOLD
    }
}

fun main(args: Array<String>) {
    println("--- Day 17: Set and Forget ---");

    val br = BufferedReader(FileReader("puzzle_input/day17-input1.txt"))
    var line = br.readLine()

    day17PartOne(line)

    day17PartTwo(line)
}

fun day17PartOne(line: String) {
    println("\n--- Part One ---")

    val program = IntcodeProgram(line)

    val interpreter = IntcodeInterpreter(program)

    interpreter.runProgram()

    val map = ScaffoldMap(interpreter.outputBuffer.map { it.value.toChar() }.toCharArray())

    val outputImage = map.rawMap.joinToString(separator = "")
    println("Output of program:\n$outputImage")

    println("Found crossings: ${map.crossings}")
    val alignment = map.crossings.map { it.x * it.y }.sum()
    println("Resulting alignment: $alignment")
}


fun day17PartTwo(line: String) {
    println("\n--- Part One ---")

    val program = IntcodeProgram("2" + line.substring(1))

    val interpreter = IntcodeInterpreter(program)

    // Full program:
    //R,8,R,10,R,10,R,4,R,8,R,10,R,12,R,8,R,10,R,10,R,12,R,4,L,12,L,12,R,8,R,10,R,10,R,4,R,8,R,10,R,12,R,12,R,4,L,12,L,12,R,8,R,10,R,10,R,4,R,8,R,10,R,12,R,12,R,4,L,12,L,12
    val programMain = "A,B,A,C,A,B,C,A,B,C"
    val programA = "R,8,R,10,R,10"
    val programB = "R,4,R,8,R,10,R,12"
    val programC = "R,12,R,4,L,12,L,12"

    val sb = StringBuilder()
    sb.appendln(programMain)
    sb.appendln(programA)
    sb.appendln(programB)
    sb.appendln(programC)
    sb.appendln("n")

    sb.toString().toCharArray().forEach {
        if (it != 13.toChar()) {
            interpreter.appendInput(it.toLong())
        }
    }
    interpreter.runProgram()

//    val map = ScaffoldMap(interpreter.outputBuffer.map { it.value.toChar() }.toCharArray())
//
//    val outputImage = map.rawMap.joinToString(separator = "")
//    println("Output of program:\n$outputImage")
    println("Resulting output: ${interpreter.outputBuffer}")
    printOutputBufferAsAsciiString(interpreter)
}

fun printOutputBufferAsAsciiString(interpreter: IntcodeInterpreter) {
    val outString = interpreter.outputBuffer.map { it.value.toChar() }.toCharArray().joinToString(separator = "")
    println(outString)
}