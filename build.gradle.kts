plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor.plugin) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "com.terabyte.angamessenger"
    version = "1.0"
}
