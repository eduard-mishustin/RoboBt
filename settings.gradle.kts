pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RoboBt"

include(
    ":app",
    ":common:android",
    ":common:navigation:api",
    ":common:navigation:voyager",
    ":common:pure",
    ":common:tea:android",
    ":common:tea:compose",
    ":common:tea:core",
    ":common:ui",
    ":feature:connect:api",
    ":feature:connect:impl",
    ":feature:control:api",
    ":feature:control:impl",
    ":repository:accelerometer",
    ":repository:bluetooth",
    ":wear",
)
