package dukemarty.aoc2019.common

class IntcodeProgram(prog: String) {

    private var opcodes: IntArray

    init {
        opcodes = prog.split(",").map { it.toInt() }.toIntArray()
    }

    fun get(index: Int): Int {
        if (index > opcodes.size) {
            val newOpcodes = IntArray(2 * index)
            opcodes.copyInto(newOpcodes)
            opcodes = newOpcodes
        }

        return opcodes[index]
    }

    fun read(index: Int, mode: Int): Int {
        return when (mode) {
            MODE_POSITION -> gget(index)
            MODE_IMMEDIATE -> get(index)
            else -> 0
        }
    }

    fun gget(index: Int): Int {
        val pos = get(index)

        return get(pos)
    }

    fun set(index: Int, value: Int) {
        if (index > opcodes.size) {
            val newOpcodes = IntArray(2 * index)
            opcodes.copyInto(newOpcodes)
            opcodes = newOpcodes
        }

        opcodes[index] = value
    }

    override fun toString(): String {
        return "Program: ${opcodes.joinToString()}"
    }
}