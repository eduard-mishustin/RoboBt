plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinAndroid)
}

applyCommonAndroid()

android {
	namespace = "magym.robobt.common.tea.android"
}

dependencies {
	api(project(":common:tea:core"))
	implementation(libs.kotlinxCoroutines)
	implementation(libs.lifecycleKtx)
}