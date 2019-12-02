class IntcodeInterpreter(val program: IntcodeProgram) {

    val OPCODE_END = 99
    val OPCODE_ADD = 1
    val OPCODE_MUL = 2

    private var pc: Int = 0


    fun step(stepWidth: Int) {
        pc += stepWidth
    }

    fun runDay1Style() {

        var op = program.get(pc)
        while (op != OPCODE_END) {
//            println("$program")
//            println("pc=$pc -> opcode=$op")

            when (op) {
                OPCODE_ADD -> performAdd()
                OPCODE_MUL -> performMul()
            }

            pc += 4
            op = program.get(pc)
        }

//        println("$program")
    }

    private fun performAdd() {
        program.set(program.get(pc + 3), program.gget(pc + 1) + program.gget(pc + 2))
    }

    private fun performMul() {
        program.set(program.get(pc + 3), program.gget(pc + 1) * program.gget(pc + 2))
    }
}
