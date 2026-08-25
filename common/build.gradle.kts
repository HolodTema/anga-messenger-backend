plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.java.jwt)
}
