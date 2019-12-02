
package dukemarty.aoc2019.common

class IntcodeInterpreter(private val program: IntcodeProgram) {

    private val OPCODE_END = 99
    private val OPCODE_ADD = 1
    private val OPCODE_MUL = 2

    private var pc: Int = 0


    fun step(stepWidth: Int) {
        pc += stepWidth
    }

    fun runDay1Style() {

        var op = program.get(pc)
        while (op != OPCODE_END) {

            when (op) {
                OPCODE_ADD -> performAdd()
                OPCODE_MUL -> performMul()
            }

            pc += 4
            op = program.get(pc)
        }
    }

    private fun performAdd() {
        program.set(program.get(pc + 3), program.gget(pc + 1) + program.gget(pc + 2))
    }

    private fun performMul() {
        program.set(program.get(pc + 3), program.gget(pc + 1) * program.gget(pc + 2))
    }
}
