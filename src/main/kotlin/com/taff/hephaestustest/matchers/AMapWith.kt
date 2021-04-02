package com.taff.hephaestustest.matchers

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.taff.hephaestustest.Config
import java.lang.IllegalArgumentException

/**
 * A matcher that checks whether the expected set of entries are a subset of the actual map.
 */
inline fun <K> aMapWith(vararg expectedEntries: Pair<K, *>) = object : Matcher<Map<K, *>> {

    private val serializedEntries by lazy {
        """{${
            expectedEntries.joinToString {
                "\n\t\"${it.first}\": ${it.second}"
            }
        }}""".trimIndent()
    }

    override val description = serializedEntries

    override fun invoke(actuals: Map<K, *>) = expectedEntries
        .firstOrNull { (key, expected) ->
            actuals[key]
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
                MatchResult.Mismatch("expected to find the entry: {${nonMatchingEntry.first}: ${nonMatchingEntry.second}}. $ actualMap: $actuals")
            }
        }

    /**
     * helper function for determining whether 2 values that could be different types (including matchers) match.
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
}
