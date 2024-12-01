import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-android")
}

configure<BaseExtension> {
    compileSdkVersion(35)

    defaultConfig {
        targetSdk = 35
        minSdk = 26

        versionCode = 1
        versionName = "1"
        vectorDrawables.useSupportLibrary = true
    }

    packagingOptions {
        setExcludes(
            setOf(
                "META-INF/*.kotlin_module",
                "META-INF/MANIFEST.MF",
                "META-INF/com.android.tools/proguard/coroutines.pro",
                "META-INF/proguard/coroutines.pro",
            )
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }

        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs += "-Xcontext-receivers"
    }
}