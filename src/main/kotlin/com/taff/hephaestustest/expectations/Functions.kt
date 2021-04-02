package com.taff.hephaestustest.expectations

/**
 * declarativeky use matchers to run your assrtations, e.g:
 *
 * expect(mapOf(1 to 2)) {
 *    toBeAMapWith(1 to 2)
 * }
 */
fun <T> expect(actual: T, body: Expectation<T>.() -> Unit) = Expectation<T>()
    .apply(body)
    .check(actual)