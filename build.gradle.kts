import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.4.32"
	id("com.github.johnrengelman.shadow") version "6.1.0"
	idea
}

group = "com.taff"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_15

repositories {
	jcenter()
	maven("https://jitpack.io")
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.github.microutils:kotlin-logging-jvm:2.0.6")
	implementation("com.natpryce:hamkrest:1.8.0.1")
	implementation("com.google.guava:guava:30.1.1-jre")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.0")
	implementation("org.junit.jupiter:junit-jupiter")
	implementation(enforcedPlatform("org.junit:junit-bom:5.7.1"))
	implementation("org.spekframework.spek2:spek-dsl-jvm:2.0.15")
	implementation("org.spekframework.spek2:spek-runner-junit5:2.0.15")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "15"
	}
}

tasks.withType<Test> { useJUnitPlatform() }

tasks.withType<ShadowJar> {
	archiveFileName.set("${rootProject.name}.${rootProject.version}.jar")
}
