plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.app.screentime.wallpaper"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    // Config module (translations)
    implementation(project(":config"))
    implementation(project(":analytics"))

    // Ads module (interstitial during image load)
    implementation(project(":ads"))


    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    
    // Compose dependencies
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${libs.versions.lifecycleViewmodelCompose.get()}")
    
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.adaptive)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    
    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.core)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${libs.versions.kotlinxSerializationJson.get()}")
    
    // Material Icons
    implementation(libs.material.icons.extended)
    
    // ODS System (from odsystem module)
    api(project(":odsystem"))

    // Core Network system
    implementation(project(":core:network"))

    // Ktor for WallpapersCraft API
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    // Coil for image loading
    implementation(libs.coil.compose)

    implementation(libs.ktor.client.logging)

}

