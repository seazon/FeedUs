import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sqldelight)
}

val appVersion = "1.0.0"

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
//            @OptIn(ExperimentalKotlinGradlePluginApi::class)
//            compilerOptions {
//                jvmTarget.set(JvmTarget.JVM_11)
//            }
        }
    }

//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "ComposeApp"
//            isStatic = true
//        }
//    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        all {
//            languageSettings.optIn("androidx.compose.material.ExperimentalMaterialApi")
//            languageSettings.optIn("androidx.compose.material3.ExperimentalMaterial3Api")
//            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
//            languageSettings.optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.android.driver)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
//            implementation(libs.androidx.compose.material3)
            implementation(libs.koin.androidx.compose)
        }
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
                implementation(project(":lib"))
            }
        }
        commonMain.dependencies {
            implementation(project(":lib"))
            implementation(compose.runtime)
            implementation(compose.foundation)
//            implementation(compose.material)
            implementation(compose.ui)
            implementation(libs.bundles.compose)
            implementation(compose.components.resources)
//            implementation(compose.components.uiToolingPreview)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.content.negotiation)
//            implementation(libs.koin.androidx.compose)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.runtime)
            implementation(libs.kotlinx.datetime)
//            implementation(libs.koin.core)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation("io.insert-koin:koin-compose:4.0.0")
            implementation("io.insert-koin:koin-compose-viewmodel:4.0.0")
            implementation("io.insert-koin:koin-compose-viewmodel-navigation:4.0.0")
//            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
//            implementation("tech.annexflow.compose:constraintlayout-compose-multiplatform:0.5.0")
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
            implementation("com.russhwolf:multiplatform-settings:1.1.1")
            implementation("com.russhwolf:multiplatform-settings-no-arg:1.1.1")
            implementation("io.coil-kt.coil3:coil-compose:3.0.4")
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.0.4")
            implementation("io.github.dokar3:sonner:0.3.8")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.sqlite.driver)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.native.driver)
        }
    }
}

android {
    namespace = "com.seazon.feedus"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.seazon.feedus"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = appVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":lib"))
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.seazon.feedus.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.seazon.feedus"
            packageVersion = appVersion
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.seazon.feedus.cache") // should same with path and name in src/main/sqldelight
        }
    }
}