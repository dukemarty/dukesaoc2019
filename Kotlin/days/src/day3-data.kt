package dukemarty.aoc2019.days.day3

data class Coord(var x: Int, var y: Int) {

    constructor(orig: Coord) : this(orig.x, orig.y)
}

data class Wire(val Direction: Char, val Width: Int)

data class WireLine(var Line: List<Wire>)
