package com.taff.hephaestustest.matchers

import com.natpryce.hamkrest.MatchResult
import com.taff.hephaestustest.expectations.expect
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ACollectionWithSpek : Spek({

    val actual by memoized { listOf(1, 2) }
    val expected by memoized { listOf(1, 2) }
    val strictOrderingMatcher by memoized { anOrderedCollectionWith(*expected.toTypedArray()) }
    val looseOrderingMatcher by memoized { anUnorderedCollectionWith(*expected.toTypedArray()) }

    describe("invoke") {
        it("correctly matches") {
           expect(strictOrderingMatcher.invoke(actual)) {
               toSatisfy { this == MatchResult.Match }
           }
            expect(looseOrderingMatcher.invoke(actual)) {
                toSatisfy { this == MatchResult.Match }
            }
        }

        context("with entries out of order") {
            val actual by memoized { listOf(2, 1) }
            val strictOrderingMatcher by memoized { anOrderedCollectionWith(*expected.toTypedArray()) }
            val looseOrderingMatcher by memoized { anUnorderedCollectionWith(*expected.toTypedArray()) }

            it("correctly matches") {
                expect(strictOrderingMatcher.invoke(actual)) {
                    toSatisfy { this != MatchResult.Match && this is MatchResult.Mismatch }
                }
                expect(looseOrderingMatcher.invoke(actual)) {
                    toSatisfy { this == MatchResult.Match }
                }
            }
        }

        context("with nested ordered matching") {
            val actual by memoized { listOf(1, listOf(2)) }
            val expected by memoized { listOf(1, anUnorderedCollectionWith(2)) }
            val strictOrderingMatcher by memoized { anOrderedCollectionWith(*expected.toTypedArray()) }
            val looseOrderingMatcher by memoized { anUnorderedCollectionWith(*expected.toTypedArray()) }

            it("correctly matches") {
                expect(strictOrderingMatcher.invoke(actual)) {
                    toSatisfy { this == MatchResult.Match }
                }
                expect(looseOrderingMatcher.invoke(actual)) {
                    toSatisfy { this == MatchResult.Match }
                }
            }

            context("with several levels of nesting") {
                val actual by memoized { listOf(1, listOf(2, listOf(3))) }
                val expected by memoized { listOf(1, anUnorderedCollectionWith(2, anOrderedCollectionWith(3))) }
                val strictOrderingMatcher by memoized { anOrderedCollectionWith(*expected.toTypedArray()) }
                val looseOrderingMatcher by memoized { anUnorderedCollectionWith(*expected.toTypedArray()) }

                it("correctly matches") {
                    expect(strictOrderingMatcher.invoke(actual)) {
                        toSatisfy { this == MatchResult.Match }
                    }
                    expect(looseOrderingMatcher.invoke(actual)) {
                        toSatisfy { this == MatchResult.Match }
                    }
                }
            }
        }

        context("with nested unordered matching") {
            val actual by memoized { listOf(listOf(2), 1) }
            val expected by memoized { listOf(1, anUnorderedCollectionWith(2)) }
            val strictOrderingMatcher by memoized { anOrderedCollectionWith(*expected.toTypedArray()) }
            val looseOrderingMatcher by memoized { anUnorderedCollectionWith(*expected.toTypedArray()) }

            it("correctly matches") {
                expect(strictOrderingMatcher.invoke(actual)) {
                    toSatisfy { this != MatchResult.Match }
                }
                expect(looseOrderingMatcher.invoke(actual)) {
                    toSatisfy { this == MatchResult.Match }
                }
            }

            context("with several levels of nesting") {
                val actual by memoized { listOf(1, listOf(2, listOf(3))) }
                val expected by memoized { listOf(1, anUnorderedCollectionWith(anOrderedCollectionWith(3), 2)) }
                val strictOrderingMatcher by memoized { anOrderedCollectionWith(*expected.toTypedArray()) }
                val looseOrderingMatcher by memoized { anUnorderedCollectionWith(*expected.toTypedArray()) }

                it("correctly matches") {
                    expect(strictOrderingMatcher.invoke(actual)) {
                        toSatisfy { this == MatchResult.Match }
                    }
                    expect(looseOrderingMatcher.invoke(actual)) {
                        toSatisfy { this == MatchResult.Match }
                    }
                }

                context("when the ordering mismatches are nested") {
                    val actual by memoized { listOf(1, listOf(2, listOf(3))) }
                    val expected by memoized { listOf(1, anOrderedCollectionWith(anUnorderedCollectionWith(3), 2)) }
                    val strictOrderingMatcher by memoized { anOrderedCollectionWith(*expected.toTypedArray()) }
                    val looseOrderingMatcher by memoized { anUnorderedCollectionWith(*expected.toTypedArray()) }

                    it("correctly matches") {
                        expect(strictOrderingMatcher.invoke(actual)) {
                            toSatisfy { this != MatchResult.Match && this is MatchResult.Mismatch }
                        }
                        expect(looseOrderingMatcher.invoke(actual)) {
                            toSatisfy { this != MatchResult.Match && this is MatchResult.Mismatch }
                        }
                    }
                }

                context("when nested types don't match") {
                    val actual by memoized { listOf(1, listOf(2, 3)) }
                    val expected by memoized { listOf(1, anOrderedCollectionWith(anUnorderedCollectionWith(3), 2)) }
                    val strictOrderingMatcher by memoized { anOrderedCollectionWith(*expected.toTypedArray()) }
                    val looseOrderingMatcher by memoized { anUnorderedCollectionWith(*expected.toTypedArray()) }

                    it("correctly matches") {
                        expect(strictOrderingMatcher.invoke(actual)) {
                            toSatisfy { this != MatchResult.Match }
                        }
                        expect(looseOrderingMatcher.invoke(actual)) {
                            toSatisfy { this != MatchResult.Match }
                        }
                    }
                }
            }
        }
    }
})