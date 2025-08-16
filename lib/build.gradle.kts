import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("maven-publish")
    kotlin("multiplatform") version "2.2.0"
    alias(libs.plugins.kotlinxSerialization)
}

group = "com.seazon.feedme"
version = "0.86"

publishing {
    repositories {
        maven {
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvm("desktop") {
    }
//    iosX64()
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.runtime)
                implementation(libs.kotlinx.datetime)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.compose.viewmodel.navigation)
                implementation(libs.coil)
                implementation(libs.ksoup.html)
                implementation(libs.ksoup.entities)
                implementation(kotlin("reflect"))
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        val iosArm64Main by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        val iosSimulatorArm64Main by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}