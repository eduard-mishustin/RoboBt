package magym.robobt.utils

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

/**
 * https://github.com/gradle/gradle/issues/15383
 */
fun Project.libs(block: (libs: LibrariesForLibs) -> Unit) {
    if (project.name != "gradle-kotlin-dsl-accessors") {
        val libs = the<LibrariesForLibs>()
        block.invoke(libs)
    }
}