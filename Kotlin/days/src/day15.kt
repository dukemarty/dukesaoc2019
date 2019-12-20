package dukemarty.aoc2019.days.day15

import dukemarty.aoc2019.common.Coordinates
import dukemarty.aoc2019.common.IntcodeInterpreter
import dukemarty.aoc2019.common.IntcodeProgram
import dukemarty.aoc2019.common.Map
import dukemarty.aoc2019.days.day3.Coord
import java.io.BufferedReader
import java.io.FileReader

const val NORTH = 1
const val SOUTH = 2
const val WEST = 3
const val EAST = 4


data class Robot(val startX: Int, val startY: Int, var pos: Coordinates = Coordinates(startX, startY), var dir: Int = NORTH) {

    fun turnRight() {
        dir = nextDirClockwise(dir)
    }

    fun turnLeft() {
        dir = nextDirAntiClockwise(dir)
    }

    fun findExploreDir(map: Map): Int {
        if (map.get(getStepInDir(nextDirClockwise(dir))) != '#'){
            return nextDirClockwise(dir)
        } else if (map.get(getStepInDir(dir)) != '#'){
            return dir
        } else if (map.get(getStepInDir(nextDirAntiClockwise(dir))) != '#'){
            return nextDirAntiClockwise(dir)
        } else {
            return nextDirClockwise(nextDirClockwise(dir))
        }
    }

    fun findFreeDirClockwise(map: Map): Int {
        var res = dir
        while (map.get(getStepInDir(res)) == '#') {
            res = nextDirClockwise(res)
        }

        return res
    }

    fun moveStep() {
        when (dir) {
            NORTH -> pos.y -= 1
            EAST -> pos.x += 1
            SOUTH -> pos.y += 1
            WEST -> pos.x -= 1
        }
    }

    fun getStepInDir(moveDir: Int): Coordinates {
        return when (moveDir) {
            NORTH -> pos.step(0, -1)
            EAST -> pos.step(1, 0)
            SOUTH -> pos.step(0, 1)
            WEST -> pos.step(-1, 0)
            else -> Coordinates(pos)
        }
    }

    private fun nextDirClockwise(startDir: Int): Int {
        return when (startDir) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
            else -> -1
        }
    }

    private fun nextDirAntiClockwise(startDir: Int): Int {
        return when (startDir) {
            NORTH -> WEST
            EAST -> NORTH
            SOUTH -> EAST
            WEST -> SOUTH
            else -> -1
        }
    }
}

fun Map.findShortestPath(start: Coordinates, destination: Coordinates): List<Coordinates>{
    
}

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

    val posD = Coordinates(0, 0)
    var posO: Coordinates? = null
    val map = Map(51, 51, 25, 25)
    map.set(0, 0, 'D')
    val rob = Robot(0, 0)

    var oxygenFound = false
    while (!oxygenFound) {

        val nextDir = rob.findExploreDir(map)
        interpreter.appendInput(nextDir)
        interpreter.runProgram()

        when (interpreter.outputBuffer.last().value.toInt()) {
            0 -> map.set(rob.getStepInDir(nextDir), '#')
            1 -> {
                rob.dir = nextDir
                rob.moveStep()
                map.set(rob.pos, '.')
            }
            2 -> {
                oxygenFound = true
                rob.dir = nextDir
                rob.moveStep()
                map.set(rob.pos, 'O')
                posO = Coordinates(rob.pos)
            }
        }

        println(rob)
        map.printMap()
    }

    map.printMap()
}

