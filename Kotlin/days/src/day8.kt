import java.io.BufferedReader
import java.io.FileReader


data class SpaceImage(val rawImage: String) {
    val imageWidth = 25
    val imageHeight = 6
    val pixelsPerLayer = imageWidth * imageHeight
    val layerCount = rawImage.length / pixelsPerLayer

    fun getLayer(index: Int): String {
        return rawImage.substring(pixelsPerLayer * index, pixelsPerLayer * (index + 1))
    }

    fun printLayer(index: Int) {
        val layer = getLayer(index)

        for (i in IntRange(0, imageHeight - 1)) {
            println(layer.substring(imageWidth * i, imageWidth * (i + 1)))
        }
    }

    fun formatLayer(index: Int, black: Char, white: Char) {
        var layer = getLayer(index)
        layer = layer.replace(black, ' ')
        layer = layer.replace(white, '*')

        for (i in IntRange(0, imageHeight - 1)) {
            println(layer.substring(imageWidth * i, imageWidth * (i + 1)))
        }
    }

    fun mergeLayers(transparent: Char): SpaceImage {
        val resImage = CharArray(pixelsPerLayer)

        for (i in IntRange(0, pixelsPerLayer - 1)) {
            resImage[i] = topNonTransparentPixel(transparent, i)
        }

        return SpaceImage(String(resImage))
    }

    private fun topNonTransparentPixel(transparentColor: Char, pos: Int): Char {
        for (i in IntRange(0, layerCount - 1)) {
            if (rawImage[pixelsPerLayer * i + pos] != transparentColor) {
                return rawImage[pixelsPerLayer * i + pos]
            }
        }

        return transparentColor
    }
}


fun main(args: Array<String>) {
    println("--- Day 8: Space Image Format ---");

    val br = BufferedReader(FileReader("puzzle_input/day8-input1.txt"))
    val image = SpaceImage(br.readLine())

    println("Loaded image:\n$image")
    println("${image.pixelsPerLayer} px/layer, ${image.layerCount} layers")

    day8partOne(image)

    day8partTwo(image)
}

fun day8partOne(image: SpaceImage) {

    println("\n--- Part One ---")

    val count0 = IntArray(image.layerCount)
    val count1 = IntArray(image.layerCount)
    val count2 = IntArray(image.layerCount)

    for (i in IntRange(0, image.layerCount - 1)) {
        val layer = image.rawImage.substring(image.pixelsPerLayer * i, image.pixelsPerLayer * (i + 1))
        count0[i] = layer.count { it == '0' }
        count1[i] = layer.count { it == '1' }
        count2[i] = layer.count { it == '2' }
    }

    var checkLayer = 0
    for (i in IntRange(1, image.layerCount - 1)) {
        if (count0[i] < count0[checkLayer]) {
            checkLayer = i
        }
    }

    println("Checksum: ${count1[checkLayer]} * ${count2[checkLayer]} = ${count1[checkLayer] * count2[checkLayer]}")
}

fun day8partTwo(image: SpaceImage) {
    println("\n--- Part Two  ---")

    val mergedImage = image.mergeLayers('2')

    println("Resulting image after merge:")
    mergedImage.formatLayer(0, '0', '1')
}

