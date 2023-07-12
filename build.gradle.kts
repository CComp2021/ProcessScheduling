plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "com.github.rok"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.formdev:flatlaf:3.1.1")
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("org.knowm.xchart:xchart:3.8.4")

}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}
