fun main(args: Array<String>) {
    val cannotChange = "value1"
    var canChange = "value2"
    println("$canChange and $cannotChange")

    canChange = "value3"
    println("$canChange and $cannotChange")

    var aString: String

    aString = "$canChange "
    aString += "and $cannotChange"

    println(aString)

    val anInt: Int = 1
    val aLong: Long = anInt.toLong()

    println("My long value: $aLong")

    // ---

    val lowest = if (anInt < aLong) anInt else aLong.toInt()

    when (lowest) {
        Math.abs(0) -> println("Zero")
        Math.max(1, 0) -> println("One")
        in 2..5 -> println("Between two and five")
        else -> println("Else")
    }

    for (item in 1..9) {
        print("$item, ")
    }

    println()

    for ((index, item) in 10.rangeTo(20).step(2).withIndex()) {
        print("#${index + 1}: $item, ")
    }

    println()

    for (ch in "character") {
        print("$ch, ")
    }

    println()

    val ints = arrayOf(10, 20, 30)
    for (index in ints.indices) {
        print("#${index + 1}: ${ints[index]}, ")
    }

    // ---

    println(add1(23, 49))
    println(add2(23, 49))
    println(add3(23, 49))
    println(add4())
    println(add4(7))
    println(add4(i2 = 48))
}

fun add1(i1: Int, i2: Int): Int {
    return i1 + i2
}

fun add2(i1: Int, i2: Int): Int = i1 + i2

fun add3(i1: Int, i2: Int) = i1 + i2

fun add4(i1: Int = 23, i2: Int = 32) = i1 + i2
