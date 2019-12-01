
import java.io.*


fun main(args: Array<String>) {

    println("--- Day 1: The Tyranny of the Rocket Equation ---")

    var br = BufferedReader(FileReader("puzzle_input/day1-input1.txt"))
    val puzzleInput1FromFile = br.lineSequence().map { it -> it.toInt() }.toList()

    partOne(puzzleInput1FromFile)

    partTwo(puzzleInput1FromFile)
}

fun partOne(rocketModules: List<Int>) {
    println("\n--- Part One ---")

    var res = 0
    for (rm in rocketModules){
        res += (rm / 3) - 2
    }

    println("Required fuel for all modules of spacecraft: $res")
}

fun partTwo(rocketModules: List<Int>) {
    println("\n--- Part Two ---")

    var res = 0
    for (rm in rocketModules){
        res += calcAllFuelForModule(rm)
    }

    println("Required fuel for all modules of spacecraft: $res")
}

fun calcAllFuelForModule(moduleWeight: Int): Int {
    val base = (moduleWeight / 3) - 2
    var res = base

    var fuelBase = base
    while (fuelBase > 0) {
        var addFuel = (fuelBase / 3) - 2
        if (addFuel < 0){
            addFuel = 0
        }

        res += addFuel
        fuelBase = addFuel
    }

    return res
}