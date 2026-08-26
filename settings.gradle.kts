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

rootProject.name = "Chatty"
include(":app")
include(":odsystem")
include(":core:model")
include(":core:network")
include(":core:ui")
include(":feature:auth")
include(":feature:discover")
include(":feature:chat")
include(":feature:call")
include(":feature:wallet")
include(":feature:profile")
include(":config")
include(":analytics")
include(":ads")
include(":molecule")
