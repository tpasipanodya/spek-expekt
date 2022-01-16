package io.taff.spek.expekt.map

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import io.taff.spek.expekt.Config
import io.taff.spek.expekt._compare
import java.lang.IllegalArgumentException

/**
 * Checks whether the expected set of entries are a subset of the actual map.
 *
 * values are compared using [io.taff.spek.expekt.Config.comparers]
 * ```
 * mapOf(1 to 2) should contain(mapOf(1 to 2))
 * ```
 */
inline fun <K, V> contain(expectedMap: Map<K, V>) = expectedMap
    .entries
    .map { it.toPair() }
    .toTypedArray()
    .let { contain(*it) }

/**
 * A matcher that checks whether the expected set of entries are a subset of the actual map.
 *
 * values are compared using [io.taff.spek.expekt.Config.comparers]
 * ```
 * mapOf(1 to 2) shouldNot contain(2 to 1)
 * ```
 */
inline fun <K, V> contain(vararg expectedEntries: Pair<K, V>) = object : Matcher<Map<K, V>> {

    private val serializedEntries by lazy {
        """{${
            expectedEntries.joinToString {
                "\n\t\"${it.first}\": ${it.second}"
            }
        }}""".trimIndent()
    }

    override val description = serializedEntries

    /**
     * Compare the actual map against the expected map.
     */
    override fun invoke(actualMap: Map<K, V>) = expectedEntries
        .firstOrNull { (key, expected) ->
            actualMap[key]
                ?.let { actual -> !actualValueMatchesExpected(actual, expected) }
                ?: (expected != null)
        }.let { nonMatchingEntry ->
            if (nonMatchingEntry == null) {
                MatchResult.Match.also {
                    Config.logger.info {
                        "Success! Json object contained attributes {${
                            expectedEntries.joinToString { "\n\t\"${it.first}\": ${it.second}" }
                        }\n}"
                    }
                }
            } else {
                MatchResult.Mismatch("actual: $actualMap\n problematic entry: {${nonMatchingEntry.first}: ${nonMatchingEntry.second}}")
            }
        }

    /**
     * helper function for determining whether 2 values that could be different types (including matchers) match.
     *
     *  values are compared using [io.taff.spek.expekt.Config.comparers]
     */
    private fun actualValueMatchesExpected(actual: Any, expected: Any?) = expected?.let { safeExpected ->
        try {
            if (safeExpected is Matcher<*>) {
                    (safeExpected as Matcher<Any?>).asPredicate()(actual)
            }
            else _compare(expected, actual)
        } catch (e: ClassCastException) {
            throw unknownTypesError(safeExpected,actual, e)
        }
    } ?: (actual == expected)

    private fun unknownTypesError(expected: Any?, actual: Any, e: Exception? = null) = IllegalArgumentException(
        "Cannot compare the actual value against the expected because the actual value's type is unknown." +
                "\n\tactual value $actual\n\texpected value: $expected ", e
    )

    override fun toString() = description
}
