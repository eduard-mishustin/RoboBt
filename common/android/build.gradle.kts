plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinAndroid)
	id("common-android")
}

android {
	namespace = "magym.robobt.common.android"
}

dependencies {
	api(libs.lifecycleKtx)
	implementation(libs.appcompat)
}