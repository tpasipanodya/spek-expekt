package com.taff.hephaestustest.expectation.any

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.equalTo

/**
 * Declaratively check whether the given predicate returns true, eg:
 * ```
 * true should satisfy { this }
 * ```
 */
inline fun <T> satisfy(description: String = "satisfies the given condition", crossinline condition: T.() -> Boolean) = object : Matcher<T> {
    override val description = description

    override fun invoke(actual: T) = condition(actual).let { isSatisfied ->
        if (isSatisfied) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch("Expected the following value to satisfy this condition but it didn't. actual: $actual")
        }
    }
}

/**
 * Declaratively compare whether actual == expected, eg:
 * ```
 * 1 should equal(1)
 * ```
 */
inline fun <T> equal(expected: T) = satisfy<T> { this == expected }

