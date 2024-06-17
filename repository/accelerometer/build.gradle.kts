plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

applyCommonAndroid()

android {
    namespace = "magym.robobt.repository.accelerometer"
}

dependencies {
    implementation(libs.koinAndroid)
    implementation(project(":common:android"))
}