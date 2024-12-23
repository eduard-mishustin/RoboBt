plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
    id("common-android")
}

android {
    namespace = "magym.robobt.feature.control"
    buildFeatures.compose = true
}

dependencies {
    api(project(":feature:control:api"))
    implementation(libs.activity)
    implementation(libs.coil)
    implementation(libs.composeMaterial3)
    implementation(libs.composeUi)
    implementation(libs.koinCompose)
    implementation(libs.koinCore)
    implementation(libs.okhttp3)
    implementation(platform(libs.composeBom))
    implementation(project(":common:android"))
    implementation(project(":common:navigation:voyager"))
    implementation(project(":common:pure"))
    implementation(project(":common:tea:compose"))
    implementation(project(":common:ui"))
    implementation(project(":repository:bluetooth"))
    implementation(project(":repository:controller"))
    implementation(project(":repository:video-stream"))
}