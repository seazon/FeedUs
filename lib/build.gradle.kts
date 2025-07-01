import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("java-library")
    id("maven-publish")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.kotlinxSerialization)
}

group = "com.seazon.feedme"
version = "0.63"

publishing {
    repositories {
        maven {
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
    dependencies {
        api(libs.ktor.client.core)
        api(libs.ktor.client.okhttp)
        api(libs.kotlinx.coroutines.core)
        api(libs.ktor.client.content.negotiation)
        api(libs.ktor.serialization.kotlinx.json)
        api(libs.runtime)
        api(libs.kotlinx.datetime)
        api(project.dependencies.platform(libs.koin.bom))
        api(libs.koin.core)
        implementation(libs.koin.compose)
        implementation(libs.koin.compose.viewmodel)
        implementation(libs.koin.compose.viewmodel.navigation)
        implementation(libs.coil)
        api(libs.ksoup.html)
        api(libs.ksoup.entities)
        implementation(kotlin("reflect"))
    }
}