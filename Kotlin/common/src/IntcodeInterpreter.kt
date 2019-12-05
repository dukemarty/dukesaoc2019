package dukemarty.aoc2019.common

class IntcodeInterpreter(private val program: IntcodeProgram) {

    private val OPCODE_END = 99
    private val OPCODE_ADD = 1
    private val OPCODE_MUL = 2
    private val OPCODE_INP = 3
    private val OPCODE_OUT = 4

    val machineLanguage = hashMapOf<Int, IntcodeInstruction>(
            OPCODE_ADD to IntcodeInstruction(OPCODE_ADD, 3, { instr, prog -> performAdd(instr, prog) }),
            OPCODE_MUL to IntcodeInstruction(OPCODE_MUL, 3, { instr, prog -> performMul(instr, prog) }),
            OPCODE_INP to IntcodeInstruction(OPCODE_INP, 1, { instr, prog -> performInput(instr, prog) }),
            OPCODE_OUT to IntcodeInstruction(OPCODE_OUT, 0, { instr, prog -> performOutput(instr, prog) }),
            OPCODE_END to IntcodeInstruction(OPCODE_END, 0, { instr, prog -> true })
            )

    private var pc: Int = 0

    private var ip: Int = 0

    var inputBuffer: IntArray = IntArray(0);
    val outputBuffer: ArrayList<Int> = ArrayList<Int>()

    fun step(stepWidth: Int) {
        pc += stepWidth
    }

    fun runDay1Style() {

//        var op = program.get(pc)
        var stopRun = false
        while (!stopRun) {
            val instr = LiveInstruction(program.get(pc))

            when (instr.opcode) {
                OPCODE_ADD -> stopRun = performAdd(instr, program)
                OPCODE_MUL -> stopRun = performMul(instr, program)
                OPCODE_END -> stopRun = true
            }

            if (machineLanguage.containsKey(instr.opcode)){
                pc += 1 + machineLanguage[instr.opcode]!!.parameterCount
            }
        }
    }

    private fun performAdd(instruction: LiveInstruction, program: IntcodeProgram): Boolean {
//        program.set(program.get(pc + 3), program.gget(pc + 1) + program.gget(pc + 2))
        program.set(program.read(pc+3, instruction.paramModes[2]),
                program.read(pc+1, instruction.paramModes[0]) + program.read(pc+2, instruction.paramModes[1]))

        return false
    }

    private fun performMul(instruction: LiveInstruction, program: IntcodeProgram): Boolean {
//        program.set(program.get(pc + 3), program.gget(pc + 1) * program.gget(pc + 2))
        program.set(program.read(pc+3, instruction.paramModes[2]),
                program.read(pc+1, instruction.paramModes[0]) * program.read(pc+2, instruction.paramModes[1]))
        return false
    }

    private fun performInput(instruction: LiveInstruction, program: IntcodeProgram): Boolean {
        if (ip < inputBuffer.size) {
            program.set(program.get(pc + 1), inputBuffer[ip])
            ++ip
        }

        return false
    }

    private fun performOutput(instruction: LiveInstruction, program: IntcodeProgram): Boolean {
        outputBuffer.add(program.gget(pc+1))

        return false
    }
}
