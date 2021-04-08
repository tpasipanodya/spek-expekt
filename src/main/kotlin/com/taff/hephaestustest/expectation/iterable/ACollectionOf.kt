package com.taff.hephaestustest.expectation.iterable

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.taff.hephaestustest.Config
import com.taff.hephaestustest.expectation._compare

/**
 * Compares 2 collections, ignoring ordering. elements are compared using fuzzy matching logic defined in
 * [com.taff.hephaestustest.Config.compaprers] and recursively for nested matchers, e.g:
 * ```
 * setOf(1, 2) shouldNot beAnOrderedCollectionOf(1, 2, 3)
 * ```
 */
inline fun <T> beAnUnOrderedCollectionOf(vararg expecteds: T) = object : Matcher<Iterable<T>> {

    override val description = "contains (${Config.objectMapper.writeValueAsString(expecteds)})"

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
}

/**
 * Compares 2 collections with strict ordering. elements are compared using fuzzy matching logic defined in
 * [com.taff.hephaestustest.Config.comparers] and recursively for nested matchers, e.g:
 * ```
 * arrayOf(3, 2, 1) should beAnOrderedCollectionOf(3, 2, 1)
 * ```
 */
inline fun <T> beAnOrderedCollectionOf(vararg expecteds: T) = object : Matcher<Iterable<T>> {

    override val description = "contains (${Config.objectMapper.writeValueAsString(expecteds)})"

    override fun invoke(actuals: Iterable<T>) = actuals.toList().let { actualList ->
        expecteds.mapIndexed { index, expected ->
            if (index >= actualList.size) {
                MatchResult.Mismatch(
                    "Expected $expected to be in the collection but it wasn't. Actual collection: ${Config.objectMapper.writeValueAsString(actuals)}"
                )
            } else {
                val actual = actualList[index]
                val misMatch = MatchResult.Mismatch("Expected $expected to be in the collection but it wasn't. Actual collection: ${Config.objectMapper.writeValueAsString(actuals)}")

                when (expected) {
                    is Matcher<*> -> {
                        try { (expected as Matcher<T?>).invoke(actual) }

                        catch (e: ClassCastException) { misMatch }
                    }

                    else -> if (_compare(expected, actual)) {
                        MatchResult.Match
                    } else misMatch
                }
            }
        }
    }.firstOrNull { it is MatchResult.Mismatch } ?: MatchResult.Match
}