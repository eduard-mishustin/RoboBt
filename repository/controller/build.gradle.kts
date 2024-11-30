plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("common-android")
    id("common-tests")
}

android {
    namespace = "magym.robobt.repository.controller"
}

dependencies {
    implementation(libs.koinAndroid)
    implementation(project(":common:android"))
    implementation(project(":repository:input-data"))
}