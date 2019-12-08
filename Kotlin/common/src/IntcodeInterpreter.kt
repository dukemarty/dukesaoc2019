package dukemarty.aoc2019.common

import kotlin.system.exitProcess

data class ProgramOutput(val pc: Int, val value: Int)

class IntcodeInterpreter(private val program: IntcodeProgram) {

    val MISSING_INPUT_IGNORE = 0
    val MISSING_INPUT_HALT = 1

    val STATE_INIT = 0
    val STATE_RUNNING = 1
    val STATE_HALTED = 2
    val STATE_STOPPED = 3

    private val OPCODE_END = 99
    private val OPCODE_ADD = 1
    private val OPCODE_MUL = 2
    private val OPCODE_INP = 3
    private val OPCODE_OUT = 4
    private val OPCODE_JNZ = 5
    private val OPCODE_JZR = 6
    private val OPCODE_LTH = 7
    private val OPCODE_EQL = 8

    val machineLanguage = hashMapOf<Int, IntcodeInstruction>(
            OPCODE_ADD to IntcodeInstruction(OPCODE_ADD, 3, { instr, prog -> performAdd(instr, prog) }),
            OPCODE_MUL to IntcodeInstruction(OPCODE_MUL, 3, { instr, prog -> performMul(instr, prog) }),
            OPCODE_INP to IntcodeInstruction(OPCODE_INP, 1, { instr, prog -> performInput(instr, prog) }),
            OPCODE_OUT to IntcodeInstruction(OPCODE_OUT, 1, { instr, prog -> performOutput(instr, prog) }),
            OPCODE_JNZ to IntcodeInstruction(OPCODE_JNZ, 2, { instr, prog -> performJumpIfTrue(instr, prog) }),
            OPCODE_JZR to IntcodeInstruction(OPCODE_JZR, 2, { instr, prog -> performJumpIfFalse(instr, prog) }),
            OPCODE_LTH to IntcodeInstruction(OPCODE_LTH, 3, { instr, prog -> performLessThan(instr, prog) }),
            OPCODE_EQL to IntcodeInstruction(OPCODE_EQL, 3, { instr, prog -> performEquals(instr, prog) }),
            OPCODE_END to IntcodeInstruction(OPCODE_END, 0, { instr, prog -> performEnd(instr, prog) })
    )


    private var pc: Int = 0

    private var ip: Int = 0

    var inputBuffer = ArrayList<Int>();
    val outputBuffer = ArrayList<ProgramOutput>()
    var outputForward = ArrayList<IntcodeInterpreter>()

    var state = STATE_INIT

    var missingInputHandling = MISSING_INPUT_IGNORE

    fun step(stepWidth: Int) {
        pc += stepWidth
    }

    fun addOutputInterpreter(target: IntcodeInterpreter) {
        outputForward.add(target)
    }

    fun appendInput(newInput: Int) {
        inputBuffer.add(newInput)
    }

    fun runProgram() {

//        var op = program.get(pc)

        state = STATE_RUNNING

        var stopRun = false
        var execCount = 0
        while (!stopRun) {
            val instr = LiveInstruction(program.get(pc))

            if (machineLanguage.containsKey(instr.opcode)) {
                val result = machineLanguage[instr.opcode]!!.executor(instr, program)

                stopRun = result.stopRun
//            when (instr.opcode) {
//                OPCODE_ADD -> stopRun = performAdd(instr, program)
//                OPCODE_MUL -> stopRun = performMul(instr, program)
//                OPCODE_END -> stopRun = true
//            }

                if (result.updatePc) {
                    pc += 1 + machineLanguage[instr.opcode]!!.parameterCount
                }
            } else {
                println("ERROR: Encountered unknown opcode in step $execCount: ${instr.opcode}  at index $pc.")
                exitProcess(1)
            }

            ++execCount
        }
    }

    private fun performAdd(instruction: LiveInstruction, program: IntcodeProgram): InstructionResult {
//        program.set(program.get(pc + 3), program.gget(pc + 1) + program.gget(pc + 2))
        val dest = program.read(pc + 3, MODE_IMMEDIATE)
//        val dest = program.read(pc + 3, instruction.paramModes[2])
        val src1 = program.read(pc + 1, instruction.paramModes[0])
        val src2 = program.read(pc + 2, instruction.paramModes[1])
        program.set(dest, src1 + src2)

        return InstructionResult(false, true)
    }

    private fun performMul(instruction: LiveInstruction, program: IntcodeProgram): InstructionResult {
//        program.set(program.get(pc + 3), program.gget(pc + 1) * program.gget(pc + 2))
        val dest = program.read(pc + 3, MODE_IMMEDIATE)
//        val dest = program.read(pc + 3, instruction.paramModes[2])
        val src1 = program.read(pc + 1, instruction.paramModes[0])
        val src2 = program.read(pc + 2, instruction.paramModes[1])
        program.set(dest, src1 * src2)

        return InstructionResult(false, true)
    }

    private fun performInput(instruction: LiveInstruction, program: IntcodeProgram): InstructionResult {
        if (ip < inputBuffer.size) {
//            val dest = program.read(pc + 1, instruction.paramModes[0])
            val dest = program.read(pc + 1, MODE_IMMEDIATE)
            program.set(dest, inputBuffer[ip])
            ++ip

            return InstructionResult(false, true)
        } else {
            state = STATE_HALTED
            return InstructionResult(true, false)
        }
    }

    private fun performOutput(instruction: LiveInstruction, program: IntcodeProgram): InstructionResult {
        val src = program.read(pc + 1, instruction.paramModes[0])
//        val src = program.get(pc + 1)
        outputBuffer.add(ProgramOutput(pc, src))

        for (outTarget in outputForward) {
            outTarget.appendInput(src)
        }

        return InstructionResult(false, true)
    }

    private fun performJumpIfTrue(instruction: LiveInstruction, program: IntcodeProgram): InstructionResult {
        val compValue = program.read(pc + 1, instruction.paramModes[0])

        var performedJump = false
        if (compValue != 0) {
            pc = program.read(pc + 2, instruction.paramModes[1])
            performedJump = true
        }

        return InstructionResult(false, !performedJump)
    }

    private fun performJumpIfFalse(instruction: LiveInstruction, program: IntcodeProgram): InstructionResult {
        val compValue = program.read(pc + 1, instruction.paramModes[0])

        var performedJump = false
        if (compValue == 0) {
            pc = program.read(pc + 2, instruction.paramModes[1])
            performedJump = true
        }

        return InstructionResult(false, !performedJump)
    }

    private fun performLessThan(instruction: LiveInstruction, program: IntcodeProgram): InstructionResult {
        val lValue = program.read(pc + 1, instruction.paramModes[0])
        val rValue = program.read(pc + 2, instruction.paramModes[1])
        val dest = program.read(pc + 3, MODE_IMMEDIATE)

        program.set(dest, if (lValue < rValue) {
            1
        } else {
            0
        })

        return InstructionResult(false, true)
    }

    private fun performEquals(instruction: LiveInstruction, program: IntcodeProgram): InstructionResult {
        val lValue = program.read(pc + 1, instruction.paramModes[0])
        val rValue = program.read(pc + 2, instruction.paramModes[1])
        val dest = program.read(pc + 3, MODE_IMMEDIATE)

        program.set(dest, if (lValue == rValue) {
            1
        } else {
            0
        })

        return InstructionResult(false, true)
    }

    private fun performEnd(instruction: LiveInstruction, program: IntcodeProgram): InstructionResult {
        state = STATE_STOPPED

        return InstructionResult(true, false)
    }
}
