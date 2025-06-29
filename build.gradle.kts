plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.happypill"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

val compileOnly by configurations.getting
val annotationProcessor by configurations.getting

compileOnly.extendsFrom(annotationProcessor)

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation ("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    implementation("com.auth0:java-jwt:4.5.0")
    implementation(platform("software.amazon.awssdk:bom:2.31.59"))
    implementation("software.amazon.awssdk:s3")

    implementation("io.portone:server-sdk:0.19.0")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    implementation("io.github.openfeign.querydsl:querydsl-core:7.0")
    implementation("io.github.openfeign.querydsl:querydsl-jpa:7.0")
    annotationProcessor("io.github.openfeign.querydsl:querydsl-apt:7.0:jpa")

    runtimeOnly("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")


    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("net.datafaker:datafaker:2.4.2")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.named<Delete>("clean") {
    delete("build/generate")
}