import java.io.BufferedReader
import java.io.FileReader
import java.lang.StringBuilder
import kotlin.math.abs

data class Coordinate(val x: Int, val y: Int)

data class Asteroid(val Coord: Coordinate, var WatchCount: Int = 0)

data class AsteroidMap(val rawMap: List<String>) {

    val asteroids: HashSet<Asteroid> = listAsteroids()

    init {
    }

    private fun listAsteroids(): HashSet<Asteroid> {
        var res = HashSet<Asteroid>()

        for ((ri, row) in rawMap.withIndex()) {
            for ((ci, column) in row.withIndex()) {
                if (column == '#') {
                    res.add(Asteroid(Coordinate(ci, ri)))
                }
            }
        }

        return res
    }

    override fun toString(): String {
        val sb = StringBuilder()

        sb.appendln("Image:")
        sb.appendln(rawMap.joinToString(separator = "\n") { it })

        sb.appendln("\nAsteroids:")
        sb.appendln("$asteroids")

        return sb.toString()
    }
}

fun main(args: Array<String>) {
    println("--- Day 10: Monitoring Station ---");

    val br = BufferedReader(FileReader("puzzle_input/day10-test1.txt"))
    val map = AsteroidMap(br.readLines())

    println("Loaded map:\n$map")

    day10partOne(map)

    day10partTwo(map)
}

fun day10partOne(map: AsteroidMap) {

    println("\n--- Part One ---")

    countAsteroidsInSight(map)

    println("Processed asteroids:\n${map.asteroids}")
}

private fun countAsteroidsInSight(map: AsteroidMap) {
    for (ast in map.asteroids) {
        println("Calculating for $ast")
        var potential = HashSet<Asteroid>(map.asteroids)
        var checked = HashSet<Asteroid>()
        potential.remove(ast)

        while (!potential.isEmpty()) {
            val next = potential.first()
            println("    Comparing next=$next")
            val dirX = ast.Coord.x - next.Coord.x
            val dirY = ast.Coord.y - next.Coord.y

            val sightLine =
                    if (dirX == 0) {
                        potential.filter { (ast.Coord.y - it.Coord.y) * dirY > 0 }
                    } else if (dirY == 0) {
                        potential.filter { (ast.Coord.x - it.Coord.x) * dirX > 0 }
                    } else {
                        potential.filter {
                            if (it.Coord.y != ast.Coord.y) {
                                val diff = abs((ast.Coord.x - it.Coord.x) / (ast.Coord.y - it.Coord.y) - dirX / dirY)
                                println("            Compare: $ast with $it -> $diff")
                                diff < 0.0001
                            } else {
                                false
                            }
                        }
                    }

            println("        Asteroids in line: $sightLine")
            val closest = sightLine.minBy { abs(ast.Coord.x - it.Coord.x) + abs(ast.Coord.y - it.Coord.y) }!!
            println("        Closest of those: $closest")
            checked.add(closest)
            potential.removeAll(sightLine)
        }

        ast.WatchCount = checked.size
    }
}

fun day10partTwo(map: AsteroidMap) {
    println("\n--- Part Two  ---")

//    val mergedImage = image.mergeLayers('2')
//
//    println("Resulting image after merge:")
//    mergedImage.formatLayer(0, '0', '1')
}

