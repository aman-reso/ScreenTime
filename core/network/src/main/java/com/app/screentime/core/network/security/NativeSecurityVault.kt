package com.app.screentime.core.network.security

/**
 * Native request-token vault.
 *
 * Tokens are minted entirely inside libsecurity_vault.so. The AES key never
 * crosses the JNI boundary. Generation is refused unless this process is the
 * genuine com.app.screentime APK (package + signing cert).
 */
object NativeSecurityVault {
    @Volatile
    private var isLoaded = false

    init {
        try {
            System.loadLibrary("security_vault")
            isLoaded = true
        } catch (_: Throwable) {
            isLoaded = false
        }
    }

    @JvmStatic
    private external fun nvMint(timestamp: Long): String

    @JvmStatic
    private external fun nvUrl(isDebug: Boolean): String

    @JvmStatic
    private external fun nvBill(): String

    @JvmStatic
    private external fun nvGid(): String

    fun getToken(timestamp: Long = System.currentTimeMillis()): String {
        if (!isLoaded) return ""
        return try {
            nvMint(timestamp)
        } catch (_: Throwable) {
            ""
        }
    }

    fun getBaseUrl(isDebug: Boolean = false): String {
        if (!isLoaded) {
            return if (isDebug) {
                "https://connect-api-test-324708289864.us-central1.run.app"
            } else {
                "https://api.winterdating.com"
            }
        }
        return try {
            nvUrl(isDebug).ifBlank {
                if (isDebug) "https://connect-api-test-324708289864.us-central1.run.app"
                else "https://api.winterdating.com"
            }
        } catch (_: Throwable) {
            if (isDebug) "https://connect-api-test-324708289864.us-central1.run.app"
            else "https://api.winterdating.com"
        }
    }

    fun getBillingBaseUrl(): String {
        if (!isLoaded) return "https://connect-api-test-324708289864.us-central1.run.app"
        return try {
            nvBill().ifBlank { "https://connect-api-test-324708289864.us-central1.run.app" }
        } catch (_: Throwable) {
            "https://connect-api-test-324708289864.us-central1.run.app"
        }
    }

    fun getGoogleWebClientId(): String {
        val fallback = "203569877791-c1ke6b8kg28gg7og5ish10oo26b9ffsb.apps.googleusercontent.com"
        if (!isLoaded) return fallback
        return try {
            nvGid().ifBlank { fallback }
        } catch (_: Throwable) {
            fallback
        }
    }
}
