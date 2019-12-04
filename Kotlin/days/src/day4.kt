
const val puzzleInputDay4 = "273025-767253"

data class PasswordRange(val range: String) {

    val lowerBound: Int
    val upperBound: Int

    init {
        val parts = range.split("-")
        lowerBound = parts[0].toInt()
        upperBound = parts[1].toInt()
    }
}

fun main(args: Array<String>) {
    println("--- Day 3: Crossed Wires ---");

    val range = PasswordRange(puzzleInputDay4)

    partOne(range)

    partTwo(range)
}

fun partOne(passwordRange: PasswordRange) {
    println("\n--- Part One ---");

    var count = 0
    for (i in IntRange(passwordRange.lowerBound, passwordRange.upperBound)) {
        if (checkPasswordValidityPart1(i.toString())) {
            ++count
        }
    }
    println("Found number of valid passwords: $count")
}

fun partTwo(passwordRange: PasswordRange) {
    println("\n--- Part Two ---");

    var count = 0
    for (i in IntRange(passwordRange.lowerBound, passwordRange.upperBound)) {
        if (checkPasswordValidityPart2(i.toString())) {
            ++count
        }
    }
    println("Found number of valid passwords: $count")
}

private fun checkPasswordValidityPart1(candidate: String): Boolean {

    var doubleFound = false
    for (i in IntRange(1, 5)) {
        if (candidate[i].toInt() < candidate[i - 1].toInt()) {
            return false
        }
        if (candidate[i] == candidate[i - 1]) {
            doubleFound = true
        }
    }

    return doubleFound
}

private fun checkPasswordValidityPart2(candidate: String): Boolean {

    var doubleFound = false
    var matchCount = 1
    for (i in IntRange(1, 5)) {
        if (candidate[i].toInt() < candidate[i - 1].toInt()) {
            return false
        }
        if (candidate[i] == candidate[i - 1]) {
            ++matchCount
        } else {
            if (matchCount == 2){
                doubleFound = true
            }

            matchCount = 1
        }
    }

    if (matchCount == 2){
        doubleFound = true
    }

    return doubleFound
}

