# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 0.10.3

- `jvm` version `1.9.10`

## 0.10.2

- Going forwards, this library will only be available on GitHub Packages instead of 
  `https://tmpasipanodya.jfrog.io/artifactory/releases`.
- `jvm` version `1.9.0`
- `jdk` version `20`

## 0.10.1

- Bumped JVM compatibility to JDK 18

## 0.10.0

- `jvm` version `1.8.21`
- `jackson-datatype-jsr310` & `jackson-module-kotlin` version `2.15.1`
- `junit-bom` version `5.9.3`

## 0.9.0

- `jdk` version `18`

## 0.8.1

- `jvm` version `1.7.22`
- `jackson-datatype-jsr310` version `2.14.1`
- `org.jetbrains.dokka` version `1.7.20`
- `kotlin-logging-jvm` version `3.0.4`

## 0.8.0

- `jdk` version `18`
- `spek-dsl-jvm` version `2.0.19`
- `spek-runner-junit5` version `2.0.19`
- `jackson-module-kotlin` version `2.14.1`
- `junit-bom` version `5.9.1`

## 0.7.4

- `jvm` version `0.7.5`

## 0.7.4

- `kotlin-logging-jvm` version `2.1.23`
- `jvm` plugin version `1.7.0`

## 0.7.3
- `jackson-datatype-jsr310` version `2.13.3`
- `jackson-module-kotlin` version `2.13.3`
- `jvm` version `1.6.21`

## 0.7.2
- Additional test coverage for implicit type-based value comparisons.
- Added implicit type-based value comparisons for Temporal types.
- Removed unused `offsetDatetimeFormatter` from config.
- Removed `objectMapper` from config since it's no longer needed for logging purposes.
- `com.jfrog.artifactory` version `4.27.1`.
- `guava` version `31.1-jre`.
-  `spek-dsl-jvm` version `2.0.18`.
- `spek-runner-junit5` version `2.0.18`.

## 0.6.3
- `com.jfrog.artifactory` version `4.25.5`
- CI & CD cleanup.

## 0.6.2
- Added a release step that reports test results.
- Added a CI badge to `README.md`

## 0.6.1
- `org.jetbrains.dokka` version `1.6.10`
- `com.jfrog.artifactory` version `4.25.4`
- Added a Build step that reports test results.

## 0.6.0
- `kotlin-logging-jvm` version `2.1.21`
- Renamed `beAnOrderedCollectionOf` to `containInOrder`
- Renamed `beAnUnorderedCollectionOf` to `containInAnyOrder`

## 0.5.0

- Kotlin 1.6.10

## 0.4.0

- Renamed the project to `spek-expekt`
- updated `README.md`
- Added dependabot config

## 0.3.0

- Upgraded Java to v16 from v14
- Changed group id to `io.taff` from `com.taff`
- Setup CI and CD via GitHub Actions

## 0.2.0

- Upgraded Kotlin to 1.6.0

## 0.1.0

- Initiated README
- Added Spek2 `should` DSL.
- Added map and collection matchers
- Added `satisfy` and `true`/`false` matchers

