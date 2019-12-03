package dukemarty.aoc2019.days.day3

import java.io.BufferedReader
import java.io.FileReader

const val testInput1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72\nU62,R66,U55,R34,D71,R55,D58,R83"
const val testDistance1 = 159

const val testInput2 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\nU98,R91,D20,R16,D67,R40,U7,R15,U6,R7"
const val testDistance2 = 135




fun main() {
    println("--- Day 3: Crossed Wires ---")

    val br = BufferedReader(FileReader("puzzle_input/day3-input1.txt"))
    val puzzleInput1FromFile = br.lineSequence()
    val puzzleLayout = Layout(puzzleInput1FromFile)
    println("Loaded layout:\n$puzzleLayout")

    partOne(puzzleLayout)

    partTwo(puzzleLayout)
}

fun partOne(layout: Layout) {
    println("\nPart One")

    val board = CircuitBoard(11000, 11000)

    board.drawWire('1', layout.lines[0])
    board.drawWire('2', layout.lines[1])

    println("All found crossings: ${board.crossings}")
    println("All crossings distances: ${board.getCrossingDistances()}")
    println("Shortest distance to a crossing: ${board.getCrossingDistances().min()}")
}

fun partTwo(layout: Layout) {
    println("\nPart Two")

    val board = CircuitBoard(11000, 11000)

    board.drawAndMeasureWire('1', layout.lines[0])
    board.drawAndMeasureWire('2', layout.lines[1])

    println("All found crossings: ${board.crossings}")
    println("All crossings summed step distances: ${board.getCrossingsStepDistances()}")
    println("Shortest distance to a crossing: ${board.getCrossingsStepDistances().min()}")
}
