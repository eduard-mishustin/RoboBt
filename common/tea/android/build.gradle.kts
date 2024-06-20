plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinAndroid)
	id("common-android")
}

android {
	namespace = "magym.robobt.common.tea.android"
}

dependencies {
	api(project(":common:tea:core"))
	implementation(libs.kotlinxCoroutines)
	implementation(libs.lifecycleKtx)
}