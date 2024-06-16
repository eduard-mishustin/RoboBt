android {
    compileSdk = 34

    defaultConfig {
        targetSdk = 34
        minSdk = 26

        versionCode = 1
        versionName = "1"
        vectorDrawables.useSupportLibrary = true
    }

    packagingOptions.exclude("META-INF/*.kotlin_module")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs += "-Xcontext-receivers"
    }

    buildTypes {
        debug {
            setMinifyEnabled(false)
        }

        release {
            setMinifyEnabled(true)
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
