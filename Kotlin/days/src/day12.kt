import java.io.BufferedReader
import java.io.FileReader
import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.sign

//<x=1, y=-4, z=3>
//<x=-14, y=9, z=-4>
//<x=-4, y=-6, z=7>
//<x=6, y=-9, z=-11>

data class Vector3d(var x: Int = 0, var y: Int = 0, var z: Int = 0) {

    operator fun plusAssign(other: Vector3d) {
        x += other.x
        y += other.y
        z += other.z
    }

    fun getAbsSum(): Int {
        return (abs(x.toDouble()) + abs(y.toDouble()) + abs(z.toDouble())).toInt()
    }
}

data class Moon(val line: String, var position: Vector3d = Vector3d(), val velocity: Vector3d = Vector3d()) {

    init {
        val coords = line
                .trim('<', '>')
                .split(",")
                .map { it.trim() }
                .map { it.split("=")[1].toInt() }

        position = Vector3d(coords[0], coords[1], coords[2])
    }

    fun getEnergy(): Int {
        return position.getAbsSum() * velocity.getAbsSum()
    }

    fun applyVelocity() {
        position += velocity
    }
}

class StarSystem(lines: Collection<String>) {
    var moons: List<Moon> = lines.map { Moon(it) }.toList()

    fun getTotalEnergy(): Int {
        return moons.sumBy { it.getEnergy() }
    }

    fun updateVelocity() {
        for (i in IntRange(0, moons.size - 2)) {
            for (j in IntRange(i + 1, moons.size - 1)) {
                applyGravity(i, j)
            }
        }
    }

    fun applyVelocity() {
        for (m in moons) {
            m.applyVelocity()
        }
    }

    private fun applyGravity(i: Int, j: Int) {
        moons[i].velocity.x -= moons[i].position.x.compareTo(moons[j].position.x).sign
        moons[j].velocity.x += moons[i].position.x.compareTo(moons[j].position.x).sign

        moons[i].velocity.y -= moons[i].position.y.compareTo(moons[j].position.y).sign
        moons[j].velocity.y += moons[i].position.y.compareTo(moons[j].position.y).sign

        moons[i].velocity.z -= moons[i].position.z.compareTo(moons[j].position.z).sign
        moons[j].velocity.z += moons[i].position.z.compareTo(moons[j].position.z).sign
    }

    override fun toString(): String {
        val sb = StringBuilder()

        sb.appendln("StarSystem:")
        sb.appendln(moons.joinToString(separator = "\n") { it.toString() })

        return sb.toString()
    }
}

fun main(args: Array<String>) {
    println("--- Day 12: The N-Body Problem ---");

    val br = BufferedReader(FileReader("puzzle_input/day12-input1.txt"))
    val starSystem = StarSystem(br.readLines())

    println("Loaded all celestial bodies:: $starSystem")

    for (step in IntRange(1, 1000)) {
        starSystem.updateVelocity()
        starSystem.applyVelocity()

        if (step > 998) {
            println("Step $step:\n$starSystem")
            println("Energy: ${starSystem.getTotalEnergy()}")
        }
    }

//    while (step < 2) {
//
//    }

//    val stationAsteroid = day10partOne(map)
//
//    if (stationAsteroid != null) {
//        day10partTwo(map, stationAsteroid)
//    } else {
//        println("Skipped part two because no station was found. :)")
//    }
}

