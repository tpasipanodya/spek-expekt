# Spek Expekt #

A collection of declarative, and composable expectation matchers for [Spek2](https://www.spekframework.org).

## How to use

```kotlin
implementation("io.taff:spek-expekt:0.4.0")
```

### Using the Specification DSL 
```kotlin
data class Record(vararg values: Long)

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
        
        context("testing complex object graphs by composing matchers") {
            val records by memoized { 
                mapOf(
                    "column1" to mapOf(
                        "row1" to listOf(Record(2, 4, 6, 8), Record(16, 32, 64)),
                        "row2" to Record(10)
                    ),
                    "column2" to listOf(
                        mapOf("row1" to Record(3, 9, 15, 18))
                    )
                )
            }

            it("is a list of sub-list of a sub-map of") {
                records should beAMapOf(
                    "column1" to mapOf(
                        "row1" to beAnOrderedCollectionOf(
                            satisfy<Record> { values should beAnOrderedCollectionOf(2, 4, 6, 8) },
                            satisfy<Record> { values should satisfy { all { this % 2 == 0 } } }
                        )
                    )
                )
            }
        }
    }
})

```

## Customising The Declarative Matchers

This library adheres to the principle of convention over configuration. Sane defaults are configured, but they are
also easily overridable.

```kotlin
/** You can customize how comparisons are performed directly */
Config.comparers[MyFunkyType::class] = ::myFunkyComparisonFunction

/** Or via the configure function */
val hephaestusTestConfig = configure {
    comparers[MyFunkyType::class] = ::myFunkyComparisonFunction
}
```

Configured comparers will be applied to all implicit equals comparisons (e.g `beAnOrderedCollectionOf(2, 4, 6, 8)` implicitly compares ints).
