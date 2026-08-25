plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor.plugin) apply false
    alias(libs.plugins.ktlint) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "com.terabyte.angamessenger"
    version = "1.0"

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
