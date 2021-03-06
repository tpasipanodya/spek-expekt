package io.taff.spek.expekt

import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import kotlin.reflect.KClass

/**
 * default mappings of comparers used for fuzzy matching.
 */
internal val defaultComparers = mutableMapOf<KClass<*>, (Any?, Any?) -> Boolean> (

    Int::class to ::compareInts,

    Long::class to ::compareLongs,

    Double::class to ::compareDoubles,

    Float::class to ::compareFloats,

    BigInteger::class to ::compareBigIntegers,

    BigDecimal::class to ::compareBigDecimals,

    Boolean::class to ::compareComparables,

    String::class to ::compareComparables,

    OffsetDateTime::class to ::compareOffsetDateTimes,

    Instant::class to ::compareInstants,

    LocalDate::class to ::compareLocalDates,

    LocalDateTime::class to ::compareLocalDateTimes,

    ZonedDateTime::class to ::compareZonedDateTimes
)
