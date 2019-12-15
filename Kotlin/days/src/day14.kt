import java.io.BufferedReader
import java.io.FileReader
import java.lang.Integer.min


class ChemicalStore {

    private val store = mutableListOf<Chemical>()

    fun contains(name: String): Boolean {
        val stored = store.firstOrNull { it.name == name }
        return stored != null
    }

    fun getStoreSize(): Int {
        return store.size
    }

    fun getStoredAmount(name: String): Int {
        val stored = store.first { it.name == name }
        return stored.amount
    }

    fun storeChemical(name: String, amount: Int) {
        val stored = store.firstOrNull { it.name == name }

        if (stored == null) {
            store.add(Chemical(name, amount))
        } else {
            stored.amount += amount
        }
    }

    fun retrieveChemical(name: String, maxAmount: Int): Int {
        val stored = store.first { it.name == name }

        val resAmount = min(stored.amount, maxAmount)

        if (stored.amount < resAmount) {
            throw IllegalArgumentException("amount too large")
        } else if (stored.amount == resAmount) {
            store.remove(stored)
        } else {
            stored.amount -= resAmount
        }

        return resAmount
    }

    override fun toString(): String {
        return "STORE: [${store.joinToString(separator = ",") { it.toString() }}]"
    }
}

data class Chemical(val description: String) {

    val name: String
    var amount: Int

    init {
        val tokens = description.trim().split(" ")
        name = tokens[1]
        amount = tokens[0].toInt()
    }

    constructor(name: String, amount: Int) : this("$amount $name")

    override fun toString(): String {
        return "Chemical($name, $amount)"
    }
}

data class Reaction(val line: String) {

    val inputs: Collection<Chemical>
    val output: Chemical

    init {
        val parts = line.split("=>")
        output = Chemical(parts[1])
        inputs = parts[0].split(",").map { Chemical(it) }
    }

    override fun toString(): String {
        return "Reaction([${inputs.joinToString(separator = ",") { "$it" }}] -> $output)"
    }
}


fun main(args: Array<String>) {
    println("--- Day 14: Space Stoichiometry ---");

    val br = BufferedReader(FileReader("puzzle_input/day14-input1.txt"))
    val reactions = br.readLines().map { Reaction(it) }
//    println("Parsed reactions:\n${reactions.joinToString(separator = "\n") { "$it" }}")

    day14PartOne(reactions)

    day14PartTwo(reactions)
}


fun day14PartOne(reactions: List<Reaction>) {
    println("\n--- Part One ---")

    val required = mutableListOf(Chemical("FUEL", 1))
    val store = ChemicalStore()
    var oreCount = 0

    while (required.any()) {
//        println("#$oreCount / STORE: $store / REQ: $required")
        val nextChem = required[0]
        required.removeAt(0)

        if (store.contains(nextChem.name)) {
            val retrieved = store.retrieveChemical(nextChem.name, nextChem.amount)
            nextChem.amount -= retrieved
            if (nextChem.amount == 0) {
                continue
            }
        }

        val nextReaction = reactions.first { it.output.name == nextChem.name }
        val times = calculateRequiredMultiplicity(nextChem, nextReaction)

        for (i in nextReaction.inputs) {
            if (i.name == "ORE") {
                oreCount += times * i.amount
            } else {
                if (i.amount > 0) {
                    required.add(Chemical(i.name, i.amount * times))
                }
            }
        }

        if (nextReaction.output.amount * times > nextChem.amount) {
            val remainder = nextReaction.output.amount * times - nextChem.amount
            store.storeChemical(nextChem.name, remainder)
        }
    }

    println("Required ORE: $oreCount")
    println("  with remaining store: $store")
}

private fun calculateRequiredMultiplicity(nextChem: Chemical, nextReaction: Reaction): Int {
    var times = 0
    if (nextChem.amount < nextReaction.output.amount) {
        times = 1
    } else if (nextChem.amount == nextReaction.output.amount) {
        times = 1
    } else if (nextChem.amount % nextReaction.output.amount == 0) {
        times = nextChem.amount / nextReaction.output.amount
    } else {
        times = nextChem.amount / nextReaction.output.amount + 1
    }
    return times
}


fun day14PartTwo(reactions: List<Reaction>) {
    println("\n--- Part Two ---")

    val availableOre: Long = 1000000000000

    var producedFuel: Long = 0
    val store = ChemicalStore()
    var oreCount: Long = 0

    while (oreCount < availableOre) {
        val required = mutableListOf(Chemical("FUEL", 1))
        while (required.any()) {
//        println("#$oreCount / STORE: $store / REQ: $required")
            val nextChem = required[0]
            required.removeAt(0)

            if (store.contains(nextChem.name)) {
                val retrieved = store.retrieveChemical(nextChem.name, nextChem.amount)
                nextChem.amount -= retrieved
                if (nextChem.amount == 0) {
                    continue
                }
            }

            val nextReaction = reactions.first { it.output.name == nextChem.name }
            val times = calculateRequiredMultiplicity(nextChem, nextReaction)

            for (i in nextReaction.inputs) {
                if (i.name == "ORE") {
                    oreCount += times * i.amount
                } else {
                    if (i.amount > 0) {
                        required.add(Chemical(i.name, i.amount * times))
                    }
                }
            }

            if (nextReaction.output.amount * times > nextChem.amount) {
                val remainder = nextReaction.output.amount * times - nextChem.amount
                store.storeChemical(nextChem.name, remainder)
            }
        }

        if (oreCount <= availableOre) {
            producedFuel++
        }

        if (store.getStoreSize() == 0){
            println("Reached empty store again with $producedFuel FUEL for $oreCount ORE")

            val remainingOre = availableOre - oreCount
            if (remainingOre > oreCount){
                val mult = remainingOre / oreCount
                oreCount += mult * oreCount
                producedFuel += mult * producedFuel

                println("Used to speed-up calculation with mult=$mult: $producedFuel FUEL for $oreCount ORE")
            }
        } else {
            println("Store size: ${store.getStoreSize()}")
        }
    }

    println("Produced fuel: $producedFuel")
    println("  with remaining store: $store")
}
