pluginManagement {
    repositories {
        google()
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

dependencyResolutionManagement {

    repositories {

        maven {
            setUrl("https://jitpack.io")
        }
    }
}

rootProject.name = "Coffee and Happiness"
include(":app")
