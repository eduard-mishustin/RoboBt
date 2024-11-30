plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("common-android")
    id("common-tests")
}

android {
    namespace = "magym.robobt.repository.input_data"
}

dependencies {
    implementation(libs.koinAndroid)
    implementation(project(":common:android"))
}