package dukemarty.aoc2019.common

data class Coordinates(var x: Int, var y: Int) {

    constructor(orig: Coordinates) : this(orig.x, orig.y)

    fun step(xMove: Int, yMove: Int): Coordinates {
        return Coordinates(x + xMove, y + yMove)
    }
}

data class Map(val width: Int, val height: Int, val offsetX: Int, val offsetY: Int) {

    val plane = Array(height) { CharArray(width) { ' ' } }

    init {

    }

    fun get(pos: Coordinates) = get(pos.x, pos.y)

    fun get(x: Int, y: Int): Char {
        return plane[y + offsetY][x + offsetX]
    }

    fun set(pos: Coordinates, value: Char) = set(pos.x, pos.y, value)

    fun set(x: Int, y: Int, value: Char) {
        plane[y + offsetY][x + offsetX] = value
    }

    fun printMap() {
        println(plane.joinToString(separator = "\n") { it.joinToString(separator = "") { it.toString() } })
    }
}