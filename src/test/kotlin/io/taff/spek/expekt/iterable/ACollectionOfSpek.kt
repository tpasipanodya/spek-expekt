package io.taff.spek.expekt.iterable

import io.taff.spek.expekt.should
import io.taff.spek.expekt.shouldNot
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ACollectionOfSpek : Spek({

    val actual by memoized { listOf(1, 2) }
    val expected by memoized { listOf(1, 2) }

    describe("invoke") {
        it("correctly matches") {
            actual should containInOrder(*expected.toTypedArray())
            actual should containInAnyOrder(*expected.toTypedArray())
        }

        context("with entries out of order") {
            val actual by memoized { listOf(2, 1) }

            it("correctly matches") {
                actual should containInOrder(*expected.toTypedArray()).not()
                actual should containInAnyOrder(*expected.toTypedArray())
            }
        }

        context("with nested ordered matching") {
            val actual by memoized { listOf(1, listOf(2)) }
            val expected by memoized { listOf(1, containInAnyOrder(2)) }

            it("correctly matches") {
                actual should containInOrder(*expected.toTypedArray())
                actual should containInAnyOrder(*expected.toTypedArray())
            }

            context("with several levels of nesting") {
                val actual by memoized { listOf(1, listOf(2, listOf(3))) }
                val expected by memoized { listOf(1, containInAnyOrder(2, containInOrder(3))) }

                it("correctly matches") {
                    actual should containInOrder(*expected.toTypedArray())
                    actual should containInAnyOrder(*expected.toTypedArray())
                }
            }
        }

        context("with nested unordered matching") {
            val actual by memoized { listOf(listOf(2), 1) }
            val expected by memoized { listOf(1, containInAnyOrder(2)) }

            it("correctly matches") {
                actual should containInOrder(*expected.toTypedArray()).not()
                actual should containInAnyOrder(*expected.toTypedArray())
            }

            context("with several levels of nesting") {
                val actual by memoized { listOf(1, listOf(2, listOf(3))) }
                val expected by memoized { listOf(1, containInAnyOrder(containInOrder(3), 2)) }

                it("correctly matches") {
                    actual should containInOrder(*expected.toTypedArray())
                    actual should containInAnyOrder(*expected.toTypedArray())
                }

                context("when the ordering mismatches are nested") {
                    val actual by memoized { listOf(1, listOf(2, listOf(3))) }
                    val expected by memoized { listOf(1, containInOrder(containInAnyOrder(3), 2)) }

                    it("correctly mismatches") {
                        actual should containInOrder(*expected.toTypedArray()).not()
                        actual should containInAnyOrder(*expected.toTypedArray()).not()
                    }
                }

                context("when nested types don't match") {
                    val actual by memoized { listOf(1, listOf(2, 3)) }
                    val expected by memoized { listOf(1, containInOrder(containInAnyOrder(3), 2)) }

                    it("correctly matches") {
                        actual should containInOrder(*expected.toTypedArray()).not()
                        actual shouldNot containInAnyOrder(*expected.toTypedArray())
                    }
                }
            }
        }
    }
})
