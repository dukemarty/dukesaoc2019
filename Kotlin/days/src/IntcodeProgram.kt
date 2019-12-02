class IntcodeProgram {

    var opcodes: IntArray

    constructor(prog: String) {
        opcodes = prog.split(",").map { it -> it.toInt() }.toIntArray()
    }

    fun get(index: Int): Int {
        if (index > opcodes.size) {
            var newOpcodes = IntArray(2 * index)
            opcodes.copyInto(newOpcodes)
            opcodes = newOpcodes
        }

        return opcodes[index]
    }

    fun gget(index: Int): Int {
        val pos = get(index)

        return get(pos)
    }

    fun set(index: Int, value: Int) {
        if (index > opcodes.size) {
            var newOpcodes = IntArray(2 * index)
            opcodes.copyInto(newOpcodes)
            opcodes = newOpcodes
        }

        opcodes[index] = value
    }

    override fun toString(): String {
        return "Program: ${opcodes.joinToString()}"
    }
}