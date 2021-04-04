package com.taff.hephaestustest.expectations

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import com.taff.hephaestustest.matchers.aMapWith
import com.taff.hephaestustest.matchers.anOrderedCollectionWith
import com.taff.hephaestustest.matchers.anUnorderedCollectionWith

/**
 * This is intended to allow declarative assertions ohf the following style:
 *
 * val testResult = object { val count = 1 }
 * expect(myTestResult) {
 *   toSatisfy { count == 1 }
 * }
 *
 * @param A the actual value's type.
 */
class Expectation<A> {

    private val matchers = mutableListOf<Matcher<A>>()

    /**
     * Checks whether the actual map is a superset of the expected map. Keys should pass a hashcode and equality test
     * whilst corresponding values are compared using the fuzzy matching logic configured on com.taff.hephaestustest.Config.comparers.
     *
     * Collections are compared using the matcher [com.taff.hephaestustest.matchers.anOrderedCollectionWith].
     *
     */
    fun <E> toBeAMapWith(vararg entries: Pair<E, *>) = matchers.add(aMapWith(*entries) as Matcher<A>)

    /**
     * Checks whether the actual collection is a superset of the expected collection.
     * elements are compared using the fuzzy matching logic configured in [com.taff.hephaestustest.Config.comparers].
     *
     * Collections are compared using the matcher [com.taff.hephaestustest.matchers.anOrderedCollectionWith].
     *
     * @param K THe type
     */
    fun <K> toBeAnOrderedCollectionWith(vararg expecteds: K) {
        matchers.add(anOrderedCollectionWith(expecteds) as Matcher<A>)
    }

    /**
     * Checks whether the actual collection is a superset of the expected collection.
     * elements are compared using the fuzzy matching logic configured on com.taff.hephaestustest.Config.comparers.
     *
     * Collections are compared using the matcher [com.taff.hephaestustest.matchers.anOrderedCollectionWith].
     */
    fun <K> toBeAnUnorderedCollectionWith(vararg expecteds: K) {
        matchers.add(anUnorderedCollectionWith(expecteds) as Matcher<A>)
    }

    /**
     * Checks whether the actual value == true
     */
    fun toBeTrue() = matchers.add(equalTo(true))

    /**
     * Checks whether the actual value == false
     */
    fun toBeFalse() = matchers.add(equalTo(false))

    /**
     * Checks whether the actual value satisfies the given predicate/condition.
     */
    fun toSatisfy(description: String = "the specified condition", fxn: A.() -> Boolean) {
        matchers.add(
            object : Matcher<A> {
                override fun invoke(actual: A) =
                    if (fxn(actual)) {
                        MatchResult.Match
                    } else {
                        MatchResult.Mismatch("the following value failed to satisfy the given condition: $actual")
                    }

                override val description = "satisfies $description"
            }
        )
    }

    /**
     * Checks whether the actual value is equal to the expected
     */
    fun toEqual(expected: Any?) {
        matchers.add(
            object : Matcher<A?> {
                override fun invoke(actual: A?) =
                    if (expected == actual) {
                        MatchResult.Match
                    } else {
                        MatchResult.Mismatch(" $actual didn't pass the equality test")
                    }

                override val description = "equals $expected"
            }
        )
    }

    fun check(actual: A) = matchers.forEach {
        assertThat(actual, it)
    }
}