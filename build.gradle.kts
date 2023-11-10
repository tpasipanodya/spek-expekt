import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.0"
	id("org.jetbrains.dokka") version "1.9.10"
	id("maven-publish")
	idea
}

group = "io.taff"
version = "0.10.2${ if (isReleaseBuild()) "" else "-SNAPSHOT" }"
java.sourceCompatibility = JavaVersion.VERSION_20

repositories {
	mavenCentral()
	maven("https://jitpack.io")
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

dependencies {
	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	api("io.github.microutils:kotlin-logging-jvm:3.0.5")
	api("com.natpryce:hamkrest:1.8.0.1")
	api("com.google.guava:guava:32.1.1-jre")
	api("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
	api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
	api("org.junit.jupiter:junit-jupiter")
	implementation(enforcedPlatform("org.junit:junit-bom:5.10.0"))
	api("org.spekframework.spek2:spek-dsl-jvm:2.0.19")
	api("org.spekframework.spek2:spek-runner-junit5:2.0.19")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "20"
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
	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/tpasipanodya/spek-expekt")
			credentials {
				username = System.getenv("GITHUB_ACTOR")
				password = System.getenv("GITHUB_TOKEN")
			}
		}
	}
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

fun isReleaseBuild() = System.getenv("IS_RELEASE_BUILD")?.toBoolean() == true
