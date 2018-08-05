/*
    Pros:
        - no messy state issues
        - easier code reuse
        - easier testing
        - safe multithreading

    Cons:
        - less efficient code (recursion, no loops,  ...)
        - harder to conceptualize
 */

data class Student(val name: String, var age: Int) {}

fun getStudents(): List<Student> {
    return listOf(
            Student("Ginger", 19),
            Student("Michael", 23),
            Student("Maria", 20),
            Student("Joe", 39),
            Student("Bob", 16)
    )
}

// Unit == void
// num is kept in the closure
fun closureMaker(): () -> Unit {
    var num = 0
    return { println(num++) }
}

fun main(args: Array<String>) {
    // lambdas
    println("All: " + getStudents().map { s -> "${s.name} : ${s.age}" })
    println("Oldest: " + getStudents().maxBy { it.age })
    println("Long names: " + getStudents().filter { it.name.length > 5 })

    // closures
    val maker1 = closureMaker()
    val maker2 = closureMaker()
    maker1()
    maker1()
    maker1()
    maker2()
    maker2()
    maker1()

    // sequences
    println(getStudents().drop(1).take(3).toList())

    val numbers = generateSequence(100) { it + 1 }
    println(numbers.drop(5).filter { it % 2 == 0 }.take(20).toList())

    val fib = generateSequence(1 to 1) { it.second to it.first + it.second }.map { it.first }
    println(fib.take(10).toList())

}