package io.taff.spek.expekt

import io.taff.spek.expekt.iterable.containInOrder
import io.taff.spek.expekt.map.contain
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

/** Compare 2 items that implement Comparable. May throw class cast exceptions. */
internal fun compareComparables(expected: Any?, actual: Any?) = if (actual is Comparable<*> && expected is Comparable<*>) {
    compareValues(expected, actual) == 0
} else false

/** Compare other numbers against integers */
internal fun compareInts(expected: Any?, actual: Any?) = when(expected) {
    is Int -> when(actual) {
        is Int ->  expected.compareTo(actual)
        is Long -> expected.compareTo(actual)
        is Float  -> expected.compareTo(actual)
        is Double -> expected.compareTo(actual)
        is BigInteger -> BigInteger.valueOf(expected.toLong()).compareTo(actual)
        is BigDecimal -> BigDecimal.valueOf(expected.toLong()).compareTo(actual)
        else -> false
    } == 0
    else -> expected == actual
}

/** Compare other numbers against longs */
internal fun compareLongs(expected: Any?, actual: Any?) = when(expected) {
    is Long -> when(actual) {
        is Int ->  expected.compareTo(actual)
        is Long -> expected.compareTo(actual)
        is Float  -> expected.compareTo(actual)
        is Double -> expected.compareTo(actual)
        is BigInteger -> BigInteger.valueOf(expected).compareTo(actual)
        is BigDecimal -> BigDecimal.valueOf(expected).compareTo(actual)
        else -> false
    } == 0
    else -> expected == actual
}

/** Compare other numbers against floats */
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

/** compare other numbers against doubles */
internal fun compareDoubles(expected: Any?, actual: Any?) = when(expected) {
    is Double -> {
        when(actual) {
            is Int ->  expected.compareTo(actual) == 0
            is Long -> expected.compareTo(actual) == 0
            is Float  -> expected.compareTo(actual) == 0
            is Double -> expected.compareTo(actual) == 0
            is BigInteger -> BigInteger.valueOf(expected.toLong()).compareTo(actual) == 0  && actual.toDouble() == expected
            is BigDecimal -> BigDecimal.valueOf(expected.toLong()).compareTo(actual) == 0  && actual.toDouble() == expected
            else -> false
        }
    }
    else -> false
}

/** Compare other numbers against big integers */
internal fun compareBigIntegers(expected: Any?, actual: Any?) = when(expected) {
    is BigInteger -> when(actual) {
        is Int ->  expected.compareTo(BigInteger.valueOf(actual.toLong())) == 0
        is Long -> expected.compareTo(BigInteger.valueOf(actual.toLong())) == 0
        is Float  -> expected.compareTo(BigInteger.valueOf(actual.toLong())) == 0  && expected.toFloat() == actual.toFloat()
        is Double -> expected.compareTo(BigInteger.valueOf(actual.toLong())) == 0  && expected.toDouble() == actual.toDouble()
        is BigInteger -> BigInteger.valueOf(expected.toLong()).compareTo(actual) == 0
        is BigDecimal -> BigDecimal.valueOf(expected.toLong()).compareTo(actual) == 0
        else -> false
    }
    else -> expected == actual
}

/** Compare other numbers against big decimals */
internal fun compareBigDecimals(expected: Any?, actual: Any?) = when(expected) {
    is BigDecimal -> when(actual) {
        is Int ->  expected.compareTo(BigDecimal.valueOf(actual.toLong())) == 0
        is Long -> expected.compareTo(BigDecimal.valueOf(actual.toLong())) == 0
        is Float -> expected.compareTo(BigDecimal.valueOf(actual.toLong()))  == 0 && expected.toFloat() == actual.toFloat()
        is Double -> expected.compareTo(BigDecimal.valueOf(actual))  == 0 && expected.toDouble() == actual.toDouble()
        is BigInteger -> expected.compareTo(actual.toBigDecimal()) == 0
        is BigDecimal -> expected.compareTo(actual) == 0
        else -> false
    }
    else -> expected == actual
}

/** Compare other temporal types against instants */
internal fun compareInstants(expected: Any?, actual: Any?) = when (expected) {
    is Instant -> when(actual) {
        is Instant -> expected.compareTo(actual)
        is OffsetDateTime -> expected.compareTo(actual.toInstant())
        is LocalDateTime -> LocalDateTime.ofInstant(expected, ZoneOffset.systemDefault()).compareTo(actual)
        is LocalDate -> LocalDate.ofInstant(expected, ZoneOffset.systemDefault()).compareTo(actual)
        is ZonedDateTime -> expected.atZone(actual.zone).compareTo(actual)
        else -> false
    } == 0
    else -> expected == actual
}

/** Compare other temporal types against local dates */
internal fun compareLocalDates(expected: Any?, actual: Any?) = when (expected) {
    is LocalDate -> when(actual) {
        is Instant -> expected.atStartOfDay().toInstant(ZoneOffset.UTC).compareTo(actual)
        is OffsetDateTime -> expected.compareTo(actual.toLocalDate())
        is LocalDateTime -> expected.compareTo(actual.toLocalDate())
        is LocalDate -> expected.compareTo(actual)
        is ZonedDateTime -> expected.compareTo(actual.toLocalDate())
        else -> expected == actual
    } == 0
    else -> expected == actual
}

/** Compare other temporal types against local date times */
internal fun compareLocalDateTimes(expected: Any?, actual: Any?) = when (expected) {
    is LocalDateTime -> when(actual) {
        is Instant -> expected.toInstant(ZoneOffset.UTC).compareTo(actual)
        is OffsetDateTime -> expected.compareTo(actual.toLocalDateTime())
        is LocalDateTime -> expected.compareTo(actual)
        is LocalDate -> expected.toLocalDate().compareTo(actual)
        is ZonedDateTime -> expected.atZone(actual.zone).compareTo(actual)
        else -> expected == actual
    } == 0
    else -> expected == actual
}

/** Compare other temporal types against OffsetDateTimes */
internal fun compareOffsetDateTimes(expected: Any?, actual: Any?) = when (expected) {
    is OffsetDateTime -> when(actual) {
        is OffsetDateTime -> expected.compareTo(actual)
        is Instant -> expected.compareTo(actual.atOffset(expected.offset))
        is ZonedDateTime -> expected.atZoneSameInstant(actual.zone).compareTo(actual)
        is LocalDate -> expected.toLocalDate().compareTo(actual)
        is LocalDateTime -> expected.compareTo(actual.atOffset(expected.offset))
        else -> expected == actual
    } == 0
    else -> false
}

/** Compare other temporal types against ZonedDateTime */
internal fun compareZonedDateTimes(expected: Any?, actual: Any?) = when (expected) {
    is ZonedDateTime -> when(actual) {
        is Instant -> expected.compareTo(actual.atZone(expected.zone))
        is OffsetDateTime -> expected.toOffsetDateTime().compareTo(actual)
        is LocalDateTime -> expected.compareTo(actual.atZone(expected.zone))
        is LocalDate -> expected.toLocalDate().compareTo(actual)
        is ZonedDateTime -> expected.compareTo(actual)
        else -> expected == actual
    } == 0
    else -> expected == actual
}

/**
 * Helper function for comparing 2 objects. Looks up a comparer to use (if any exists) and uses it for the comparison.
 * Also handles cases where the expected value is a collection or map that may or may not in-turn contain matchers and
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
                    containInOrder(*expectedList.toTypedArray())
                        .asPredicate()
                        .invoke(actualList) && actualList.size == expectedList.size
                } else (expected == actual)
            }

            is Map<*, *> -> {
                if (actual is Map<*, *>) {
                    val expectedEntries = expected.map { it.toPair() }.toTypedArray()
                    val actualMap = actual as Map<Any?, *>
                    contain(*expectedEntries)
                        .asPredicate()
                        .invoke(actualMap) && actualMap.size == expected.size
                } else (expected == actual)
            }

            else -> (expected == actual)
        }
}  ?: (expected == actual)
