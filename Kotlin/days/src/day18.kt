package dukemarty.aoc2019.days.day18

import dukemarty.aoc2019.common.Coordinates
import dukemarty.aoc2019.days.day3.Coord
import java.io.BufferedReader
import java.io.FileReader


val ENTRANCE: Char = '@'


data class Edge(val v1: Vertex, val v2: Vertex, val distance: Int)

data class Vertex(val position: Coordinates, val content: Char, var closed: Boolean)

data class Graph(var vertices: ArrayList<Vertex> = ArrayList(), var edges: ArrayList<Edge> = ArrayList()) {

    fun addVertex(v: Vertex) {
        vertices.add(v)
    }

    fun addEdge(e: Edge) {
        if (!vertices.contains(e.v1)) {
            vertices.add(e.v1)
        }

        if (!vertices.contains(e.v2)) {
            vertices.add(e.v2)
        }

        edges.add(e)
    }

    fun findVertex(pos: Coordinates): Vertex? {
        val res = vertices.firstOrNull { it.position == pos }

        return res
    }
}

data class Map(val rawMap: List<String>) {

    var entrance: Vertex? = null
    var editableMap = rawMap.map { it.toCharArray() }
    var pathGraph = Graph()

    init {
        parseMap()
    }

    fun parseMap() {
        val start = findStart()
        entrance = Vertex(start, ENTRANCE, false)
        editableMap[start.y][start.x] = '*'

        buildGraph(entrance!!, editableMap)
    }

    private fun buildGraph(start: Vertex, map: List<CharArray>) {
        pathGraph.addVertex(start)
        val open = mutableListOf(start)

        while (open.any()) {
            printEditableMap()
            val next = open.first()
            val ways = openWays(next.position)

            if (ways.size == 0) {
                open.remove(next)
            } else if (ways.size == 1) {
                val edge = followPath(next, ways[0])
                if (edge != null) {
                    pathGraph.addEdge(edge)
                    open.add(edge.v2)
                }
                open.remove(next)
            } else {
                val edge = followPath(next, ways[0])
                if (edge != null) {
                    pathGraph.addEdge(edge)
                    open.add(edge.v2)
                }
            }
        }
    }

    private fun printEditableMap() {
        val s = editableMap.joinToString(separator = "\n") { it.joinToString(separator = "") { it.toString() } }
        println(s)
    }

    private fun followPath(start: Vertex, step: Coordinates): Edge? {
        var pos = step
        var distance = 1
        var finished = false
        var res: Edge? = null
        var toWrite = '*'

        while (!finished) {
            val field = editableMap[pos.y][pos.y]
            if (field.isLetter()) {
                val end = Vertex(pos, field, field.isUpperCase())
                res = Edge(start, end, distance)
                finished = true
            } else if (field == '+') {
                val end = pathGraph.findVertex(pos)
                if (end != null) {
                    res = Edge(start, end, distance)
                    finished = true
                } else {
                    println("ERROR: found no vertex for reached crossing at $pos")
                    res = null
                    finished = true
                }
            } else {
                val seqs = openWays(pos)
                when (seqs.size) {
                    0 -> {
                        println("Cul-de-sac, skipping this path!")
                        res = null
                        finished = true
                    }
                    1 -> {
                        pos = seqs.first()!!
                    }
                    else -> {
                        toWrite = '+'
                        val end = Vertex(pos, '.', false)
                        res = Edge(start, end, distance)
                    }
                }
                if (seqs.size > 1) {
                    println("No what to do?")
                }
            }

            editableMap[pos.y][pos.y] = toWrite
        }

        return res
    }

    private fun openWays(pos: Coordinates): ArrayList<Coordinates> {
        val res = ArrayList<Coordinates>()

        if (pos.x > 0 && isOpen(pos.x - 1, pos.y)) {
            res.add(Coordinates(pos.x - 1, pos.y))
        }

        if (pos.x < editableMap[pos.y].size - 1 && isOpen(pos.x + 1, pos.y)) {
            res.add(Coordinates(pos.x + 1, pos.y))
        }

        if (pos.y > 0 && isOpen(pos.x, pos.y - 1)) {
            res.add(Coordinates(pos.x, pos.y - 1))
        }

        if (pos.y < editableMap.size - 1 && isOpen(pos.x, pos.y + 1)) {
            res.add(Coordinates(pos.x, pos.y + 1))
        }


        return res
    }

    private fun isOpen(x: Int, y: Int): Boolean {
        return editableMap[y][x] != '#' && editableMap[y][x] != '*'
    }

    private fun findStart(): Coordinates {

        for (y in 0 until rawMap.size) {
            for (x in 0 until rawMap[y].length) {
                if (rawMap[y][x] == ENTRANCE) {
                    return Coordinates(x, y)
                }
            }
        }

        return Coordinates(-1, -1)
    }
}

fun main(args: Array<String>) {
    println("--- Day 18: Many-Worlds Interpretation ---");

    val br = BufferedReader(FileReader("puzzle_input/day18-input1.txt"))
    val rawMap = br.readLines()

    partOne(rawMap)

    partTwo(rawMap)
}


fun partOne(rawMap: List<String>) {
    println("\n--- Part One ---")

    val map = Map(rawMap)

    print(map)
}


fun partTwo(rawMap: List<String>) {
    println("\n--- Part Two ---")

}
