plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
    id("common-android")
}

android {
    namespace = "magym.robobt.wear"
    buildFeatures.compose = true
}

dependencies {
    debugImplementation(libs.composeUiTooling)
    implementation(libs.activityCompose)
    implementation(libs.composeUi)
    implementation(libs.koinAndroid)
    implementation(libs.koinCore)
    implementation(libs.wearCoreSplashscreen)
    implementation(libs.wearHorologistComposeTools)
    implementation(libs.wearHorologistTiles)
    implementation(libs.wearPlayServicesWearable)
    implementation(libs.wearTiles)
    implementation(libs.wearTilesMaterial)
    implementation(libs.wearUiToolingPreview)
    implementation(libs.wearWatchfaceComplicationsDataSourceKtx)
    implementation(platform(libs.composeBom))
    implementation(project(":repository:accelerometer"))
    implementation(project(":repository:bluetooth"))
}