package dukemarty.aoc2019.common

class IntcodeProgram(prog: String) {

    private val rawProgram: String = prog
    private var opcodes: LongArray

    init {
        val inputOpcodes = prog.split(",").map { it.toLong() }
        opcodes = inputOpcodes.toLongArray().copyOf(inputOpcodes.size + 10000)
    }

    fun reset(){
        val inputOpcodes = rawProgram.split(",").map { it.toLong() }
        opcodes = inputOpcodes.toLongArray().copyOf(inputOpcodes.size + 10000)
    }

    fun get(index: Int): Long {
        if (index > opcodes.size) {
            val newOpcodes = LongArray(2 * index)
            opcodes.copyInto(newOpcodes)
            opcodes = newOpcodes
        }

        return opcodes[index]
    }

    fun read(index: Int, mode: Int, relativeBase: Int): Long {
        return when (mode) {
            MODE_POSITION -> get(get(index).toInt())
            MODE_IMMEDIATE -> get(index)
            MODE_RELATIVE -> get(get(index).toInt() + relativeBase)
            else -> 0
        }
    }

    fun readAddress(index: Int, mode: Int, relativeBase: Int): Long {
        return when (mode) {
            MODE_POSITION -> get(index)
            MODE_IMMEDIATE -> {
                println("ERROR: Requested address in IMMEDIATE mode.")
                get(index)
//                index.toLong()
            }
            MODE_RELATIVE -> get(index) + relativeBase
            else -> 0
        }
    }

    fun set(index: Int, value: Long) {
        if (index > opcodes.size) {
            val newOpcodes = LongArray(2 * index)
            opcodes.copyInto(newOpcodes)
            opcodes = newOpcodes
        }

        opcodes[index] = value
    }

    override fun toString(): String {
        return "Program: ${opcodes.joinToString()}"
    }
}