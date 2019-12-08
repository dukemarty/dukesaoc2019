import java.io.BufferedReader
import java.io.FileReader


data class SpaceImage(val rawImage: String) {

}


fun main(args: Array<String>) {
    println("--- Day 8: Space Image Format ---");

    val br = BufferedReader(FileReader("puzzle_input/day8-input1.txt"))
    val rawImage = br.readLine()

    day8partOne(rawImage)

    day8partTwo(rawImage)
}

fun day8partOne(rawImage: String) {
    val imageWidth = 25
    val imageHeight = 6
    val pixelsPerLayer = imageWidth * imageHeight
    val layerCount = rawImage.length / pixelsPerLayer

    println("\n--- Part One ---")

    val count0 = IntArray(layerCount)
    val count1 = IntArray(layerCount)
    val count2 = IntArray(layerCount)

    for (i in IntRange(0, layerCount - 1)) {
        val layer = rawImage.substring(pixelsPerLayer * i, pixelsPerLayer * (i + 1))
        count0[i] = layer.count { it == '0' }
        count1[i] = layer.count { it == '1' }
        count2[i] = layer.count { it == '2' }
    }

    var checkLayer = 0
    for (i in IntRange(1, layerCount - 1)) {
        if (count0[i] < count0[checkLayer]){
            checkLayer = i
        }
    }

    println("Checksum: ${count1[checkLayer]} * ${count2[checkLayer]} = ${count1[checkLayer] * count2[checkLayer]}")
}

fun day8partTwo(rawImage: String) {
    println("\n--- Part Two  ---")


//    println("Optimal amplifier output: $maxRes for combination [${combination.joinToString { "$it" }}]")
}

