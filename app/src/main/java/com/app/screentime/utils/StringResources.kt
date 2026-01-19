package com.app.screentime.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.app.screentime.config.R

/**
 * Utility object for accessing string resources
 */
object StringResources {
    /**
     * Get string resource by ID
     */
    @Composable
    fun getString(@StringRes id: Int): String {
        return LocalContext.current.getString(id)
    }

    /**
     * Get string resource with format arguments
     */
    @Composable
    fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
        return LocalContext.current.getString(id, *formatArgs)
    }
}

