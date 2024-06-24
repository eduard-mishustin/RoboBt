plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
    id("common-android")
    id("common-tests")
}

android {
    namespace = "magym.robobt.feature.control"
    buildFeatures.compose = true
}

dependencies {
    api(project(":feature:control:api"))
    implementation(libs.activity)
    implementation(libs.composeMaterial3)
    implementation(libs.composeUi)
    implementation(libs.koinCore)
    implementation(platform(libs.composeBom))
    implementation(project(":common:android"))
    implementation(project(":common:navigation:voyager"))
    implementation(project(":common:pure"))
    implementation(project(":common:tea:compose"))
    implementation(project(":common:ui"))
    implementation(project(":repository:accelerometer"))
    implementation(project(":repository:bluetooth"))
}