repositories {
    mavenCentral()
}

plugins {
    `kotlin-dsl`
}

tasks.test {
    useJUnitPlatform()
}