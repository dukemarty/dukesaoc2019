package dukemarty.aoc2019.days.day13

import dukemarty.aoc2019.common.*
import java.io.BufferedReader
import java.io.FileReader


data class Tile(val x: Int, val y: Int, val tileId: Int)

data class Screen(val gameOutput: List<Long>, val tiles: ArrayList<Tile> = ArrayList<Tile>(), var score: Int = 0) {

    val field = Array(25) { CharArray(45) { '.' } }

    init {
        var i = 0;
        while (i < gameOutput.size) {
            val nextTile = Tile(gameOutput[i].toInt(), gameOutput[i + 1].toInt(), gameOutput[i + 2].toInt())

            if (nextTile.x == -1 && nextTile.y == 0) {
                score = nextTile.tileId
            } else {
                tiles.add(nextTile)
            }

            i += 3
        }
    }

    fun getMax(): Pair<Int, Int> {
        val maxX = tiles.map { it.x }.max()
        val maxY = tiles.map { it.y }.max()

        return Pair(maxX!!.toInt(), maxY!!.toInt())
    }

    fun applyTiles() {
        for (t in tiles) {
            val block = when (t.tileId) {
                0 -> '.'
                1 -> '#'
                2 -> '='
                3 -> '-'
                4 -> '*'
                else -> 'x'
            }

            field[t.y][t.x] = block
        }
    }

    fun countBlocks(blockType: Char): Int {
        var res = 0
        for (row in field) {
            res += row.count { it == blockType }
        }

        return res
    }

    fun printScreen() {
        println(field.joinToString(separator = "\n") { it.joinToString(separator = "") { it.toString() } })
    }
}

fun main(args: Array<String>) {
    println("--- Day 13: Care Package ---");

    val br = BufferedReader(FileReader("puzzle_input/day13-input1.txt"))
    var line = br.readLine()

//    line = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
//    line = "1102,34915192,34915192,7,4,7,99,0"
//    line = "104,1125899906842624,99"

    day13PartOne(line)
    day13PartTwo(line)
}


fun day13PartOne(line: String) {
    println("\n--- Part One ---")

    val program = IntcodeProgram(line)

    val interpreter = IntcodeInterpreter(program)

    interpreter.runProgram()

    val outputList = interpreter.outputBuffer.map { it.value }
    val screen = Screen(outputList)

    println("Length of output: ${outputList.size}")
    println("Playing field size: ${screen.getMax()}")
    println(screen)
    screen.applyTiles()
    screen.printScreen()
    println("Final result (#block tiles): ${screen.countBlocks('=')}")
}

data class AutoPlayer(val interpreter: IntcodeInterpreter) : InterpreterOutputReceiver {

    val input = ArrayList<Long>()
    var ballOld = Coordinates(0, 0)
    var ball = Coordinates(0, 0)
    var paddle = Coordinates(0, 0)

    init {
        interpreter.addOutputInterpreter(this)
//        interpreter.appendInput(0L)
    }

    override fun appendInput(newInput: Long) {
        input.add(newInput)

        if (input.size == 3) {
            when (input[2]) {
                4L -> {
                    println("Received ball")
                    println("Paddle: $paddle")
                    ballOld.x = ball.x
                    ballOld.y = ball.y
                    ball.x = input[0].toInt()
                    ball.y = input[1].toInt()
                    println("Old ball: $ballOld")
                    println("Ball: $ball")

                    val diffX = paddle.x - ball.x
                    val diffY = ball.y - paddle.y
                    if (ballOld.x < ball.x) { // ball moves right
                        println("BALL moves right")
                        if (diffX <= 0) {
                            println("MOVE RIGHT")
                            interpreter.appendInput(1L)
                        } else {
                            if (diffX > diffY) {
                                println("MOVE LEFT")
                                interpreter.appendInput(-1L)
                            } else if (diffX < diffY) {
                                println("MOVE RIGHT")
                                interpreter.appendInput(1L)
                            } else {
                                println("STAY")
                                interpreter.appendInput(0L)
                            }
                        }
                    } else { // ball moves left
                        println("BALL moves left")
                        if (diffX >= 0) {
                            println("MOVE LEFT")
                            interpreter.appendInput(-1L)
                        } else {
                            if (diffX < diffY) {
                                println("MOVE RIGHT")
                                interpreter.appendInput(1L)
                            } else if (diffX > diffY) {
                                println("MOVE LEFT")
                                interpreter.appendInput(-1L)
                            } else {
                                println("STAY")
                                interpreter.appendInput(0L)
                            }
                        }
                    }
                }
                3L -> {
                    paddle.x = input[0].toInt()
                    paddle.y = input[1].toInt()
                }
            }

            input.clear()
        }
    }
}

fun day13PartTwo(line: String) {
    println("\n--- Part Two ---")

    val program = IntcodeProgram(line)
    program.set(0, 2)

    val interpreter = IntcodeInterpreter(program)
    val player = AutoPlayer(interpreter)

    var round = 0
    while (interpreter.state != interpreter.STATE_STOPPED) {
        println("Round $round")
        interpreter.runProgram()

        val screen = Screen(interpreter.outputBuffer.map { it.value })
        screen.applyTiles()
        screen.printScreen()

        ++round
    }

    println("Interpreter state: ${interpreter.state}")

    val outputList = interpreter.outputBuffer.map { it.value }
    val screen = Screen(outputList)
    println("Length of output: ${outputList.size}")
    println(screen)
    screen.applyTiles()
    screen.printScreen()
    println("Final result (#block tiles): ${screen.countBlocks('=')}")
    println("Final result (score): ${screen.score}")
}

