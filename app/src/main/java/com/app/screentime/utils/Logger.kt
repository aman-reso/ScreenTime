package com.app.screentime.utils

import android.util.Log
import com.app.screentime.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Logger utility class for app-wide logging
 * Only logs in DEBUG builds to avoid logging in production
 */
@Singleton
class Logger @Inject constructor() {
    
    companion object {
        private const val TAG = "ScreenTimeApp"
    }
    
    fun d(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }
    
    fun d(tag: String, message: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message, throwable)
        }
    }
    
    fun i(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }
    
    fun i(tag: String, message: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message, throwable)
        }
    }
    
    fun w(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message)
        }
    }
    
    fun w(tag: String, message: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message, throwable)
        }
    }
    
    fun e(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }
    
    fun e(tag: String, message: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, throwable)
        }
    }
    
    fun v(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message)
        }
    }
    
    fun v(tag: String, message: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message, throwable)
        }
    }
    
    // Convenience methods with default TAG
    fun d(message: String) = d(TAG, message)
    fun d(message: String, throwable: Throwable) = d(TAG, message, throwable)
    fun i(message: String) = i(TAG, message)
    fun i(message: String, throwable: Throwable) = i(TAG, message, throwable)
    fun w(message: String) = w(TAG, message)
    fun w(message: String, throwable: Throwable) = w(TAG, message, throwable)
    fun e(message: String) = e(TAG, message)
    fun e(message: String, throwable: Throwable) = e(TAG, message, throwable)
    fun v(message: String) = v(TAG, message)
    fun v(message: String, throwable: Throwable) = v(TAG, message, throwable)
}

