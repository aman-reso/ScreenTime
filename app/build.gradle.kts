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
    //noinspection GradleDependency
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
        //noinspection OldTargetApi
        targetSdk = 36
        versionCode = 114
        versionName = "100.11.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
        manifestPlaceholders["appLabel"] = "@string/app_name"
    }

    signingConfigs {
        create("release") {
            val keystorePath = localProperties.getProperty("KEYSTORE_FILE")
            val keystoreFile = when {
                !keystorePath.isNullOrEmpty() -> rootProject.file(keystorePath)
                rootProject.file("apptime.jks").exists() -> rootProject.file("apptime.jks")
                else -> null
            }
            val storePass = localProperties.getProperty("KEYSTORE_PASSWORD")
            val alias = localProperties.getProperty("KEY_ALIAS")
            val keyPass = localProperties.getProperty("KEY_PASSWORD", storePass)

            if (keystoreFile != null && keystoreFile.exists() && !storePass.isNullOrEmpty() && !alias.isNullOrEmpty()) {
                storeFile = keystoreFile
                storePassword = storePass
                keyAlias = alias
                keyPassword = keyPass
            }
        }
    }

    buildTypes {
        release {
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"${localProperties.getProperty("PROD_API_BASE_URL", localProperties.getProperty("API_BASE_URL", "https://api.winterdating.com"))}\""
            )
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            val releaseConfig = signingConfigs.getByName("release")
            signingConfig = if (releaseConfig.storeFile != null && releaseConfig.storeFile!!.exists()) {
                releaseConfig
            } else {
                signingConfigs.getByName("debug")
            }
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
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }


    compileOptions {
        isCoreLibraryDesugaringEnabled = true
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

    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
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
    // Core & Feature modules
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:ui"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:discover"))
    implementation(project(":feature:chat"))
    implementation(project(":feature:call"))
    implementation(project(":feature:wallet"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:preferences"))
    implementation(project(":core:faceverification"))

    // Config module (translations)
    implementation(project(":config"))

    // ODS Library module with sources
    implementation(project(":appdesign"))

    // Analytics module
    implementation(project(":analytics"))

    // Ads module
    implementation(project(":ads"))
    implementation(project(":molecule"))

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
    implementation(libs.play.services.auth)
    ksp(libs.hilt.compiler)

    // Room dependencies
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Material dependencies
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
    implementation(libs.coil.gif)


    // Google Play In-App Updates & Reviews
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)
    implementation(libs.play.review)
    implementation(libs.play.review.ktx)


    // Biometric Authentication
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.biometric.ktx)

    // Security/Crypto for EncryptedSharedPreferences
    implementation(libs.androidx.security.crypto)

    // Datastore
    implementation(libs.datastore.preferences)
    implementation(libs.startup.android)

    //Startup
    implementation(libs.startup.android)

    //WorkManager
    implementation(libs.androidx.core.splashscreen)

//     Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    // Firebase Analytics is now in :analytics module
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.config)

    // ConstraintLayout Compose
    implementation(libs.androidx.constraintlayout.compose)

    // Google Play Install Referrer (campaign/source attribution)
    implementation(libs.play.installreferrer)

    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
