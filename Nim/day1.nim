
import strutils
from sequtils import map

let puzzle_input = """123835
66973
63652
99256
56009
58012
130669
109933
52958
131656
144786
50437
134194
80230
50326
118204
102780
135520
142248
80341
51071
71346
134081
142321
136230
55934
79697
90116
107825
133052
130259
99566
83066
90923
58475
134697
91830
105838
109003
125258
108679
87310
79813
109814
65616
69275
118405
105178
93140
79535
138051
55728
71875
121207
52011
81209
129059
135782
62791
72135
77765
109498
73862
134825
148898
81633
53277
109858
91672
115105
132871
138334
135049
73083
79234
129281
86062
88448
99612
52138
149290
120562
118975
92896
51162
122410
75479
137800
142149
123518
67806
89937
85963
104764
56710
51314
67275
61135
77580
74726"""


proc calc_fuel(weight: int): int =
    var base = weight
    while base > 0:
        var add = (base div 3) - 2
        if add < 0:
            add = 0
        result += add
        base = add



echo "--- Day 1: The Tyranny of the Rocket Equation ---"

# var puzzle_input = readFile("day1-puzzleinput1.txt")

let rocket_modules = puzzle_input.split("\n").map(parseInt)

echo "\n--- Part One ---"

var res1 = 0
for m in rocket_modules:
    res1 += (m div 3) - 2

echo "Required fuel (simply calculated): ", res1


echo "\n--- Part Two ---"

var res2 = 0
for m in rocket_modules:
    res2 += calc_fuel(m)

echo "Required fuel (complex calculation): ", res2


     

