package dukemarty.aoc2019.days.day3


class Layout(layoutDescription: Sequence<String>) {

    var lines: ArrayList<WireLine> = ArrayList()

    init {
        parseLines(layoutDescription)
    }

    private fun parseLines(layout: Sequence<String>) {
        for (wire in layout) {
            lines.add(WireLine(wire.split(",").map { Wire(it[0], it.substring(1).toInt()) }))
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()

        for (i in IntRange(0, lines.size - 1)) {
            sb.appendln("Line $i: ${lines[i]}")
        }

        return sb.toString()
    }
}