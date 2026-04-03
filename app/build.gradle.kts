/*
 * Copyright (c) 2026 tutosrive. All rights reserved.
 *
 * Author: tutosrive
 * GitHub: https://github.com/tutosrive
 *
 * This source code is PROPRIETARY and CONFIDENTIAL.
 * Unauthorized copying, modification, or distribution of this file,
 * via any medium, is strictly prohibited.
 *
 * This software is provided "as is", without warranty of any kind.
 * In no event shall the author be liable for any claim or damages.
 */

plugins {
    alias(libs.plugins.android.application)
}

val versionApp = "0.2.5"

base {
    archivesName.set("WhySudo-v$versionApp")
}

android {
    namespace = "com.srm.whysudo"
    compileSdk {
        version = release(36) {
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
            // isShrinkResources = false // Just in AAB Release
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("io.noties.markwon:core:4.6.2")
    // Source: https://mvnrepository.com/artifact/androidx.sqlite/sqlite-bundled
//    implementation("androidx.sqlite:sqlite-bundled:2.6.2")
    // Source: https://mvnrepository.com/artifact/androidx.sqlite/sqlite
    implementation("androidx.sqlite:sqlite:2.6.2")
    implementation("net.zetetic:sqlcipher-android:4.14.0@aar")


}