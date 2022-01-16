package io.taff.spek.expekt

import io.taff.spek.expekt.iterable.containInAnyOrder
import io.taff.spek.expekt.iterable.containInOrder
import io.taff.spek.expekt.map.contain
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

enum class A {
    A, B
}

enum class B {
    A, B
}
private val config = configure {
    comparers[A::class] = { expected: Any?, actual: Any? ->
        when (expected) {
            is Enum<*> -> when (actual) {
                is Enum<*> -> expected == actual
                is String -> expected.name == actual
                else -> false
            }
            else -> expected == actual
        }
    }
}

object ImplicitTypeComparisons : Spek({

    describe("._compare") {
        val matches by memoized {
            { expected: Any,
              matchingActuals: List<Any>,
              mismatching: List<Any> ->
                matchingActuals.forEach {
                    listOf(it) should containInOrder(expected)
                    listOf(it) should containInAnyOrder(expected)
                    mapOf(1 to it) should contain(1 to expected)
                }
                mismatching.forEach {
                    listOf(it) shouldNot containInOrder(expected)
                    listOf(it) shouldNot containInAnyOrder(expected)
                    mapOf(1 to it) shouldNot contain(1 to expected)
                }
            }
        }
        context("ints") {
            val expected: Int by memoized { 1 }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(1, 1L, 1.0F, 1.0, BigInteger.valueOf(1), BigDecimal.valueOf(1)),
                    listOf(2, 2L, 1.0001F, 1.0001, BigInteger.valueOf(2), BigDecimal.valueOf(1.0001))
                )
            }
        }

        context("longs") {
            val expected: Long by memoized { 1L }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(1, 1L, 1.0F, 1.0, BigInteger.valueOf(1), BigDecimal.valueOf(1)),
                    listOf(2, 2L, 1.0001F, 1.0001, BigInteger.valueOf(2), BigDecimal.valueOf(1.0001))
                )
            }
        }

        context("floats") {
            val expected: Float by memoized { 1.0F }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(1, 1L, 1.0F, 1.0, BigInteger.valueOf(1), BigDecimal.valueOf(1)),
                    listOf(2, 2L, 1.0001F, 1.0001, BigInteger.valueOf(2), BigDecimal.valueOf(1.0001))
                )
            }
        }

        context("douables") {
            val expected: Double by memoized { 1.0 }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(1, 1L, 1.0F, 1.0, BigInteger.valueOf(1), BigDecimal.valueOf(1)),
                    listOf(2, 2L, 1.0001F, 1.0001, BigInteger.valueOf(2), BigDecimal.valueOf(1.0001))
                )
            }
        }

        context("big integers") {
            val expected: BigInteger by memoized { BigInteger.valueOf(1) }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(1, 1L, 1.0F, 1.0, BigInteger.valueOf(1), BigDecimal.valueOf(1)),
                    listOf(2, 2L, 1.0001F, 1.0001, BigInteger.valueOf(2), BigDecimal.valueOf(1.0001))
                )
            }
        }

        context("big decimals") {
            val expected: BigDecimal by memoized { BigDecimal.valueOf(1) }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(1, 1L, 1.0F, 1.0, BigInteger.valueOf(1), BigDecimal.valueOf(1)),
                    listOf(2, 2L, 1.0001F, 1.0001, BigInteger.valueOf(2), BigDecimal.valueOf(1.0001))
                )
            }
        }

        context("strings") {
            val expected: String by memoized { "foo" }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf("foo"),
                    listOf("foob")
                )
            }
        }

        context("with a custom override") {
            val expected: A by memoized { A.A }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(A.A),
                    listOf(A.B, B.A, B.B)
                )
            }
        }

        context("instants") {
            val expected by memoized { Instant.now() }
            val misMatchingInstant by memoized { expected.plusMillis(10) }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(
                        expected,
                        expected.atOffset(ZoneOffset.UTC),
                        expected.atZone(ZoneOffset.systemDefault()).toLocalDate(),
                        expected.atZone(ZoneOffset.systemDefault()).toLocalDateTime(),
                        expected.atZone(ZoneOffset.UTC)
                    ),
                    listOf(
                        misMatchingInstant,
                        misMatchingInstant.atOffset(ZoneOffset.UTC),
                        misMatchingInstant.plus(1L, ChronoUnit.DAYS),
                        misMatchingInstant.atZone(ZoneOffset.systemDefault()).toLocalDateTime(),
                        misMatchingInstant.atZone(ZoneOffset.UTC))
                )
            }
        }

        context("local dates") {
            val expected by memoized { LocalDate.now() }
            val misMatchingInstant by memoized { expected.plusDays(1) }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(
                        expected,
                        expected.atStartOfDay(),
                        expected.atStartOfDay().toInstant(ZoneOffset.UTC),
                        expected.atStartOfDay().atOffset(ZoneOffset.UTC),
                        expected.atStartOfDay().atZone(ZoneOffset.systemDefault())
                    ),
                    listOf(
                        misMatchingInstant,
                        misMatchingInstant.atStartOfDay(),
                        misMatchingInstant.atStartOfDay().toInstant(ZoneOffset.UTC),
                        misMatchingInstant.atStartOfDay().atOffset(ZoneOffset.UTC),
                        misMatchingInstant.atStartOfDay().atZone(ZoneOffset.systemDefault())
                    )
                )
            }
        }

        context("local datetimes") {
            val expected by memoized { LocalDateTime.now() }
            val misMatchingInstant by memoized { expected.plusDays(1) }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(
                        expected,
                        expected.toInstant(ZoneOffset.UTC),
                        expected.atOffset(ZoneOffset.UTC),
                        expected.atZone(ZoneId.systemDefault()),
                        expected.toLocalDate()
                    ),
                    listOf(
                        misMatchingInstant,
                        misMatchingInstant.toInstant(ZoneOffset.UTC),
                        misMatchingInstant.atOffset(ZoneOffset.UTC),
                        misMatchingInstant.atZone(ZoneId.systemDefault()),
                        misMatchingInstant.toLocalDate()
                    )
                )
            }
        }

        context("offset datetimes") {
            val expected by memoized { OffsetDateTime.now() }
            val misMatchingInstant by memoized { expected.plusDays(1) }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(
                        expected,
                        expected.toInstant(),
                        expected.toLocalDate(),
                        expected.toLocalDateTime(),
                        expected.toZonedDateTime()
                    ),
                    listOf(
                        misMatchingInstant,
                        misMatchingInstant.toInstant(),
                        misMatchingInstant.toLocalDate(),
                        misMatchingInstant.toLocalDateTime(),
                        misMatchingInstant.toZonedDateTime()
                    )
                )
            }
        }

        context("zoned datetimes") {
            val expected by memoized { ZonedDateTime.now() }
            val misMatchingInstant by memoized { expected.plusDays(1) }

            it("correctly compares them") {
                matches(
                    expected,
                    listOf(
                        expected,
//                        expected.toInstant(),
//                        expected.toLocalDate(),
//                        expected.toLocalDateTime(),
//                        expected.toOffsetDateTime()
                    ),
                    listOf(
//                        misMatchingInstant,
//                        misMatchingInstant.toInstant(),
//                        misMatchingInstant.toLocalDate(),
//                        misMatchingInstant.toLocalDateTime(),
//                        misMatchingInstant.toOffsetDateTime()
                    )
                )
            }
        }
    }
})
