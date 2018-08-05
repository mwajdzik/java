interface Matcher<T> {
    fun test(lhs: T): Unit

    infix fun or(other: Matcher<T>): Matcher<T> = object : Matcher<T> {
        override fun test(lhs: T) {
            try {
                this@Matcher.test(lhs)
            } catch (e: RuntimeException) {
                other.test(lhs)
            }
        }
    }
}

infix fun <T> T.should(matcher: Matcher<T>) {
    matcher.test(this)
}

infix fun <T> Collection<T>.should(fn: CollectionMatchers<T>.() -> Unit) {
    val matchers = CollectionMatchers(this)
    matchers.fn()
}

class CollectionMatchers<T>(val collection: Collection<T>) {
    fun contain(rhs: T): Unit {
        if (!collection.contains(rhs)) {
            throw RuntimeException("Collection did not contain $rhs")
        }
    }

    fun notContain(rhs: T): Unit {
        if (collection.contains(rhs)) {
            throw RuntimeException("Collection should not contain $rhs")
        }
    }

    fun haveSizeLessThan(size: Int): Unit {
        if (collection.size >= size) {
            throw RuntimeException("Collection should have size less than $size")
        }
    }
}

fun unitTest() {
    val listOfNames = listOf("April", "May", "June")
    listOfNames should { notContain("Portia") }
}