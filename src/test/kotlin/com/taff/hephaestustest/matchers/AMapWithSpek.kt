package com.taff.hephaestustest.matchers

import com.natpryce.hamkrest.MatchResult
import com.taff.hephaestustest.expectations.expect
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.math.BigDecimal
import java.math.BigInteger
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object AMapWithSpek : Spek({

    describe(".invoke") {
        fun correctlyMatchesTheExpectedValue(scenario: String, expectedValue: Any?, actualValue: Any?) =
            context("$scenario") {
                val actual by memoized { mapOf(1 to actualValue) }
                val expected by memoized { arrayOf(1 to expectedValue) }
                val matcher by memoized { aMapWith(*expected) }

                it("correctly matches") {
                    expect(matcher.invoke(actual)) {
                        toSatisfy { this == MatchResult.Match }
                    }
                }
            }

        fun correctlyMismatchesTheExpectedValue(scenario: String, expectedValue: Any?, actualValue: Any?) =
            context("$scenario") {
                val actual by memoized { mapOf(1 to actualValue) }
                val expected by memoized { arrayOf(1 to expectedValue) }
                val matcher by memoized { aMapWith(*expected) }

                it("correctly matches") {
                    expect(matcher.invoke(actual)) {
                        toSatisfy { this is MatchResult.Mismatch }
                    }
                }
            }

        context("with numbers") {
            context("when they match") {
                context("flat comparisons") {
                    fun <T> correctlyMatchesNumber(expectedTypeName: String, expectedValue:T) =
                        context("expecting $expectedTypeName") {
                            correctlyMatchesTheExpectedValue(
                                scenario = "and actuals are longs",
                                expectedValue = expectedValue,
                                actualValue = 2L
                            )
                            correctlyMatchesTheExpectedValue(
                                scenario = "and actuals are floats",
                                expectedValue = expectedValue,
                                actualValue = 2.0f
                            )
                            correctlyMatchesTheExpectedValue(
                                scenario = "and actuals are doubles",
                                expectedValue = expectedValue,
                                actualValue = 2.0
                            )
                            correctlyMatchesTheExpectedValue(
                                scenario = "and actuals are  big integers",
                                expectedValue = expectedValue,
                                actualValue = BigInteger.valueOf(2L)
                            )
                            correctlyMatchesTheExpectedValue(
                                scenario = "and actuals are big decimals",
                                expectedValue = expectedValue,
                                actualValue = BigDecimal.valueOf(2L)
                            )
                        }

                    correctlyMatchesNumber("ints", 2)
                    correctlyMatchesNumber("longs", 2L)
                    correctlyMatchesNumber("floats", 2f)
                    correctlyMatchesNumber("doubles", 2.0)
                    correctlyMatchesNumber("big ints", BigInteger.valueOf(2L))
                    correctlyMatchesNumber("big decimals", BigDecimal.valueOf(2L))
                }

                context("nested comparisons") {
                    correctlyMatchesTheExpectedValue(
                        scenario = "1 level nesting",
                        expectedValue = aMapWith(2 to 3),
                        actualValue = mapOf(2 to 3)
                    )

                    correctlyMatchesTheExpectedValue(
                        scenario = "1 level nesting with a collection matcher",
                        expectedValue = aMapWith(2 to anOrderedCollectionWith(3)),
                        actualValue = mapOf(2 to listOf(3))
                    )

                    correctlyMatchesTheExpectedValue(
                        scenario = "2 level nesting",
                        expectedValue = aMapWith("foo" to aMapWith(2 to 3)),
                        actualValue = mapOf("foo" to mapOf(2 to 3))
                    )

                    correctlyMatchesTheExpectedValue(
                        scenario = "3 level nesting with a collection matcher and map matcher",
                        expectedValue = aMapWith("foo" to anOrderedCollectionWith(aMapWith(2 to 3))),
                        actualValue = mapOf("foo" to listOf(mapOf(2 to 3)))
                    )
                }
            }

            context("when they don't match") {
                context("flat comparisons") {
                    fun <T> correctlyMismatchesNumber(expectedTypeName: String, expectedValue:T) =
                        context("expecting $expectedTypeName") {

                            correctlyMismatchesTheExpectedValue(
                                scenario = "and the actuals are longs",
                                expectedValue = expectedValue,
                                actualValue = 2L
                            )
                            correctlyMismatchesTheExpectedValue(
                                scenario = "and the actuals are floats",
                                expectedValue = expectedValue,
                                actualValue = 2.0f
                            )
                            correctlyMismatchesTheExpectedValue(
                                scenario = "and the actuals are doubles",
                                expectedValue = expectedValue,
                                actualValue = 2.0
                            )
                            correctlyMismatchesTheExpectedValue(
                                scenario = "and the actuals are bigints",
                                expectedValue = expectedValue,
                                actualValue = BigInteger.valueOf(2L)
                            )
                            correctlyMismatchesTheExpectedValue(
                                scenario = "and the actuals are bigdecimals",
                                expectedValue = expectedValue,
                                actualValue = BigDecimal.valueOf(2L)
                            )
                        }
                    correctlyMismatchesNumber("ints", 3)
                    correctlyMismatchesNumber("longs", 3L)
                    correctlyMismatchesNumber("floats", 3.1f)
                    correctlyMismatchesNumber("doubles", 2.1)
                    correctlyMismatchesNumber("big ints", BigInteger.valueOf(-2L))
                    correctlyMismatchesNumber("big decimals", BigDecimal.valueOf(-2L))
                }

                context("nested comparisons") {
                    correctlyMismatchesTheExpectedValue(
                        scenario = "1 level nesting",
                        expectedValue = aMapWith(2 to 4),
                        actualValue = mapOf(2 to 3)
                    )

                    correctlyMismatchesTheExpectedValue(
                        scenario = "1 level nesting with a collection matcher",
                        expectedValue = aMapWith(2 to anOrderedCollectionWith(4)),
                        actualValue = mapOf(2 to listOf(3))
                    )

                    correctlyMismatchesTheExpectedValue(
                        scenario = "2 level nesting",
                        expectedValue = aMapWith("foo" to aMapWith(2 to 4)),
                        actualValue = mapOf("foo" to mapOf(2 to 3))
                    )

                    correctlyMismatchesTheExpectedValue(
                        scenario = "3 level nesting with a collection matcher and map matcher",
                        expectedValue = aMapWith("foo" to anOrderedCollectionWith(aMapWith(2 to 4))),
                        actualValue = mapOf("foo" to listOf(mapOf(2 to 3)))
                    )
                }
            }
        }

        context("with Strings") {
            context("when they match") {
                correctlyMatchesTheExpectedValue(
                    scenario ="matching strings",
                    expectedValue = "hello",
                    actualValue ="hello"
                )

                OffsetDateTime.now().let { now ->
                    correctlyMatchesTheExpectedValue(
                        scenario = "matching dates encoded as Strings",
                        expectedValue = now,
                        actualValue = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    )
                }

                context("nested maps") {
                    correctlyMatchesTheExpectedValue(
                        scenario = "matching strings",
                        expectedValue = aMapWith("hello" to "world"),
                        actualValue = mapOf("hello" to "world")
                    )

                    OffsetDateTime.now().let { now ->
                        correctlyMatchesTheExpectedValue(
                            scenario = "matching dates encoded as Strings",
                            expectedValue = aMapWith("hello" to now),
                            actualValue = mapOf("hello" to now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)),
                        )
                    }
                }
            }

            context("when they don't match") {
                correctlyMismatchesTheExpectedValue(
                    scenario = "matching strings",
                    expectedValue = "hello",
                    actualValue = "hell"
                )

                OffsetDateTime.now().let { now ->
                    correctlyMismatchesTheExpectedValue(
                        scenario = "matching dates encoded as Strings",
                        expectedValue = now,
                        actualValue = now.plusDays(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    )
                }

                context("nexted maps") {
                    correctlyMismatchesTheExpectedValue(
                        scenario = "matching strings",
                        expectedValue = aMapWith("hello" to "world"),
                        actualValue = mapOf("hello" to "waldo")
                    )
                    OffsetDateTime.now().let { now ->
                        correctlyMismatchesTheExpectedValue(
                            scenario = "matching dates encoded as Strings",
                            expectedValue = aMapWith("hello" to now),
                            actualValue = mapOf("hello" to now.plusDays(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)),
                        )
                    }
                }
            }
        }

        context("with booleans") {
            context("flat comparisons") {
                correctlyMatchesTheExpectedValue(
                    scenario = "when booleans match",
                    expectedValue = true,
                    actualValue = true
                )
                correctlyMismatchesTheExpectedValue(
                    scenario = "when booleans don't match",
                    expectedValue = true,
                    actualValue = false
                )
            }

            context("nested comparisons") {
                correctlyMatchesTheExpectedValue(
                    scenario = "when 1 level nesting matches",
                    expectedValue = aMapWith("items" to true),
                    actualValue = mapOf("items" to true)
                )
                correctlyMismatchesTheExpectedValue(
                    scenario = "when 1 level nesting doesn't match",
                    expectedValue = aMapWith("items" to true),
                    actualValue = mapOf("items" to false)
                )
            }
        }

        context("iterables") {
            context("flat comparisons") {
                correctlyMatchesTheExpectedValue(
                    scenario = "when iterables match",
                    expectedValue = listOf(1, 2),
                    actualValue = listOf(1, 2)
                )
                correctlyMismatchesTheExpectedValue(
                    scenario = "when booleans don't match",
                    expectedValue = listOf(1, 2),
                    actualValue = listOf(2, 1)
                )
            }
        }
    }
})