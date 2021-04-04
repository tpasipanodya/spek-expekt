# Hephaestus Test #

A suite of utilities for testing [hephaestus](https://github.com/tpasipanodya/hephaestus-test) based projects. 
Provides the following:
- A [spek2](https://www.spekframework.org/) specification DSL for declaratively evaluating test expectations.
- Declarative test matchers for arbitrarily comparing collections i.e, comparing lists & maps for logical equality 
  instead of relying on `setOf(1) == listOf(1)`
- App lifecycle management that boots up and tears down different layers of your hephaestus project between tests,
  e.g starting up the web layer to run regression tests against the actual API and tearing it down when done.

## Installation

```kotlin
implementation("com.taff:hephaestus-test:0.1.0")
```
#### Using Gradle

## Use
This library adheres to the principle of convention over configuration. Sane defaults are configured, but they are 
also easily overridable. In general, this is a heavily configurable test orchestration library.

### using the Specification DSL 
```kotlin
fun add(num1: Long, num2: Long) = num1 + num2

object AddSpek : Spek({

    describe(".add") {
        context("positive numbers") {
            val result by memoized { add(2, 5) }

            expect(result) { toEqual(7) }
        }
    }
})

```

## Using The Declarative Matchers

Declarative matchers are woven into the specification dsl and can be very useful for testing json 
APIs without having to wrestle with static types and custom equality checks.

```kotlin
fun add(num1: Long, num2: Long) = num1 + num2

object AddSpek : Spek({

    describe(".add") {
        context("positive numbers") {
            val result by memoized { 
                mapOf("actual" to listOf(add(2, 5))) 
            }

            expect(result) { 
                toBeAMapWith("actual" to anOrderdCollectionWith(7)) 
            }
        }
    }
})

```

How the expected value for any given map entry or collection element is compared against the actual 
corresponding value can be customized by setting your own comparer. For example, adding a custom 
comparer for strings:

```kotlin
Config.comparers[String::class] = { expected: Any?, actual: Any? ->
    expected == actual 
}
```

Configured comparers apply to all matchers.