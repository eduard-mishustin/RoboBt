plugins {
	alias(libs.plugins.kotlinJvm)
}

dependencies {
	api(libs.voyagerScreenmodel)
	implementation(libs.kotlinxCoroutines)
}