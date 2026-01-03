package com.app.screentime.service

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.ContextCompat
import com.app.screentime.R
import com.app.screentime.applock.repository.AppLockRepository

class AppLockOverlayController(private val ctx: Context) {
    private var wm: WindowManager? = null
    private var view: View? = null
    var currentPackageName: String? = null
        private set
    private val appLockRepository = AppLockRepository(ctx)
    private val pinCircles = mutableListOf<ImageView>()
    private val pinDigits = mutableListOf<Int>()
    private var errorMessage: TextView? = null
    private var isErrorState = false

    @SuppressLint("SetTextI18n")
    fun showPINOverlay(
        packageName: String,
        appName: String,
        onPINVerified: () -> Unit,
        onCancel: () -> Unit
    ) {
        // Prevent showing overlay if already showing for the same package
        if (view != null && currentPackageName == packageName) {
            Log.d("AppLockOverlay", "Overlay already showing for $packageName, skipping")
            return
        }

        // Remove existing overlay if any
        hideOverlay()

        currentPackageName = packageName

        val windowType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_FULLSCREEN

        val sheetLayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            windowType,
            flags,
            android.graphics.PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 0
            alpha = 1.0f
            // Ensure overlay is on top with high z-order
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            }
        }

        wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Inflate layout
        val inflater = LayoutInflater.from(ctx)
        view = inflater.inflate(R.layout.app_lock_overlay, null)

        // Make the view extend into system UI areas (status bar, navigation bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view?.setOnApplyWindowInsetsListener { v, insets ->
                // Ignore insets to make it full screen
                WindowInsets.CONSUMED
            }
        } else {
            @Suppress("DEPRECATION")
            view?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }

        // Set app name
        view?.findViewById<TextView>(R.id.app_name)?.text = appName

        // Get PIN circles
        pinCircles.clear()
        pinCircles.add(view?.findViewById(R.id.pin_circle_1)!!)
        pinCircles.add(view?.findViewById(R.id.pin_circle_2)!!)
        pinCircles.add(view?.findViewById(R.id.pin_circle_3)!!)
        pinCircles.add(view?.findViewById(R.id.pin_circle_4)!!)

        // Initialize PIN digits
        pinDigits.clear()

        // Error message
        errorMessage = view?.findViewById(R.id.error_message)

        // Setup numeric keypad
        setupNumericKeypad(onPINVerified, onCancel)

        try {
            // Check overlay permission before showing
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!android.provider.Settings.canDrawOverlays(ctx)) {
                    Log.e("AppLockOverlay", "Overlay permission not granted")
                    Toast.makeText(
                        ctx,
                        "Overlay permission required for app lock",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
            }

            if (view != null && wm != null) {
                wm!!.addView(view, sheetLayoutParams)
                Log.d("AppLockOverlay", "PIN overlay shown for $packageName ($appName)")
                // Verify overlay is visible
                view!!.post {
                    if (view?.visibility != View.VISIBLE) {
                        view?.visibility = View.VISIBLE
                    }
                }
            } else {
                Log.e("AppLockOverlay", "View or WindowManager is null")
            }
        } catch (e: WindowManager.BadTokenException) {
            Log.e(
                "AppLockOverlay",
                "BadTokenException: Overlay permission denied for $packageName",
                e
            )
            // Permission was revoked, clean up
            view = null
            Toast.makeText(
                ctx,
                "Overlay permission was revoked. Please enable it in settings.",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Log.e("AppLockOverlay", "Error showing PIN overlay: ${e.message}", e)
            e.printStackTrace()
            // Clean up on any error
            view = null
            Toast.makeText(ctx, "Error showing lock screen: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setupNumericKeypad(
        onPINVerified: () -> Unit,
        onCancel: () -> Unit
    ) {
        // Number buttons (0-9)
        val numberButtons = listOf(
            view?.findViewById(R.id.btn_0),
            view?.findViewById(R.id.btn_1),
            view?.findViewById(R.id.btn_2),
            view?.findViewById(R.id.btn_3),
            view?.findViewById(R.id.btn_4),
            view?.findViewById(R.id.btn_5),
            view?.findViewById(R.id.btn_6),
            view?.findViewById(R.id.btn_7),
            view?.findViewById(R.id.btn_8),
            view?.findViewById<Button>(/* id = */ R.id.btn_9)
        )

        numberButtons.forEachIndexed { digit, button ->
            button?.setOnClickListener {
                if (pinDigits.size < 4) {
                    // Clear error state when user starts typing
                    if (isErrorState) {
                        clearErrorState()
                    }

                    pinDigits.add(digit)
                    updatePINCircles()
                    vibrate(30) // Light vibration on input

                    // Auto-verify when 4 digits entered
                    if (pinDigits.size == 4) {
                        val pin = getEnteredPIN()
                        view?.postDelayed({
                            if (appLockRepository.verifyPIN(pin)) {
                                // PIN verified, unlock app
                                vibrate(100) // Success vibration
                                currentPackageName?.let { pkg ->
                                    com.app.screentime.applock.manager.AppLockManager.unlockApp(pkg)
                                }
                                hideOverlay()
                                onPINVerified()
                            } else {
                                // Show error
                                showError("Incorrect PIN. Please try again.")
                                vibrate(200) // Error vibration
                            }
                        }, 100)
                    }
                }
            }
        }

        // Delete button
        view?.findViewById<Button>(R.id.btn_delete)?.setOnClickListener {
            if (pinDigits.isNotEmpty()) {
                pinDigits.removeAt(pinDigits.size - 1)
                updatePINCircles()
                vibrate(30)
                if (isErrorState) {
                    clearErrorState()
                }
            }
        }

        // Cancel/Close button
        view?.findViewById<Button>(R.id.btn_cancel)?.setOnClickListener {
            vibrate(50)
            hideOverlay()
            onCancel()
        }

        // Apply app font to all text elements
        applyAppFont()
    }

    private fun updatePINCircles() {
        pinCircles.forEachIndexed { index, circle ->
            if (index < pinDigits.size) {
                // Show filled circle
                circle.setImageDrawable(
                    ContextCompat.getDrawable(
                        ctx,
                        R.drawable.pin_circle_filled
                    )
                )
            } else {
                // Show empty circle
                circle.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.pin_circle_empty))
            }
        }
    }

    private fun showError(message: String) {
        isErrorState = true
        errorMessage?.visibility = View.VISIBLE
        errorMessage?.text = message

        // Shake animation
        view?.findViewById<View>(R.id.pin_container)?.let { container ->
            val shake = AnimationUtils.loadAnimation(ctx, R.anim.shake)
            container.startAnimation(shake)
        }

        clearPIN()
    }

    private fun clearErrorState() {
        isErrorState = false
        errorMessage?.visibility = View.GONE
        updatePINCircles()
    }

    private fun vibrate(duration: Long) {
        try {
            val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(
                        duration,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(duration)
            }
        } catch (e: Exception) {
            // Vibration not available or permission not granted
        }
    }

    private fun getEnteredPIN(): String {
        return pinDigits.joinToString("") { it.toString() }
    }

    private fun clearPIN() {
        pinDigits.clear()
        updatePINCircles()
        // Clear error state after a delay
        view?.postDelayed({
            clearErrorState()
        }, 2000)
    }

    fun hideOverlay() {
        if (view != null) {
            try {
                // Check if view is attached before removing
                if (view?.parent != null && wm != null) {
                    wm?.removeView(view)
                }
            } catch (e: Exception) {
                Log.e("AppLockOverlay", "Error removing overlay view", e)
            }
            view = null
            pinDigits.clear()
            pinCircles.clear()
            currentPackageName = null
        }
    }

    fun isOverlayShowing(): Boolean {
        return view != null && currentPackageName != null
    }

    fun destroy() = hideOverlay()

    private fun applyAppFont() {
        try {
            // Apply font to all keypad buttons
            val buttonIds = listOf(
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6,
                R.id.btn_7, R.id.btn_8, R.id.btn_9
            )


        } catch (e: Exception) {
            Log.e("AppLockOverlay", "Error applying app font", e)
        }
    }

    fun showBlockOverlay(
        packageName: String,
        appName: String,
        onClose: () -> Unit,
        onEmergency: () -> Unit
    ) {
        // Prevent showing overlay if already showing for the same package
        if (view != null && currentPackageName == packageName) {
            return
        }

        // Remove existing overlay if any
        hideOverlay()

        currentPackageName = packageName

        val windowType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_FULLSCREEN

        val sheetLayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            windowType,
            flags,
            android.graphics.PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 0
            alpha = 1.0f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            }
        }

        wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val inflater = LayoutInflater.from(ctx)
        view = inflater.inflate(R.layout.accessibility_overlay, null)

        // Handle window insets
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view?.setOnApplyWindowInsetsListener { _, _ -> WindowInsets.CONSUMED }
        } else {
            @Suppress("DEPRECATION")
            view?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }

        // Set text
        view?.findViewById<TextView>(R.id.overlay_description)?.text =
            "Limit reached for $appName. Take a break!"

        // Button actions
        view?.findViewById<Button>(R.id.btn_ok)?.setOnClickListener {
            onClose()
            hideOverlay()
        }

        view?.findViewById<View>(R.id.emergency)?.setOnClickListener {
            onEmergency()
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!android.provider.Settings.canDrawOverlays(ctx)) {
                    Toast.makeText(ctx, "Overlay permission required", Toast.LENGTH_LONG).show()
                    return
                }
            }

            if (view != null && wm != null) {
                wm!!.addView(view, sheetLayoutParams)

                view!!.post {
                    if (view?.visibility != View.VISIBLE) {
                        view?.visibility = View.VISIBLE
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("AppLockOverlay", "Error showing block overlay", e)
            view = null
        }
    }
}

