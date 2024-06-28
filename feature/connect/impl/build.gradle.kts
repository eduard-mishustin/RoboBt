plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
    id("common-android")
}

android {
    namespace = "magym.robobt.feature.connect"
    buildFeatures.compose = true
}

dependencies {
    api(project(":feature:connect:api"))
    implementation(libs.composeMaterial3)
    implementation(libs.composeUi)
    implementation(libs.koinCore)
    implementation(platform(libs.composeBom))
    implementation(project(":common:android"))
    implementation(project(":common:navigation:voyager"))
    implementation(project(":common:pure"))
    implementation(project(":common:tea:compose"))
    implementation(project(":common:ui"))
    implementation(project(":feature:control:api"))
    implementation(project(":repository:bluetooth"))
}