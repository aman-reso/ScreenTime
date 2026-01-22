import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.app.screentime"
    compileSdk = 36

    // Load secrets from local.properties
    val localPropertiesFile = rootProject.file("local.properties")
    val localProperties = Properties()
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
    }

    defaultConfig {
        applicationId = "com.app.screentime"
        minSdk = 24
        targetSdk = 36
        versionCode = 73
        versionName = "7.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // BuildConfig fields for secrets (read from local.properties or use defaults)
        buildConfigField(
            "String",
            "API_BASE_URL",
            "\"${localProperties.getProperty("API_BASE_URL", "https://api.apptime.in")}\""
        )
        buildConfigField(
            "String",
            "API_DEV_BASE_URL",
            "\"${localProperties.getProperty("API_DEV_BASE_URL", "https://abd310e69b89.ngrok-free.app")}\""
        )
        buildConfigField(
            "String",
            "TOTP_FALLBACK_SECRET",
            "\"${localProperties.getProperty("TOTP_FALLBACK_SECRET", "O5YRY4I2737IGHVYOHXM6T7RWWNAW3X7")}\""
        )
        buildConfigField(
            "String",
            "TOTP_DECODE_SECRET",
            "\"${localProperties.getProperty("TOTP_DECODE_SECRET", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567")}\""
        )
    }

    buildTypes {
        firebaseCrashlytics {
            mappingFileUploadEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
        compilerOptions {
            freeCompilerArgs.add("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    hilt {
        enableAggregatingTask = false
    }
    ksp {
        arg("room.incremental", "true")
        arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
    }

    configurations.all {
        resolutionStrategy {
            eachDependency {
                // Suppress missing sources warnings for local AAR files
            }
        }
    }

}

dependencies {
    // Config module (translations)
    implementation(project(":config"))

    // ODS Library module with sources
    implementation(project(":odsystem"))

    // Core Network module
    implementation(project(":core:network"))

    // Wallpaper module
    implementation(project(":wallpaper"))

    // Analytics module
    implementation(project(":analytics"))

    // AppLock module
    implementation(project(":applock"))
    
    // FileManager module
  //  implementation(project(":filemanager"))

    // Ads module
    implementation(project(":ads"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Hilt dependencies
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.hilt.common)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.appcompat)
    ksp(libs.hilt.compiler)

    // Room dependencies
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Material dependencies
    implementation(libs.material.icons.extended)
    implementation(libs.material3)

    // Navigation dependencies
    implementation(libs.navigation.compose)

    // Joda Time dependency
    implementation(libs.joda.time)

    // Ktor dependencies
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.okhttp)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)

    // Coil for image loading
    implementation(libs.coil.compose)

    // Lottie for animations
    implementation("com.airbnb.android:lottie-compose:6.3.0")

    // Google Play In-App Updates
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)

    // Security/Crypto for EncryptedSharedPreferences
    implementation(libs.androidx.security.crypto)

    // Datastore
    implementation(libs.datastore.preferences)
    implementation(libs.startup.android)

    //Startup
    implementation(libs.startup.android)

    //WorkManager
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.core.splashscreen)

//     Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    // Firebase Analytics is now in :analytics module
    implementation(libs.firebase.messaging)

    // ConstraintLayout Compose
    implementation(libs.androidx.constraintlayout.compose)

    // Google Play Services Location
    implementation(libs.play.services.location)
}