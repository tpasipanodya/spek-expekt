import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig
import groovy.lang.GroovyObject

plugins {
	kotlin("jvm") version "1.6.10"
	id("org.jetbrains.dokka") version "1.6.10"
	id("com.jfrog.artifactory") version "4.25.5"
	id("maven-publish")
	idea
}

group = "io.taff"
version = "0.6.4${ if (isReleaseBuild()) "" else "-SNAPSHOT" }"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven("https://jitpack.io")
	maven {
		name = "JFrog"
		url = uri("https://tmpasipanodya.jfrog.io/artifactory/releases")
		credentials {
			username = System.getenv("ARTIFACTORY_USER")
			password = System.getenv("ARTIFACTORY_PASSWORD")
		}
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

dependencies {
	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	api("io.github.microutils:kotlin-logging-jvm:2.1.21")
	api("com.natpryce:hamkrest:1.8.0.1")
	api("com.google.guava:guava:31.0.1-jre")
	api("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
	api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.1")
	api("org.junit.jupiter:junit-jupiter")
	implementation(enforcedPlatform("org.junit:junit-bom:5.8.2"))
	api("org.spekframework.spek2:spek-dsl-jvm:2.0.17")
	api("org.spekframework.spek2:spek-runner-junit5:2.0.17")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks {
	register<Jar>("dokkaJar") {
		from(dokkaHtml)
		dependsOn(dokkaHtml)
		archiveClassifier.set("javadoc")
	}

	register<Jar>("sourcesJar") {
		from(sourceSets.main.get().allSource)
		archiveClassifier.set("sources")
	}
}

tasks.withType<Test> { useJUnitPlatform() }

tasks.withType<GenerateModuleMetadata> {
	suppressedValidationErrors.add("enforced-platform")
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {

			from(components["java"])
			artifact(tasks["dokkaJar"])
			artifact(tasks["sourcesJar"])

			pom {
				name.set("project.name")
				description.set("${project.name} $version - A collection of extensions and expectation matchers for spek2")
				url.set("https://github.com/tpasipanodya/spek-expekt")

				licenses {
					license {
						name.set("The Apache Software License, Version 2.0")
						url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
					}
				}

				developers {
					developer {
						name.set("Tafadzwa Pasipanodya")
						email.set("tmpasipanodya@gmail.com")
					}
				}

				scm {
					connection.set("scm:git:git://github.com/tpasipanodya/spek-expekt.git")
					developerConnection.set("scm:git:ssh://github.com/tpasipanodya/spek-expekt.git")
					url.set("http://github.com/tpasipanodya/spek-expekt/tree/main")
				}
			}
		}
	}
}

artifactory {
	setContextUrl("https://tmpasipanodya.jfrog.io/artifactory/")

	publish(delegateClosureOf<PublisherConfig> {
		repository(delegateClosureOf<GroovyObject> {
			setProperty("repoKey", if (isReleaseBuild()) "releases" else "snapshots")
			setProperty("username", System.getenv("ARTIFACTORY_USER"))
			setProperty("password", System.getenv("ARTIFACTORY_PASSWORD"))
			setProperty("maven", true)
		})
		defaults(delegateClosureOf<GroovyObject> {
			invokeMethod("publications", "mavenJava")
		})
	})
	resolve(delegateClosureOf<ResolverConfig> {
		setProperty("repoKey", if (isReleaseBuild()) "releases" else "snapshots")
	})
}

fun isReleaseBuild() = System.getenv("IS_RELEASE_BUILD")?.toBoolean() == true
