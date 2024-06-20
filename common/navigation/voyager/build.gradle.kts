plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinAndroid)
	alias(libs.plugins.composeCompiler)
	id("common-android")
}

android {
	namespace = "magym.robobt.navigation.voyager"
	buildFeatures.compose = true
}

dependencies {
	api(libs.voyagerNavigator)
	api(project(":common:navigation:api"))
	implementation(libs.appcompat)
	implementation(libs.composeUi)
	implementation(libs.koinCore)
	implementation(libs.kotlinxCoroutines)
	implementation(libs.voyagerBottomsheet)
	implementation(platform(libs.composeBom))
	implementation(project(":common:android"))
}