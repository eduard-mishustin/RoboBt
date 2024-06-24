plugins {
    `kotlin-dsl`
}

group = "magym.robobt.build-logic"

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.androidGradlePlugin)
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.commonsConfiguration)
}
