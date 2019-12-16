import java.io.BufferedReader
import java.io.FileReader
import java.lang.Integer.min
import java.lang.Integer.parseInt

data class Signal(var data: List<Int>) {

    constructor(raw: String) : this(raw.toCharArray().map { parseInt(it.toString()) })

    fun calcFFT(pattern: FFTPattern) {
        val resList = mutableListOf<Int>()

        for (i in IntRange(0, data.size - 1)) {
            var pos=0
            val patt = pattern.getForElem(i).iterator()
            var res = 0
            while (pos < data.size){
                val next = patt.next()
                val toPos = min(data.size, pos+next.first)
                if (next.second != 0) {
                    res += data.subList(pos, toPos).sum() * next.second
                }

                pos = toPos
            }
            resList.add(kotlin.math.abs(res) % 10)
        }

        data = resList.toList()
    }
}

data class FFTPattern(val basePattern: IntArray) {
    fun getForElem(index: Int) = sequence {

        val multiplicity = index + 1

        if (multiplicity > 1){
            yield(Pair(multiplicity -1, basePattern[0]))
        }

        var posInPattern = 1

        while (true) {
            yield(Pair(multiplicity, basePattern[posInPattern]))

            posInPattern = (posInPattern + 1) % basePattern.size
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
