package io.taff.hephaestustest.expectation.any

import io.taff.hephaestustest.expectation.should
import io.taff.hephaestustest.expectation.shouldNot
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SatisfiesSpek : Spek({
    describe(".satsifies") {
        it("correctly asserts") {
            true should satisfy { this }
            false shouldNot satisfy { this }
        }
    }

    describe(".equals") {
        it("correctly asserts") {
            1 should equal(1)
            1 shouldNot equal(2)
        }
    }
})