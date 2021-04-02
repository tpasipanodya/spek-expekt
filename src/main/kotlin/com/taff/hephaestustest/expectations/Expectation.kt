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
 */
class Expectation<T> {

    private val matchers = mutableListOf<Matcher<T>>()

    /**
     * Checks whether the actual map is a superset of the expected map. Keys should pass a hashcode and equality test
     * whilst corresponding values are compared using the fuzzy matching logic configured on com.taff.hephaestustest.Config.comparers.
     *
     * Collections are compared using the matcher com.taff.hephaestustest.matchers.anOrderedCollectionWith.
     */
    fun <K> toBeAMapWith(vararg entries: Pair<K, *>) = matchers.add(aMapWith(*entries) as Matcher<T>)

    /**
     * Checks whether the actual map is a superset of the expected map. Keys should pass a hashcode and equality test
     * whilst corresponding values are compared using the fuzzy matching logic configured on com.taff.hephaestustest.Config.comparers.
     *
     * Collections are compared using the matcher com.taff.hephaestustest.matchers.anOrderedCollectionWith.
     */
    fun <K> toBeAMapWith(expected: Map<K, *>) = matchers
        .add(aMapWith(*expected
            .entries
            .map { it.toPair() }
            .toTypedArray()) as Matcher<T>)

    /**
     * Checks whether the actual collection is a superset of the expected collection.
     * elements are compared using the fuzzy matching logic configured on com.taff.hephaestustest.Config.comparers.
     *
     * Collections are compared using the matcher com.taff.hephaestustest.matchers.anOrderedCollectionWith.
     */
    fun <K> toBeAnOrderedCollectionWith(vararg expecteds: K) {
        matchers.add(anOrderedCollectionWith(expecteds) as Matcher<T>)
    }

    /**
     * Checks whether the actual collection is a superset of the expected collection.
     * elements are compared using the fuzzy matching logic configured on com.taff.hephaestustest.Config.comparers.
     *
     * Collections are compared using the matcher com.taff.hephaestustest.matchers.anOrderedCollectionWith.
     */
    fun <K> toBeAnUnorderedCollectionWith(vararg expecteds: K) {
        matchers.add(anUnorderedCollectionWith(expecteds) as Matcher<T>)
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
    fun toSatisfy(description: String = "the specified condition", fxn: T.() -> Boolean) {
        matchers.add(
            object : Matcher<T> {
                override fun invoke(actual: T) =
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
            object : Matcher<T?> {
                override fun invoke(actual: T?) =
                    if (expected == actual) {
                        MatchResult.Match
                    } else {
                        MatchResult.Mismatch(" $actual didn't pass the equality test")
                    }

                override val description = "equals $expected"
            }
        )
    }

    fun check(actual: T) = matchers.forEach {
        assertThat(actual, it)
    }
}