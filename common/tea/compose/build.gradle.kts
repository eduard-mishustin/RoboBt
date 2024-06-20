plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinAndroid)
	alias(libs.plugins.composeCompiler)
	id("common-android")
}

android {
	namespace = "magym.robobt.common.tea.compose"
	buildFeatures.compose = true
}

dependencies {
	api(libs.lifecycleKtx)
	api(project(":common:tea:core"))
	implementation(libs.composeUi)
	implementation(platform(libs.composeBom))
}