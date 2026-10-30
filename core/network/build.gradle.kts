import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

// Load secrets from local.properties
val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

android {
    namespace = "com.app.screentime.core.network"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // BuildConfig fields for secrets (read from local.properties or use defaults)
        buildConfigField(
            "String",
            "API_BASE_URL",
            "\"${localProperties.getProperty("API_BASE_URL", "https://api.winterdating.com")}\""
        )
        buildConfigField(
            "String",
            "API_DEV_BASE_URL",
            "\"${localProperties.getProperty("API_DEV_BASE_URL", "https://connect-api-test-324708289864.us-central1.run.app")}\""
        )

        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
                arguments += listOf("-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON")
            }
        }
        ndk {
            abiFilters.addAll(listOf("arm64-v8a", "armeabi-v7a", "x86_64"))
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    buildTypes {
        release {
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"${localProperties.getProperty("PROD_API_BASE_URL", localProperties.getProperty("API_BASE_URL", "https://api.winterdating.com"))}\""
            )
            buildConfigField(
                "long",
                "PLAY_INTEGRITY_PROJECT_NUMBER",
                "${localProperties.getProperty("PLAY_INTEGRITY_PROD_PROJECT_NUMBER", "377649712053")}L"
            )
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"${localProperties.getProperty("API_DEV_BASE_URL", "https://connect-api-test-324708289864.us-central1.run.app")}\""
            )
            buildConfigField(
                "long",
                "PLAY_INTEGRITY_PROJECT_NUMBER",
                "${localProperties.getProperty("PLAY_INTEGRITY_TEST_PROJECT_NUMBER", "324708289864")}L"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.exifinterface)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.okhttp)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Security for EncryptedSharedPreferences & Google Play Integrity
    implementation(libs.androidx.security.crypto)
    implementation(libs.play.integrity)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    // AppCompat for language management
    implementation(libs.androidx.appcompat)

    // Media3 Transformer for fast hardware-accelerated video trimming & compression
    implementation(libs.androidx.media3.transformer)
    implementation(libs.androidx.media3.effect)
    implementation(libs.androidx.media3.common)
}