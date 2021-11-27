package io.taff.hephaestustest.expectation

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat

/**
 * Declaratively use matchers on objects of type `T`, e.g:
 *
 * ```
 * 1 should equal(1)
 * ```
 * @param T The expected value's type.
 */
infix fun <T> T.should(matcher: Matcher<T>) = assertThat(this, matcher)


/**
 * Declaratively use matchers on objects of type `T`, e.g:
 * ```
 * 1 shouldNot equal(2)
 * ```
 */
infix fun <T> T.shouldNot(matcher: Matcher<T>) = assertThat(this, matcher.not())

