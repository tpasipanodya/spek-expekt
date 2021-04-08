package com.taff.hephaestustest.expectation.boolean

import com.taff.hephaestustest.expectation.any.satisfy

/**
 * Declaratively checks whether the actual value == true
 * ```
 * true should beTrue()
 * ```
 */
inline fun beTrue() = satisfy<Boolean> { this }

/**
 * Declaratively checks whether the actual value == false
 * ```
 * false should beFalse()
 * ```
 */
fun beFalse() =  satisfy<Boolean> { this }.not()