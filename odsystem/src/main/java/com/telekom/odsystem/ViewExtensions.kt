@file:Suppress("UnusedPrivateMember", "MagicNumber", "TooManyFunctions")

package com.telekom.odsystem

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.telekom.odsystem.foundations.ODSTextStyle

fun TextView.setStyle(resId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextAppearance(
            resId
        )
    } else {
        this.setTextAppearance(
            context,
            resId
        )
    }
}

fun TextView.setTextStyle(textStyle: ODSTextStyle) {
    textStyle.applyTextStyle(this)
}

fun Number.toDP(): Float {
    // Get the screen's density scale
    val scale: Float = Resources.getSystem().displayMetrics.density
    // Convert the DPs to pixels, based on density scale
    return (this.toFloat() * scale + DP_TO_PX_SCALE)
}

val Float.toSp get() = this * Resources.getSystem().displayMetrics.scaledDensity

fun convertDptoPx(dp: Float): Int {
    // Get the screen's density scale
    val scale: Float = Resources.getSystem().displayMetrics.density
    // Convert the DPs to pixels, based on density scale
    return (dp * scale + DP_TO_PX_SCALE).toInt()
}

const val DP_TO_PX_SCALE = 0.5f

inline fun View.afterMeasured(crossinline f: View.() -> Unit) {
    if (measuredWidth > 0 && measuredHeight > 0) {
        f()
    } else {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if ((measuredWidth > 0 || measuredHeight > 0) && isShown) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }
}

inline fun View.afterMeasuredOnPreDraw(crossinline f: View.() -> Unit) {
    if (measuredWidth > 0 && measuredHeight > 0) {
        f()
    } else {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if ((measuredWidth > 0 || measuredHeight > 0) && isShown) {
                    viewTreeObserver.removeOnPreDrawListener(this)
                    f()
                }
                return true
            }
        })
    }
}

inline fun View.fadeIn(duration: Long, noinline onComplete: () -> Unit = {}) {
    this.apply {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        alpha = 0f
        visibility = View.VISIBLE

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        animate()
            .alpha(1f)
            .withEndAction(onComplete)
            .setDuration(duration)
            .setListener(null)
    }
}

inline fun View.fadeOut(duration: Long, noinline onComplete: () -> Unit = {}) {
    this.apply {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        alpha = 1f

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        animate()
            .alpha(0f)
            .setDuration(duration)
            .withEndAction(onComplete)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    this@apply.visibility = View.GONE
                }
            })
    }
}

inline fun View.resetRotation(
    duration: Long,
    delay: Long = 0L,
    noinline onComplete: () -> Unit = {}
) {
    animate()
        .rotation(0f)
        .setStartDelay(delay)
        .withEndAction(onComplete)
        .setDuration(duration)
        .setInterpolator(BounceInterpolator())
}

inline fun View.pop(duration: Long, delay: Long = 0L, noinline onComplete: () -> Unit = {}) {
    this.apply {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        visibility = View.VISIBLE
        scaleX = 0f
        scaleY = 0f
        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        animate()
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(delay)
            .withEndAction(onComplete)
            .setDuration(duration)
            .setInterpolator(BounceInterpolator())
    }
}

inline fun View.scale(duration: Long, delay: Long = 0L, noinline onComplete: () -> Unit = {}) {
    this.apply {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        visibility = View.VISIBLE
        scaleX = 1f
        scaleY = 1f
        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setStartDelay(delay)
            .withEndAction {
                animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .withEndAction(onComplete)
                    .setInterpolator(BounceInterpolator())
            }
            .setDuration(duration / 2)
            .setInterpolator(LinearInterpolator())
    }
}

inline fun View.setOnSingleClickListener(timeout: Long = 400, crossinline listener: () -> Unit) {
    this.setOnClickListener {
        if (!this.isEnabled) {
            return@setOnClickListener
        }
        val enabled = this?.isEnabled ?: true
        this?.isEnabled = false
        Handler().postDelayed({
            this?.isEnabled = enabled
        }, timeout)
        listener()
    }
}

inline fun View.setOnClickListenerWithTimeout(
    timeout: Long = 700,
    crossinline listener: () -> Unit
) {
    this.setOnClickListener {
        if (!this.isEnabled) {
            return@setOnClickListener
        }
        val enabled = this?.isEnabled ?: true
        this?.isEnabled = false
        Handler().postDelayed({
            this?.isEnabled = enabled
        }, timeout)
        listener()
    }
}

fun View.setMarginEnd(marginEnd: Int) {
    val layoutParams = this.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    layoutParams.marginEnd = marginEnd
    this.layoutParams = layoutParams
}

fun View.setMarginStart(marginStart: Int) {
    val layoutParams = this.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    layoutParams.marginStart = marginStart
    this.layoutParams = layoutParams
}

fun Context.isHighPerformingDevice(): Boolean {
    try {
        val activityManager =
            (getSystemService(Context.ACTIVITY_SERVICE) as?
                    ActivityManager)
                ?: return true
        return !activityManager.isLowRamDevice &&
                Runtime.getRuntime().availableProcessors() >= 4 &&
                activityManager.memoryClass >= 128
    } catch (e: Exception) {
        print(e)
        return true
    }
}

fun ImageView.setGreyScale(enable: Boolean) {
    if (enable) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f) // 0 means grayscale
        val cf = ColorMatrixColorFilter(matrix)
        this.colorFilter = cf
        this.imageAlpha = 128 // 128 = 0.5
    } else {
        this.colorFilter = null
        this.imageAlpha = 255
    }
}

fun View.startAttentionShakeAnimation(repeatCount: Int = -1, startDelay: Long = 200) {
    val firstValue = 2f
    val secondValue = 1f
    val objectAnimator = ObjectAnimator.ofFloat(
        this,
        View.TRANSLATION_X,
        0f,
        -firstValue,
        firstValue,
        -secondValue,
        secondValue,
        0f,
        0f
    )
    val duration = 1000
    objectAnimator.duration = duration.toLong()
    if (repeatCount == -1) {
        objectAnimator.repeatCount = ObjectAnimator.INFINITE
    } else {
        objectAnimator.repeatCount = repeatCount
    }
    objectAnimator.startDelay = startDelay
    objectAnimator.interpolator = AccelerateDecelerateInterpolator()
    objectAnimator.start()
}

fun WebView.destroyWebView() {
    this.clearHistory()
    this.clearSslPreferences()
    this.clearFormData()
    this.clearMatches()
    // NOTE: clears RAM cache, if you pass true, it will also clear the disk cache.
    // Probably not a great idea to pass true if you have other WebViews still alive.
    this.clearCache(true)
    CookieManager.getInstance().removeAllCookie()
}

fun WebView.initWebView() {
    settings.javaScriptEnabled = true
    settings.useWideViewPort = true
    settings.loadWithOverviewMode = true
    settings.setSupportZoom(true)
    settings.builtInZoomControls = true
    settings.domStorageEnabled = true
    settings.cacheMode = WebSettings.LOAD_NO_CACHE
}

fun Drawable.setColorFilterCompat(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
    } else {
        this.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
}

fun View.getProperColor(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(context, colorId)
}

fun View.getProperColorStateList(@ColorRes colorId: Int): ColorStateList? {
    return ContextCompat.getColorStateList(context, colorId)
}

fun EditText.setCursorColor(@ColorInt colorInt: Int) {
    if (Build.VERSION.SDK_INT >= 29) {
        val drawable = this.textCursorDrawable ?: return
        val cursorDrawable = drawable.mutate()
        cursorDrawable.colorFilter = PorterDuffColorFilter(colorInt, PorterDuff.Mode.SRC_IN)
        this.textCursorDrawable = cursorDrawable
    } else {
        setCursorColorViaReflection(this, colorInt, context)
    }
}

fun View.createBorder(
    borderWidth: Float,
    borderColor: Int,
    backgroundColor: Int,
    cornerRadius: Float,
) {
    val border = GradientDrawable()
    border.mutate()
    border.setColor(backgroundColor)

    border.cornerRadius = cornerRadius
    border.setStroke(
        borderWidth.toInt(),
        ColorStateList.valueOf(borderColor)
    )
    this.background = border
}

/**
 * Note: This is only ever called in API 28 and less.
 */
@SuppressLint("DiscouragedPrivateApi", "SoonBlockedPrivateApi")
private fun setCursorColorViaReflection(editText: EditText, color: Int, context: Context) {
    try {
        val fCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        fCursorDrawableRes.isAccessible = true
        val mCursorDrawableRes = fCursorDrawableRes.getInt(editText)
        val fEditor = TextView::class.java.getDeclaredField("mEditor")
        fEditor.isAccessible = true
        val editor = fEditor[editText]
        val clazz: Class<*> = editor.javaClass
        val fCursorDrawable = clazz.getDeclaredField("mCursorDrawable")
        fCursorDrawable.isAccessible = true
        val drawables = arrayOfNulls<Drawable>(2)

        drawables[0] =
            ResourcesCompat.getDrawable(context.resources, mCursorDrawableRes, null)
        drawables[1] =
            ResourcesCompat.getDrawable(context.resources, mCursorDrawableRes, null)
        drawables[0]?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        drawables[1]?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        fCursorDrawable[editor] = drawables
    } catch (ignored: Throwable) {
        // do nothing
    }
}

fun View.setMargin(
    left: Float? = null,
    top: Float? = null,
    right: Float? = null,
    bottom: Float? = null
) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun Context.dpToPx(dp: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
        .toInt()


fun getTintDrawable(drawable: Drawable?, color: Int): Drawable? {
    try {
        val unwrappedDrawable = drawable ?: return null
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    } catch (e: Exception) {
        print(e)
        return null
    }
}
