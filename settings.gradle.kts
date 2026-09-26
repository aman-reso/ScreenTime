pluginManagement {
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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        flatDir {
            dirs("app/libs")
        }
    }
}

rootProject.name = "Winter"
include(":app")
include(":appdesign")
include(":core:model")
include(":core:network")
include(":core:ui")
include(":feature:auth")
include(":feature:discover")
include(":feature:chat")
include(":feature:call")
include(":feature:wallet")
include(":feature:profile")
include(":feature:preferences")
include(":config")
include(":analytics")
include(":ads")
include(":molecule")
