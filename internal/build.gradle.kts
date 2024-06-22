import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("org.springframework.boot") version "3.2.6"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("plugin.jpa") version "1.9.24"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
}

group = "me.func"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.session:spring-session-core")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.5")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.liquibase:liquibase-core")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
	implementation("javax.servlet:servlet-api:2.5")
	implementation("com.google.ortools:ortools-java:9.10.4067")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("com.jayway.jsonpath:json-path")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
		jvmTarget = JvmTarget.JVM_21
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir("build/generated-snippets")
}

tasks.jar {
	dependsOn(tasks.test)
	from("build/generated-snippets") {
		into("static/docs")
	}
}