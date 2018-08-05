// binary: +, -, *, / * %, .. (range)
// unary:  ++, --, +, -, !
// +=, -=, *=, /=, %=

fun main(args: Array<String>) {
    val p1 = Position(23, 98)
    val p2 = p1.copy()                      // data class
    p2.y = 77

    println("p1: " + p1.toString())         // data class
    println("p2: (${p2.x}, ${p2.y})")

    println(p1 + p2)


    // ---

    val (x, y) = p1
    println("destructuring $p1, got $x and $y")
}

// https://kotlinlang.org/docs/reference/data-classes.html

data class Position(var x: Int, var y: Int) {

    operator fun plus(other: Position): Position {
        return Position(x + other.x, y + other.y)
    }
}