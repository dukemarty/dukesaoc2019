package dukemarty.aoc2019.common

data class IntcodeInstruction(val opcode: Int, val parameterCount: Int, val executor: (LiveInstruction, IntcodeProgram) -> Boolean)

public const val MODE_POSITION = 0
public const val MODE_IMMEDIATE = 1

class ParameterModeList(instruction: Int) {

    val modes = ArrayList<Int>()

    init {
        var value = instruction / 100
        while (value > 0) {
            modes.add(value % 10)
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

data class LiveInstruction(val instruction: Int) {

    val opcode: Int = instruction % 100
    var paramModes = ParameterModeList(instruction)
}