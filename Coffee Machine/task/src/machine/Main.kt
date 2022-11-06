package machine


fun main() {



    val machine = CoffeeMachine()

    while (true) {

        println("Write action (buy, fill, take, remaining, exit):")

        when (val action = readln()) {
            "buy" -> {
                println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:")
                val ans = readln()
                if (ans != "back") machine.getInput(ans)
            }
            "exit" -> break
            else -> machine.getInput(action)
        }
    }
    return
}

class CoffeeMachine {

    private val recipes = listOf(
        mapOf("id" to 1, "water" to 250, "milk" to 0, "beans" to 16, "cups" to 1, "money" to -4), //espresso
        mapOf("id" to 2, "water" to 350, "milk" to 75, "beans" to 20, "cups" to 1, "money" to -7),  //latte
        mapOf("id" to 3, "water" to 200, "milk" to 100, "beans" to 12, "cups" to 1, "money" to -6) //cappuccino
    )

    private var capability = mutableMapOf(
        "water" to 400, "milk" to 540, "beans" to 120, "cups" to 9, "money" to 550)

    fun getInput(input: String) {
        when (input){
            "remaining" -> {
                outputCapability()
            }
            "fill" -> fill()
            "take" -> take()
            else -> buy(input.toInt())
        }
    }

    private fun outputCapability() {
        val initData = """
            The coffee machine has:
            ${capability["water"]} ml of water
            ${capability["milk"]} ml of milk
            ${capability["beans"]} g of coffee beans
            ${capability["cups"]} disposable cups
            ${'$'}${capability["money"]} of money
        """.trimIndent()
        println(initData)
    }

    fun calcMaxCups(recipe: Map<String, Int>): Int {
        return recipe.filter { it.key != "id" || it.value != 0 }.map{
            capability.getValue(it.key) / it.value
        }.minOrNull() ?: 0
    }

    fun checkCapable(cups: Int) {

/*        val nCups = maxCups - cups

        if (nCups == 0) {
            println("Yes, I can make that amount of coffee")
        } else if (nCups < 0) {
            println("No, I can make only $maxCups cups of coffee")
        } else {
            println("Yes, I can make that amount of coffee (and even $nCups more than that)")
        }*/
    }

    private fun buy(coffee: Int) {

        val newCapability = capability.mapValues {
            it.value - (recipes.find { line -> line.getValue("id") == coffee }?.getValue(it.key) ?: 0)
        }.toMutableMap()

        if (newCapability.filter { it.key != "id" || it.key != "money" }.all { it.value > 0 }) {
            capability = newCapability
            println("I have enough resources, making you a coffee!")
        } else {
            println("Sorry, not enough " +
                    "${newCapability
                        .filter { it.key != "id" || it.key != "money" || it.value < 0 }.keys
                        .joinToString(", ")}!"
            )
        }
    }

    private fun fill() {
        println("Write how many ml of water you want to add:")
        capability["water"] = capability["water"]!! + readln().toInt()

        println("Write how many ml of milk you want to add:")
        capability["milk"] = capability["milk"]!! + readln().toInt()

        println("Write how many grams of coffee beans you want to add:")
        capability["beans"] = capability["beans"]!! + readln().toInt()

        println("Write how many disposable cups you want to add:")
        capability["cups"] = capability["cups"]!! + readln().toInt()
    }

    private fun take() {
        println("I gave you ${"$"}${capability["money"]}")
        capability["money"] = 0
    }

}
