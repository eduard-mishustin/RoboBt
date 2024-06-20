plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("common-android")
}

android {
    namespace = "magym.robobt.repository.connect.wifi"
}

dependencies {
    api(project(":repository:connect:api"))
    implementation(libs.koinAndroid)
    implementation(project(":common:android"))
}