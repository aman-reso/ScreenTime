# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# Hilt
-keep class * extends androidx.hilt.work.HiltWorker
-keep class dagger.hilt.android.internal.managers.**
-keep class dagger.hilt.internal.processedrootsentinel.codegen.**
-keep class hilt_aggregated_deps.**
-keep class org.ccci.gto.android.common.dagger.**

# Ktor
-keep class io.ktor.client.engine.okhttp.** { *; }
-keep class io.ktor.client.plugins.auth.** { *; }
-keep class io.ktor.client.plugins.contentnegotiation.** { *; }
-keep class io.ktor.client.plugins.logging.** { *; }
-keep class io.ktor.client.plugins.** { *; }
-keep class io.ktor.client.request.** { *; }
-keep class io.ktor.client.statement.** { *; }
-keep class io.ktor.http.** { *; }
-keep class io.ktor.serialization.kotlinx.json.** { *; }

# Room
-keep class androidx.room.** { *; }

# Coroutines
-keep class kotlinx.coroutines.** { *; }

# Compose
-keep class androidx.compose.runtime.** { *; }
-keepclassmembers class ** { @androidx.compose.runtime.Composable <methods>; }
-keep class com.google.errorprone.annotations.* { *; }
-dontwarn com.google.errorprone.annotations.**
-keep class com.google.crypto.tink.** { *; }
-dontwarn com.google.crypto.tink.**

-keep class org.joda.convert.** { *; }
-dontwarn org.joda.convert.**
-keep class org.joda.time.** { *; }
-dontwarn org.joda.time.**

# Keep PreferencesManager constants for language support
-keep class com.app.screentime.core.network.preferences.PreferencesManager {
    public static final java.lang.String PREFS_NAME;
    public static final java.lang.String KEY_LANGUAGE;
}

# Keep MainActivity attachBaseContext for locale handling
-keep class com.app.screentime.MainActivity {
    protected void attachBaseContext(android.content.Context);
}

# LiveKit & WebRTC
-keep class io.livekit.android.** { *; }
-dontwarn io.livekit.android.**
-keep class org.webrtc.** { *; }
-dontwarn org.webrtc.**
-dontwarn timber.log.**