package dukemarty.aoc2019.days.day18

import java.io.BufferedReader
import java.io.FileReader

data class Map(val rawMap: List<String>){

    init {
        parseMap()
    }

    fun parseMap(){

    }
}

fun main(args: Array<String>) {
    println("--- Day 18: Many-Worlds Interpretation ---");

    val br = BufferedReader(FileReader("puzzle_input/day18-input1.txt"))
    val rawMap = br.readLines()

    partOne(rawMap)

//    day16PartTwo(rawSignal)
}


fun partOne(rawMap: List<String>) {
    println("\n--- Part One ---")

}


fun partTwo(rawSignal: String) {
    println("\n--- Part Two ---")

}
