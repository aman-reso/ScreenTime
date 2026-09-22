package com.telekom.odsystem

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat.recreate
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.darkMode
import com.telekom.odsystem.tokens.tokens.lightMode
import java.lang.ref.WeakReference

// Foundations
typealias DSVariables = ODSVariables
typealias DSTextStyles = ODSTextStyles

val neutralScheme: ODSTheme
    get() = ODSystem.colors.value
val invertedScheme: ODSTheme
    get() = if (ODSystem.colors.value == lightMode) darkMode else lightMode

class ODSystem(private val context: Context) {
    companion object {
        var colors = mutableStateOf(lightMode)
        private var themeType: ODSThemeType? = null

        fun init(context: Context, defaultThemeType: ODSThemeType = ODSThemeType.LIGHT) {
            themeType = loadTheme(context)
            if (themeType == null) {
                setTheme(context, defaultThemeType)
            } else {
                setTheme(context)
            }
        }

        fun setTheme(context: Context, newThemeType: ODSThemeType? = null) {
            if (newThemeType != themeType) {
                newThemeType?.let { themeType = it }
                when (themeType) {
                    ODSThemeType.LIGHT -> {
                        colors.value = lightMode
                        themeType = ODSThemeType.LIGHT
                    }

                    ODSThemeType.DARK -> {
                        colors.value = darkMode
                        themeType = ODSThemeType.DARK
                    }

                    else -> {
                        colors.value = getSystemColors(context)
                        themeType = ODSThemeType.SYSTEM
                    }
                }
                // Choose Between ODSThemeLiveDataHolder and notifyThemeChanged()
                ODSThemeLiveDataHolder.updateODSTheme(colors.value)
                notifyThemeChanged()
                saveTheme(context, themeType)
            }
        }

        fun setSystemTheme(context: Context) {
            if (themeType == ODSThemeType.SYSTEM) {
                setTheme(context)
            }
        }

        fun getCurrentThemeType(): ODSThemeType? {
            return themeType
        }

        fun getThemeTypes(): List<ODSThemeType> {
            ODSThemeType.values().toList().let {
                return it
            }
        }
    }
}

internal fun saveTheme(context: Context, theme: ODSThemeType?) {
    // save theme choice locally in shared preferences
    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
    )
    with(sharedPref.edit()) {
        putString(context.getString(R.string.preference_theme_key), theme?.name)
        commit()
    }
}

internal fun loadTheme(context: Context): ODSThemeType? {
    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
    )
    val themeName = sharedPref.getString(context.getString(R.string.preference_theme_key), null)
    return themeName?.let { ODSThemeType.valueOf(it) }
}

enum class ODSThemeType {
    LIGHT,
    DARK,
    SYSTEM
}

internal fun getSystemColors(context: Context): ODSTheme {
    return if (context.isDarkThemeOn()) {
        darkMode
    } else {
        lightMode
    }
}

internal fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

private val openActivities = mutableListOf<WeakReference<Activity>>()

fun registerActivity(activity: Activity) {
    openActivities.add(WeakReference(activity))
}

fun unregisterActivity(activity: Activity) {
    openActivities.removeAll { it.get() == null || it.get() == activity }
}

internal fun notifyThemeChanged() {
    openActivities.forEach { weakRef ->
        weakRef.get()?.let { activity ->
            recreate(activity)
        }
    }
}
