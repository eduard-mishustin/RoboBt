import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

fun Project.applyCommonAndroid() {
    apply(from = "$rootDir/buildSrc/common-android.kts")
}