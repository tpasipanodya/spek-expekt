package com.taff.hephaestustest.expectations

import com.taff.hephaestustest.Config
import com.taff.hephaestustest.expectations.iterable.beAnOrderedCollectionOf
import com.taff.hephaestustest.expectations.map.beAMapWith
import java.math.BigDecimal
import java.math.BigInteger
import java.time.OffsetDateTime

/**
 * Compare 2 items that implement Comparable. May throw class cast exceptions.
 */
internal fun compareComparables(expected: Any?, actual: Any?) = if (actual is Comparable<*> && expected is Comparable<*>) {
    compareValues(expected, actual) == 0
} else false

/**
 * other numbers against integers
 */
internal fun compareInts(expected: Any?, actual: Any?) = when(expected) {
    is Int -> when(actual) {
        is Int ->  expected.compareTo(actual) == 0

        is Long -> expected.compareTo(actual) == 0

        is Float  -> expected.compareTo(actual) == 0

        is Double -> expected.compareTo(actual) == 0

        is BigInteger -> BigInteger.valueOf(expected.toLong()).compareTo(actual) == 0

        is BigDecimal -> BigDecimal.valueOf(expected.toLong()).compareTo(actual) == 0

        else -> false
    }
    else -> expected == actual
}

/**
 * other numbers against longs
 */
internal fun compareLongs(expected: Any?, actual: Any?) = when(expected) {
    is Long -> when(actual) {
        is Int ->  expected.compareTo(actual) == 0

        is Long -> expected.compareTo(actual) == 0

        is Float  -> expected.compareTo(actual) == 0

        is Double -> expected.compareTo(actual) == 0

        is BigInteger -> BigInteger.valueOf(expected).compareTo(actual) == 0

        is BigDecimal -> BigDecimal.valueOf(expected).compareTo(actual) == 0

        else -> false
    }
    else -> expected == actual
}

/**
 * other numbers against floats
 */
internal fun compareFloats(expected: Any?, actual: Any?) = when(expected) {
    is Float -> when(actual) {
        is Int ->  expected.compareTo(actual) == 0

        is Long -> expected.compareTo(actual) == 0

        is Float  -> expected.compareTo(actual) == 0

        is Double -> expected.compareTo(actual) == 0

        is BigInteger -> BigInteger.valueOf(expected.toLong()).compareTo(actual) == 0 && actual.toFloat() == expected

        is BigDecimal -> BigDecimal.valueOf(expected.toLong()).compareTo(actual) == 0  && actual.toFloat() == expected

        else -> false
    }
    else -> expected == actual
}

/**
 * other numbers against doubles
 */
internal fun compareDoubles(expected: Any?, actual: Any?) = when(expected) {
    is Double -> when(actual) {
        is Int ->  expected.compareTo(actual) == 0

        is Long -> expected.compareTo(actual) == 0

        is Float  -> expected.compareTo(actual) == 0

        is Double -> expected.compareTo(actual) == 0

        is BigInteger -> BigInteger.valueOf(expected.toLong()).compareTo(actual) == 0  && actual.toDouble() == expected

        is BigDecimal -> BigDecimal.valueOf(expected.toLong()).compareTo(actual) == 0  && actual.toDouble() == expected

        else -> false
    }
    else -> expected == actual
}

/**
 * other numbers against big integers
 */
internal fun compareBigIntegers(expected: Any?, actual: Any?) = when(expected) {
    is BigInteger -> when(actual) {
        is Int ->  expected.compareTo(BigInteger.valueOf(actual.toLong())) == 0

        is Long -> expected.compareTo(BigInteger.valueOf(actual.toLong())) == 0

        is Float  -> expected.compareTo(BigInteger.valueOf(actual.toLong())) == 0  && expected.toFloat() == actual

        is Double -> expected.compareTo(BigInteger.valueOf(actual.toLong())) == 0  && expected.toDouble() == actual

        is BigInteger -> BigInteger.valueOf(expected.toLong()).compareTo(actual) == 0

        is BigDecimal -> BigDecimal.valueOf(expected.toLong()).compareTo(actual) == 0

        else -> false
    }
    else -> expected == actual
}

/**
 * other numbers against big decimals
 */
internal fun compareBigDecimals(expected: Any?, actual: Any?) = when(expected) {
    is BigDecimal -> when(actual) {
        is Int ->  expected.compareTo(BigDecimal.valueOf(actual.toLong())) == 0

        is Long -> expected.compareTo(BigDecimal.valueOf(actual.toLong())) == 0

        is Float -> expected.compareTo(BigDecimal.valueOf(actual.toLong())) == 0  && expected.toFloat() == actual

        is Double -> expected.compareTo(BigDecimal.valueOf(actual.toLong())) == 0 && expected.toDouble() == actual

        is BigInteger -> BigInteger.valueOf(expected.toLong()).compareTo(actual) == 0

        is BigDecimal -> BigDecimal.valueOf(expected.toLong()).compareTo(actual) == 0

        else -> false
    }
    else -> expected == actual
}

/**
 * Compare other items against OffsetDateTime
 */
internal fun compareDateTimes(expected: Any?, actual: Any?) = if (actual is String) {
    when (expected) {
        is OffsetDateTime -> compareValues(Config.dateTimeDeserializer(actual), expected) == 0

        is Comparable<*> -> compareValues(actual, expected) == 0

        else -> expected == actual
    }
} else expected == actual

/**
 * Helper function for comparing 2 types. Resolves a comparer to use (if one exists) and uses that.
 * Also handles cases where the expected value is a collection or map that may or may not inturn contain matchers and
 * therefore requires recursive matching.
 */
fun _compare(expected: Any?, actual: Any?) : Boolean = expected?.let { safeExpected ->
    Config.comparers[safeExpected::class]
        ?.let { it(safeExpected, actual) }
        ?: when(expected) {
            is Iterable<*> -> {
                if (actual is Iterable<*>) {
                    val expectedList = expected.toList()
                    val actualList = actual.toList()
                    beAnOrderedCollectionOf(*expectedList.toTypedArray())
                        .asPredicate()
                        .invoke(actualList) && actualList.size == expectedList.size
                } else (expected == actual)
            }

            is Map<*, *> -> {
                if (actual is Map<*, *>) {
                    val expectedEntries = expected.map { it.toPair() }.toTypedArray()
                    val actualMap = actual as Map<Any?, *>
                    beAMapWith(*expectedEntries)
                        .asPredicate()
                        .invoke(actualMap) && actualMap.size == expected.size
                } else (expected == actual)
            }

            else -> (expected == actual)
        }
}  ?: (expected == actual)