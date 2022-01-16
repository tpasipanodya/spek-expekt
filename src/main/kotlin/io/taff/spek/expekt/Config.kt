package io.taff.spek.expekt

import mu.NamedKLogging

/**
 * Configuration used throughout this library.
 */
object Config {

    /**
     * Used for all logging.
     */
    var logger = NamedKLogging("spek-expekt").logger

    /**
     * Allows re-configuring/augmenting fuzzy matching rules. E.g, to add a new comparer:
     *
     * Config.comparers[MyType::class] = { expected: Any?, actual: Any -> expected == actual }
     *
     * when a value is expected to be of MyType, yur comparer will be used to compare the expected and actual values.
     */
    val comparers = defaultComparers
}

/**
 * Configure this library.
 */
fun configure(fxn: Config.()-> Unit) { fxn(Config) }
