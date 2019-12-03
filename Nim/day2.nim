
import strutils
from sequtils import map

let puzzle_input = "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,9,19,1,19,5,23,2,23,13,27,1,10,27,31,2,31,6,35,1,5,35,39,1,39,10,43,2,9,43,47,1,47,5,51,2,51,9,55,1,13,55,59,1,13,59,63,1,6,63,67,2,13,67,71,1,10,71,75,2,13,75,79,1,5,79,83,2,83,9,87,2,87,13,91,1,91,5,95,2,9,95,99,1,99,5,103,1,2,103,107,1,10,107,0,99,2,14,0,0"



echo "--- Day 2: 1202 Program Alarm ---"

# var puzzle_input = readFile("day1-puzzleinput1.txt")

var program = puzzle_input.split(",").map(parseInt)
echo "Parsed input:\n", program

echo "\n--- Part One ---"

program[1] = 12
program[2] = 2

var ip = 0

while (program[ip] != 99):
    case program[ip]:
        of 1:
             program[program[ip+3]] = program[program[ip+1]] + program[program[ip+2]]
        of 2:
            program[program[ip+3]] = program[program[ip+1]] * program[program[ip+2]]
        else:
            echo "don't care"
    ip += 4
    

echo "Result in program[0]: ", program[0]



echo "\n--- Part Two ---"

block findNounAndVerb:

    for noun in 0..99:
        for verb in 0..99:
            program = puzzle_input.split(",").map(parseInt)
            program[1] = noun
            program[2] = verb
            ip = 0
            while (program[ip] != 99):
                case program[ip]:
                    of 1:
                         program[program[ip+3]] = program[program[ip+1]] + program[program[ip+2]]
                    of 2:
                        program[program[ip+3]] = program[program[ip+1]] * program[program[ip+2]]
                    else:
                        echo "don't care"
                ip += 4
            if program[0] == 19690720:
                echo "Found noun and verb: ", noun, " & ", verb
                echo "Result: ", 100*noun * verb
                break findNounAndVerb

echo "\nDay 2: Finished! :-)"

