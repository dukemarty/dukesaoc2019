package dukemarty.aoc2019.common

data class InstructionResult(val stopRun: Boolean, val updatePc: Boolean)

data class IntcodeInstruction(val opcode: Int, val parameterCount: Int, val executor: (LiveInstruction, IntcodeProgram) -> InstructionResult)

public const val MODE_POSITION = 0
public const val MODE_IMMEDIATE = 1
public const val MODE_RELATIVE = 2

class ParameterModeList(instruction: Long) {

    val modes = ArrayList<Int>()

    init {
        var value = instruction / 100
        while (value > 0) {
            modes.add((value % 10).toInt())
            value /= 10
        }
    }

    operator fun get(i: Int): Int {
        return if (i < modes.size) {
            modes[i]
        } else {
            0
        }
    }
}

data class LiveInstruction(val instruction: Long) {

    val opcode: Int = (instruction % 100).toInt()
    var paramModes = ParameterModeList(instruction)
}