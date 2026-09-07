/*
 * Copyright (c) 2026 tutosrive
 *
 * Author: tutosrive
 * GitHub: https://github.com/tutosrive
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    alias(libs.plugins.android.application)
}

val versionApp = "0.3.1"

base {
    archivesName.set("WhySudo-v$versionApp")
}

android {
    namespace = "com.srm.whysudo"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.srm.whysudo"
        minSdk = 24
        targetSdk = 36
        versionName = versionApp

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionCode = 1
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
            isUniversalApk = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true // Just when is release
//            isShrinkResources = false // Just in AAB Release
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            applicationIdSuffix = ".r"

            packaging {
                jniLibs {
                    useLegacyPackaging = false
                }
            }
        }
        getByName("debug") {
            applicationIdSuffix = ".d"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    flavorDimensions += listOf("version")
    productFlavors {
        create("free") {
            dimension = "version"
            applicationIdSuffix = ".free"
        }
        create("pro") {
            dimension = "version"
            applicationIdSuffix = ".pro"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Source: https://mvnrepository.com/artifact/androidx.sqlite/sqlite
    implementation("androidx.sqlite:sqlite:2.6.2")
    implementation("net.zetetic:sqlcipher-android:4.14.0@aar")
    // Source: https://mvnrepository.com/artifact/io.noties.markwon/core
    implementation("io.noties.markwon:core:4.6.2")
    // Source: https://mvnrepository.com/artifact/io.noties.markwon/recycler
    implementation("io.noties.markwon:recycler:4.6.2")
    // Source: https://mvnrepository.com/artifact/com.github.ismaeldivita/chip-navigation-bar
    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    
}