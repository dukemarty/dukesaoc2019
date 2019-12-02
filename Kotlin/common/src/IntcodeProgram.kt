package dukemarty.aoc2019.common

class IntcodeProgram {

    var opcodes: IntArray

    constructor(prog: String) {
        opcodes = prog.split(",").map { it -> it.toInt() }.toIntArray()
    }

    fun get(index: Int): Int {
        return opcodes[index]
    }

    fun set(index: Int, value: Int) {
        opcodes[index]= value
    }
}