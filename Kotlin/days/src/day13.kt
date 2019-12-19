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

            if (nextTile.x == -1 && nextTile.y == 0){
                score = nextTile.tileId
            }else {
                tiles.add(nextTile)
            }

            i += 3
        }
    }

    fun getMax(): Pair<Int, Int>{
        val maxX = tiles.map{it.x}.max()
        val maxY = tiles.map{it.y}.max()

        return Pair(maxX!!.toInt(), maxY!!.toInt())
    }

    fun applyTiles(){
        for (t in tiles){
            val block = when (t.tileId){
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
        for (row in field){
            res += row.count { it == blockType }
        }

        return res
    }

    fun printScreen(){
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
//    day13PartTwo(line)
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

fun day13PartTwo(line: String) {
    println("\n--- Part Two ---")

    val program = IntcodeProgram(line)
    program.set(0, 2)

    val interpreter = IntcodeInterpreter(program)

    while (interpreter.state != interpreter.STATE_STOPPED){
        interpreter.runProgram()
        val outputList = interpreter.outputBuffer.map { it.value }
        val screen = Screen(outputList)
        println(screen)

        println("TODO XXXX")

        interpreter.clearOutput()
    }


//    println("Length of output: ${outputList.size}")
//    println(screen)
//    screen.applyTiles()
//    screen.printScreen()
//    println("Final result (#block tiles): ${screen.countBlocks('=')}")
}

