import dukemarty.aoc2019.common.IntcodeInterpreter
import dukemarty.aoc2019.common.IntcodeProgram
import dukemarty.aoc2019.common.InterpreterOutputReceiver
import java.io.BufferedReader
import java.io.FileReader


const val NONE = ' '
const val BLACK = '.'
const val WHITE = '#'

const val UP = 0
const val RIGHT = 1
const val DOWN = 2
const val LEFT = 3

data class Robot(var dir: Int = UP, var x: Int = 0, var y: Int = 0) : InterpreterOutputReceiver {

    var phaseFinished = true

    var hull: Hull? = null
    var sensorReceiver: InterpreterOutputReceiver? = null

    fun readSensor() {
        if (sensorReceiver != null && hull != null) {
            val colorUnderRobot = hull!!.getPanelColor(x, y)

            sensorReceiver!!.appendInput(
                    when (colorUnderRobot) {
                        NONE -> 0
                        BLACK -> 0
                        WHITE -> 1
                        else -> 0
                    })
        }
    }

    fun turn(turnDir: Int) {
        when (turnDir) {
            0 -> {
                dir = (dir + 3) % 4
            }
            1 -> {
                dir = (dir + 1) % 4
            }
        }
    }

    fun moveForward(stepWide: Int) {
        when (dir) {
            UP -> y += stepWide
            RIGHT -> x += stepWide
            DOWN -> y -= stepWide
            LEFT -> x -= stepWide
        }
    }

    fun paint(color: Int) {
        hull?.paintPanel(x, y, color)
    }

    override fun appendInput(newInput: Long) {
        if (phaseFinished) {
            paint(newInput.toInt())
            phaseFinished = false
        } else {
            turn(newInput.toInt())
            moveForward(1)
            phaseFinished = true
            readSensor()
        }
    }
}


data class Hull(val halfSize: Int, var paintCount: Int = 0) {

    val plane = Array(2 * halfSize + 1) { CharArray(2 * halfSize + 1) { NONE } }

    init {
        plane[halfSize][halfSize] = WHITE
    }

    fun getPanelColor(x: Int, y: Int): Char {
        val (row, col) = xyToRowCol(x, y)

        return plane[row][col]
    }

    fun paintPanel(x: Int, y: Int, color: Int) {
        val (row, col) = xyToRowCol(x, y)

        if (plane[row][col] == NONE) {
            ++paintCount
        }

        when (color) {
            0 -> {
                plane[row][col] = BLACK
            }
            1 -> {
                plane[row][col] = WHITE
            }
        }
    }

    fun printHull() {
        println("${plane.joinToString(separator = "\n") { it.joinToString(separator = "") { it.toString() } }}")
    }

    private fun xyToRowCol(x: Int, y: Int): Pair<Int, Int> {
        val row = halfSize - y
        val col = x + halfSize

        return Pair(row, col)
    }
}


fun main(args: Array<String>) {
    println("--- Day 11: Space Police ---");

    val br = BufferedReader(FileReader("puzzle_input/day11-input1.txt"))
    var line = br.readLine()

    day11PartOne(line)

//    day11PartTwo(line)
}


fun day11PartOne(line: String) {
    println("\n--- Part One ---")

    val program = IntcodeProgram(line)

    val hull = Hull(50)

    val robot = Robot()
    robot.hull = hull

    val interpreter = IntcodeInterpreter(program)
    interpreter.addOutputInterpreter(robot)

    robot.sensorReceiver = interpreter
    robot.readSensor()

    interpreter.runProgram()

    println("Output of program: ${interpreter.outputBuffer}")
    println("Number of painted panels: ${hull.paintCount}")
    println("Hull:")
    hull.printHull()
}

