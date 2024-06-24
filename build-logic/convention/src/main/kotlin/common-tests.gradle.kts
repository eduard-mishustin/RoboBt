import magym.robobt.utils.libs
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper

tasks.withType<Test> {
    useJUnitPlatform()
}

libs { libs ->
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