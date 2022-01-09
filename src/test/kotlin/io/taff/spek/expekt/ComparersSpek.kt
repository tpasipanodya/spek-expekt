package io.taff.spek.expekt

import io.taff.spek.expekt.iterable.containInOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ComparersSpek : Spek({

    val ints by memoized { arrayOf(1) }
    val strings by memoized { arrayOf("foo") }
    val longs by memoized { arrayOf(1L) }

    describe("._compare") {
        context("ints") {
            ints should containInOrder(1)
        }

    }

})
