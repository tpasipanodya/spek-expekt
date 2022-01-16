package io.taff.spek.expekt.iterable

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import io.taff.spek.expekt.Config
import io.taff.spek.expekt._compare

/**
 * Compares 2 collections, ignoring ordering. elements are compared using fuzzy matching logic defined in
 * [io.taff.spek.expekt.Config.compaprers] and recursively for nested matchers, e.g:
 * ```
 * setOf(1, 2) shouldNot containInOrder(1, 2, 3)
 * ``
 */
inline fun <T> containInAnyOrder(vararg expecteds: T) = object : Matcher<Iterable<T>> {

    override val description = "containsInAnyOrder${Config.objectMapper.writeValueAsString(expecteds)}"

    override fun invoke(actuals: Iterable<T>) = actuals!!.toList().let { actualList ->
        expecteds.all { expected->
            when (expected) {
                is Matcher<*> -> {
                    (expected.asPredicate() as (T) -> Boolean).let { matcher ->
                        actualList.any { actual ->
                            try {
                                matcher(actual)
                            } catch (e: ClassCastException) {
                                false
                            }
                        }
                    }
                }

                else -> actuals.any { actual -> _compare(expected, actual) }
            }
        }
    }.let { suceeded ->
        if (suceeded) MatchResult.Match
        else MatchResult.Mismatch("actual: ${Config.objectMapper.writeValueAsString(actuals)}")
    }

    override fun toString() = description
}

/**
 * Compares 2 collections with strict ordering. elements are compared using fuzzy matching logic defined in
 * [io.taff.spek.expekt.Config.comparers] and recursively for nested matchers, e.g:
 * ```
 * arrayOf(3, 2, 1) should containInOrder(3, 2, 1)
 * ```
 */
inline fun <T> containInOrder(vararg expecteds: T) = object : Matcher<Iterable<T>> {

    override val description = "containsInOrder${Config.objectMapper.writeValueAsString(expecteds)}"

    override fun invoke(actuals: Iterable<T>) = actuals.toList().let { actualList ->
        expecteds.mapIndexed { index, expected ->
            if (index >= actualList.size) {
                mismatch(expected, actuals)
            } else {
                val actual = actualList[index]

                when (expected) {
                    is Matcher<*> -> {
                        try { (expected as Matcher<T?>).invoke(actual) }

                        catch (e: ClassCastException) { mismatch(expected, actuals) }
                    }

                    else -> if (_compare(expected, actual)) {
                        MatchResult.Match
                    } else mismatch(expected, actuals)
                }
            }
        }
    }.firstOrNull { it is MatchResult.Mismatch } ?: MatchResult.Match

    override fun toString() = description

    private fun mismatch(expected: T, actuals: Iterable<T>) =  MatchResult.Mismatch("actual: ${Config.objectMapper.writeValueAsString(actuals)}\n missing item: $expected")
}
