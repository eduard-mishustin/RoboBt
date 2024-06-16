plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinAndroid)
}

applyCommonAndroid()

android {
	namespace = "magym.robobt.common.android"
}

dependencies {
	api(libs.lifecycleKtx)
	implementation(libs.appcompat)
}