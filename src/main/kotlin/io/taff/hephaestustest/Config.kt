package io.taff.hephaestustest

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.taff.hephaestustest.expectation.defaultComparers
import mu.NamedKLogging
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * Configuration used throughout this library.
 */
object Config {

    /**
     * used for serializing objects for logging as well as deserializing json into lists and maps.
     */
    var objectMapper = jacksonObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        registerModule(JavaTimeModule())
    }

    /**
     * Used for deserializing dates. When dates are represented as strings (e.g after a partially complete json eserialization),
     * we use this to attempt deserializing strings to dates for comparison. e.g,assuming today is 2021/10/10:
     * expect(listOf(OffsetDateTime.now()) {
     *    toBeAnOrderedCollectionOf("2021/10/10")
     * }
     */
    var dateTimeDeserializer: (String) -> OffsetDateTime = { str: String ->
        OffsetDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    /**
     * Used for all logging.
     */
    var logger = NamedKLogging("hephaestus-test").logger

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
fun configure(fxn: Config.()-> Unit) {
    fxn(Config)
}