import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {

    androidResources {
        generateLocaleConfig = false
    }

    namespace = "com.example.skyline"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.skyline"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 🔐 SAFE API KEY
        val apiKey = project.findProperty("WEATHER_API_KEY")?.toString() ?: ""

        buildConfigField(
            "String",
            "WEATHER_API_KEY",
            "\"$apiKey\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.runtime)
    implementation("androidx.compose.foundation:foundation")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.compose.material:material:1.6.0")

    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.core:core-ktx:1.12.0")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

    // 🌐 Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // 🖼 Images
    implementation("io.coil-kt:coil-compose:2.7.0")

    // 🎨 Icons
    implementation("androidx.compose.material:material-icons-extended")

    // 🗄 ROOM DATABASE (FINAL CORRECT SETUP)
    implementation("androidx.room:room-runtime:2.7.0")
    implementation("androidx.room:room-ktx:2.7.0")
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.activity.ktx)
    ksp("androidx.room:room-compiler:2.7.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}