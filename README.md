# Hephaestus Test #

A suite of utilities for testing [hephaestus](https://github.com/tpasipanodya/hephaestus-test) based projects. 
Provides the following:
- A [spek2](https://www.spekframework.org/) specification DSL for declaratively evaluating test expectations.
- Declarative test matchers for arbitrarily comparing collections i.e, comparing lists & maps for logical equality 
  instead of relying on `setOf(1) == listOf(1)`
- App lifecycle management that boots up and tears down different layers of your hephaestus project between tests,
  e.g starting up the web layer to run regression tests against the actual API and tearing it down when done.

## Download

```kotlin
implementation("io.taff:hephaestus-test:0.1.0")
```
#### Using Gradle

## Use

This library adheres to the principle of convention over configuration. Sane defaults are configured, but they are 
also easily overridable. In general, this is a heavily configurable test orchestration library.

## Setup

```kotlin
/**
 * You can directly manipulate the mutable configuration variables
 */
Config.comparers[MyFunkyType::class] = ::myFunkyComparisonFunction

/**
 * Or use the functional API
 */
val hephaestusTestConfig = configure {
    comparers[MyFunkyType::class] = ::myFunkyComparisonFunction
}

```

Additional configuration details are discussed further [down here](Customising The Declarative Matchers)

### Using the Specification DSL 
```kotlin
object AddSpek : Spek({

    describe("demonstrating the DSL") {
        context("testing addition using the equalTo matcher") {
           it("adds") { 
             (1 + 1) should equal(2) 
             (1 + 2) shouldNot equal(2)
           }
        }
        
        context("testing addition using the satisfy matcher") {
           it("adds") { 
             (1 + 1) should satisfy { this == 2 }
             (1 + 2) shouldNot satisfy { this == 2}
           }
        }
        
        context("testing for subsets using the beAnUnOrderedCollectionOf matcher") {
            it("is a subset") { 
              setOf(1, 3, 2) should beAnUnOrderedCollectionOf(2, 1)
              setOf(1, 3, 2) shouldNot beAnUnOrderedCollectionOf(7, 1)
            }
        }

        context("testing for sublists using the beAnOrderedCollectionOf matcher") {
          it("is a sublist") { 
            listOf(1, 2, 3) should beAnOrderedCollectionOf(1, 2)
            listOf(3, 2, 1) shouldNot beAnOrderedCollectionOf(1, 2)
          }
        }

        context("comparing maps using the beAMapOf matcher") {
          val map by memoized {
            mapOf(1 to 2, 2 to mapOf(3 to mapOf("foo" to "bar")))
          }
          it("is a sub-map") {
            map should beAMapOf(mapOf(2 to mapOf(3 to beAMapOf("foo" to "bar"))))
            map shouldNot beAMapOf(mapOf(2 to mapOf(3 to beAMapOf("foo" to "lorem"))))
          }
        }
    }
})

```

## Customising The Declarative Matchers

How the expected value for any given map entry or collection element is compared against the actual 
value can be customized by setting your own comparers. For example, adding a custom 
comparer for strings:

```kotlin
Config.comparers[String::class] = { expected: Any?, actual: Any? ->
    expected == actual 
}

/* or */


val hephaestusTestConfig = configure {
  comparers[String::class] = { expected: Any?, actual: Any? ->
    expected == actual
  }
}
```

Configured comparers apply to all collection/map matchers.