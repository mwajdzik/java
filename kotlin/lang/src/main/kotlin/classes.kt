// primary constructor

@Deprecated("Don't use anymore", ReplaceWith("String"))
class Person internal constructor(override var firstName: String, lastName: String) : IPerson {

    init {
        println("Creating a Person $firstName $lastName")
    }

    // secondary constructor (must call the primary one)
    constructor(firstName: String, lastName: String, middleName: String) : this(firstName, lastName) {
    }

    override fun getAge() = 23
}

object tempSingleton {
    var temps = arrayOf(32, 23, 10)

    fun getLastTemp() = temps.last()
}

interface IPerson {
    val firstName: String

    fun hello() = println("Hello from ${firstName}")

    fun getAge(): Int
}

fun <T : Comparable<T>> max(p1: T, p2: T): T {
    val results = p1.compareTo(p2)
    return if (results > 0) p1 else p2
}

// ---

fun main(args: Array<String>) {
    val p1 = Person("John", "Doe")
    val p2 = Person("Mary", "Doe", "Ann")

    println(p1)
    println(p2)

    p1.hello()

    val location = object {
        var xPos = 200
        var yPos = 400

        fun printIt() {
            println("Location is (${xPos}, ${yPos})")
        }
    }

    println("Location is (${location.xPos}, ${location.yPos})")

    location.xPos = 500
    location.printIt()

    println("The latest temp is ${tempSingleton.getLastTemp()}")

    println("Max of 1, 4 is ${max(1, 4)}")
    println("Max of 'abc', 'popi' is ${max("abc", "popi")}")
}