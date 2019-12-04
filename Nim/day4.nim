
import strutils
from sequtils import map

let puzzle_input = "273025-767253"



proc check_validity_1(cand: string): bool =
    var foundDouble = false
    for i in 1..5:
        if parseInt(cand.substr(i, i)) < parseInt(cand.substr(i-1, i-1)):
            return false
        if cand[i] == cand[i-1]:
            foundDouble = true

    return foundDouble


proc check_validity_2(cand: string): bool =
    var foundDouble = false
    var seqCount = 1
    for i in 1..5:
        if parseInt(cand.substr(i, i)) < parseInt(cand.substr(i-1, i-1)):
            return false
        if cand[i] != cand[i-1]:
            if seqCount == 2:
                foundDouble = true
            seqCount = 1
        else:
            seqCount += 1

    if seqCount == 2:
        foundDouble = true

    return foundDouble


echo "--- Day 4: Secure Container ---"

# var puzzle_input = readFile("day1-puzzleinput1.txt")

let passwordRange = puzzle_input.split("-").map(parseInt)

var count1 = 0
var count2 = 0

for i in passwordRange[0]..passwordRange[1]:
    if check_validity_1(i.intToStr):
        count1 += 1
    if check_validity_2(i.intToStr):
        count2 += 1


echo "\n--- Part One ---"

echo "Result of valid passwords in range variant 1: ", count1


echo "\n--- Part Two ---"

echo "Result of valid passwords in range variant 2: ", count2


