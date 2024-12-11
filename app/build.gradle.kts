plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
    id("common-android")
}

android {
    namespace = "magym.robobt"
    buildFeatures.compose = true

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"
        }

        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(libs.activity)
    implementation(libs.activityCompose)
    implementation(libs.composeMaterial3)
    implementation(libs.coreKtx)
    implementation(libs.koinAndroid)
    implementation(libs.koinCore)
    implementation(libs.lifecycleKtx)
    implementation(libs.voyagerBottomsheet)
    implementation(libs.voyagerNavigator)
    implementation(libs.voyagerTransitions)
    implementation(platform(libs.composeBom))
    implementation(project(":common:android"))
    implementation(project(":common:navigation:voyager"))
    implementation(project(":common:pure"))
    implementation(project(":common:ui"))
    implementation(project(":feature:connect:impl"))
    implementation(project(":feature:control:impl"))
    implementation(project(":repository:bluetooth"))
    implementation(project(":repository:controller"))
    implementation(project(":repository:input-data"))
    implementation("org.java-websocket:Java-WebSocket:1.5.2")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("io.coil-kt:coil-compose:2.7.0")
}