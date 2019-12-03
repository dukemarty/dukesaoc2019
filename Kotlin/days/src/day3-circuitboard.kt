package dukemarty.aoc2019.days.day3


class CircuitBoard(halfWidth: Int, halfHeight: Int) {

    var x0: Int = halfWidth
    var y0: Int = halfHeight

    var board: Array<CharArray>
    var measureBoard: Array<IntArray>

    var crossings = mutableListOf<Coord>()

    init {
        board = Array(2 * halfHeight + 1) { CharArray(2 * halfWidth + 1, { '.' }) }
        measureBoard = Array(2 * halfHeight + 1) { IntArray(2 * halfWidth + 1, { 0 }) }
    }

    fun getCrossingsStepDistances(): Collection<Int> {
        return crossings.map { c -> measureBoard[c.y][c.x] }
    }

    fun getCrossingDistances(): Collection<Int> {
        return crossings.map { c -> Math.abs(c.x - x0) + Math.abs(c.y - y0) }
    }

    fun drawAndMeasureWire(id: Char, wire: WireLine) {
        val currPos = Coord(x0, y0)
        var stepCount = 1

        for (bend in wire.Line) {
            when (bend.Direction) {
                'R' -> stepCount = drawAndMeasureBend(id, currPos, bend.Width, { pos -> pos.x++ }, stepCount)
                'D' -> stepCount = drawAndMeasureBend(id, currPos, bend.Width, { pos -> pos.y++ }, stepCount)
                'L' -> stepCount = drawAndMeasureBend(id, currPos, bend.Width, { pos -> pos.x-- }, stepCount)
                'U' -> stepCount = drawAndMeasureBend(id, currPos, bend.Width, { pos -> pos.y-- }, stepCount)
            }
        }
    }

    fun drawWire(id: Char, wire: WireLine) {
        val currPos = Coord(x0, y0)

        for (bend in wire.Line) {
            when (bend.Direction) {
                'R' -> drawBend(id, currPos, bend.Width, { pos -> pos.x++ })
                'D' -> drawBend(id, currPos, bend.Width, { pos -> pos.y++ })
                'L' -> drawBend(id, currPos, bend.Width, { pos -> pos.x-- })
                'U' -> drawBend(id, currPos, bend.Width, { pos -> pos.y-- })
            }
        }
    }

    private fun drawAndMeasureBend(id: Char, pos: Coord, width: Int, step: (Coord) -> Unit, stepCount: Int): Int {
        var count = stepCount

        for (i in IntRange(1, width)) {
            step(pos)
            markField(id, pos)
            if (board[pos.y][pos.x] == 'X') {
                measureBoard[pos.y][pos.x] += count
            } else if (measureBoard[pos.y][pos.x] == 0) {
                measureBoard[pos.y][pos.x] = count
            }
            ++count
        }

        return count
    }

    private fun drawBend(id: Char, pos: Coord, width: Int, step: (Coord) -> Unit) {
        for (i in IntRange(1, width)) {
            step(pos)
            markField(id, pos)
        }
    }

    private fun markField(id: Char, pos: Coord) {
        when (board[pos.y][pos.x]) {
            '.' -> board[pos.y][pos.x] = id
            id -> Unit
            else -> {
                board[pos.y][pos.x] = 'X'
                crossings.add(Coord(pos))
            }
        }
    }

    override fun toString(): String {
        return board.joinToString(separator = "\n", transform = { line -> line.joinToString("") })
    }
}