pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven("https://naver.jfrog.io/artifactory/maven/")
        gradlePluginPortal()
    }
    plugins {
        id("dagger.hilt.android.plugin") version "2.44"
        id("org.jetbrains.kotlin.android") version "1.8.10"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven("https://naver.jfrog.io/artifactory/maven/")
    }
}

rootProject.name = "nicepricemap"
include(":app")
 