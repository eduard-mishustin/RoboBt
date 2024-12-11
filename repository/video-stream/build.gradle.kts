plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("common-android")
}

android {
    namespace = "magym.robobt.repository.video_stream"
}

dependencies {
    implementation(libs.koinAndroid)
    implementation(libs.okhttp3)
    implementation(project(":common:android"))
}