import java.io.BufferedReader
import java.io.FileReader
import java.lang.Integer.min
import java.lang.Integer.parseInt

data class Signal(var data: List<Int>) {

    constructor(raw: String) : this(raw.toCharArray().map { parseInt(it.toString()) })

    fun calcFFT(pattern: FFTPattern) {
        val resList = mutableListOf<Int>()

        for (i in IntRange(0, data.size - 1)) {
            val zipList = data.zip(pattern.getForElem(i).take(data.size).toList())
            val next = zipList.map { it.first * it.second }.sum()
            resList.add(kotlin.math.abs(next) % 10)
        }

        data = resList.toList()
    }
}

data class FFTPattern(val basePattern: IntArray) {
    fun getForElem(index: Int) = sequence {

        val multiplicity = index + 1

        var posInPattern = 0
        var posInSequence = 1

        while (true) {
            if (posInSequence == multiplicity) {
                posInPattern = (posInPattern + 1) % basePattern.size
                posInSequence = 0
            }

            yield(basePattern[posInPattern])

            posInSequence++
        }
    }
}

fun main(args: Array<String>) {
    println("--- Day 16: Flawed Frequency Transmission ---");

    val br = BufferedReader(FileReader("puzzle_input/day16-input1.txt"))
    val rawSignal = br.readLine()

    day16PartOne(rawSignal)

//    day16PartTwo(rawSignal)
}


fun day16PartOne(rawSignal: String) {
    println("\n--- Part One ---")

    val signal = Signal(rawSignal)
    val pattern = FFTPattern(intArrayOf(0, 1, 0, -1))

    repeat(100) {
        signal.calcFFT(pattern)
    }

    println("Resulting signal after 100 phases:\n$signal")
    println("\nResult: ${signal.data.subList(0, 8).joinToString(separator = "") { it.toString() }}")
}


fun day16PartTwo(rawSignal: String) {
    println("\n--- Part Two ---")

    val resultPosition = parseInt(rawSignal.substring(0, 7))

    val signal = Signal(buildString {
        (0..10000).forEach { append(rawSignal) }
    })

    val pattern = FFTPattern(intArrayOf(0, 1, 0, -1))

    repeat(100) {
        signal.calcFFT(pattern)
        println("Fold $it done...")
    }

    println("\nResult: ${signal.data.subList(resultPosition+1, resultPosition+9).joinToString(separator = "") { it.toString() }}")
}
