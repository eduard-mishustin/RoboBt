import magym.robobt.util.withVersionCatalog
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper

tasks.withType<Test> {
    useJUnitPlatform()
}

project.withVersionCatalog { libs ->
    plugins.withType<KotlinBasePluginWrapper> {
        dependencies {
            testImplementation(libs.testJunitJupiter)
            testImplementation(libs.testKotestAssertions)
        }
    }
}

fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? {
    return add("testImplementation", dependencyNotation)
}