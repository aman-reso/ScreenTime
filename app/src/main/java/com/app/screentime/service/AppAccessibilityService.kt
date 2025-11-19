package com.app.screentime.service

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.core.content.edit
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.app.screentime.R
import com.app.screentime.blocking.model.BlockingRule
import com.app.screentime.blocking.repository.BlockingRepository
import android.view.*
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo

class AppAccessibilityService : AccessibilityService() {

    private val prefs by lazy { getSharedPreferences("tracker_prefs", Context.MODE_PRIVATE) }
    private val counts by lazy { AppCounts(this) }
    private val overlayController by lazy { OverlayController(this) }
    private val blockingRepository by lazy { BlockingRepository(this) }
    private val appUsageTracker by lazy { AppUsageTracker(this) }

    // Track current foreground app and its start time
    private var currentForegroundApp: String? = null
    private var appStartTime: Long = 0L

    override fun onServiceConnected() {
        super.onServiceConnected()
        Toast.makeText(this, "Accessibility service connected", Toast.LENGTH_SHORT).show()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val pkg = event.packageName?.toString() ?: return

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                handleAppLaunch(pkg)
            }

            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                when (pkg) {
                    "com.instagram.android" -> detectInstagramReels(event)
                    "com.google.android.youtube" -> detectYouTubeShorts(event)
                    "com.android.chrome" -> detectBrowserReelsOrShorts(event)
                }
            }
        }

        trackAppUsage(pkg)
    }

    private fun detectYouTubeShorts(event: AccessibilityEvent) {
        val rootNode = rootInActiveWindow ?: return

        val shortsNode = findNodeByIdOrText(
            rootNode,
            ids = listOf(
                "com.google.android.youtube:id/reel_player_view",
                "com.google.android.youtube:id/reel_container",
                "com.google.android.youtube:id/shorts_player"
            ),
            texts = listOf("Shorts", "Add sound", "@")
        )

        if (shortsNode != null) {
            Log.d("AppAccessibilityService", "🎥 YouTube Shorts detected")
            overlayController.showOverlay(
                packageName = "com.google.android.youtube",
                appName = "YouTube",
                message = "Shorts are blocked 🚫",
                blockType = "feature"
            )
        }
    }

    private fun detectBrowserReelsOrShorts(event: AccessibilityEvent) {
        if (event.packageName != "com.android.chrome") return

        val rootNode = rootInActiveWindow ?: return

        val urlNode = findNodeByIdOrText(
            rootNode,
            ids = listOf(
                "com.android.chrome:id/url_bar"
            ),
            texts = listOf("instagram.com/reels", "youtube.com/shorts")
        )

        if (urlNode != null) {
            Log.d("AppAccessibilityService", "Browser Reels/Shorts detected")
            overlayController.showOverlay(
                packageName = "com.android.chrome",
                appName = "Browser",
                message = "This content is blocked 🚫",
                blockType = "web"
            )
        }
    }


    private fun handleAppLaunch(packageName: String) {
        val rule = blockingRepository.getRule(packageName) ?: return

        when (rule) {
            is BlockingRule.InstantBlock -> {
                Log.d("AppAccessibilityService", "Instant block triggered for $packageName")
                overlayController.showOverlay(
                    packageName = packageName,
                    appName = rule.appName,
                    message = "This app is blocked",
                    blockType = "instant"
                )
            }

            is BlockingRule.LaunchBasedBlock -> {
                // Increment launch count
                val currentLaunches = counts.increment(packageName)
                val updatedRule = rule.copy(currentLaunches = currentLaunches)
                blockingRepository.saveRule(updatedRule)

                // Check if launch limit exceeded
                if (currentLaunches >= rule.maxLaunches) {
                    Log.d(
                        "AppAccessibilityService",
                        "Launch limit exceeded for $packageName: $currentLaunches >= ${rule.maxLaunches}"
                    )
                    overlayController.showOverlay(
                        packageName = packageName,
                        appName = rule.appName,
                        message = "Launch limit exceeded ($currentLaunches/${rule.maxLaunches})",
                        blockType = "launch"
                    )
                } else {
                    // Hide overlay if limit not exceeded
                    overlayController.hideOverlay()
                }
            }

            is BlockingRule.DurationBasedBlock -> {
                // Duration-based blocking is handled in trackAppUsage
                // Just hide overlay initially
                overlayController.hideOverlay()
            }
        }
    }

    private fun trackAppUsage(packageName: String) {
        val rule = blockingRepository.getRule(packageName) ?: return

        // Only track duration for DurationBasedBlock
        if (rule !is BlockingRule.DurationBasedBlock) return

        val currentTime = System.currentTimeMillis()

        // Check if app changed
        if (currentForegroundApp != packageName) {
            // Save previous app's usage time
            currentForegroundApp?.let { previousApp ->
                val previousRule = blockingRepository.getRule(previousApp)
                if (previousRule is BlockingRule.DurationBasedBlock) {
                    val elapsedMinutes = (currentTime - appStartTime) / (1000 * 60)
                    if (elapsedMinutes > 0) {
                        val updatedDuration = previousRule.currentDurationMinutes + elapsedMinutes
                        val updatedRule =
                            previousRule.copy(currentDurationMinutes = updatedDuration)
                        blockingRepository.saveRule(updatedRule)
                    }
                }
            }

            // Start tracking new app
            currentForegroundApp = packageName
            appStartTime = currentTime
        }

        // Calculate current usage time
        val elapsedMinutes = (currentTime - appStartTime) / (1000 * 60)
        val totalDurationMinutes = rule.currentDurationMinutes + elapsedMinutes

        // Check if duration limit exceeded
        if (totalDurationMinutes >= rule.maxDurationMinutes) {
            Log.d(
                "AppAccessibilityService",
                "Duration limit exceeded for $packageName: $totalDurationMinutes >= ${rule.maxDurationMinutes}"
            )
            overlayController.showOverlay(
                packageName = packageName,
                appName = rule.appName,
                message = "Usage time limit exceeded (${totalDurationMinutes}min/${rule.maxDurationMinutes}min)",
                blockType = "duration"
            )
        } else {
            // Update duration in repository periodically (every minute)
            if (elapsedMinutes >= 1) {
                val updatedRule = rule.copy(currentDurationMinutes = totalDurationMinutes)
                blockingRepository.saveRule(updatedRule)
                appStartTime = currentTime // Reset start time after saving
            }
        }
    }

    override fun onInterrupt() {
        // no-op
    }

    override fun onDestroy() {
        // Save current app's usage time before destroying
        currentForegroundApp?.let { packageName ->
            val rule = blockingRepository.getRule(packageName)
            if (rule is BlockingRule.DurationBasedBlock) {
                val elapsedMinutes = (System.currentTimeMillis() - appStartTime) / (1000 * 60)
                if (elapsedMinutes > 0) {
                    val updatedDuration = rule.currentDurationMinutes + elapsedMinutes
                    val updatedRule = rule.copy(currentDurationMinutes = updatedDuration)
                    blockingRepository.saveRule(updatedRule)
                }
            }
        }
        overlayController.destroy()
        super.onDestroy()
    }

    private fun detectInstagramReels(event: AccessibilityEvent) {
        val rootNode = rootInActiveWindow ?: return
        val reelNode = findNodeByIdOrText(
            rootNode,
            listOf(
                "com.instagram.android:id/reel_viewer_root",
                "com.instagram.android:id/clips_video_container"
            ),
            listOf("Reel", "Reels")
        )

        if (reelNode != null) {
            Log.d("AppAccessibilityService", "Instagram Reels detected!")
            overlayController.showOverlay(
                packageName = "com.instagram.android",
                appName = "Instagram",
                message = "Reels are blocked 🚫",
                blockType = "feature"
            )
        }
    }

    private fun findNodeByIdOrText(
        node: AccessibilityNodeInfo?,
        ids: List<String>,
        texts: List<String>
    ): AccessibilityNodeInfo? {
        if (node == null) return null

        for (id in ids) {
            val byId = node.findAccessibilityNodeInfosByViewId(id)
            if (!byId.isNullOrEmpty()) return byId.first()
        }

        for (text in texts) {
            val byText = node.findAccessibilityNodeInfosByText(text)
            if (!byText.isNullOrEmpty()) return byText.first()
        }

        for (i in 0 until node.childCount) {
            val result = findNodeByIdOrText(node.getChild(i), ids, texts)
            if (result != null) return result
        }
        return null
    }


}

class AppCounts(private val context: Context) {
    private val prefs = context.getSharedPreferences("counts_prefs", Context.MODE_PRIVATE)

    fun increment(pkg: String): Int {
        val key = "count_$pkg"
        val cur = prefs.getInt(key, 0) + 1
        prefs.edit { putInt(key, cur) }
        return cur
    }

    fun reset(pkg: String) {
        prefs.edit { remove("count_$pkg") }
    }

    fun get(pkg: String): Int {
        return prefs.getInt("count_$pkg", 0)
    }
}

class OverlayController(private val ctx: Context) {
    private var wm: WindowManager? = null
    private var view: View? = null

    @SuppressLint("SetTextI18n")
    fun showOverlay(
        packageName: String,
        appName: String,
        message: String,
        blockType: String
    ) {
        // Remove existing overlay if any
        hideOverlay()

        val sheetLayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
            android.graphics.PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        }

        wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Inflate layout
        val inflater = LayoutInflater.from(ctx)
        view = inflater.inflate(R.layout.accessibility_overlay, null)
        val tv = view?.findViewById<TextView>(R.id.overlay_description)
        tv?.text = "$appName\n$message"

        val btnReset = view?.findViewById<TextView>(R.id.emergency)
        if (blockType == "launch") {
            btnReset?.visibility = View.VISIBLE
            btnReset?.setOnClickListener {
                AppCounts(ctx).reset(packageName)
                val blockingRepository = BlockingRepository(ctx)
                blockingRepository.getRule(packageName)?.let { rule ->
                    if (rule is BlockingRule.LaunchBasedBlock) {
                        val updatedRule = rule.copy(currentLaunches = 0)
                        blockingRepository.saveRule(updatedRule)
                    }
                }
                Toast.makeText(ctx, "Launch count reset", Toast.LENGTH_SHORT).show()
                hideOverlay()
            }
        } else {
            btnReset?.visibility = View.GONE
        }

        view?.findViewById<Button>(R.id.btn_ok)?.setOnClickListener {
            hideOverlay()
        }

        wm?.addView(view, sheetLayoutParams)
    }

    fun hideOverlay() {
        if (view != null) {
            wm?.removeView(view)
            view = null
        }
    }

    fun destroy() = hideOverlay()
}

// Helper class to track app usage time
class AppUsageTracker(private val context: Context) {
    private val prefs = context.getSharedPreferences("app_usage_tracker", Context.MODE_PRIVATE)

    fun getAppStartTime(packageName: String): Long {
        return prefs.getLong("start_time_$packageName", 0L)
    }

    fun setAppStartTime(packageName: String, startTime: Long) {
        prefs.edit {
            putLong("start_time_$packageName", startTime)
        }
    }

    fun clearAppStartTime(packageName: String) {
        prefs.edit {
            remove("start_time_$packageName")
        }
    }
}
