plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor.plugin)
}

application {
    mainClass.set("com.terabyte.angamessender.chatservice.ApplicationKt")
}

dependencies {
    implementation(project(":common"))

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.status.pages)

    implementation(libs.hikari)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.logback.classic)
}
