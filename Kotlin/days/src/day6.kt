import java.io.BufferedReader
import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class SpaceObject(val name: String) {

    var orbits: SpaceObject? = null
    var orbiters = ArrayList<SpaceObject>()

    var fullOrbitCount: Int = 0
    var fullOrbitersCount: Int = 0

    var followedLinks = ArrayList<SpaceObject>()
    var alreadyFollowed = false

    fun updateFullOrbitCount() {
        fullOrbitCount = 0
        fullOrbitersCount = 0

        for (o in orbiters) {
            fullOrbitCount += o.fullOrbitCount + 1 + o.fullOrbitersCount
            fullOrbitersCount += o.fullOrbitersCount + 1
        }
    }

    fun resetSearchCache() {
        followedLinks.clear()
        alreadyFollowed = false
    }

    fun markAsFollowed() {
        alreadyFollowed = true
    }

    fun nextUnfollowed(): String? {
        for (os in orbiters) {
            if (!followedLinks.contains(os) && !os.alreadyFollowed) {
                followedLinks.add(os)
                return os.name
            }
        }

        if (orbits != null && !followedLinks.contains(orbits!!) && !orbits!!.alreadyFollowed) {
            followedLinks.add(orbits!!)
            return orbits?.name
        }

        return null
    }

    override fun toString(): String {
        val sb = StringBuilder("SO('$name'")

        if (orbits != null) {
            sb.append(", <${orbits!!.name}")
        }

        if (orbiters != null && orbiters.size > 0) {
            sb.append(", >[${orbiters.joinToString { it.name }} ]")
        }

        sb.append(") -> #$fullOrbitCount with #$fullOrbitersCount")

        return sb.toString()
    }
}

class OrbitGraph {

    val graph = HashMap<String, SpaceObject>()
    val leaves = HashSet<String>()
    val roots = HashSet<String>()

    fun addConnection(to: String, from: String) {
        if (!graph.containsKey(from)) {
            graph[from] = SpaceObject(from)
            leaves.add(from)
        }
        if (roots.contains(from)) {
            roots.remove(from)
        }
        val orbiter = graph[from]

        if (!graph.containsKey(to)) {
            graph[to] = SpaceObject(to)
            roots.add((to))
        }
        if (leaves.contains(to)) {
            leaves.remove(to)
        }
        val orbited = graph[to]

        if (orbited != null && orbiter != null) {
            orbited.orbiters.add(orbiter)
            orbiter.orbits = orbited
        }
    }

    fun countAllOrbits() {
        val workList = leaves.toMutableList()

        while (!workList.isEmpty()) {
            val next = workList.first()
            workList.removeAt(0)

            val obj = graph[next]
            if (obj != null) {
                obj.updateFullOrbitCount()

                if (obj.orbits != null) {
                    val o = obj.orbits
                    if (!workList.contains(o!!.name)) {
                        workList.add(o.name)
                    }
                }
            }
        }


    }

    fun searchPath(from: String, to: String): Collection<String> {
        return depthFirstSearch(from) { it.name == to }
    }

    private fun depthFirstSearch(start: String, checkTarget: (SpaceObject) -> Boolean): Collection<String> {
        val res = Stack<String>()

        res.push(start)
        var current = graph[res.peek()]!!
        current.markAsFollowed()
        while (!checkTarget(current)) {

            val next = current.nextUnfollowed()

            if (next == null) {
                val popped = res.pop()
//                println("Popped: $popped")
            } else {
                res.push(next)
                graph[next]!!.markAsFollowed()
//                println("Now should follow ${graph[next]!!}")
            }

            current = graph[res.peek()]!!
        }

        return res
    }
}

fun main(args: Array<String>) {
    println("--- Day 6: Universal Orbit Map ---");

    val orbitGraph = loadInput("puzzle_input/day6-input1.txt")

    println("Root(s): ${orbitGraph.roots}")
    println("Leaves: ${orbitGraph.leaves}")

    partOne(orbitGraph)

    partTwo(orbitGraph)
}

fun loadInput(filename: String): OrbitGraph {
    val orbitGraph = OrbitGraph()

    val br = BufferedReader(FileReader(filename))

    for (line in br.readLines()) {
        val parts = line.split(")")

        orbitGraph.addConnection(parts[0], parts[1])
    }

//    for (so in orbitGraph.graph.keys) {
//        println("${orbitGraph.graph[so]} <- ${orbitGraph.graph[so]!!.orbiters.toString()}")
//    }

    return orbitGraph
}

fun partOne(orbits: OrbitGraph) {
    println("\n--- Part One ---")

    orbits.countAllOrbits()

    val sumOfAllOrbits = orbits.roots.map { orbits.graph[it]!!.fullOrbitCount }.sum()

    println("Sum of all orbits: $sumOfAllOrbits.")
}

fun partTwo(orbits: OrbitGraph) {
    println("\n--- Part Two ---")

    println("Start: ${orbits.graph["YOU"]}")
    println("Target: ${orbits.graph["SAN"]}")

    val path = orbits.searchPath("YOU", "SAN")

    println("Found path: $path")
    println("#Required orbit transits: ${path.size - 3}.")
}
