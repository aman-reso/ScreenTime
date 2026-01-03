package com.app.screentime.applock.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.util.Log
import java.lang.reflect.Method

/**
 * Helper class to get information about the top/current activity.
 * Provides multiple methods with fallback options.
 */
object TopActivityHelper {
    
    /**
     * Gets the current top activity package name using UsageStatsManager.
     * This is the most reliable method that works on all Android versions (5.0+).
     * 
     * @return Package name of the top activity, or null if not available
     */
    fun getTopActivityPackage(context: Context): String? {
        return try {
            val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val currentTime = System.currentTimeMillis()
            
            // Query usage stats for a small time window
            val usageStatsList: List<UsageStats> = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                currentTime - 1000,
                currentTime
            )
            
            if (usageStatsList.isEmpty()) {
                null
            } else {
                // Find the app with the most recent lastTimeUsed
                val recentUsageStats = usageStatsList.maxByOrNull { it.lastTimeUsed }
                recentUsageStats?.packageName
            }
        } catch (e: SecurityException) {
            Log.e("TopActivityHelper", "USAGE_STATS permission not granted", e)
            null
        } catch (e: Exception) {
            Log.e("TopActivityHelper", "Error getting top activity package", e)
            null
        }
    }
    
    /**
     * Gets the current top activity class name using ActivityManager.
     * Note: This method is deprecated and may not work on newer Android versions.
     * 
     * @return Activity class name (e.g., "com.example.MainActivity"), or null if not available
     */
    @SuppressLint("Deprecated")
    @Suppress("DEPRECATION")
    fun getTopActivityClassName(context: Context): String? {
        return try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                // For older Android versions, use getRunningTasks
                val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val runningTasks = activityManager.getRunningTasks(1)
                if (runningTasks.isNotEmpty()) {
                    val topActivity = runningTasks[0].topActivity
                    topActivity?.className
                } else {
                    null
                }
            } else {
                // For Android 5.0+, try using reflection (may not work due to restrictions)
                getTopActivityUsingReflection(context)
            }
        } catch (e: Exception) {
            Log.e("TopActivityHelper", "Error getting top activity class name", e)
            null
        }
    }
    
    /**
     * Gets the top activity using reflection (hacky method, may not work on all devices).
     * This is a workaround for Android 5.0+ where getRunningTasks is restricted.
     */
    @SuppressLint("PrivateApi")
    private fun getTopActivityUsingReflection(context: Context): String? {
        return try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            
            // Try to get getRunningTasks method using reflection
            val getRunningTasksMethod: Method = activityManager.javaClass.getMethod(
                "getRunningTasks",
                Int::class.javaPrimitiveType
            )
            val runningTasks = getRunningTasksMethod.invoke(activityManager, 1) as List<*>
            
            if (runningTasks.isNotEmpty()) {
                val taskInfo = runningTasks[0]
                val topActivityField = taskInfo?.javaClass?.getDeclaredField("topActivity")
                topActivityField?.isAccessible = true
                val componentName = topActivityField?.get(taskInfo)
                val classNameMethod = componentName?.javaClass?.getMethod("getClassName")
                classNameMethod?.invoke(componentName) as? String
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("TopActivityHelper", "Reflection method failed", e)
            null
        }
    }
    
    /**
     * Gets both package name and activity class name of the top activity.
     * 
     * @return Pair of (packageName, className) or null if not available
     */
    fun getTopActivityInfo(context: Context): Pair<String, String?>? {
        val packageName = getTopActivityPackage(context) ?: return null
        val className = getTopActivityClassName(context)
        return Pair(packageName, className)
    }
    
    /**
     * Gets detailed information about the top activity.
     * 
     * @return TopActivityInfo object with package name, class name, and timestamp
     */
    fun getTopActivityDetails(context: Context): TopActivityInfo? {
        val packageName = getTopActivityPackage(context) ?: return null
        val className = getTopActivityClassName(context)
        
        return TopActivityInfo(
            packageName = packageName,
            className = className,
            timestamp = System.currentTimeMillis()
        )
    }
    
    /**
     * Data class containing information about the top activity.
     */
    data class TopActivityInfo(
        val packageName: String,
        val className: String?,
        val timestamp: Long
    ) {
        override fun toString(): String {
            return if (className != null) {
                "$packageName/$className"
            } else {
                packageName
            }
        }
    }
}

