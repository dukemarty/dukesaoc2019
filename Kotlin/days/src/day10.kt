import java.io.BufferedReader
import java.io.FileReader
import java.lang.StringBuilder
import kotlin.math.*

data class Coordinate(val x: Int, val y: Int)

data class Asteroid(val Coord: Coordinate, var WatchCount: Int = 0, var Angle: Double = -1.0, var VaporIndex: Int = -1) {

    fun distance(other: Asteroid): Double {
        return sqrt((Coord.x - other.Coord.x).toDouble().pow(2) + (Coord.y - other.Coord.y).toDouble().pow(2))
    }
}

data class AsteroidMap(val rawMap: List<String>) {

    val asteroids: HashSet<Asteroid> = listAsteroids()

    init {
    }

    fun calcThetasRelativeTo(root: Asteroid) {
//        println("Relative to root=$root:")
        for (ast in asteroids) {
            if (ast == root) continue

            val reversedFrac = atan2(ast.Coord.y.toDouble() - root.Coord.y, ast.Coord.x.toDouble() - root.Coord.x)
            ast.Angle = ((reversedFrac + PI / 2.0) + 2.0 * PI) % (2.0 * PI)
//            println("    $ast")
        }
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

    val br = BufferedReader(FileReader("puzzle_input/day10-input1.txt"))
    val map = AsteroidMap(br.readLines())

    println("Loaded map:\n$map")

    val stationAsteroid = day10partOne(map)

    if (stationAsteroid != null) {
        day10partTwo(map, stationAsteroid)
    } else {
        println("Skipped part two because no station was found. :)")
    }
}

fun day10partOne(map: AsteroidMap): Asteroid? {

    println("\n--- Part One ---")

    countAsteroidsInSight(map)

    println("Processed asteroids:\n${map.asteroids}")
    val optimalAsteroid = map.asteroids.maxBy { it.WatchCount }
    println("Optimal position: $optimalAsteroid")

    return optimalAsteroid
}

private fun countAsteroidsInSight(map: AsteroidMap) {
    for (ast in map.asteroids) {
//        println("Calculating for $ast")
        var potential = HashSet<Asteroid>(map.asteroids)
        var checked = HashSet<Asteroid>()
        potential.remove(ast)

        while (!potential.isEmpty()) {
            val next = potential.first()
//            println("    Comparing next=$next")
            val dirX = ast.Coord.x - next.Coord.x
            val dirY = ast.Coord.y - next.Coord.y

            val sightLine =
                    if (dirX == 0) {
                        potential.filter { ast.Coord.x == it.Coord.x && (ast.Coord.y - it.Coord.y) * dirY > 0 }
                    } else if (dirY == 0) {
                        potential.filter { ast.Coord.y == it.Coord.y && (ast.Coord.x - it.Coord.x) * dirX > 0 }
                    } else {
                        potential.filter {
                            if (it.Coord.y != ast.Coord.y) {
                                val dx = ast.Coord.x - it.Coord.x
                                val dy = ast.Coord.y - it.Coord.y
                                val diff = abs(dx.toFloat() / dy.toFloat() - dirX.toFloat() / dirY.toFloat())
//                                println("            Compare: $ast with $it -> $diff")
                                dx * dirX > 0 && dy * dirY > 0 && diff < 0.0001
                            } else {
                                false
                            }
                        }
                    }

//            println("        Asteroids in line: $sightLine")
            val closest = sightLine.minBy { abs(ast.Coord.x - it.Coord.x) + abs(ast.Coord.y - it.Coord.y) }!!
//            println("        Closest of those: $closest")
            checked.add(closest)
            potential.removeAll(sightLine)
        }

        ast.WatchCount = checked.size
    }
}

fun day10partTwo(map: AsteroidMap, station: Asteroid) {
    println("\n--- Part Two  ---")

    map.calcThetasRelativeTo(station)
    shootAsteroids(map, station)

    if (map.asteroids.size < 200) {
        println("Not enough asteroids there for part two puzzle?!")
        return
    }

    val resAsteroid = map.asteroids.first { it.VaporIndex == 200 }

    if (resAsteroid != null) {
        println("Found result asteroid which is vaporized as 200th: $resAsteroid")
        println("Puzzle solution: ${resAsteroid.Coord.x * 100 + resAsteroid.Coord.y}")
    }
}

fun shootAsteroids(map: AsteroidMap, station: Asteroid) {
    var ordered = map.asteroids.sortedBy { it.Angle }.drop(1)

    var vi = 1
    var shootAngle = 0.0
    while (vi < ordered.size) {
        val potTargetIndex = ordered.indexOfFirst { it.VaporIndex < 0 && it.Angle >= shootAngle }
        if (potTargetIndex < 0) {
            shootAngle = 0.0
            continue
        }
        shootAngle = ordered[potTargetIndex].Angle
        var target = ordered.filter { it.VaporIndex < 0 && (it.Angle - shootAngle) < 0.0001 }.minBy { station.distance(it) }

        if (target != null) {
            target.VaporIndex = vi

            ++vi
        }

        shootAngle += 0.001
    }

//    println("${map.asteroids.sortedBy { it.VaporIndex }}")
//    println("${ordered.sortedBy { it.VaporIndex }}")
}