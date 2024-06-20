plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinAndroid)
	alias(libs.plugins.composeCompiler)
	id("common-android")
}

android {
	namespace = "magym.robobt.common.ui"
	buildFeatures.compose = true
}

dependencies {
	implementation(libs.activityCompose)
	implementation(libs.appcompat)
	implementation(libs.composeMaterial3)
	implementation(libs.composeUiTooling)
	implementation(libs.koinCore)
	implementation(libs.lifecycleKtx)
	implementation(libs.voyagerKoin)
	implementation(libs.voyagerNavigator)
	implementation(platform(libs.composeBom))
	implementation(project(":common:pure"))
	implementation(project(":common:tea:compose"))
}