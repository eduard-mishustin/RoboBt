plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "magym.robobt.web"
version = "1.0.0"
application {
    mainClass.set("magym.robobt.web.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(libs.ktorServerCore)
    implementation(libs.ktorClientCio)
    implementation(libs.ktorServerNetty)
    implementation(libs.ktorServerWebsockets)
    implementation(libs.logback)
    implementation(projects.shared)
    testImplementation(libs.kotlinTestJunit)
}